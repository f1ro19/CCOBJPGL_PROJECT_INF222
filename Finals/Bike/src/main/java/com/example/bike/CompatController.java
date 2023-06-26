package com.example.bike;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CompatController {
    @FXML
    private ChoiceBox<GPU> gpuChoiceBox;

    @FXML
    private ImageView gameImageView;

    @FXML
    private Text compatText;

    @FXML
    private Button returnButton;

    private ObservableList<GPU> gpuList = FXCollections.observableArrayList();

    private String gameName;
    private int gamePerformanceRequired;

    @FXML
    private void initialize() {
        loadGPUList();
        gpuChoiceBox.setItems(gpuList);
        setGameImageView();

        gpuChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            GPU currentGPU = newValue;
            int currentGPUPerformance = currentGPU.getPerformance();

            if(currentGPUPerformance >= gamePerformanceRequired*2 || currentGPUPerformance >= gamePerformanceRequired + 10000) {
                compatText.setText("EXCEPTIONAL COMPATIBILITY");
            } else if(currentGPUPerformance > gamePerformanceRequired + 2000) {
                compatText.setText("GREAT COMPATIBILITY");
            } else if(currentGPUPerformance >= gamePerformanceRequired && currentGPUPerformance <= gamePerformanceRequired + 2000) {
                compatText.setText("GOOD COMPATIBILITY");
            } else if(currentGPUPerformance < gamePerformanceRequired && currentGPUPerformance >= gamePerformanceRequired - 2000) {
                compatText.setText("OKAY COMPATIBILITY");
            } else if(currentGPUPerformance < gamePerformanceRequired - 2000 && currentGPUPerformance >= gamePerformanceRequired - 4000) {
                compatText.setText("BAD COMPATIBILITY");
            } else {
                compatText.setText("NOT COMPATIBLE");
            }


        });
    }

    @FXML
    void setGameImageName(String gameImageName) {
        gameName = gameImageName;
    }

    void setGameImageView() {
        gameImageView.setImage(new Image(getClass().getResource(gameName).toExternalForm()));
    }

    void setGamePerformanceRequired(int gamePerformanceRequired) {
        this.gamePerformanceRequired = gamePerformanceRequired;
    }
    private void loadGPUList() {
        try(BufferedReader reader = new BufferedReader(new FileReader("gpus.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] gpuInfo = line.split(",");
                String gpuName = gpuInfo[0].trim();
                String gpuPerformance = gpuInfo[1].trim();
                GPU completedGPU = new GPU(gpuName, gpuPerformance);
                gpuList.add(completedGPU);
            }
        } catch (IOException e) {
            System.out.println("BONK!! System got into an error, sending error message:");
            e.printStackTrace();
        }
    }
    @FXML
    private void returnButtonOnClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}