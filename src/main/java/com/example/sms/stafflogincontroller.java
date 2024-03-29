package com.example.sms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class stafflogincontroller {
    static ArrayList<String> staffLoginDetails = new ArrayList<>(); //to store login details of the staff
    static String username1;

    @FXML
    private Button enterbutton;

    @FXML
    private PasswordField passwordinput;

    @FXML
    private Button signupbutton;

    @FXML
    private TextField usernameinput;

    @FXML
    private Label message;

    private Stage stage; //create variables for scene, stage and root
    private Scene scene;
    private Parent root;
    private DatabaseConnection connect;

public void advisorlogin(ActionEvent event) throws IOException, SQLException{
    String username = usernameinput.getText();
    String password = passwordinput.getText();

    String selectQuery = "SELECT * FROM `teachers`;"; //to get data from the teachers table
    Connection comm= connect.connect();
    try (PreparedStatement statement = comm.prepareStatement(selectQuery)) {
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            if (username.equals(results.getString(4)) && password.equals(results.getString(5))) {
                staffLoginDetails.add(username);
                staffLoginDetails.add(password);
                username1 = username;
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("advisor.fxml")));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }

        }

    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (SQLException ex) {
        throw new RuntimeException(ex);
    }
    message.setText("Incorrect username or password");
}

    public void signup(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("staffreg.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

