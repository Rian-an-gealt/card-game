package client;

public abstract class Card {
	private String words;
	
	public Card(){
		words = "";
	}

	public Card(String words) {
		this.words = words;
	}

	public String getWords() {
		return words;
	}
}