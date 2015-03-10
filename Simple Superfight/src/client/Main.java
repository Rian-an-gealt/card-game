package client;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import server.Network;
import server.Network.ChatMessage;
import server.Network.FlipCards;
import server.Network.GetRandomStartHand;
import server.Network.ReadyUp;
import server.Network.RecieveSelected;
import server.Network.RegisterName;
import server.Network.SendSelected;
import server.Network.SendVote;
import server.Network.SendWinner;
import server.Network.StartGame;
import clientUI.ScreenManager;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Main extends Application {

	private static Hand hand;
	private static Stage stage;
	private static ScreenManager sm;
	private static Client client;
	private static String character;
	private static String attribute;

	public static void main(String[] args) {
		launch(args);
	}
	//TODO implement all the superfight rules? ie Battle Royale?

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		sm = new ScreenManager(this);
		sm.createStart(stage);
		sm.createLobby(stage);
		sm.setScreen("start");
	}

	public void sendMessage(String text) {
		if (text.equals("Vote 1")) {
			SendVote vote = new SendVote();
			vote.vote = 1;
			client.sendTCP(vote);
			System.out.println("Vote 1 command works");
		} else {
			if (text.equals("Vote 2")) {
				SendVote vote =  new SendVote();
				vote.vote = 2;
				client.sendTCP(vote);
			} else {
				ChatMessage cm = new ChatMessage();
				cm.text = text;
				client.sendTCP(cm);
			}
		}
	}

	public static ScreenManager getScreenManager() {
		return sm;
	}

	public static boolean connect(String name) {
		client = new Client();
		client.start();
		Network.register(client);

		client.addListener(new Listener() {
			public void connected(Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received(Connection connection, Object object) {
				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage) object;
					// chatFrame.addMessage(chatMessage.text);
					sm.printMessage(chatMessage.text);
					return;
				}

				if (object instanceof Hand) {
					hand = (Hand) object;
					int count = 1;
					for (Card c : hand.getHand()) {
						sm.setButtonName(count, c.getWords());
						count++;
					}
				}

				if (object instanceof StartGame) {
					//StartGame sg = (StartGame) object;
					if (connection.getID() == 1 || connection.getID() == 2) {
						startHand();
					} else {
						
					}
				}

				if (object instanceof RecieveSelected) {
					RecieveSelected rs = (RecieveSelected) object;
					sm.setTheirButtonName(1, rs.character);
					sm.setTheirButtonName(2, rs.attribute);
					sm.setTheirButtonName(3, rs.random);
				}

				if (object instanceof FlipCards) {
					sendSelectedCards();
				}
				
				if(object instanceof SendWinner){
					SendWinner winner = (SendWinner) object;
					sm.printMessage(winner.winner + " Wins!");
				}
			}

			public void disconnected(Connection connection) {

			}
		});
		try {
			client.connect(5000, "localhost", Network.port);
			System.out.println("Connected");
			return true;
		} catch (IOException ex) {
			System.err.println("Could not connect. Server must be down.");
		}

		return false;
	}

	public static Stage getStage() {
		return stage;
	}

	public static void startHand() {
		GetRandomStartHand grsh = new GetRandomStartHand();
		grsh.text = "";
		client.sendTCP(grsh);
	}

	public void setReadyUp() {
		ReadyUp ru = new ReadyUp();
		ru.isReady = true;
		ru.type = "start";
		client.sendTCP(ru);
	}

	public void setCardsSelected(String character, String attribute) {
		this.character = character;
		this.attribute = attribute;
		ReadyUp ru = new ReadyUp();
		ru.isReady = true;
		ru.type = "play";
		client.sendTCP(ru);
	}

	public static void sendSelectedCards() {
		System.out.println("Sending cards");
		SendSelected ss = new SendSelected();
		ss.character = character;
		ss.attribute = attribute;
		client.sendTCP(ss);
	}

}
