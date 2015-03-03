package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.Deck;

public class ServerMain {
	private static Socket clientSocket;
	private static Deck deck;

	public static void main(String[] args) throws IOException {
		int portNumber = 255;
		ServerSocket serverSocket;
		deck = new Deck();
		deck.init();
		boolean listening = true;
		try{
			serverSocket = new ServerSocket(portNumber);
			while(listening){
				new MultiServerThread(serverSocket.accept()).start();
			}
		} catch (IOException e){
			System.err.println("Could not even");
			System.exit(-1);
		}
	}

	public static Deck getDeck() {
		return deck;
	}
}
