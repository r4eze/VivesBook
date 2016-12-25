package ui.controller;

import bags.Account;
import bags.Post;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import ui.VIVESbook;

public class PostWijzigenController implements Initializable{
    
    private VIVESbook mainApp;
    
    private Account loggedInAccount;
    
    private Post selectedPost;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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
}