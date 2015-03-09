package server;

import java.io.IOException;
import java.util.ArrayList;

import server.Network.ChatMessage;
import server.Network.Deck;
import server.Network.GetRandomStartHand;
import server.Network.ReadyUp;
import server.Network.RegisterName;
import server.Network.StartGame;
import client.Hand;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ChatServer {
	Server server;
	Deck deck;
	ArrayList<String> namesList;
	ArrayList<Boolean> isReady;
	ArrayList<GameConnection> connections;

	public ChatServer() throws IOException {
		namesList = new ArrayList<String>();
		isReady = new ArrayList<Boolean>();
		connections = new ArrayList<GameConnection>();
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
					// Ignore the object if a client has already registered a
					// name. This is
					// impossible with our client, but a hacker could send
					// messages at any time.
					if (connection.name != null)
						return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterName) object).name;
					if (name == null)
						return;
					name = name.trim();
					if (name.length() == 0)
						return;
					// Store the name on the connection.
					connection.name = name;
					// Send a "connected" message to everyone except the new
					// client.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					// Send everyone a new list of connection names.
					updateNames();
					namesList.add(name);
					connections.add(connection);
					return;
				}

				if (object instanceof ChatMessage) {
					// Ignore the object if a client tries to chat before
					// registering a name.
					if (connection.name == null)
						return;
					ChatMessage chatMessage = (ChatMessage) object;
					// Ignore the object if the chat message is invalid.
					String message = chatMessage.text;
					if (message == null)
						return;
					message = message.trim();
					if (message.length() == 0)
						return;
					// Prepend the connection's name and send to everyone.
					chatMessage.text = connection.name + ": " + message;
					System.out.println(chatMessage.text);
					server.sendToAllTCP(chatMessage);
					return;
				}

				if (object instanceof GetRandomStartHand) {
					// server.sendToAllTCP(getRandomStartHand());
					System.out.println("Sending hand");
					server.sendToTCP(connection.getID(), getRandomStartHand());
				}

				if (object instanceof ReadyUp) {
					isReady.add(true);
					if (isReady.size() == namesList.size()) {
						StartGame sg = new StartGame();
						for (GameConnection con : connections) {
							if (connection.getID() == 1
									|| connection.getID() == 2) {
								sg.text = "playing";
								server.sendToTCP(connection.getID(), sg);
							} else {
								sg.text = "";
								server.sendToTCP(connection.getID(), sg);
							}
						}
					}
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
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			GameConnection connection = (GameConnection) connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.

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

	// This holds per connection state.
	static class GameConnection extends Connection {
		public String name;
	}

	public static void main(String[] args) throws IOException {
		new ChatServer();
	}
}
