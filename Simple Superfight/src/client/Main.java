package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import clientUI.ScreenManager;

public class Main extends Application {

	private static Hand hand1;
	private static Hand hand2;
	private static String hostName;
	private static int portNum;
	private static Deck deck;
	private static Socket socket;
	private static String username;
	private static Stage stage;
	private static PrintWriter out;
	private static BufferedReader in;
	private static ScreenManager sm;

	public static void main(String[] args) {
		launch(args);
		hostName = "server";
		portNum = 255;
	}

	private static TextArea output;
	private TextField input;

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		sm = new ScreenManager();
		sm.createStart(stage);
		sm.createChat(stage);
		sm.createGame(stage);
		sm.setScreen("start");
		// startScreen(arg0);
		// StartScreen start = new StartScreen(arg0);
	}

	public static void sendMessage(String text) {
		out.println(text);
		System.out.println("Sent message from main");
	}

	public static ScreenManager getScreenManager() {
		return sm;
	}

	public static boolean connect(String name) {
		String hostName = "localhost";
		int portNumber = 255;
		try {
			socket = new Socket(hostName, portNumber);
			ScreenManager.printMessage(name + " connected!");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out.println(name);
		} catch (IOException e) {
			System.err.println("Connection Failed.");
		}
		return true;
	}

	public static void setUsername(String text) {
		username = text;
	}

	public static Deck getDeck() {
		return deck;
	}

	public static void setDeck(Deck deck) {
		Main.deck = deck;
	}

	public static Stage getStage() {
		return stage;
	}

	public static Socket getSocket() {
		return socket;
	}

}
