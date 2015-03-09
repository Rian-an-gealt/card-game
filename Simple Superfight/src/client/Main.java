package client;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import server.Network;
import server.Network.ChatMessage;
import server.Network.GetRandomStartHand;
import server.Network.ReadyUp;
import server.Network.RegisterName;
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

	public static void main(String[] args) {
		launch(args);
		// hostName = "server";
		// portNum = 255;

	}

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		sm = new ScreenManager(this);
		sm.createStart(stage);
		sm.createLobby(stage);
		// sm.createGame(stage);
		sm.setScreen("start");
	}

	public void sendMessage(String text) {
		ChatMessage cm = new ChatMessage();
		cm.text = text;
		client.sendTCP(cm);
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
					StartGame sg = (StartGame) object;
					if (sg.text.equals("playing")) {
						Platform.runLater(() -> {
							sm.createGame(getStage(), true);
						});
					} else {
						Platform.runLater(() -> {
							sm.createGame(getStage(), false);
						});
					}
				}
			}

			public void disconnected(Connection connection) {

			}
		});
		try {
			client.connect(5000, "localhost", Network.port);
			// Server communication after connection can go here, or in
			// Listener#connected().
			System.out.println("Connected");
			return true;
		} catch (IOException ex) {
			System.err.println("Could not connect. Server must be down.");
			// System.exit(1);
		}

		return false;
	}

	public static Stage getStage() {
		return stage;
	}

	public void startHand() {
		GetRandomStartHand grsh = new GetRandomStartHand();
		grsh.text = "";
		client.sendTCP(grsh);
	}

	public void setReadyUp() {
		ReadyUp ru = new ReadyUp();
		ru.isReady = true;
		client.sendTCP(ru);
	}

}
