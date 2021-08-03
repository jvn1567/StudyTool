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
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author John
 */
public class StudyTool extends Application {

    private BorderPane parentPane;
    private FlowPane mainMenu;
    private Button[] mainMenuButtons;
    private Button btnStart;
    private Label lblWarning;
    private ImageView imgHome;

    //TODO save and load scores
    private QuizScores quizScores;
    
    //TEMP
    private boolean front;

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
                    takeQuiz(filename + ".txt");
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
            mainMenuButtons[i] = new Button();
            FlowPane.setMargin(mainMenuButtons[i], new Insets(10, 10, 30, 10));
            mainMenuButtons[i].setFont(Font.font(16));
            mainMenuButtons[i].setText(buttonText[i]);
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
        btnStart = new Button();
        btnStart.setFont(Font.font(16));
        btnStart.setMinWidth(100);
        selectionMenu.setPadding(new Insets(10, 20, 10, 40));
        selectionMenu.getChildren().add(btnStart);
        lblWarning = new Label();
        selectionMenu.getChildren().add(lblWarning);
        lblWarning.setPadding(new Insets(10, 0, 10, 0));
        return selectionMenu;
    }

    private void reviewCards(String filename) {
        //initialize
        int index = 0;
        front = true;
        CardSet cardDeck = new CardSet(filename);
        //create main card button and navigation buttons
        Button btnCardText = new Button(cardDeck.peekFront().getFront());
        btnCardText.setFont(Font.font(36));
        btnCardText.setMaxWidth(Double.MAX_VALUE);
        btnCardText.setMaxHeight(Double.MAX_VALUE);
        btnCardText.wrapTextProperty().setValue(true);
        VBox buttonPane = new VBox();
        buttonPane.setAlignment(Pos.CENTER);
        String[] buttonNames = {"Flip Over", "Keep Card", "Toss Card"};
        Button[] cardButtons = new Button[3];
        for (int i = 1; i < 3; i++) {
            cardButtons[i] = new Button(buttonNames[i]);
            buttonPane.setMargin(cardButtons[i], new Insets(0, 10, 10, 10));
            cardButtons[i].setFont(Font.font(16));
            cardButtons[i].setMaxWidth(Double.MAX_VALUE);
            buttonPane.getChildren().add(cardButtons[i]);
        }
        parentPane.getChildren().clear();
        parentPane.setCenter(btnCardText);
        parentPane.setRight(buttonPane);
        //flip card
        btnCardText.setOnAction(e->{
            front = !front;
            if (front) {
                btnCardText.setText(cardDeck.peekFront().getFront());
            } else {
                btnCardText.setText(cardDeck.peekFront().getBack());
            }
        });
        //flip button
        btnCardText.setOnAction(e->{
            front = !front;
            if (front) {
                btnCardText.setText(cardDeck.peekFront().getFront());
            } else {
                btnCardText.setText(cardDeck.peekFront().getBack());
            }
        });
        //keep and toss TODO factor out redundancy
        cardButtons[1].setOnAction(e->{
            front = true;
            cardDeck.keepFront();
            if (cardDeck.getSize() == 0) {
                parentPane.getChildren().clear();
                parentPane.setTop(mainMenu);
                parentPane.setCenter(imgHome);
            } else {
                btnCardText.setText(cardDeck.peekFront().getFront());
            }
        });
        cardButtons[2].setOnAction(e->{
            front = true;
            cardDeck.tossFront();
            if (cardDeck.getSize() == 0) {
                parentPane.getChildren().clear();
                parentPane.setTop(mainMenu);
                parentPane.setCenter(imgHome);
            } else {
                btnCardText.setText(cardDeck.peekFront().getFront());
            }
        });
    }

    private void takeQuiz(String filename) {
        //System.out.println(filename + " TEST");
    }
}
