package ui;

import bags.Account;
import bags.Post;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.LoginController;
import ui.controller.AccountToevoegenController;
import ui.controller.HomeController;
import ui.controller.PostToevoegenController;
import ui.controller.VriendController;
import ui.controller.AccountWijzigenController;
import ui.controller.PostOverzichtController;

/**
 *
 * @author Katrien.Deleu
 */
public class VIVESbook extends Application
{
    private Stage stage;

    @Override
    public void start(Stage primaryStage)
    {
        stage = primaryStage;
        laadLoginScherm();
        primaryStage.show();
    }

    public void laadLoginScherm()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Login.fxml"));
            Parent root = loader.load();
            LoginController controller = (LoginController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Login");
            stage.setScene(scene);
        }
        catch (Exception e)
        {
            System.out.println("!!! - " + e.getMessage());

        }
    }
    
    public void laadHomeScherm(Account loggedInAccount)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Home.fxml"));
            
            // controller ophalen
            Parent root = loader.load();
            HomeController controller = (HomeController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);
            controller.setData(loggedInAccount);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Home");
            stage.setScene(scene);
        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }

    public void laadPostToevoegenScherm(Account loggedInAccount)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Post.fxml"));
            
            PostToevoegenController controller = new PostToevoegenController();
            loader.setController(controller);
            Parent root = loader.load();
            
            controller.setMainApp(this);
            controller.setData(loggedInAccount);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Post toevoegen");
            stage.setScene(scene);
        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }
    
    public void laadPostOverzichtScherm(Account loggedInAccount, Post selectedPost)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Post.fxml"));
            
            PostOverzichtController controller = new PostOverzichtController();
            
            // moet voor het laden van de controller gebeuren zodat de waarden al ingesteld zijn bij de initizalize()
            controller.setData(loggedInAccount, selectedPost);
            
            loader.setController(controller);
            Parent root = loader.load();

            controller.setMainApp(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Post toevoegen");
            stage.setScene(scene);
        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }

    public void laadAccountToevoegenScherm()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Account.fxml"));

            AccountToevoegenController controller = new AccountToevoegenController();
            loader.setController(controller);
            Parent root = loader.load();
            
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Nieuw Account aanmaken");
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }
    
    public void laadAccountWijzigenScherm(Account loggedInAccount)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Account.fxml"));
            
            AccountWijzigenController controller = new AccountWijzigenController();
            loader.setController(controller);
            Parent root = loader.load();

            controller.setMainApp(this);
            controller.setData(loggedInAccount);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Account wijzigen");
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }

    public void laadVriendScherm(Account loggedInAccount)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Vriend.fxml"));

            // controller ophalen
            Parent root = loader.load();
            VriendController controller = (VriendController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);
            controller.setData(loggedInAccount);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Lijst van Vrienden");
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}