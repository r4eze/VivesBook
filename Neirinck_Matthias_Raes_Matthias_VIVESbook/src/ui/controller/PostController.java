/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import bags.Account;
import bags.Post;
import database.PostDB;
import exception.ApplicationException;
import exception.DBException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import transactie.PostTrans;
import transactie.VriendschapTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author Katrien.Deleu
 */
public class PostController implements Initializable
{

    // referentie naar mainapp (start)
    private VIVESbook mainApp;

    private Account loggedInAccount;

    @FXML
    private Label account;

    @FXML
    private ListView postView;

    private ArrayList<Post> posts = new ArrayList<>();

    private int postIndex = -1;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        errorLabel.setVisible(false);
    }

    /**
     * Referentie naar mainApp (start) instellen
     *
     * @param mainApp referentie naar de runnable class die alle oproepen naar
     *                de schermen bestuurt
     */
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    public Account getAccount()
    {
        return loggedInAccount;
    }

    public void setAccount(Account account)
    {
        this.loggedInAccount = account;
    }

    public void initializeListView(Account account) throws IOException
    {
        PostDB pdb = new PostDB();
        ObservableList view = observableArrayList();
        try
        {
            posts = pdb.zoekAllePostsVanAccountEnVrienden(account.getLogin());
            for (Post v : posts)
            {
                view.add(v);
            }
            postView.setItems(view);
            postView.setFixedCellSize(-1);

        }
        catch (DBException e)
        {
            throw new IOException("Problem with database: " + e.getMessage());
        }
    }

    public void setAccountText(String voornaam)
    {
        this.account.setText(voornaam);
    }

    @FXML
    private void btnPostToevoegen(ActionEvent event)
    {
        mainApp.setPost(null);
        mainApp.laadPosttoevoegenScherm();
    }

    @FXML
    private void btnPostOverzicht(ActionEvent event)
    {
        if (postIndex != -1)
        {
            mainApp.setPost(posts.get(postIndex));
            mainApp.laadPosttoevoegenScherm();
        }
        else
        {
            errorLabel.setVisible(true);
            errorLabel.setText("Gelieve eerst een post te selecteren");
        }
    }

    private void resetError()
    {
        errorLabel.setVisible(false);
    }

    @FXML
    private void btnAccountWijzigen(ActionEvent event)
    {
        mainApp.laadNieuwScherm();
    }

    @FXML
    private void btnVriendToevoegen(ActionEvent event)
    {
        mainApp.setAccount(loggedInAccount);
        mainApp.laadVriendScherm();
    }

    @FXML
    private void btnLogout(ActionEvent event)
    {
        mainApp.setAccount(null);
        mainApp.laadLoginScherm();
    }

    @FXML
    private void postClick(MouseEvent event)
    {
        resetError();
        if (postIndex == postView.getSelectionModel().getSelectedIndex())
        {
            btnPostOverzicht(null);
        }
        else
        {
            postIndex = postView.getSelectionModel().getSelectedIndex();
        }
    }
}
