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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
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

    private ArrayList<Post> posts = new ArrayList<>();

    private int postIndex = -1;

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
            posts = postTrans.zoekAllePostsVanAccountEnVrienden(account.getLogin());
            
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
            laErrorMessage.setText(null);
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
        /*if (postIndex != -1)
        {
            mainApp.laadPostOverzichtScherm(loggedInAccount, (Post) lvPostView.getSelectionModel().getSelectedItem());
        }
        else
        {
            errorLabel.setVisible(true);
            errorLabel.setText("Gelieve eerst een post te selecteren");
        }*/
        
        mainApp.laadPostOverzichtScherm(loggedInAccount, (Post) lvPostView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void buAccountWijzigenClicked(ActionEvent event)
    {
        mainApp.laadAccountWijzigenScherm(loggedInAccount);
    }

    @FXML
    private void buVriendToevoegenClicked(ActionEvent event)
    {
        mainApp.laadVriendScherm(loggedInAccount);
    }

    @FXML
    private void buLogoutClicked(ActionEvent event)
    {
        mainApp.laadLoginScherm();
    }

    @FXML
    private void postClick(MouseEvent event)
    {
        laErrorMessage.setText(null);
        if (postIndex == lvPostView.getSelectionModel().getSelectedIndex())
        {
            buPostOverzichtClicked(null);
        }
        else
        {
            postIndex = lvPostView.getSelectionModel().getSelectedIndex();
        }
    }
}
