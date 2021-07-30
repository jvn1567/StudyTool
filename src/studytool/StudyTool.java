/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studytool;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    
    @Override
    public void start(Stage primaryStage) {
        //panes
        FlowPane mainMenuPane = setMainMenu();
        ScrollPane topicMenu = setTopicMenu();
        //parent pane
        parentPane = new BorderPane();
        parentPane.setTop(mainMenuPane);
        parentPane.setCenter(topicMenu);
        //button events
        
        //scene
        Scene scene = new Scene(parentPane, 1200, 800);
        //primary stage
        primaryStage.setTitle("StudyTool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private FlowPane setMainMenu() {
        FlowPane mainMenu = new FlowPane();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenuButtons = new Button[3];
        String[] buttonText = {"PLACEHOLDER", "Home", "Stats"};
        for (int i = 0; i < mainMenuButtons.length; i++) {
            mainMenuButtons[i] = new Button();
            FlowPane.setMargin(mainMenuButtons[i], new Insets(10, 10, 10, 10));
            mainMenuButtons[i].setFont(Font.font(15));
            mainMenuButtons[i].setText(buttonText[i]);
            mainMenu.getChildren().add(mainMenuButtons[i]);
        }
        return mainMenu;
    }
    
    private ScrollPane setTopicMenu() {
        ScrollPane topicMenu = new ScrollPane();
        
        return topicMenu;
    }
}
