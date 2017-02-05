package ui.controller;

import bags.Account;
import bags.Like;
import bags.Post;
import datatype.LikeType;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import transactie.PostTrans;
import ui.VIVESbook;
import javafx.scene.control.ComboBox;
import transactie.LikeTrans;

public class PostToevoegenController implements Initializable
{
    private VIVESbook mainApp;
    
    private Account loggedInAccount;
    
    @FXML
    private TextArea taPostInhoud;

    @FXML
    private Label laErrorMessage;

    @FXML
    private ListView lvLikeView;

    @FXML
    private Button buLikeToevoegen, buLikeWijzigen, buLikeVerwijderen;

    @FXML
    private ComboBox cbLikeType;

    @FXML
    private Button buOk, buCancel;

    @FXML
    private Label laLikeLabel;

    @FXML
    private Button buPostVerwijderen;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setText(null);
        buPostVerwijderen.setVisible(false);
        buLikeToevoegen.setVisible(false);
        buLikeWijzigen.setVisible(false);
        lvLikeView.setVisible(false);
        laLikeLabel.setVisible(false);
        buOk.setText("Post toevoegen");
        //o
        initializeCombobox();
    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    public void setData(Account account)
    {
        this.loggedInAccount = account;
    }
    
    private void initializeCombobox()
    {
        cbLikeType.getItems().add(LikeType.leuk);
        cbLikeType.getItems().add(LikeType.geweldig);
        cbLikeType.getItems().add(LikeType.grappig);
        cbLikeType.getItems().add(LikeType.verbluft);
        cbLikeType.getItems().add(LikeType.verdrietig);
        cbLikeType.getItems().add(LikeType.boos);
    }

    @FXML
    private void buOkClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        PostTrans postTrans = new PostTrans();
        Post nieuwePost = new Post();
        nieuwePost.setEigenaar(loggedInAccount.getLogin());
        nieuwePost.setTekst(taPostInhoud.getText());
        
        LikeTrans likeTrans = new LikeTrans();
        Like nieuweLike = new Like();
        nieuweLike.setAccountlogin(loggedInAccount.getLogin());
        nieuweLike.setType((LikeType) cbLikeType.getSelectionModel().getSelectedItem());
        
        try
        {
            nieuweLike.setPostid(postTrans.postToevoegen(nieuwePost).getId());
            likeTrans.LikesToevoegen(nieuweLike);
            
            mainApp.laadHomeScherm(loggedInAccount);
        }
        catch (ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
        }
        catch (DBException e)
        {
            laErrorMessage.setText("Contacteer uw beheerder");
        }
    }

    @FXML
    private void buCancelClicked(ActionEvent event)
    {
        mainApp.laadHomeScherm(loggedInAccount);
    }

    @FXML
    private void buLikeWijzigenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
    }

    @FXML
    private void buLikeToevoegenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
    }

    @FXML
    private void buLikeVerwijderenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        cbLikeType.getSelectionModel().clearSelection();
    }

    @FXML
    private void buPostVerwijderenClicked(ActionEvent event) {}
}