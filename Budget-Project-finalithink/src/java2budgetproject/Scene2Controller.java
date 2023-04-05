package java2budgetproject;

import com.mysql.cj.jdbc.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Natalee Bui
 */
public class Scene2Controller implements Initializable {

    @FXML
    private Label currentuser;
    @FXML
    private Button btnlogout;
    @FXML
    private Button btnupload;

    @FXML
    private Label lblmessage;

    /**
     * Initializes the controller class.
     */
    private FoodExpense foodList;
    private CarExpense carList;
    private RentExpense rentList;
    @FXML
    private Button btncalculate;
    @FXML
    private Button btnAddFunds;
    @FXML
    private Label currentBalance;
    @FXML
    private Label addfunds;
    @FXML
    private TextField txtfunds;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    @FXML
    private GridPane pane2;
    @FXML
    private Label lblbalance;

    private String username;
    private double balance;

    public Scene2Controller(String username) {
        this.username = username;
    }

    public Scene2Controller(double balance) {
        this.balance = balance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        currentuser.setText("Welcome " + username +"!");
        btncalculate.setDisable(true);
        foodList = new FoodExpense(0);
        carList = new CarExpense(0);
        rentList = new RentExpense(0);

        try {
            // connect to the database
            connect = database.connect();
            prepare = connect.prepareStatement("SELECT balance FROM admin WHERE username = ?");
            prepare.setString(1, username);
            result = prepare.executeQuery();
            if (result.next()) {
                balance = result.getDouble("balance");
                lblbalance.setText(String.format("%.2f", balance));
            } else {
                lblbalance.setText("0.00");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnlogout.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void btnShowResult(ActionEvent event) throws IOException {

        
        // Load the FXML file of the new scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene3.fxml"));
        Scene3Controller thirdcontroller = new Scene3Controller(currentuser.getText());
        loader.setController(thirdcontroller);
        Parent root = loader.load();

        // Create a new scene with the new root and set it on the stage
        Scene scene = new Scene(root);
        Stage thirdstage = new Stage();
        thirdstage.setScene(scene);
        thirdstage.setTitle(username + "'s Detailed Expenses");
        thirdstage.setResizable(false);
        thirdstage.show();
        
        Stage closing = (Stage) btncalculate.getScene().getWindow();
        closing.close();

    }

    @FXML
    protected void upload(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(btnupload.getScene().getWindow());

        if (selectedFile != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                br.readLine(); // skip header

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length < 3) {
                        lblmessage.setText("Invalid data in line: " + line);
                        foodList.clearAllExpenses();
                        rentList.clearAllExpenses();
                        carList.clearAllExpenses();
                        return;
                    }
                    String category = values[1].trim();
                    if (category.isEmpty()) {
                        lblmessage.setText("Missing category in line: " + line);
                        foodList.clearAllExpenses();
                        rentList.clearAllExpenses();
                        carList.clearAllExpenses();
                        return;
                    }
                    double amount;
                    try {
                        amount = Double.parseDouble(values[2].trim());
                    } catch (NumberFormatException e) {
                        lblmessage.setText("Invalid amount in line: " + line);
                        foodList.clearAllExpenses();
                        rentList.clearAllExpenses();
                        carList.clearAllExpenses();
                        return;
                    }

                    switch (category) {
                        case "food":
                            foodList.addExpense(new FoodExpense(amount));
                            break;
                        case "rent":
                            rentList.addExpense(new RentExpense(amount));
                            break;
                        case "car":
                            carList.addExpense(new CarExpense(amount));
                            break;
                        default:
                            lblmessage.setText("Invalid category in line: " + line);
                            foodList.clearAllExpenses();
                            rentList.clearAllExpenses();
                            carList.clearAllExpenses();
                            return;
                    }
                }
                btncalculate.setDisable(false);
                lblmessage.setText("File '" + selectedFile.getName() + "' successfully uploaded!");
            } catch (IOException e) {
                lblmessage.setText("Error reading file.");
                foodList.clearAllExpenses();
                rentList.clearAllExpenses();
                carList.clearAllExpenses();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("No file selected");
            alert.setContentText("Use a valid file to upload. Are you sure you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                btncalculate.setDisable(false);
                foodList.clearAllExpenses();
                rentList.clearAllExpenses();
                carList.clearAllExpenses();
            } else {
                upload(event);
            }
        }
    }

    @FXML
    protected void addfund(ActionEvent event) throws SQLException {

        String account = currentuser.getText();

        // Prepare the SQL statement for updating the balance for the current user
        String updateBalance = "UPDATE admin SET balance = balance + ? WHERE username = ?";

        connect = database.connect();
        try {
            // Get the balance value from the text field
            double balance = Double.parseDouble(txtfunds.getText());
            // Check if the balance value is negative
            if (balance < 0) {
                // Display an alert error message and return without saving the negative balance to the database
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Funds cannot be negative.");
                alert.showAndWait();
                return;
            }
            // Prepare the SQL statement for updating the balance
            PreparedStatement statement = connect.prepareStatement(updateBalance);
            statement.setDouble(1, balance);
            statement.setString(2, account);

            // Execute the SQL statement to update the balance for the current user
            statement.executeUpdate();

            // Query the database to retrieve the updated balance for the current user
            String selectBalance = "SELECT balance FROM admin WHERE username = ?";
            PreparedStatement selectStatement = connect.prepareStatement(selectBalance);
            selectStatement.setString(1, account);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                double newBalance = resultSet.getDouble("balance");
                // Update the UI to show the new balance
                lblbalance.setText(String.format("%.2f", newBalance));
                txtfunds.clear();
            }

            // Close the statement and database connection
            statement.close();
            selectStatement.close();
            connect.close();
        } catch (NumberFormatException e) {
            // Handle error if the balance value entered is not a valid number
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message ");
            alert.setHeaderText(null);
            alert.setContentText("Invalid balance value entered: " + txtfunds.getText());
            alert.showAndWait();
        } catch (SQLException e) {
            // Handle error if there's an issue with the database connection or SQL statement
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message ");
            alert.setHeaderText(null);
            alert.setContentText("Error updating balance in database: " + e.getMessage());
            alert.showAndWait();

        }
    }

}
