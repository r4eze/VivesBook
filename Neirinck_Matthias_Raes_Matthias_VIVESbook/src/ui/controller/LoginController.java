/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import bags.Account;
import database.AccountDB;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import transactie.AccountTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author Katrien.Deleu
 */
public class LoginController implements Initializable
{

    @FXML
    private Button okKnop;

    @FXML
    private Button nieuwKnop;

    @FXML
    private TextField login;

    @FXML
    private PasswordField paswoord;

    @FXML
    private Label errorLabel;

    // referentie naar mainapp (start)
    private VIVESbook mainApp;

    private boolean ingelogd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        errorLabel.setVisible(false);
        ingelogd = false;

    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    private String processPassword(char[] pass)
    {
        String eindstring = "";
        for (char v : pass)
        {
            eindstring += v;
        }
        return eindstring;
    }

    private void login(String login, String passwoord) throws ApplicationException
    {
        resetError();
        AccountDB acct = new AccountDB();
        Account acc = null;
        try
        {
            acc = acct.zoekAccountOpLogin(login);
        }
        catch (DBException e)
        {
            errorLabel.setVisible(true);
            errorLabel.setText("Database probleem: contacteer uw IT-beheerder");
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
                ingelogd = true;
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
            login(login.getText(), paswoord.getText());
        }
        catch (ApplicationException e)
        {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void buNieuwClicked(ActionEvent event)
    {
        mainApp.laadAccountToevoegenScherm();
    }

    /**
     * Referentie naar mainApp (start) instellen
     *
     * @param mainApp referentie naar de runnable class die alle oproepen naar
     *                de schermen bestuurt
     */
    

    private void resetError()
    {
        errorLabel.setVisible(false);
    }
}
