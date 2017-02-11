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
    private ListView lvLikes;
    
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
        
        // Als je eigenaar van de post bent is de button voor de post te verwijderen beschikbaar
        buPostVerwijderen.setDisable(!selectedPost.getEigenaar().equals(loggedInAccount.getLogin()));
        
        // Geen disable want dan is de tekst minder zichtbaar
        taPostInhoud.setEditable(false); // de tekst niet kunnen wijzigen
        taPostInhoud.setMouseTransparent(true); // De textArea niet actief kunnen zetten
        taPostInhoud.setFocusTraversable(false); // er niet naar toe kunnen gaan via tab
        
        int indexLike = getIndexLikeFromAccount(loggedInAccount.getLogin());
        
        // Als het account de post reeds geliket heeft, het juiste type van de like in de combobox instellen
        // En de button om een like toe te voegen disablen
        // Als er het account de post nog niet geliket heeft, de buttons om een like te wijzigen en verwijderen disablen
        if(indexLike != -1)
        {
            buLikeToevoegen.setDisable(true);
            buLikeWijzigen.setDisable(false);
            buLikeVerwijderen.setDisable(false);
            // het type van de like instellen in de combobox
            cbLikeType.getSelectionModel().select(((Like) lvLikes.getItems().get(indexLike)).getType());
        }
        else
        {
            buLikeToevoegen.setDisable(false);
            buLikeWijzigen.setDisable(true);
            buLikeVerwijderen.setDisable(true);
        }
        
        lvLikes.setMouseTransparent(true);
        lvLikes.setFocusTraversable(false);
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
    
    // De combobox cbLikeType opvullen met de mogelijke types voor een like
    private void initializeCombobox()
    {
        cbLikeType.getItems().add(LikeType.leuk);
        cbLikeType.getItems().add(LikeType.geweldig);
        cbLikeType.getItems().add(LikeType.grappig);
        cbLikeType.getItems().add(LikeType.verbluft);
        cbLikeType.getItems().add(LikeType.verdrietig);
        cbLikeType.getItems().add(LikeType.boos);
    }
    
    // returnt de index van de like in lvLikeView voor het account (a.d.h.v. accountlogin in de parameter)
    // -1 als er geen like van dat account bestaat
    private int getIndexLikeFromAccount(String accountLogin)
    {
        ObservableList<Like> likes = lvLikes.getItems();
        
        for (int i = 0; i < likes.size(); i++)
        {
            if (likes.get(i).getAccountlogin().equals(accountLogin))
            {
                return i;
            }
        }
        
        return -1;
    }

    // De listview lvLikes opvullen met de likes van de post
    public void initializeLikeListView(Post post)
    {
        try
        {
            LikeTrans likeTrans = new LikeTrans();
            ArrayList<Like> likeList = likeTrans.zoekAlleLikesVanPost(post.getId());

            for (Like like : likeList)
            {
                lvLikes.getItems().add(like);
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
                Like like = new Like();
                like.setType((LikeType) cbLikeType.getSelectionModel().getSelectedItem());
                like.setAccountlogin(loggedInAccount.getLogin());
                like.setPostid(selectedPost.getId());
                
                try
                {
                    LikeTrans likeTrans = new LikeTrans();
                    likeTrans.likeWijzigen(like);
                    lvLikes.getItems().set(indexLike, like); // De like wijzigen op de juiste plaats in de listview
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
                Like like = new Like();
                like.setAccountlogin(loggedInAccount.getLogin());
                like.setPostid(selectedPost.getId());
                like.setType(LikeType.valueOf(cbLikeType.getValue().toString()));
            
                try
                {
                    LikeTrans likeTrans = new LikeTrans();
                    likeTrans.LikesToevoegen(like);
                    lvLikes.getItems().add(like);
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
            try
            {
                LikeTrans likeTrans = new LikeTrans();
                likeTrans.likeVerwijderen(loggedInAccount.getLogin(), selectedPost.getId());
                
                // De like uit de listview verwijderen
                lvLikes.getItems().remove(indexLike);
                
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
        if (lvLikes.getItems().size() == 0)
        {
            if (selectedPost.getEigenaar().equals(loggedInAccount.getLogin()))
            {
                try
                {
                    PostTrans postTrans = new PostTrans();
                    postTrans.postVerwijderen(selectedPost.getId(), loggedInAccount.getLogin());
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
