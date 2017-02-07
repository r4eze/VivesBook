package ui.controller;

import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    private void buLoginClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        
        try
        {
            AccountTrans accountTrans = new AccountTrans();
            
            if(accountTrans.passwordMatchesLogin(tfLogin.getText(), pfPaswoord.getText()))
            {
                accountTrans.inloggenAccount(tfLogin.getText(), pfPaswoord.getText());
                mainApp.laadHomeScherm(accountTrans.zoekAccountOpLogin(tfLogin.getText()));
            }
            else
            {
                throw new ApplicationException("Login of paswoord incorrect");
            }
        }
        catch(DBException e)
        {
            laErrorMessage.setText("Database probleem: contacteer uw IT-beheerder");
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