package studytool;

import javafx.scene.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static javafx.scene.text.TextAlignment.*;
import javafx.stage.Stage;

/**
 *
 * @author John
 */
public class StudyTool extends Application {

    //global constants
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 800;
    private static final int BUTTON_WIDTH = 125;
    private static final int TEXT_SIZE = 18;
    private static final String MENU_IMAGE = "/images/tempimage.png";
    private static final String SCORES_OUTPUT = "./src/studytool/scores.txt";
    private static final String USER_DECK = "./src/flashcards/Custom Deck.txt";
    //buttons and panes for primary menus
    private BorderPane parentPane;
    private ImageView imgHome;
    private FlowPane mainMenu;
    private ListView listCustomDeck;
    private VBox editCardsMenu;
    private Button[] mainMenuButtons; //flashcards, quizes, scores
    private Button[] selectMenuButtons; //start and custom card editor
    private Button[] cardEditorButtons; //edit, remove, add cards to custom deck
    //flashcard members
    private boolean front;
    private Label cardCount;
    private ArrayList<FlashCard> customDeck;
    //quiz members
    private MCQuestion currentQuestion;
    private String[] choices;
    private int correct;
    //score data
    private QuizScores quizScores;

    @Override
    public void start(Stage primaryStage) {
        //home image and scores
        quizScores = new QuizScores(SCORES_OUTPUT);
        Image home = new Image(getClass().getResourceAsStream(MENU_IMAGE));
        imgHome = new ImageView(home);
        //main panes
        mainMenu = setMainMenu();
        listCustomDeck = loadCustomDeck();
        ListView listQuiz = setTopicMenu("src/multiplechoice");
        ListView listCardDecks = setTopicMenu("src/flashcards");
        ListView statsMenu = new ListView();
        String[] selectMenuNames = {"Start", "Edit Custom"};
        selectMenuButtons = new Button[selectMenuNames.length];
        VBox startMenu = setSelectionMenu(selectMenuNames, selectMenuButtons);
        String[] editMenuNames = {"Edit", "Remove", "Add New"};
        Button[] buttons = new Button[editMenuNames.length];
        editCardsMenu = setSelectionMenu(editMenuNames, buttons);
        //parent pane
        parentPane = new BorderPane();
        parentPane.setTop(mainMenu);
        parentPane.setCenter(imgHome);
        parentPane.setPadding(new Insets(20, 20, 20, 20));
        parentPane.setBackground(new Background(new BackgroundFill(
                Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        //card button
        mainMenuButtons[0].setOnAction(e -> {
            parentPane.setCenter(listCardDecks);
            parentPane.setRight(startMenu);
            selectMenuButtons[0].setText("Review");
            selectMenuButtons[1].setVisible(true);
        });
        //quiz button
        mainMenuButtons[1].setOnAction(e -> {
            parentPane.setCenter(listQuiz);
            parentPane.setRight(startMenu);
            selectMenuButtons[0].setText("Start Quiz");
            selectMenuButtons[1].setVisible(false);
        });
        //stats button
        mainMenuButtons[2].setOnAction(e -> {
            updateStats(statsMenu);
            parentPane.setCenter(statsMenu);
            parentPane.setRight(startMenu);
            selectMenuButtons[0].setText("Retake");
            selectMenuButtons[1].setVisible(false);
        });
        //custom card editor button
        selectMenuButtons[1].setOnAction(e -> {
            parentPane.setCenter(listCustomDeck);
            parentPane.setRight(editCardsMenu);
        });
        //start button
        selectMenuButtons[0].setOnAction(e -> {
            try {
                if (selectMenuButtons[0].getText().equals("Review")) {
                    String filename = listCardDecks.getSelectionModel()
                            .getSelectedItem().toString();
                    reviewCards(filename);
                } else if (selectMenuButtons[0].getText().equals("Start Quiz")) {
                    String filename = listQuiz.getSelectionModel()
                            .getSelectedItem().toString();
                    takeQuiz(filename);
                } else {
                    String filename = statsMenu.getSelectionModel().
                            getSelectedItem().toString();
                    int last = filename.lastIndexOf(":");
                    filename = filename.substring(0, last);
                    takeQuiz(filename);
                }
            } catch (NullPointerException ex) {
                //nothing selected on menu, do nothing
            }
        });
        //edit card button
        buttons[0].setOnAction(e -> {
            TextField[] inputs = new TextField[2];
            String cardText = listCustomDeck.getSelectionModel()
                    .getSelectedItem().toString();
            Scanner read = new Scanner(cardText);
            inputs[0] = new TextField(read.nextLine());
            inputs[1] = new TextField(read.nextLine());
            int index = findCardIndex(cardText);
            getNewCard(inputs, index);
        });
        //remove card button
        buttons[1].setOnAction(e -> {
            String cardText = listCustomDeck.getSelectionModel()
                    .getSelectedItem().toString();
            int index = findCardIndex(cardText);
            customDeck.remove(index);
            updateCards(listCustomDeck);
            saveCustomDeck();
        });
        //add new card button
        buttons[2].setOnAction(e -> {
            TextField[] inputs = new TextField[2];
            inputs[0] = new TextField();
            inputs[1] = new TextField();
            getNewCard(inputs, customDeck.size());
        });
        //scene and primary stage
        Scene scene = new Scene(parentPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("StudyTool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int findCardIndex(String cardText) {
        for (int i = 0; i < customDeck.size(); i++) {
            if (cardText.equals(customDeck.get(i).toString())) {
                return i;
            }
        }
        return -1;
    }
    
    private ListView loadCustomDeck() {
        ListView listCustomDeck = new ListView();
        ObservableList<String> cardList = FXCollections.observableArrayList();
        customDeck = new ArrayList<>();
        try {
            Scanner read = new Scanner(new File(USER_DECK));
            while (read.hasNextLine()) {
                String front = read.nextLine();
                String back = read.nextLine();
                if (read.hasNextLine()) {
                    read.nextLine();
                }
                FlashCard card = new FlashCard(front, back);
                customDeck.add(card);
                cardList.add(card.toString());
            }
        } catch (FileNotFoundException ex) {
            //no custom deck file found, leave empty
        }
        listCustomDeck.setItems(cardList);
        setCellFormat(listCustomDeck);
        return listCustomDeck;
    }
    
    private void saveCustomDeck() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_DECK))) {
            for (int i = 0; i < customDeck.size(); i++) {
                 pw.print(customDeck.get(i) + "\n\n");
            }
        } catch (IOException ex) {
            System.out.println("FAILED TO SAVE SCORES");
        }
    }
    
    private FlowPane setMainMenu() {
        FlowPane mainMenu = new FlowPane();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenuButtons = new Button[3];
        String[] buttonText = {"Flashcards", "Quizes", "Scores"};
        for (int i = 0; i < mainMenuButtons.length; i++) {
            mainMenuButtons[i] = createButton(buttonText[i], TEXT_SIZE, 10);
            FlowPane.setMargin(mainMenuButtons[i], new Insets(10, 10, 30, 10));
            mainMenu.getChildren().add(mainMenuButtons[i]);
        }
        return mainMenu;
    }

    private ListView setTopicMenu(String folderExtension) {
        ListView topicMenu = new ListView();
        ObservableList<String> topicList = FXCollections.observableArrayList();
        File folder = new File(folderExtension);
        for (File questionFile : folder.listFiles()) {
            if (!questionFile.isDirectory()) {
                String textfile = questionFile.getName();
                int end = textfile.lastIndexOf(".");
                String noExtension = textfile.substring(0, end);
                topicList.add(noExtension);
            }
        }
        topicMenu.setItems(topicList);
        setCellFormat(topicMenu);
        return topicMenu;
    }
    
    private VBox setSelectionMenu(String[] buttonNames, Button[] buttons) {
        VBox selectionMenu = new VBox();
        selectionMenu.setAlignment(Pos.CENTER);
        selectionMenu.setPadding(new Insets(10, 20, 10, 40));
        for (int i = 0; i < buttonNames.length; i++) {
            buttons[i] = createButton(buttonNames[i], TEXT_SIZE, 10);
            VBox.setMargin(buttons[i], new Insets(5, 0, 5, 0));
            selectionMenu.getChildren().add(buttons[i]);
        }
        return selectionMenu;
    }

    private void getNewCard(TextField[] inputs, int index) {
        VBox editMenu = new VBox();
        editMenu.setAlignment(Pos.CENTER);
        String[] labelText = {"Front of card:", "Back of card:"};
        Label[] labels = new Label[labelText.length];
        for (int i = 0; i < 2; i++) {
            labels[i] = new Label(labelText[i]);
            labels[i].setFont(Font.font(TEXT_SIZE));
            editMenu.getChildren().add(labels[i]);
            VBox.setMargin(labels[i], new Insets(5, 0, 5, 0));
            inputs[i].setFont(Font.font(TEXT_SIZE));
            editMenu.getChildren().add(inputs[i]);
            VBox.setMargin(inputs[i], new Insets(5, 0, 5, 0));
        }
        Label lblWarning = new Label();
        Button btnSubmit = createButton("Submit", TEXT_SIZE, 10);
        btnSubmit.setMaxWidth(BUTTON_WIDTH);
        editMenu.getChildren().add(btnSubmit);
        editMenu.getChildren().add(lblWarning);
        VBox.setMargin(btnSubmit, new Insets(10, 0, 0, 0));
        btnSubmit.setOnAction(e -> {
            String front = inputs[0].getText();
            String back = inputs[1].getText();
            if (front.length() == 0 || back.length() == 0) {
                lblWarning.setText("Please enter card text.");
            } else {
                lblWarning.setText("");
                FlashCard newCard = new FlashCard(front, back);
                if (index == customDeck.size()) {
                    customDeck.add(newCard);
                } else {
                    customDeck.set(index, newCard);
                }
                updateCards(listCustomDeck);
                saveCustomDeck();
                parentPane.setRight(editCardsMenu);
                parentPane.setCenter(listCustomDeck);
            }
        });
        parentPane.setRight(null);
        parentPane.setCenter(editMenu);
    }
    
    private void reviewCards(String filename) {
        //create main card button
        String filepath = "./src/flashcards/" + filename + ".txt";
        CardSet cardDeck = new CardSet(filepath);
        BorderPane cardPane = new BorderPane();
        Button btnCardText = createBigButton("", 48, 50);
        btnCardText.setBackground(new Background(new BackgroundFill(
                Color.SEASHELL, new CornerRadii(100), null)));
        btnCardText.setOnAction(e -> {
            front = !front;
            if (front) {
                btnCardText.setText(cardDeck.peekFront().getFront());
            } else {
                btnCardText.setText(cardDeck.peekFront().getBack());
            }
        });
        cardPane.setCenter(btnCardText);
        BorderPane.setMargin(btnCardText, new Insets(50, 0, 50, 50));
        //menu buttons and card count label
        String[] buttonNames = {"Keep Card", "Toss Card"};
        Button[] cardButtons = new Button[buttonNames.length];
        VBox buttonPane = setSelectionMenu(buttonNames, cardButtons);
        cardCount = new Label("Cards left: " + cardDeck.getSize());
        cardCount.setFont(Font.font(TEXT_SIZE));
        buttonPane.getChildren().add(cardCount);
        //keep and clear button events
        front = true;
        cardButtons[0].setOnAction(e -> {
            front = true;
            cardDeck.keepFront();
            btnCardText.setText(cardDeck.peekFront().getFront());
        });
        cardButtons[1].setOnAction(e -> {
            front = true;
            cardDeck.tossFront();
            btnCardText.setText(cardDeck.peekFront().getFront());
            cardCount.setText("Cards left: " + cardDeck.getSize());
            if (cardDeck.getSize() == 1) {
                cardButtons[1].setText("Finish");
                cardButtons[0].setVisible(false);
            } else if (cardDeck.getSize() == 0) {
                returnHome();
            } else {
                btnCardText.setText(cardDeck.peekFront().getFront());
            }
        });
        //finalize
        parentPane.getChildren().clear();
        parentPane.setCenter(cardPane);
        parentPane.setRight(buttonPane);
        cardButtons[0].fire();
    }

    private void takeQuiz(String filename) {
        String filepath = "./src/multiplechoice/" + filename + ".txt";
        MCQuestionSet questionSet = new MCQuestionSet(filepath);
        int questionCount = questionSet.getSize();
        currentQuestion = questionSet.peekNext();
        correct = 0;
        //create question text
        BorderPane questionPane = new BorderPane();
        Button btnQuestion = createBigButton("TEMP TESTING", 36, 25);
        questionPane.setCenter(btnQuestion);
        BorderPane.setMargin(btnQuestion, new Insets(20, 20, 20, 20));
        VBox choicePane = new VBox();
        //correct answer tracker
        VBox resultPane = new VBox();
        resultPane.setAlignment(Pos.CENTER);
        Label lblCorrect = new Label();
        lblCorrect.setFont(Font.font(TEXT_SIZE));
        resultPane.getChildren().add(lblCorrect);
        //next button trigger
        Button btnNext = createButton("Next", TEXT_SIZE, 10);
        Button btnFinish = createButton("Finish", TEXT_SIZE, 10);
        btnNext.setOnAction(e -> {
            currentQuestion = questionSet.getNext();
            if (currentQuestion == null) {
                btnFinish.fire();
            } else {
                choices = currentQuestion.getChoices();
                btnQuestion.setText(currentQuestion.getQuestion());
                choicePane.getChildren().clear();
                for (int i = 0; i < choices.length; i++) {
                    Button btnChoice = createBigButton(choices[i], 24, 25);
                    btnChoice.setOnAction(e2 -> {
                        if (currentQuestion.isCorrect(btnChoice.getText())) {
                            correct++;
                        }
                        lblCorrect.setText(correct + " correct answers!");
                        btnNext.fire();
                    });
                    choicePane.getChildren().add(btnChoice);
                    VBox.setMargin(btnChoice, new Insets(5, 50, 5, 50));
                }
            }
        });
        //submit final answer trigger and menu return button
        btnFinish.setOnAction(e -> {
            String message = "Quiz Complete!\n Score: " + correct + "/";
            message = message + questionCount + "\n Click to continue.";
            Button btnFinishMessage = createBigButton(message, 54, 100);
            btnFinishMessage.setOnAction(e2 -> {
                returnHome();
            });
            quizScores.add(filename, correct, questionCount);
            try (PrintWriter pw = new PrintWriter(new FileWriter(SCORES_OUTPUT))) {
                pw.print(quizScores);
            } catch (IOException ex) {
                System.out.println("FAILED TO SAVE SCORES");
            }
            parentPane.getChildren().clear();
            parentPane.setCenter(btnFinishMessage);
        });
        //finalize
        parentPane.getChildren().clear();
        parentPane.setTop(questionPane);
        parentPane.setCenter(choicePane);
        parentPane.setBottom(resultPane);
        btnNext.fire();
    }

    private void updateStats(ListView statsMenu) {
        ObservableList<String> scoresList = FXCollections.observableArrayList();
        for (int i = 0; i < quizScores.getSize(); i++) {
            scoresList.add(quizScores.getScore(i));
        }
        statsMenu.setItems(scoresList);
        setCellFormat(statsMenu);
    }
    
    private void updateCards(ListView cardMenu) {
        ObservableList<String> scoresList = FXCollections.observableArrayList();
        for (int i = 0; i < customDeck.size(); i++) {
            scoresList.add(customDeck.get(i).toString());
        }
        cardMenu.setItems(scoresList);
        setCellFormat(cardMenu);
    }
    
    private Button createButton(String name, int fontSize, int cornerSize) {
        Button button = new Button(name);
        button.setFont(Font.font(fontSize));
        button.setMinWidth(BUTTON_WIDTH);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setBackground(new Background(new BackgroundFill(
                Color.SEASHELL, new CornerRadii(cornerSize), null)));
        return button;
    }

    private Button createBigButton(String name, int fontSize, int cornerSize) {
        Button button = createButton(name, fontSize, cornerSize);
        button.setMaxHeight(Double.MAX_VALUE);
        button.wrapTextProperty().setValue(true);
        button.setTextAlignment(CENTER);
        return button;
    }

    private void returnHome() {
        parentPane.getChildren().clear();
        parentPane.setTop(mainMenu);
        parentPane.setCenter(imgHome);
    }
    
    private void setCellFormat(ListView listView) {
        listView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setFont(Font.font(TEXT_SIZE));
                    }
                }
            };
        });
    }
}
