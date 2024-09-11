package moviesapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import moviesapp.model.Database;
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Database.createNewDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("Movies App");
        primaryStage.getIcons().add(new Image("/icons/films.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
