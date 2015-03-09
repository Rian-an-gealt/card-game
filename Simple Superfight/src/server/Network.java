package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;

import client.AttributeCard;
import client.Card;
import client.CharacterCard;
import client.Hand;
import client.LocationCard;
import client.ScenarioCard;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(GetRandomStartHand.class);
		kryo.register(Card.class);
		kryo.register(Hand.class);
		kryo.register(Class.class);
		kryo.register(CharacterCard.class);
		kryo.register(AttributeCard.class);
		kryo.register(ScenarioCard.class);
		kryo.register(LocationCard.class);
		// kryo.register(LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(StartGame.class);
		kryo.register(ReadyUp.class);
		kryo.register(Boolean.class);
	}

	static public class RegisterName {
		public String name;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class ChatMessage {
		public String text;
	}

	static public class StartGame {
		public String text;
	}

	static public class ReadyUp {
		public boolean isReady;
	}

	static public class GetRandomStartHand {
		public String text;
	}

	static public class Deck {
		private LinkedHashMap<Class<? extends Card>, LinkedList<Card>> deck;
		private LinkedHashMap<Class<? extends Card>, LinkedList<Card>> usedDeck;

		// add

		public Hand randStartHand() {
			Random rand = new Random();
			int count = 0;
			ArrayList<Card> hand = new ArrayList<Card>();
			while (count < 3) {

				int chars = rand.nextInt(deck.get(CharacterCard.class).size());
				int atts = rand.nextInt(deck.get(AttributeCard.class).size());

				Card ch = deck.get(CharacterCard.class).get(chars);
				Card at = deck.get(AttributeCard.class).get(atts);

				deck.get(CharacterCard.class).remove(ch);
				deck.get(AttributeCard.class).remove(at);
				hand.add(ch);
				hand.add(at);
				usedDeck.get(CharacterCard.class).add(ch);
				usedDeck.get(AttributeCard.class).remove(at);

				// TODO
				count++;
			}
			Hand h = new Hand(hand);
			return h;
		}

		public void init() {
			deck = new LinkedHashMap<Class<? extends Card>, LinkedList<Card>>();
			deck.put(CharacterCard.class, new LinkedList<Card>());
			deck.put(AttributeCard.class, new LinkedList<Card>());
			deck.put(LocationCard.class, new LinkedList<Card>());
			deck.put(ScenarioCard.class, new LinkedList<Card>());

			usedDeck = new LinkedHashMap<Class<? extends Card>, LinkedList<Card>>();
			usedDeck.put(CharacterCard.class, new LinkedList<Card>());
			usedDeck.put(AttributeCard.class, new LinkedList<Card>());
			usedDeck.put(LocationCard.class, new LinkedList<Card>());
			usedDeck.put(ScenarioCard.class, new LinkedList<Card>());
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						"Cards.txt"));
				String line = br.readLine();
				int last = 0;
				while (line != null) {
					if (line.equals("[Characters]")) {
						last = 1;
					}
					if (line.equals("[Attributes]")) {
						last = 2;
					}
					if (line.equals("[Locations]")) {
						last = 3;
					}
					if (line.equals("[Scenarios]")) {
						last = 4;
					}
					if (last == 1 && !line.equals("[Characters]")) {
						CharacterCard cc = new CharacterCard(line);
						deck.get(CharacterCard.class).add(cc);
						// characters.add(line);
					}
					if (last == 2 && !line.equals("[Attributes]")) {
						AttributeCard ac = new AttributeCard(line);
						deck.get(AttributeCard.class).add(ac);
						// attributes.add(line);
					}
					if (last == 3 && !line.equals("[Locations]")) {
						LocationCard lc = new LocationCard(line);
						deck.get(LocationCard.class).add(lc);
						// locations.add(line);
					}
					if (last == 4 && !line.equals("[Scenarios]")) {
						ScenarioCard sc = new ScenarioCard(line);
						deck.get(ScenarioCard.class).add(sc);
						// scenarios.add(line);
					}
					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				System.out.println("Failed to load Cards.txt");
			}

		}

		public void printDeck() {
			System.out.println("Current Deck:");
			for (Entry<Class<? extends Card>, LinkedList<Card>> c : deck
					.entrySet()) {
				for (Card x : c.getValue()) {
					System.out.println(x.getWords());
				}
			}
		}

		public void printUsedDeck() {
			System.out.println("Current Used Deck: ");
			for (Entry<Class<? extends Card>, LinkedList<Card>> c : usedDeck
					.entrySet()) {
				for (Card x : c.getValue()) {
					System.out.println(x.getWords());
				}
			}
		}
	}
}
