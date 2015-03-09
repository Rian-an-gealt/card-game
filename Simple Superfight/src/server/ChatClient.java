package server;

import java.io.IOException;

import test.Network.ChatMessage;
import test.Network.RegisterName;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ChatClient {
    Client client;
    String name;

    public ChatClient () {
            client = new Client();
            client.start();

            // For consistency, the classes to be sent over the network are
            // registered by the same method for both the client and server.
            Network.register(client);

            client.addListener(new Listener() {
                    public void connected (Connection connection) {
                            RegisterName registerName = new RegisterName();
                            registerName.name = "Test";
                            client.sendTCP(registerName);
                    }

                    public void received (Connection connection, Object object) {
                            if (object instanceof ChatMessage) {
                                    ChatMessage chatMessage = (ChatMessage)object;
                                    System.out.println(chatMessage.text);
                                    //chatFrame.addMessage(chatMessage.text);
                                    return;
                            }
                    }

                    public void disconnected (Connection connection) {
                            
                    }
            });



                            //client.sendTCP(chatMessage);
        
            

            // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
            // Connecting to localhost is usually so fast you won't see the progress bar.
            new Thread("Connect") {
                    public void run () {
                            try {
                                    client.connect(5000, "localhost", Network.port);
                                    // Server communication after connection can go here, or in Listener#connected().
                                    System.out.println("Connected");
                                    ChatMessage chatM = new ChatMessage();
                                    chatM.text = "Hello";
                                    client.sendTCP(chatM);
                            } catch (IOException ex) {
                                    ex.printStackTrace();
                                    System.exit(1);
                            }
                    }
            }.start();
            
    }

    public static void main (String[] args) {
            new ChatClient();
    }
}
