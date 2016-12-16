/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import bags.Account;
import bags.Post;
import java.io.IOException;
import java.util.HashSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.LoginController;
import ui.controller.NieuwAccountController;
import ui.controller.PostController;
import ui.controller.PosttoevoegenController;
import ui.controller.VriendController;

/**
 *
 * @author Katrien.Deleu
 */
public class VIVESbook extends Application
{

    private Stage stage;

    private Account loggedInAccount = null;

    private Post viewingPost = null;

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
            LoginController controller = (LoginController) loader.
                    getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook");
            stage.setScene(scene);

        }
        catch (Exception e)
        {
            System.out.println("!!! - " + e.getMessage());

        }

    }

    public void laadPosttoevoegenScherm()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Posttoevoegen.fxml"));

            // controller instellen
            Parent root = loader.load();
            PosttoevoegenController controller = (PosttoevoegenController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);
            controller.setAccount(loggedInAccount);
            controller.setPost(viewingPost);
            controller.initialize(null, null);

            Scene scene = new Scene(root);
            stage.setTitle("Post toevoegen");
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }

    }

    public void laadPostsScherm()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Post.fxml"));

            // controller ophalen
            Parent root = loader.load();
            PostController controller = (PostController) loader.getController();

            //account dat is ingelogd bijhouden.
            controller.setAccount(loggedInAccount);
            controller.initializeListView(loggedInAccount);
            controller.setAccountText(loggedInAccount.getVoornaam() + " " + loggedInAccount.getNaam());

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - overzicht posts");
            stage.setScene(scene);

        }

        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }

    }

    public void laadNieuwScherm()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                    "view/Nieuw.fxml"));

            // controller ophalen
            Parent root = loader.load();
            NieuwAccountController controller = (NieuwAccountController) loader.getController();

            // referentie naar hier bewaren in de controller
            controller.setMainApp(this);
            if (loggedInAccount != null)
            {
                controller.setAccount(loggedInAccount);
                controller.setValues(loggedInAccount);

            }

            Scene scene = new Scene(root);
            stage.setTitle("VIVESbook - Nieuw Account aanmaken");
            stage.setScene(scene);

        }
        catch (IOException e)
        {
            System.out.println("!!! - " + e.getMessage());
        }
    }

    public void laadVriendScherm()
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
            controller.setAccount(loggedInAccount);
            controller.initializeListview();

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

    public void setAccount(Account acc)
    {
        this.loggedInAccount = acc;
    }

    public Account getAccount()
    {
        return loggedInAccount;
    }

    public void setPost(Post post)
    {
        this.viewingPost = post;
    }

}
