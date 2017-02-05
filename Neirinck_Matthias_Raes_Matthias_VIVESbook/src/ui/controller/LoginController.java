package ui.controller;

import bags.Account;
import database.AccountDB;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import transactie.AccountTrans;
import ui.VIVESbook;

public class LoginController implements Initializable
{
    @FXML
    private TextField tfLogin;

    @FXML
    private PasswordField pfPaswoord;

    @FXML
    private Label laErrorMessage;

    private VIVESbook mainApp;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setText(null);
    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    private void login(String login, String passwoord) throws ApplicationException
    {
        laErrorMessage.setText(null);
        AccountDB acct = new AccountDB();
        Account acc = null;
        try
        {
            acc = acct.zoekAccountOpLogin(login);
        }
        catch (DBException e)
        {
            laErrorMessage.setText("Database probleem: contacteer uw IT-beheerder");
        }
        if (acc == null)
        {
            throw new ApplicationException("Gebruiker onbekend");
        }
        else
        {
            if (acc.getPaswoord().equals(passwoord))
            {
                mainApp.laadHomeScherm(acc);
            }
            else
            {
                throw new ApplicationException("Paswoord incorrect");
            }
        }
    }

    @FXML
    private void buLoginClicked(ActionEvent event)
    {
        try
        {
            login(tfLogin.getText(), pfPaswoord.getText());
        }
        catch (ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void buNieuwClicked(ActionEvent event)
    {
        mainApp.laadAccountToevoegenScherm();
    }
}