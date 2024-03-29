package com.example.sms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;



public class scheduleController implements Initializable {

    static String userClub;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField EventName;
    @FXML
    private ComboBox<String> clubSelect;
    @FXML
    private ComboBox<String> EventType;
    @FXML
    private DatePicker date;
    @FXML
    private TextField description;
    @FXML
    private Label Descriptionmessage;
    @FXML
    private Label clubmessage;
    @FXML
    private Label datemessage;
    @FXML
    private Label eventmessage;
    @FXML
    private Label EventTypemessage;
    private DatabaseConnection connectSchedule;

    ArrayList<String> clublist = new ArrayList<>();

    public void addEvent(ActionEvent actionEvent) throws IOException, SQLException {
        String eventName = EventName.getText();
        String club = clubSelect.getValue();
        String eventType = EventType.getValue();
        LocalDate DateError = date.getValue();
        String Date = null;
        if (DateError != null) {
            Date = DateError.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        } else
            datemessage.setText("Please select a Date ");
        datemessage.setText(" ");
        String Description = description.getText();

        if (!eventValidation(eventName, club,eventType, Date, Description)) {
            return;
        }
        event Event1 = new event(eventName, club,eventType, Date, Description);
        OOPCoursework.scheduleEvents.add(Event1);
        int i;
        for(i = 0 ; i < OOPCoursework.clublist.size() ; i++) {
            if(club.equals(OOPCoursework.clublist.get(i).getName())) {
                OOPCoursework.clublist.get(i).addEvent(Event1);
                break;
            }
        }
        Event1.setClub(OOPCoursework.clublist.get(i));
        String insertQuery =
                "UPDATE clubs SET events = ? WHERE AdvisorID = ?";
        Connection connection = connectSchedule.connect();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(2, OOPCoursework.clublist.get(i).getAdvisor().getUsername());
            preparedStatement.setString(1, OOPCoursework.clublist.get(i).eventString());

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            // Check the number of rows affected
            if (rowsAffected > 0) {
                System.out.println("Club updated successfully for student with username: " + club);
            } else {
                System.out.println("No rows were updated. Student with username " + club + " not found.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        insertQuery =
                "INSERT INTO events (`Event Name`, `club`,`Event Type`, `date`, `Description`) VALUES (?, ?, ?,?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, Event1.getEventName());
            preparedStatement.setString(2, Event1.getClubName());
            preparedStatement.setString(3,Event1.getEventType());
            preparedStatement.setString(4, Event1.getDate());
            preparedStatement.setString(5, Event1.getDescription());



            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Data insertion failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("calender.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 744, 689);
        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 0 ; i < OOPCoursework.scheduleEvents.size() ; i++) {
            System.out.println(OOPCoursework.scheduleEvents.get(i).getClub().getName());
        }
        try {
            previuosClubs();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<String> eventType = FXCollections.observableArrayList("Activity","Event","Meeting");
        EventType.setItems(eventType);
        ObservableList<String> clubs1 = clubSelect.getItems();
        int index = 0;
        while (index < clublist.size()) {
            clubs1.add(clublist.get(index));
            index++;
        }
    }
    private void previuosClubs() throws SQLException {
        String userName = stafflogincontroller.username1;
        String selectQuery = "SELECT * FROM `clubs`;";
        Connection comm = connectSchedule.connect();
        try (PreparedStatement statement = comm.prepareStatement(selectQuery)) {
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                if (userName.equals(results.getString(2)) ) {
                    club club = new club(results.getString(1), results.getString(2), results.getString(3), results.getInt(4));
                    userClub = results.getString(1);
                    clublist.add(club.getName());
                }
            }
        }
    }
    @FXML
    void back(ActionEvent event)throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("advisor.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public boolean eventValidation(String eventName , String club, String eventType, String date , String description) throws SQLException {

        for (int i =0 ;i<OOPCoursework.scheduleEvents.size();i++){
            if (OOPCoursework.scheduleEvents.get(i).getEventName().equals(eventName)){
                eventmessage.setText("The Event has already Created ");
                return false;
            }
        }
        eventmessage.setText("");
        if (eventName.isEmpty()){
            eventmessage.setText("Please enter a event name ");
            return false;
        }
        eventmessage.setText(" ");
        if (club == null){
            clubmessage.setText("Select the club ");
            return false;
        }
        if (eventType  == null){
            EventTypemessage.setText("Select the event type");
            return false;
        }
        EventTypemessage.setText(" ");
        clubmessage.setText(" ");
        if ( date == null){
            datemessage.setText("Please select a Date ");
            return false;
        }
        datemessage.setText(" ");
        if (description.isEmpty()){
            Descriptionmessage.setText("Please enter a Description ");
            return false;
        }
        Descriptionmessage.setText(" ");
        return true;
    }



}


