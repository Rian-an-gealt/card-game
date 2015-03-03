package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import client.Main;
import clientUI.ScreenManager;

public class MultiServerThread extends Thread {
	private Socket socket = null;
	private static ScreenManager sm;

	public MultiServerThread(Socket socket) {
		super("MultiServerThread");
		this.socket = socket;
		sm = Main.getScreenManager();
	}

	public static void setScreenManager(ScreenManager screen) {
		sm = screen;
	}

	public void run() {

		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));) {
			String inputLine, outputLine;
			// KnockKnockProtocol kkp = new KnockKnockProtocol();
			String output = "";
			// outputLine = kkp.processInput(null);
			out.println(output);

			String friendName = in.readLine();
			String input;
			while (true) {
				try {
					input = in.readLine();
					System.out.println(input);
					sm.printMessage(input);
				} catch (SocketException s) {
					System.err.println(friendName + ": connection lost");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
