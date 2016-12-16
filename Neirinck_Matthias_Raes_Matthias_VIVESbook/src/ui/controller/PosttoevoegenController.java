/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import bags.Account;
import bags.Likes;
import bags.Post;
import database.LikesDB;
import database.PostDB;
import datatype.LikeType;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import transactie.LikesTrans;
import transactie.PostTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author Katrien.Deleu
 */
public class PosttoevoegenController implements Initializable
{

    private Account account;

    // referentie naar mainapp (start)
    private VIVESbook mainApp = null;

    @FXML
    private TextArea postInhoud;

    @FXML
    private Label errorLabel;

    @FXML
    private ListView likeView;

    @FXML
    private Button btnLikeToevoegen;

    @FXML
    private Button btnLikeWijzigen;

    @FXML
    private Button btnLikeVerwijderen;

    @FXML
    private ChoiceBox likeType;

    @FXML
    private Button btnOK;

    @FXML
    private Label likeLabel;

    private Post post = null;

    @FXML
    private Button btnPostVerwijderen;

    private ArrayList<Likes> likes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        likes = new ArrayList<>();
        errorLabel.setVisible(false);
        btnLikeVerwijderen.setVisible(false);
        btnLikeToevoegen.setVisible(false);
        btnLikeWijzigen.setVisible(false);
        btnPostVerwijderen.setVisible(false);
        likeView.setVisible(false);
        likeType.setVisible(false);
        likeLabel.setVisible(false);
        postInhoud.setDisable(false);
        if (post != null)
        {
            btnLikeVerwijderen.setVisible(true);
            btnLikeToevoegen.setVisible(true);
            btnLikeWijzigen.setVisible(true);
            likeView.setVisible(true);
            likeType.setVisible(true);
            likeLabel.setVisible(true);
            postInhoud.setText(post.getTekst());
            initializeLikes(post);
            if (post.getEigenaar().equals(account.getLogin()))
            {
                btnOK.setText("Wijzigen");
                btnPostVerwijderen.setVisible(true);
                likeType.setVisible(false);
                btnLikeVerwijderen.setVisible(false);
                btnLikeToevoegen.setVisible(false);
                btnLikeToevoegen.setVisible(false);
                btnLikeWijzigen.setVisible(false);
            }
            else
            {
                postInhoud.setDisable(true);
                btnPostVerwijderen.setVisible(false);
            }
        }
        ObservableList likes = observableArrayList();
        likes.add(LikeType.leuk.toString());
        likes.add(LikeType.boos.toString());
        likes.add(LikeType.geweldig.toString());
        likes.add(LikeType.grappig.toString());
        likes.add(LikeType.verbluft.toString());
        likes.add(LikeType.verdrietig.toString());
        likeType.setItems(likes);
    }

    private boolean checkIfContainsAccount(ArrayList<Likes> likes, String like)
    {
        boolean contain = false;
        for (Likes lk : likes)
        {
            if (lk.getAccountlogin().equals(like))
            {
                contain = true;
            }
        }
        return contain;
    }

    private boolean checkIfContainsAccountLike(ArrayList<Likes> likes, Likes like)
    {
        boolean contains = false;
        for (Likes lk : likes)
        {
            if (lk.getAccountlogin().equals(account.getLogin()))
            {
                if (lk.getType().toString().equals(like.getType().toString()))
                {
                    contains = true;
                }
            }
        }
        return contains;
    }

    public ArrayList<Likes> initializeLikes(Post post)
    {
        ArrayList<Likes> likeList = new ArrayList<>();
        LikesDB ldb = new LikesDB();
        try
        {
            likeList = ldb.zoekAlleLikesVanPost(post.getId());
            likes = likeList;
            ObservableList lik = observableArrayList();
            for (Likes v : likeList)
            {
                lik.add(v.getType().toString() + "-" + v.getAccountlogin());
            }
            likeView.setItems(lik);
        }
        catch (DBException e)
        {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
        return likeList;
    }

    @FXML
    private void btnOkClicked(ActionEvent event)
    {
        resetError();
        PostTrans tr = new PostTrans();
        Post pos = new Post();
        pos.setEigenaar(account.getLogin());
        pos.setTekst(postInhoud.getText());
        if (post == null)
        {
            try
            {
                tr.postToevoegen(pos);
                mainApp.laadPostsScherm();
            }
            catch (ApplicationException e)
            {
                errorLabel.setText(e.getMessage());
                errorLabel.setVisible(true);
            }
            catch (DBException e)
            {
                errorLabel.setText("Contacteer uw beheerder");
                errorLabel.setVisible(true);
            }
        }
        else
        {
            if (post.getEigenaar().equals(account.getLogin()))
            {
                try
                {
                    tr.postWijzigen(post, pos);
                    mainApp.laadPostsScherm();
                }
                catch (ApplicationException e)
                {
                    errorLabel.setText(e.getMessage());
                    errorLabel.setVisible(true);
                }
                catch (DBException e)
                {
                    errorLabel.setText("Contacteer uw beheerder");
                    errorLabel.setVisible(true);
                }

            }
            else
            {
                mainApp.laadPostsScherm();
            }
        }

    }

    @FXML
    private void btnCancelClicked(ActionEvent event)
    {
        mainApp.laadPostsScherm();
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

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    @FXML
    private void btnLikeWijzigen(ActionEvent event)
    {
        resetError();
        if (likeType.getValue() == null)
        {
            errorLabel.setText("Gelieve een type like te selecteren");
            errorLabel.setVisible(true);
        }
        else
        {
            Likes like = new Likes();
            like.setType(LikeType.valueOf(likeType.getValue().toString()));
            like.setAccountlogin(account.getLogin());
            like.setPostid(post.getId());
            if (checkIfContainsAccountLike(likes, like))
            {
                errorLabel.setText("Je hebt deze post al geliket met " + like.getType().toString());
                errorLabel.setVisible(true);
            }
            else
            {
                LikesTrans tr = new LikesTrans();
                try
                {
                    tr.likeWijzigen(like);
                    initializeLikes(post);
                }
                catch (ApplicationException e)
                {
                    errorLabel.setText(e.getMessage());
                    errorLabel.setVisible(true);
                }
            }
        }
    }

    @FXML
    private void btnLikeToevoegen(ActionEvent event)
    {
        if (likeType.getValue() == null)
        {
            errorLabel.setText("Gelieve een type like te selecteren");
            errorLabel.setVisible(true);
        }
        else
        {
            LikesTrans tr = new LikesTrans();
            Likes like = new Likes();
            like.setAccountlogin(account.getLogin());
            like.setPostid(post.getId());
            like.setType(LikeType.valueOf(likeType.getValue().toString()));
            if (!checkIfContainsAccount(likes, like.getAccountlogin()))
            {
                try
                {
                    tr.LikesToevoegen(like);
                    initializeLikes(post);
                }
                catch (ApplicationException e)
                {
                    errorLabel.setText(e.getMessage());
                    errorLabel.setVisible(true);
                }
                catch (DBException e)
                {
                    errorLabel.setText(e.getMessage());
                    errorLabel.setVisible(true);
                }
            }
            else
            {
                errorLabel.setText("U heeft deze post al geliket");
                errorLabel.setVisible(true);
            }
        }
    }

    @FXML
    private void btnLikeVerwijderen(ActionEvent event)
    {
        if (checkIfContainsAccount(likes, account.getLogin()))
        {
            LikesTrans tr = new LikesTrans();
            try
            {
                tr.likeVerwijderen(account.getLogin(), post.getId());
                initializeLikes(post);
            }
            catch (ApplicationException e)
            {
                errorLabel.setVisible(true);
                errorLabel.setText(e.getMessage());
            }
        }
        else
        {
            errorLabel.setVisible(true);
            errorLabel.setText("U heeft deze post nog niet geliket");
        }
    }

    @FXML
    private void btnPostVerwijderen(ActionEvent event)
    {
        if (likes.isEmpty())
        {
            PostTrans tr = new PostTrans();
            if (post.getEigenaar().equals(account.getLogin()))
            {
                try
                {
                    tr.postVerwijderen(post.getId(), account.getLogin());
                    mainApp.laadPostsScherm();
                }
                catch (ApplicationException e)
                {
                    errorLabel.setVisible(true);
                    errorLabel.setText(e.getMessage());
                }
                catch (DBException e)
                {
                    errorLabel.setText("Contacteer uw beheerder");
                    errorLabel.setVisible(true);
                }
            }
            else
            {
                errorLabel.setVisible(true);
                errorLabel.setText("U kan geen post verwijderen van iemand anders");
            }
        }
        else
        {
            errorLabel.setText("U kan geen post verwijderen met likes");
            errorLabel.setVisible(true);
        }
    }

    private void resetError()
    {
        errorLabel.setVisible(false);
    }
}
