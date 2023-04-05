package java2budgetproject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java2budgetproject.FoodExpense;
import java2budgetproject.RentExpense;
import java2budgetproject.CarExpense;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author Natalee Bui
 */
public class Scene3Controller implements Initializable {

    @FXML
    private Text txtRentSpent;
    @FXML
    private Text txtFoodSpent;
    @FXML
    private Text txtCarSpent;
    @FXML
    private Label txtRemainBalance;
    @FXML
    private Pane Pane;
    @FXML
    private Button close;
    @FXML
    private TextArea txtFeedback;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    /**
     * Initializes the controller class.
     */
    private String username;

    public Scene3Controller(String username) {
        this.username = username;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        double foodTotal = (FoodExpense.getTotalAmount());
        double rentTotal = (RentExpense.getTotalAmount());
        double carTotal = (CarExpense.getTotalAmount());

        txtFoodSpent.setText(String.format("$   %.2f", foodTotal));
        txtRentSpent.setText(String.format("$   %.2f", rentTotal));
        txtCarSpent.setText(String.format("$    %.2f", carTotal));

        double finalbalance = 0.0;
        try {
            // connect to the database
            connect = database.connect();
            prepare = connect.prepareStatement("SELECT balance FROM admin WHERE username = ?");
            prepare.setString(1, username);
            result = prepare.executeQuery();
            if (result.next()) {
                double balance = result.getDouble("balance");
                finalbalance = balance - foodTotal - rentTotal - carTotal;
            }
            prepare = connect.prepareStatement("UPDATE admin SET balance = ? WHERE username = ?");
            prepare.setDouble(1, finalbalance);
            prepare.setString(2, username);
            prepare.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtRemainBalance.setText(String.format("$ %.2f", finalbalance));

        if (finalbalance < 0) {
            txtFeedback.setText("WARNING: Your balance is negative! \n YOU NEED TO BUDGET BETTER");
        } else if (finalbalance > 0) {
            txtFeedback.setText(String.format("You have saved  $%.2f \nKEEP IT UP!", finalbalance));
        } else {
            txtFeedback.setText("REMINDER: You have spent all your money! \n Be careful with your budgeting strategy");
        }
        

    }

    @FXML
    protected void close(ActionEvent event) throws IOException {

        CarExpense.clearAllExpenses();
        RentExpense.clearAllExpenses();
        FoodExpense.clearAllExpenses();
        Stage closing = (Stage) close.getScene().getWindow();
        closing.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene2.fxml"));
        Scene2Controller scene2Controller = new Scene2Controller(username);
        loader.setController(scene2Controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
