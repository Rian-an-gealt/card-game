package clientUI;

import java.util.HashMap;

import server.ServerMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import client.Card;
import client.Deck;
import client.Hand;
import client.Main;

public class ScreenManager extends StackPane {
	private HashMap<String, Scene> screens = new HashMap<>();
	private static TextArea output;
	private TextField input;
	private String username;
	private Deck deck;
	private Hand hand1;
	private Hand hand2;

	public ScreenManager() {
		super();
	}

	public void addScreen(String name, Scene screen) {
		screens.put(name, screen);
	}

	public Scene getScreen(String name) {
		return screens.get(name);
	}

	public boolean setScreen(final String name) {
		if (screens.get(name) != null) {
			setOpacity(0.0);
			// getChildren().add(screens.get(name));
			Main.getStage().setScene(screens.get(name));
			return true;
		} else {
			System.out.println("screen hasn't been loaded!!! \n");
			return false;
		}
	}

	public boolean unloadScreen(String name) {
		if (screens.remove(name) == null) {
			System.out.println("Screen didn't exist");
			return false;
		} else {
			return true;
		}
	}

	public static TextArea getOutputArea() {
		return output;
	}
	
	public static void printMessage(String msg) {
		// Platform.runLater(() -> {output.appendText(sender + ": " + msg +
		// "\n");});
		if (getOutputArea() != null) {
			getOutputArea().appendText(msg + "\n");
		}
	}

	public void createGame(Stage stage) {
		deck = new Deck();
		deck.init();
		deck.printDeck();
		hand1 = deck.randStartHand();
		hand1.sortHand();
		hand2 = deck.randStartHand();
		hand2.sortHand();
		System.out.println("Player 1 Hand:");
		hand1.printHand();
		System.out.println("Player 2 Hand:");
		hand2.printHand();
		deck.printDeck();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		int xCount = 0;
		int yCount = 0;
		// deck = ServerMain.getDeck();
		// hand1 = deck.randStartHand();
		// hand1.sortHand();
		// hand2 = deck.randStartHand();
		// hand2.sortHand();
		for (Card c : hand1.getHand()) {
			Button char1 = new Button();
			char1.setText(c.getWords());
			char1.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println(char1.getText());
				}
			});
			char1.setMaxSize(200, 200);
			char1.setWrapText(true);
			char1.setAlignment(Pos.TOP_LEFT);
			grid.add(char1, xCount, yCount);
			if (xCount == 2) {
				xCount = 0;
				yCount++;
			} else {
				xCount++;
			}
		}
		Scene scene = new Scene(grid, 650, 500);
		addScreen("game", scene);
	}

	public void createChat(Stage stage) {
		Group root = new Group();
		// stage.setScene(new Scene(root, 650, 500));
		GridPane grid = new GridPane();
		VBox box = new VBox();
		box.setPadding(new Insets(8.0));
		box.setSpacing(8.0);

		output = new TextArea();
		box.getChildren().add(output);
		grid.add(box, 0, 0);
		output.setEditable(false);
		output.setStyle("-fx-border-style: none");
		output.setFocusTraversable(false);

		HBox chatLine = new HBox();
		chatLine.setPadding(new Insets(8.0));
		chatLine.setSpacing(8.0);
		grid.add(chatLine, 0, 1);
		input = new TextField();
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String text = "[" + username + "] " + input.getText()
							+ "\n";
					//output.appendText(text);
					System.out.println(text);
					Main.sendMessage(text);
					input.clear();
				}
			}
		});
		chatLine.getChildren().add(input);

		Button send = new Button();
		send.setText("Send");
		send.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String text = "[" + username + "] " + input.getText() + "\n";
				//output.appendText(text);
				System.out.println(text);
				Main.sendMessage(text);
				input.clear();
			}
		});

		Button start = new Button();
		start.setText("Start");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setScreen("game");
			}
		});

		chatLine.getChildren().add(send);
		chatLine.getChildren().add(start);
		root.getChildren().add(grid);
		stage.show();
		input.requestFocus();
		Scene scene = new Scene(root, 650, 500);
		addScreen("chat", scene);
	}

	public void createStart(Stage stage) {
		GridPane loginGrid = new GridPane();
		loginGrid.setAlignment(Pos.CENTER);
		loginGrid.setHgap(10);
		loginGrid.setVgap(10);
		loginGrid.setPadding(new Insets(25, 25, 25, 25));
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		loginGrid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		loginGrid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					String text = userTextField.getText();
					System.out.println(text);
					if (!text.isEmpty()) {
						username = text;
						if (Main.connect(username)) {
							setScreen("chat");
						}
					} else {
						// TODO print error on the screen
					}
				}
			}
		});
		loginGrid.add(userTextField, 1, 1);

		BorderPane border = new BorderPane();
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		Button start = new Button();
		start.setText("Start");
		start.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String text = userTextField.getText();
				System.out.println(text);
				if (!text.isEmpty()) {
					username = text;
					if (Main.connect(username)) {
						setScreen("chat");
					}
				} else {
					// TODO print error on the screen
				}
			}
		});
		Button exit = new Button();
		exit.setText("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println(exit.getText());
				try {
					// TODO
				} catch (Exception e) {
					System.err.println("Failed to stop the client");
				}
			}
		});
		start.setPrefSize(100, 20);
		exit.setPrefSize(100, 20);
		hbox.getChildren().addAll(start, exit);
		border.setBottom(hbox);

		border.setCenter(loginGrid);

		Scene scene = new Scene(border, 650, 500);

		// Scene scene = new Scene(buttonGrid, 650, 500);
		// stage.setTitle("Test Game");
		// stage.setScene(scene);
		// stage.show();
		addScreen("start", scene);
	}
}
