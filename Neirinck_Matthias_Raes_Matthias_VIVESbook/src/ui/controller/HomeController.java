package ui.controller;

import bags.Account;
import bags.Post;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private ListView lvPosts;
    
    @FXML
    private Button buPostOverzicht;
    
    @FXML
    private Label laErrorMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setText(null);
        buPostOverzicht.setDisable(true);
        
        // De button om een post overzicht te tonen enkel beschikbaar maken als er een post geselecteerd is
        // Kan ook opgelost worden door een if te schrijven en error te tonen,
        // maar door de button te disablen volgen we dezelfde ideologie als in het post overzicht scherm (buttons voor like disablen)
        lvPosts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Post>() {
            
            @Override
            public void changed(ObservableValue<? extends Post> observable, Post oldValue, Post newValue) {
                if(newValue != null)
                {
                    buPostOverzicht.setDisable(false);
                }
                else
                {
                    buPostOverzicht.setDisable(true);
                }
            }
        });
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
                lvPosts.getItems().add(p);
            }
            
            lvPosts.setFixedCellSize(-1);
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
        // Deze if is in principe niet nodig doordat de button disabled is als er geen post geselecteerd is
        // maar je weet maar nooit...
        if(!lvPosts.getSelectionModel().isEmpty())
        {
            mainApp.laadPostOverzichtScherm(loggedInAccount, (Post) lvPosts.getSelectionModel().getSelectedItem());
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
