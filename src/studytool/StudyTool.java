/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studytool;

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
    private Button[] mainMenuButtons;
    private Button btnStart;

    @Override
    public void start(Stage primaryStage) {
        //panes
        FlowPane mainMenu = setMainMenu();
        ListView topicMenuQuiz = setTopicMenu("src/multiplechoice");
        ListView topicMenuCards = setTopicMenu("src/flashcards");
        HBox selectionMenu = setSelectionMenu();
        //parent pane
        parentPane = new BorderPane();
        parentPane.setTop(mainMenu);
        parentPane.setCenter(topicMenuCards);
        parentPane.setRight(selectionMenu);
        parentPane.setPadding(new Insets(20, 20, 20, 20));
        //card topic menu
        mainMenuButtons[0].setOnAction(e->{
            parentPane.getChildren().remove(topicMenuQuiz);
            parentPane.setCenter(topicMenuCards);
            parentPane.setRight(selectionMenu);
            btnStart.setText("Review");
        });
        //quiz topic menu
        mainMenuButtons[1].setOnAction(e->{
            parentPane.getChildren().remove(topicMenuCards);
            parentPane.setCenter(topicMenuQuiz);
            parentPane.setRight(selectionMenu);
            btnStart.setText("Start Quiz");
        });
        //stats menu
        mainMenuButtons[2].setOnAction(e->{
            parentPane.getChildren().remove(topicMenuCards);
            parentPane.getChildren().remove(topicMenuQuiz);
            parentPane.getChildren().remove(selectionMenu);
        });
        //start button
        btnStart.setOnAction(e->{
            //parentPane.getChildren().remove(mainMenu);
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
        String[] buttonText = {"Flashcards", "Quizes", "Stats"};
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
        //add all .txt files to the list of topics
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
        //set ListView font size
        topicMenu.setCellFactory(cell -> {
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
        return topicMenu;
    }
    
    private HBox setSelectionMenu() {
        HBox selectionMenu = new HBox();
        selectionMenu.setAlignment(Pos.CENTER);
        btnStart = new Button("Start Quiz");
        btnStart.setFont(Font.font(16));
        selectionMenu.setPadding(new Insets(10, 20, 10, 40));
        selectionMenu.getChildren().add(btnStart);
        return selectionMenu;
    }
}
