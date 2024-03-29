package com.example.sms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class studentlogincontroller {

    static ArrayList<String> studentLoginDetails = new ArrayList<>();
    @FXML
    private AnchorPane container;

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
    public void login(ActionEvent event) throws IOException, SQLException {
        String username = usernameinput.getText();
        String password = passwordinput.getText();
        studentLoginDetails.add(username);
        studentLoginDetails.add(password);
        String selectQuery = "SELECT * FROM `students`;";
        Connection comm= connect.connect();
        try (PreparedStatement statement = comm.prepareStatement(selectQuery)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) { //to check if the username and password enterd matches the records in the database.
                if (username.equals(results.getString(5)) && password.equals(results.getString(6))) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("studentmenu.fxml"));
                    Parent root = loader.load();
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
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("studentreg.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void back(ActionEvent event)throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}