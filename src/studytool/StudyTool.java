/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studytool;

import javafx.scene.image.*;
import java.io.File;
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

    private static final int BUTTON_WIDTH = 100;

    //buttons and panes for primary menus
    private BorderPane parentPane;
    private ImageView imgHome;
    private FlowPane mainMenu;
    private Button[] mainMenuButtons;
    private Button btnStart;
    //private Label lblWarning;

    //flashcard members
    private boolean front;
    private Label cardCount;
    //quiz members
    private MCQuestion currentQuestion;
    private String[] choices;
    private int correct; //TEMP

    //TODO save and load scores
    private QuizScores quizScores;

    @Override
    public void start(Stage primaryStage) {
        //home image
        Image home = new Image(getClass().getResourceAsStream(
                "/images/tempimage.png"));
        imgHome = new ImageView(home);
        //main panes
        mainMenu = setMainMenu();
        ListView topicMenuQuiz = setTopicMenu("src/multiplechoice");
        ListView topicMenuCards = setTopicMenu("src/flashcards");
        VBox selectionMenu = setSelectionMenu();
        //parent pane
        parentPane = new BorderPane();
        parentPane.setTop(mainMenu);
        parentPane.setCenter(imgHome);
        parentPane.setPadding(new Insets(20, 20, 20, 20));
        parentPane.setBackground(new Background(new BackgroundFill(
                Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        //card topic menu
        mainMenuButtons[0].setOnAction(e -> {
            parentPane.getChildren().clear();
            parentPane.setTop(mainMenu);
            parentPane.setCenter(topicMenuCards);
            parentPane.setRight(selectionMenu);
            btnStart.setText("Review");
        });
        //quiz topic menu
        mainMenuButtons[1].setOnAction(e -> {
            parentPane.getChildren().clear();
            parentPane.setTop(mainMenu);
            parentPane.setCenter(topicMenuQuiz);
            parentPane.setRight(selectionMenu);
            btnStart.setText("Start Quiz");
        });
        //stats menu
        mainMenuButtons[2].setOnAction(e -> {
            parentPane.getChildren().clear();
            parentPane.setTop(mainMenu);
            //parentPane.setCenter(statsMenu);
        });
        //start button
        btnStart.setOnAction(e -> {
            //parentPane.getChildren().clear();
            try {
                if (btnStart.getText().equals("Review")) {
                    String filename = topicMenuCards.getSelectionModel().getSelectedItem().toString();
                    parentPane.getChildren().clear();
                    reviewCards("./src/flashcards/" + filename + ".txt");
                } else {
                    String filename = topicMenuQuiz.getSelectionModel().getSelectedItem().toString();
                    parentPane.getChildren().clear();
                    takeQuiz("./src/multiplechoice/" + filename + ".txt");
                }
            } catch (NullPointerException ex) {
                //lblWarning.setText("Nothing selected.");
            }
        });
        //scene
        Scene scene = new Scene(parentPane, 800, 600);
        //primary stage
        primaryStage.setTitle("StudyTool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private FlowPane setMainMenu() {
        FlowPane mainMenu = new FlowPane();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenuButtons = new Button[3];
        String[] buttonText = {"Flashcards", "Quizes", "Scores"};
        for (int i = 0; i < mainMenuButtons.length; i++) {
            mainMenuButtons[i] = createButton(buttonText[i], 16, 10);
            FlowPane.setMargin(mainMenuButtons[i], new Insets(10, 10, 30, 10));
            mainMenu.getChildren().add(mainMenuButtons[i]);
        }
        return mainMenu;
    }

    private ListView setTopicMenu(String folderExtension) {
        //add all .txt files in the passed-in folder to the list of topics
        ListView topicMenu = new ListView();
        ObservableList<String> topicList = FXCollections.observableArrayList();
        File folder = new File(folderExtension);
        for (File questionFile : folder.listFiles()) {
            if (!questionFile.isDirectory()) {
                String textfile = questionFile.getName();
                int end = textfile.indexOf(".");
                String noExtension = textfile.substring(0, end);
                topicList.add(noExtension);
            }
        }
        topicMenu.setItems(topicList);
        setCellFormat(topicMenu);
        return topicMenu;
    }

    private void setCellFormat(ListView listView) {
        listView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setFont(Font.font(16));
                    }
                }
            };
        });
    }

    private VBox setSelectionMenu() {
        VBox selectionMenu = new VBox();
        selectionMenu.setAlignment(Pos.CENTER);
        btnStart = createButton("", 16, 10);
        selectionMenu.setPadding(new Insets(10, 20, 10, 40));
        selectionMenu.getChildren().add(btnStart);
        /*lblWarning = new Label();
        selectionMenu.getChildren().add(lblWarning);
        lblWarning.setPadding(new Insets(10, 0, 10, 0));*/
        return selectionMenu;
    }

    private void reviewCards(String filename) {
        //create main card button
        CardSet cardDeck = new CardSet(filename);
        BorderPane cardPane = new BorderPane();
        Button btnCardText = createBigTextButton("", 36, 50);
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
        BorderPane.setMargin(btnCardText, new Insets(50, 50, 50, 50));
        //menu buttons and card count label
        VBox buttonPane = new VBox();
        buttonPane.setAlignment(Pos.CENTER);
        String[] buttonNames = {"Keep Card", "Toss Card"};
        Button[] cardButtons = new Button[buttonNames.length];
        for (int i = 0; i < cardButtons.length; i++) {
            cardButtons[i] = createButton(buttonNames[i], 16, 10);
            VBox.setMargin(cardButtons[i], new Insets(0, 10, 10, 10));
            buttonPane.getChildren().add(cardButtons[i]);
        }
        cardCount = new Label();
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
            cardCount.setText("Cards left: " + cardDeck.getSize());
            cardCount.setFont(Font.font(16));
            if (cardDeck.getSize() == 1) {
                cardButtons[1].setText("Finish");
                buttonPane.getChildren().remove(cardButtons[0]);
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
        MCQuestionSet questionSet = new MCQuestionSet(filename);
        int questionCount = questionSet.getSize();
        currentQuestion = questionSet.peekNext();
        correct = 0;
        //TODO for previous: boolean[] correct = new boolean[questionSet.getSize()];
        //create question text
        BorderPane questionPane = new BorderPane();
        Button btnQuestion = createBigTextButton("TEMP TESTING", 28, 25);
        questionPane.setCenter(btnQuestion);
        BorderPane.setMargin(btnQuestion, new Insets(20, 20, 20, 20));
        VBox choicePane = new VBox();
        //questions navigation menu TODO back button
        VBox menuPane = new VBox();
        menuPane.setAlignment(Pos.CENTER);
        String[] buttonNames = {"Next", "Previous", "Submit"};
        Button[] quizButtons = new Button[buttonNames.length];
        for (int i = 0; i < buttonNames.length; i++) {
            quizButtons[i] = createButton(buttonNames[i], 16, 10);
            VBox.setMargin(quizButtons[i], new Insets(0, 10, 10, 10));
            menuPane.getChildren().add(quizButtons[i]);
        }
        Label lblQuestionNumber = new Label();
        lblQuestionNumber.setFont(Font.font(14));
        menuPane.getChildren().add(lblQuestionNumber);
        //next and answer buttons
        quizButtons[0].setOnAction(e -> {
            currentQuestion = questionSet.getNext();
            if (questionSet.getSize() == 0) {
                menuPane.getChildren().remove(quizButtons[0]);
            } 
            if (currentQuestion == null) {
                quizButtons[2].fire();
            } else {
                choices = currentQuestion.getChoices();
                btnQuestion.setText(currentQuestion.getQuestion());
                choicePane.getChildren().clear();
                for (int i = 0; i < choices.length; i++) {
                    Button btnChoice = createBigTextButton(choices[i], 24, 25);
                    btnChoice.setOnAction(e2 -> {
                        if (currentQuestion.getAnswer().equals(btnChoice.getText())) {
                            correct++;
                        }
                        lblQuestionNumber.setText(correct + " correct answers!");
                        quizButtons[0].fire();
                    });
                    choicePane.getChildren().add(btnChoice);
                    VBox.setMargin(btnChoice, new Insets(5, 50, 5, 50));
                }
            }
        });
        //submit and return home
        quizButtons[2].setOnAction(e -> {
            returnHome();
        });
        //finalize
        parentPane.getChildren().clear();
        parentPane.setTop(questionPane);
        parentPane.setCenter(choicePane);
        parentPane.setRight(menuPane);
        quizButtons[0].fire();
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

    private Button createBigTextButton(String name, int fontSize, int cornerSize) {
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
}
