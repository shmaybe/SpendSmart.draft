package java2budgetproject;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Natalee Bui
 */
public class Java2BudgetProject extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("scene1.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Budget Tracker Application");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
