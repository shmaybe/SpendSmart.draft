/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package fuckoff;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author MayxT
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label REGISTERUSERFORM;

    @FXML
    private Label haa_r;

    @FXML
    private BorderPane login;

    @FXML
    private Button login_l;

    @FXML
    private Button login_r;

    @FXML
    private Label loginuserform_l;

    @FXML
    private PasswordField pword_l;

    @FXML
    private PasswordField pword_r;

    @FXML
    private BorderPane register;

    @FXML
    private Button register_r;

    @FXML
    private Label rna_l;

    @FXML
    private Label s_smart_l;

    @FXML
    private Button s_up_l;

    @FXML
    private Label ssmart_r;

    @FXML
    private TextField uSER_NAMER;

    @FXML
    private TextField user_name_l;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void loginAccount() {
        String sql = "SELECT username, password FROM admin WHERE username = ? and password = ?";

        connect = databasee.connect();

        try {
            Alert alert;
            if (user_name_l.getText().isEmpty() || pword_l.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("oop, plz fill in the required blanks.");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, user_name_l.getText());
                prepare.setString(2, pword_l.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message ");
                    alert.setHeaderText(null);
                    alert.setContentText("YAY! Successfully Logged in!");
                    alert.showAndWait();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Sorry! Incorrect Username/Password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerAccount() {
        String sql = "INSERT INTO admin (username, password) VALUES (?,?)";

        connect = databasee.connect();

        try {
            Alert alert;
            if (uSER_NAMER.getText().isEmpty() || pword_r.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("oop, plz fill in the required blanks.");
                alert.showAndWait();
            } else {

                String checkData = "SELECT username FROM admin WHERE username = '" + uSER_NAMER.getText() + "'";
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(uSER_NAMER.getText() + " is already taken");
                    alert.showAndWait();
                } else {

                    if (pword_r.getText().length() < 8) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid password </3. You need at least 8 characters!");
                        alert.showAndWait();

                    } else {
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, uSER_NAMER.getText());
                        prepare.setString(2, pword_r.getText());

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully created a new account!");
                        alert.showAndWait();
                        
                        login.setVisible(true);
                        register.setVisible(false);
                        uSER_NAMER.setText("");
                        pword_r.setText("");
                        
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_r) {
            login.setVisible(true);
            register.setVisible(false);
        } else if (event.getSource() == s_up_l) {
            login.setVisible(false);
            register.setVisible(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
