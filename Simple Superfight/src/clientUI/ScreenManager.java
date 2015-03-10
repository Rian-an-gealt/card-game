package clientUI;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import client.Main;

public class ScreenManager extends StackPane {
	private HashMap<String, Scene> screens = new HashMap<>();
	private static TextArea output;
	private TextField input;
	private String username;
	private Main main;
	private Button yours1;
	private Button yours2;
	private Button yours3;
	private Button yours4;
	private Button yours5;
	private Button yours6;
	private Button theirs1;
	private Button theirs2;
	private Button theirs3;
	private String selectedChar;
	private String selectedAtt;

	public ScreenManager(Main main) {
		super();
		this.main = main;
	}

	public void addScreen(String name, Scene screen) {
		screens.put(name, screen);
	}

	public Scene getScreen(String name) {
		return screens.get(name);
	}

	public void setButtonName(int number, String text) {
		switch (number) {
		case 1:
			Platform.runLater(() -> {
				yours1.setText(text);
			});
		case 2:
			Platform.runLater(() -> {
				yours2.setText(text);
			});
		case 3:
			Platform.runLater(() -> {
				yours3.setText(text);
			});
		case 4:
			Platform.runLater(() -> {
				yours4.setText(text);
			});
		case 5:
			Platform.runLater(() -> {
				yours5.setText(text);
			});
		case 6:
			Platform.runLater(() -> {
				yours6.setText(text);
			});
		default:
			System.out.println("Failed to update button");
		}
	}

	public void setTheirButtonName(int number, String text) {
		switch (number) {
		case 1:
			Platform.runLater(() -> {
				theirs1.setText(text);
			});
		case 2:
			Platform.runLater(() -> {
				theirs2.setText(text);
			});
		case 3:
			Platform.runLater(() -> {
				theirs3.setText(text);
			});
		default:
			System.out.println("Failed to update Their buttons");
		}
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

	public void printMessage(String msg) {
		// Platform.runLater(() -> {output.appendText(sender + ": " + msg +
		// "\n");});
		if (getOutputArea() != null) {
			// getOutputArea().appendText(msg + "\n");
			Platform.runLater(() -> {
				output.appendText(msg + "\n");
			});
		}
	}

	public void createLobby(Stage stage) {
		// Group root = new Group();
		// stage.setScene(new Scene(root, 650, 500));
		selectedAtt = "";
		selectedChar = "";
		VBox mainPanel = new VBox();
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.setPadding(new Insets(8.0));
		mainPanel.setSpacing(8.0);

		GridPane chatGrid = new GridPane();
		chatGrid.setAlignment(Pos.BOTTOM_CENTER);
		VBox box = new VBox();
		box.setPadding(new Insets(8.0));
		box.setSpacing(8.0);

		output = new TextArea();
		box.getChildren().add(output);
		chatGrid.add(box, 0, 0);
		output.setEditable(false);
		output.setStyle("-fx-border-style: none");
		output.setFocusTraversable(false);

		HBox chatLine = new HBox();
		chatLine.setPadding(new Insets(8.0));
		chatLine.setSpacing(8.0);
		chatGrid.add(chatLine, 0, 1);
		input = new TextField();
		input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					System.out.println(input.getText());
					main.sendMessage(input.getText());
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
				main.sendMessage(input.getText());
				input.clear();
			}
		});

		Button start = new Button();
		start.setText("Ready");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// createGame(stage);
				// setScreen("game");
				main.setReadyUp();
			}
		});
		VBox vbox = new VBox();
		// Platform.runLater(() -> {
		// main.startDeck();
		// main.startHand();
		// hand = main.getStartHand();
		// });

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.BOTTOM_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		yours1 = new Button();
		yours1.setMaxSize(200, 200);
		yours1.setWrapText(true);
		yours1.setAlignment(Pos.TOP_LEFT);
		yours2 = new Button();
		yours2.setMaxSize(200, 200);
		yours2.setWrapText(true);
		yours2.setAlignment(Pos.TOP_LEFT);
		yours3 = new Button();
		yours3.setMaxSize(200, 200);
		yours3.setWrapText(true);
		yours3.setAlignment(Pos.TOP_LEFT);
		yours4 = new Button();
		yours4.setMaxSize(200, 200);
		yours4.setWrapText(true);
		yours4.setAlignment(Pos.TOP_LEFT);
		yours5 = new Button();
		yours5.setMaxSize(200, 200);
		yours5.setWrapText(true);
		yours5.setAlignment(Pos.TOP_LEFT);
		yours6 = new Button();
		yours6.setMaxSize(200, 200);
		yours6.setWrapText(true);
		yours6.setAlignment(Pos.TOP_LEFT);
		yours1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours2.disableProperty().set(true);
				yours3.disableProperty().set(true);
				selectedChar = yours1.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 1");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		yours2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours1.disableProperty().set(true);
				yours3.disableProperty().set(true);
				selectedChar = yours2.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 2");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		yours3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours2.disableProperty().set(true);
				yours1.disableProperty().set(true);
				selectedChar = yours3.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 3");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		yours4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours5.disableProperty().set(true);
				yours6.disableProperty().set(true);
				selectedAtt = yours4.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 4");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		yours5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours4.disableProperty().set(true);
				yours6.disableProperty().set(true);
				selectedAtt = yours5.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 5");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		yours6.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				yours5.disableProperty().set(true);
				yours4.disableProperty().set(true);
				selectedAtt = yours6.getText();
				if (!selectedChar.equals("") && !selectedAtt.equals("")) {
					System.out.println("Sending in 6");
					main.setCardsSelected(selectedChar, selectedAtt);
				}
			}
		});
		grid.add(yours1, 0, 0);
		grid.add(yours2, 0, 1);
		grid.add(yours3, 0, 2);
		grid.add(yours4, 1, 0);
		grid.add(yours5, 1, 1);
		grid.add(yours6, 1, 2);

		GridPane theirCards = new GridPane();
		theirCards.setAlignment(Pos.TOP_CENTER);
		theirCards.setHgap(10);
		theirCards.setVgap(10);
		theirCards.setPadding(new Insets(25, 25, 25, 25));
		theirs1 = new Button();
		theirs1.setMaxSize(200, 200);
		theirs1.setWrapText(true);
		theirs1.setAlignment(Pos.TOP_LEFT);
		theirs2 = new Button();
		theirs2.setMaxSize(200, 200);
		theirs2.setWrapText(true);
		theirs2.setAlignment(Pos.TOP_LEFT);
		theirs3 = new Button();
		theirs3.setMaxSize(200, 200);
		theirs3.setWrapText(true);
		theirs3.setAlignment(Pos.TOP_LEFT);
		theirCards.add(theirs1, 0, 0);
		theirCards.add(theirs2, 0, 1);
		theirCards.add(theirs3, 0, 2);
		vbox.getChildren().addAll(theirCards, grid);

		chatLine.getChildren().add(send);
		chatLine.getChildren().add(start);
		mainPanel.getChildren().add(vbox);
		mainPanel.getChildren().add(chatGrid);

		// root.getChildren().add(mainPanel);
		stage.show();
		input.requestFocus();
		Scene scene = new Scene(mainPanel, 1200, 800);
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
						} else {
							// TODO print error on the screen
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
					} else {
						// TODO print error on the screen
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
					main.stop();
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

		Scene scene = new Scene(border, 1200, 800);

		// Scene scene = new Scene(buttonGrid, 650, 500);
		// stage.setTitle("Test Game");
		// stage.setScene(scene);
		// stage.show();
		addScreen("start", scene);
	}
}
