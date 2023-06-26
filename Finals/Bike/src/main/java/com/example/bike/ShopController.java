package com.example.bike;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ShopController {
    
    @FXML
    private FlowPane flexPane;

    @FXML
    private ScrollPane itemScrollPane;

    private final ArrayList<String> gameNames = new ArrayList<>();

    @FXML
    private void initialize() {
        try(BufferedReader reader = new BufferedReader(new FileReader("gamenames.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                gameNames.add(line.strip());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }


        try(BufferedReader reader = new BufferedReader(new FileReader("games.txt"))){
            String line;
            int index = 0;
            while((line = reader.readLine()) != null) {
                System.out.println("[Debug] " + line);
                String[] gameInfo = line.split(",");
                String savedGameName = gameInfo[0].strip();
                String savedGamePerformanceRequired = gameInfo[1].strip();
                String savedGameImageURL = gameInfo[2].strip();


                HBox hbox = new HBox();
                hbox.alignmentProperty().setValue(Pos.TOP_LEFT);
                hbox.setPrefHeight(225);
                hbox.setPrefWidth(250);


                VBox vbox = new VBox();
                hbox.getChildren().add(vbox);
                HBox.setHgrow(vbox, Priority.ALWAYS);
                vbox.alignmentProperty().setValue(Pos.TOP_CENTER);
                vbox.setSpacing(10);
                vbox.getStylesheets().add(getClass().getResource("shop-styles.css").toExternalForm());
                vbox.getStyleClass().add("GameBackground");

                ImageView gameImageView = new ImageView();
                gameImageView.setImage(new Image(getClass().getResource(savedGameImageURL).toExternalForm()));
                gameImageView.setFitWidth(236);
                gameImageView.setFitHeight(145);
                gameImageView.setSmooth(true);
                gameImageView.setPreserveRatio(true);
                gameImageView.getStyleClass().add("ElementalShadow");

                Text gameName = new Text();
                gameName.getStyleClass().add("GameNameText");
                gameName.setText(gameNames.get(index));

                Button gameButton = new Button();
                gameButton.setText("Check Compatibility");
                gameButton.setId(savedGameName + "-Button");
                gameButton.getStyleClass().add("compat-button");

                gameButton.setOnAction(event -> {
                    try {
                        onClick(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });



                vbox.getChildren().add(gameImageView);
                vbox.getChildren().add(gameName);
                vbox.getChildren().add(gameButton);

                flexPane.getChildren().add(hbox);
                index++;
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource(); // Get which Button is clicked, since this function is for multiple buttons;
        String clickedButtonID = clickedButton.getId(); // The info is stored in the css ID.
        String[] clickedButtonIDInfo = clickedButtonID.split("-"); // The format of css ID is "GameName-Button"
        System.out.println(Arrays.toString(clickedButtonIDInfo));

        String gameInfo = clickedButtonIDInfo[0].toLowerCase();   // Get the Info out for use.
        System.out.println(gameInfo);
        String savedGameName = null, performanceRequired = null, imageName = null; // Null are for initialization

        try(BufferedReader reader = readGameFile()) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] savedInfo = line.split(",");
                savedGameName = savedInfo[0].trim();
                performanceRequired = savedInfo[1].trim();
                imageName = savedInfo[2].trim();

                if(gameInfo.equals(savedGameName)) { // we found the game.
                    break;
                }

            }
        } catch (IOException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        FXMLLoader loader = loadCompatFile();
        Stage currentStage = (Stage) clickedButton.getScene().getWindow();
        CompatController controller = new CompatController();
        controller.setGameImageName(imageName);
        controller.setGamePerformanceRequired(Integer.parseInt(performanceRequired));
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        currentStage.setScene(scene);
        currentStage.show();
    }

    private BufferedReader readGameFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader("games.txt"));
    }

    private FXMLLoader loadCompatFile() {
        return new FXMLLoader(getClass().getResource("Compat.fxml"));
    }


}
