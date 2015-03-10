package server;

import java.io.IOException;
import java.util.ArrayList;

import server.Network.ChatMessage;
import server.Network.Deck;
import server.Network.FlipCards;
import server.Network.GetRandomStartHand;
import server.Network.ReadyUp;
import server.Network.RecieveSelected;
import server.Network.RegisterName;
import server.Network.SendSelected;
import server.Network.StartGame;
import client.Card;
import client.Hand;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	Server server;
	Deck deck;
	ArrayList<String> namesList;
	ArrayList<Boolean> isReady;
	int firstPlayerVotes;
	int secondPlayerVotes;
	boolean gameStarted;

	public GameServer() throws IOException {
		namesList = new ArrayList<String>();
		isReady = new ArrayList<Boolean>();
		firstPlayerVotes = 0;
		secondPlayerVotes = 0;
		gameStarted = false;
		server = new Server() {
			protected Connection newConnection() {
				return new GameConnection();
			}
		};

		Network.register(server);

		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				GameConnection connection = (GameConnection) c;
				if (object instanceof RegisterName) {
					if (connection.name != null)
						return;
					String name = ((RegisterName) object).name;
					if (name == null)
						return;
					name = name.trim();
					if (name.length() == 0)
						return;
					if(gameStarted){
						//TODO this needs to be somewhere else to not allow them to connect at all.
						return;
					}
					connection.name = name;

					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);

					updateNames();
					namesList.add(name);
					return;
				}

				if (object instanceof ChatMessage) {
					if (connection.name == null)
						return;
					ChatMessage chatMessage = (ChatMessage) object;
					String message = chatMessage.text;
					if (message == null)
						return;
					message = message.trim();
					if (message.length() == 0)
						return;
					chatMessage.text = connection.name + ": " + message;
					System.out.println(chatMessage.text);
					server.sendToAllTCP(chatMessage);
					return;
				}

				if (object instanceof GetRandomStartHand) {
					// server.sendToAllTCP(getRandomStartHand());
					System.out.println("Sending hand to " + connection.getID());
					server.sendToTCP(connection.getID(), getRandomStartHand());
				}

				if (object instanceof ReadyUp) {
					isReady.add(true);
					ReadyUp rup = (ReadyUp) object;
					if (isReady.size() == namesList.size() && rup.type.equals("start")) {
						StartGame sg = new StartGame();
						sg.text = "playing";
						server.sendToAllTCP(sg);
						isReady.clear();
						gameStarted = true;
					}
					if(isReady.size() == namesList.size() && rup.type.equals("play")){
						// shows cards to both at same time
						FlipCards fc = new FlipCards();
						fc.text = "";
						server.sendToAllTCP(fc);
					}
				}

				if (object instanceof SendSelected) {
					System.out.println("got hand, sending to others");
					SendSelected ss = (SendSelected) object;
					RecieveSelected rs = new RecieveSelected();
					rs.character = ss.character;
					rs.attribute = ss.attribute;
					rs.random = getRandomCard().getWords();
					server.sendToAllExceptTCP(connection.getID(), rs);
				}
			}

			public void disconnected(Connection c) {
				GameConnection connection = (GameConnection) c;
				if (connection.name != null) {
					// Announce to everyone that someone (with a registered
					// name) has left.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
				}
			}
		});
		server.bind(Network.port);
		server.start();

		startDeck();
		deck.printDeck();
	}

	void updateNames() {
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			GameConnection connection = (GameConnection) connections[i];
			names.add(connection.name);
		}

		// server.sendToAllTCP(updateNames);
	}

	private void startDeck() {
		deck = new Deck();
		deck.init();
	}

	private Deck getDeck() {
		return deck;
	}

	public Hand getRandomStartHand() {
		Hand rand = deck.randStartHand();
		rand.sortHand();
		return rand;
	}
	
	public Card getRandomCard(){
		Card card = deck.drawBlackCard();
		return card;
	}

	static class GameConnection extends Connection {
		public String name;
	}

	public static void main(String[] args) throws IOException {
		new GameServer();
	}
}
