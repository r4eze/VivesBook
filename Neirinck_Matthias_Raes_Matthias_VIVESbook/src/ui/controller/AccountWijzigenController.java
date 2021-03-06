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

public class AccountWijzigenController implements Initializable{

    private VIVESbook mainApp;
    
    private Account loggedInAccount;
    
    @FXML
    private TextField tfNaam, tfVoornaam, tfLogin, tfEmail;
    
    @FXML
    private PasswordField pfPaswoord;
    
    @FXML
    private ComboBox cbGeslacht;
    
    @FXML
    private Label laErrorMessage;
    
    @FXML
    private Button buOk;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCombobox();
        buOk.setText("Wijzigen");
        laErrorMessage.setText(null);
        tfLogin.setDisable(true);
    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }
    
    // De velden op het scherm opvullen met de velden van het account
    public void setData(Account account)
    {
        this.loggedInAccount = account;
        
        tfNaam.setText(account.getNaam());
        tfVoornaam.setText(account.getVoornaam());
        tfLogin.setText(account.getLogin());
        pfPaswoord.setText(account.getPaswoord());
        tfEmail.setText(account.getEmailadres());
        cbGeslacht.getSelectionModel().select(account.getGeslacht());
    }
    
    // De combobox cbGeslacht opvullen met de mogelijke geslachten
    private void initializeCombobox()
    {
        cbGeslacht.getItems().add(Geslacht.M);
        cbGeslacht.getItems().add(Geslacht.V);
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
            throw new ApplicationException("Adres is niet ingevuld");
        }
        if (cbGeslacht.getValue() == null)
        {
            throw new ApplicationException("Geslacht is niet ingevuld");
        }
    }

    private boolean textFieldNotFilledUp(TextField text)
    {
        return text.getText().trim().equals("");
    }
    
    // Returnt het account dat gemaakt wordt door de velden op het scherm
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
        laErrorMessage.setTextFill(Color.RED);
        
        try
        {
            checkAlleVeldenIngevuld();
            
            Account teWijzigenAccount = getAccountFromFields();
            
            if(teWijzigenAccount.equals(loggedInAccount))
            {
                laErrorMessage.setText("Er werd niets gewijzigd");
            }
            else
            {
                AccountTrans accountTrans = new AccountTrans();
            
                accountTrans.accountWijzigen(teWijzigenAccount);
                loggedInAccount = teWijzigenAccount;

                laErrorMessage.setText("Uw account werd aangepast");
                laErrorMessage.setTextFill(Color.GREEN);
            }
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
