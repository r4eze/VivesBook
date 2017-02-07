package ui.controller;

import bags.Account;
import bags.Post;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import transactie.AccountTrans;
import transactie.PostTrans;
import ui.VIVESbook;

public class HomeController implements Initializable
{
    private VIVESbook mainApp;

    private Account loggedInAccount;

    @FXML
    private Label laAccount;

    @FXML
    private ListView lvPostView;
    
    @FXML
    private Label laErrorMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setText(null);
    }

    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }
    
    public void setData(Account account)
    {
        this.loggedInAccount = account;
        initializePostListView(loggedInAccount);
        laAccount.setText(loggedInAccount.getVoornaam() + " " + loggedInAccount.getNaam() + " (" + loggedInAccount.getLogin() + ")");
    }

    public Account getAccount()
    {
        return loggedInAccount;
    }

    public void setAccount(Account account)
    {
        this.loggedInAccount = account;
    }

    public void initializePostListView(Account account)
    {
        PostTrans postTrans = new PostTrans();

        try
        {
            ArrayList<Post> posts = postTrans.zoekAllePostsVanAccountEnVrienden(account.getLogin());
            
            for (Post p : posts)
            {
                lvPostView.getItems().add(p);
            }
            
            lvPostView.setFixedCellSize(-1);
        }
        catch (DBException e)
        {
            laErrorMessage.setText("Contacteer uw beheerder");
        }
        catch(ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void buPostToevoegenClicked(ActionEvent event)
    {
        mainApp.laadPostToevoegenScherm(loggedInAccount);
    }

    @FXML
    private void buPostOverzichtClicked(ActionEvent event)
    {
        if(!lvPostView.getSelectionModel().isEmpty())
        {
            mainApp.laadPostOverzichtScherm(loggedInAccount, (Post) lvPostView.getSelectionModel().getSelectedItem());
        }
        else
        {
            laErrorMessage.setText("Geen post geselecteerd");
        }
    }

    @FXML
    private void buAccountWijzigenClicked(ActionEvent event)
    {
        mainApp.laadAccountWijzigenScherm(loggedInAccount);
    }

    @FXML
    private void buVriendenClicked(ActionEvent event)
    {
        mainApp.laadVriendScherm(loggedInAccount);
    }

    @FXML
    private void buLogoutClicked(ActionEvent event)
    {
        AccountTrans accountTrans = new AccountTrans();
        
        try
        {
            accountTrans.uitloggenAccount(loggedInAccount.getLogin(), loggedInAccount.getPaswoord());
            mainApp.laadLoginScherm();
        }
        catch(DBException e)
        {
            laErrorMessage.setText("Contacteer uw beheerder");
        }
        catch(ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
        }
    }
    
    public void uitloggenAccount()
    {
        AccountTrans accountTrans = new AccountTrans();
        try
        {
            accountTrans.uitloggenAccount(loggedInAccount.getLogin(), loggedInAccount.getPaswoord());
        }
        catch(DBException e)
        {
            laErrorMessage.setText("Contacteer uw beheerder");
        }
        catch(ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
        }
    }
}
