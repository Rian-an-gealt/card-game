package client;

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> hand;

	public Hand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void printHand() {
		String output = "[";
		for (int i = 0; i < hand.size(); i++) {
			output += hand.get(i).getClass().getSimpleName() + " : " + hand.get(i).getWords()
					+ ", ";
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
	}

	public void sortHand() {
		ArrayList<Card> chars = new ArrayList<Card>();
		ArrayList<Card> atts = new ArrayList<Card>();
		for (Card c : hand) {
			if (c instanceof CharacterCard) {
				chars.add(c);
			}
			if (c instanceof AttributeCard) {
				atts.add(c);
			}
		}
		hand.clear();
		hand.addAll(chars);
		hand.addAll(atts);
	}
}