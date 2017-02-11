package ui.controller;

import bags.Account;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import transactie.AccountTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author raesm
 */
public class AccountToevoegenController implements Initializable
{
    private VIVESbook mainApp;

    @FXML
    private TextField tfNaam, tfVoornaam, tfEmail, tfLogin;

    @FXML
    private PasswordField pfPaswoord;

    @FXML
    private ComboBox cbGeslacht;

    @FXML
    private Label laErrorMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initializeCombobox();
        laErrorMessage.setText(null);
    }
    
    private void initializeCombobox()
    {
        cbGeslacht.getItems().add(Geslacht.M);
        cbGeslacht.getItems().add(Geslacht.V);
    }

    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    private void checkAlleVeldenIngevuld() throws ApplicationException
    {
        if (textFieldNotFilledUp(tfNaam))
        {
            throw new ApplicationException("Naam is niet ingevuld");
        }
        if (textFieldNotFilledUp(tfVoornaam))
        {
            throw new ApplicationException("Voornaam is niet ingevuld");
        }
        if (textFieldNotFilledUp(tfLogin))
        {
            throw new ApplicationException("Login is niet ingevuld");
        }
        if (textFieldNotFilledUp(pfPaswoord))
        {
            throw new ApplicationException("Paswoord is niet ingevuld");
        }
        if (textFieldNotFilledUp(tfEmail))
        {
            throw new ApplicationException("adres is niet ingevuld");
        }
        if (cbGeslacht.getValue() == null)
        {
            throw new ApplicationException("Gelieve een geslacht te kiezen");
        }
    }

    private boolean textFieldNotFilledUp(TextField text)
    {
        return text.getText().trim().equals("");
    }
    
    private Account getAccountFromFields()
    {
        Account account = new Account();
        
        account.setNaam(tfNaam.getText());
        account.setVoornaam(tfVoornaam.getText());
        account.setLogin(tfLogin.getText());
        account.setPaswoord(pfPaswoord.getText());
        account.setEmailadres(tfEmail.getText());
        account.setGeslacht((Geslacht) cbGeslacht.getSelectionModel().getSelectedItem());
        
        return account;
    }
    
    @FXML
    private void buOkClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        
        try
        {
            checkAlleVeldenIngevuld();
            
            Account nieuwAccount = getAccountFromFields();
            AccountTrans accountTrans = new AccountTrans();
            
            accountTrans.accountToevoegen(nieuwAccount);
            
            laErrorMessage.setText("Het account werd aangemaakt");
            laErrorMessage.setTextFill(Color.GREEN);
        }
        catch(DBException e)
        {
            laErrorMessage.setText("Contacteer uw beheerder");
            laErrorMessage.setTextFill(Color.RED);
        }
        catch(ApplicationException e)
        {
            laErrorMessage.setText(e.getMessage());
            laErrorMessage.setTextFill(Color.RED);

        }
    }
    
    @FXML
    private void buCancelClicked(ActionEvent event)
    {
        mainApp.laadLoginScherm();
    }
}
