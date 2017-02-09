package ui.controller;

import bags.Account;
import bags.Like;
import bags.Post;
import datatype.LikeType;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import transactie.AccountTrans;
import transactie.LikeTrans;
import transactie.PostTrans;
import ui.VIVESbook;

public class PostOverzichtController implements Initializable{
    
    private VIVESbook mainApp;
    
    private Account loggedInAccount;
    
    private Post selectedPost;
    
    @FXML
    private TextArea taPostInhoud;

    @FXML
    private Label laErrorMessage;

    @FXML
    private ListView lvLikeView;
    
    @FXML
    private ComboBox cbLikeType;   

    @FXML
    private Button buLikeToevoegen, buLikeWijzigen, buLikeVerwijderen;

    @FXML
    private Button buOk, buCancel;
    
    @FXML
    private Button buPostVerwijderen; 
    

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {   
        laErrorMessage.setText(null);
        initializeLikeListView(selectedPost);
        initializeCombobox();
        taPostInhoud.setText(selectedPost.getTekst());
        
        if (selectedPost.getEigenaar().equals(loggedInAccount.getLogin()))
        {
            buPostVerwijderen.setDisable(false);
        }
        else
        {
            buPostVerwijderen.setDisable(true);
            taPostInhoud.setEditable(false); // de tekst niet kunnen wijzigen
            taPostInhoud.setMouseTransparent(true); // De textArea niet actief kunnen zetten
            taPostInhoud.setFocusTraversable(false); // er niet naar toe kunnen gaan via tab
        }
        
        int indexLike = getIndexLikeFromAccount(loggedInAccount.getLogin());
        
        if(indexLike != -1)
        {
            buLikeToevoegen.setDisable(true);
            buLikeWijzigen.setDisable(false);
            buLikeVerwijderen.setDisable(false);
            // het type van de like instellen in de combobox
            cbLikeType.getSelectionModel().select(((Like) lvLikeView.getItems().get(indexLike)).getType());
        }
        else
        {
            buLikeToevoegen.setDisable(false);
            buLikeWijzigen.setDisable(true);
            buLikeVerwijderen.setDisable(true);
        }
        
        lvLikeView.setMouseTransparent(true);
        lvLikeView.setFocusTraversable(false);
    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }
    
    public void setData(Account account, Post post)
    {
        this.loggedInAccount = account;
        this.selectedPost = post;       
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
    
    // returnt de index van de like in lvLikeView voor het accountlogin in de parameter
    // -1 als er geen like voor dat account bestaat
    private int getIndexLikeFromAccount(String accountLogin)
    {
        ObservableList<Like> likes = lvLikeView.getItems();
        
        for (int i = 0; i < likes.size(); i++)
        {
            if (likes.get(i).getAccountlogin().equals(accountLogin))
            {
                return i;
            }
        }
        
        return -1;
    }

    public void initializeLikeListView(Post post)
    {
        LikeTrans likeTrans = new LikeTrans();
        try
        {
            ArrayList<Like> likeList = likeTrans.zoekAlleLikesVanPost(post.getId());

            for (Like like : likeList)
            {
                lvLikeView.getItems().add(like);
            }
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
    private void buLikeWijzigenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        
        
        if (cbLikeType.getSelectionModel().getSelectedItem() == null)
        {
            laErrorMessage.setText("Gelieve een type like te selecteren");
        }
        else
        {
            int indexLike = getIndexLikeFromAccount(loggedInAccount.getLogin());
            
            if (indexLike == -1)
            {
                laErrorMessage.setText("Je hebt de post nog niet geliket");
            }
            else
            {
                LikeTrans likeTrans = new LikeTrans();
                Like like = new Like();
                like.setType((LikeType) cbLikeType.getSelectionModel().getSelectedItem());
                like.setAccountlogin(loggedInAccount.getLogin());
                like.setPostid(selectedPost.getId());
                
                try
                {
                    likeTrans.likeWijzigen(like);
                    lvLikeView.getItems().set(indexLike, like); // De like wijzigen op de juiste plaats in de listview
                    
                }
                catch(DBException e)
                {
                    laErrorMessage.setText("Contacteer uw beheerder");
                }
                catch (ApplicationException e)
                {
                    laErrorMessage.setText(e.getMessage());
                }
            }
        }
    }

    @FXML
    private void buLikeToevoegenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
                
        if (cbLikeType.getSelectionModel().getSelectedItem() == null)
        {
            laErrorMessage.setText("Gelieve een type like te selecteren");
        }
        else
        {
            
            if (getIndexLikeFromAccount(loggedInAccount.getLogin()) == -1)
            {
                LikeTrans likeTrans = new LikeTrans();
                Like like = new Like();
                like.setAccountlogin(loggedInAccount.getLogin());
                like.setPostid(selectedPost.getId());
                like.setType(LikeType.valueOf(cbLikeType.getValue().toString()));
            
                try
                {
                    likeTrans.LikesToevoegen(like);
                    lvLikeView.getItems().add(like);
                    buLikeToevoegen.setDisable(true);
                    buLikeWijzigen.setDisable(false);
                    buLikeVerwijderen.setDisable(false);
                }
                catch (ApplicationException e)
                {
                    laErrorMessage.setText(e.getMessage());
                }
                catch (DBException e)
                {
                    laErrorMessage.setText(e.getMessage());
                }
            }
            else
            {
                laErrorMessage.setText("U heeft deze post al geliket");
            }
        }
    }

    @FXML
    private void buLikeVerwijderenClicked(ActionEvent event)
    {
        int indexLike = getIndexLikeFromAccount(loggedInAccount.getLogin());
        
        if (indexLike != -1)
        {
            LikeTrans likeTrans = new LikeTrans();
            try
            {
                likeTrans.likeVerwijderen(loggedInAccount.getLogin(), selectedPost.getId());
                lvLikeView.getItems().remove(indexLike);
                buLikeToevoegen.setDisable(false);
                buLikeWijzigen.setDisable(true);
                buLikeVerwijderen.setDisable(true);
            }
            catch (ApplicationException e)
            {
                laErrorMessage.setText(e.getMessage());
            }
            catch(DBException ex)
            {
                laErrorMessage.setText("Contacteer uw beheerder");
            }
        }
        else
        {
            laErrorMessage.setText("U heeft deze post nog niet geliket");
        }
    }

    @FXML
    private void buPostVerwijderenClicked(ActionEvent event)
    {
        if (lvLikeView.getItems().size() == 0)
        {
            PostTrans tr = new PostTrans();
            if (selectedPost.getEigenaar().equals(loggedInAccount.getLogin()))
            {
                try
                {
                    tr.postVerwijderen(selectedPost.getId(), loggedInAccount.getLogin());
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
            else
            {
                laErrorMessage.setText("U kan geen post verwijderen van iemand anders");
            }
        }
        else
        {
            laErrorMessage.setText("U kan geen post verwijderen met likes");
        }
    }
    
    @FXML
    private void buOkClicked(ActionEvent event)
    { 
        mainApp.laadHomeScherm(loggedInAccount);
    }
    
    @FXML
    private void buCancelClicked(ActionEvent event)
    {
        mainApp.laadHomeScherm(loggedInAccount);
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
