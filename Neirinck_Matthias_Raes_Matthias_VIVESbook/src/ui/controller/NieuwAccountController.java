/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import bags.Account;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import transactie.AccountTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author raesm
 */
public class NieuwAccountController implements Initializable
{

    private VIVESbook mainApp;

    private Account account = null;

    @FXML
    private TextField voornaam;

    @FXML
    private TextField naam;

    @FXML
    private TextField adres;

    @FXML
    private TextField login;

    @FXML
    private PasswordField paswoord;

    @FXML
    private ChoiceBox geslacht;

    @FXML
    private Label errorLabel;

    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        geslacht.setItems(FXCollections.observableArrayList("M", "V"));
        errorLabel.setVisible(false);
        login.setDisable(false);
        cancelButton.setVisible(true);
    }

    @FXML
    private void btnOkClicked(ActionEvent event)
    {
        resetError();
        try
        {
            CheckIfFilledUp();
            MaakNieuwAccount();
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

    @FXML
    private void btnCancelClicked(ActionEvent event)
    {
        if (account != null)
        {
            mainApp.laadPostsScherm();
        }
        else
        {

            mainApp.laadLoginScherm();

        }
    }

    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    private void CheckIfFilledUp() throws ApplicationException
    {
        if (geslacht.getValue() == null)
        {
            throw new ApplicationException("Gelieve een geslacht te kiezen");
        }
        if (textFieldNotFilledUp(naam))
        {
            throw new ApplicationException("Naam is niet ingevuld");
        }
        if (textFieldNotFilledUp(voornaam))
        {
            throw new ApplicationException("Voornaam is niet ingevuld");
        }
        if (textFieldNotFilledUp(login))
        {
            throw new ApplicationException("Login is niet ingevuld");
        }
        if (textFieldNotFilledUp(paswoord))
        {
            throw new ApplicationException("Paswoord is niet ingevuld");
        }
        if (textFieldNotFilledUp(adres))
        {
            throw new ApplicationException("adres is niet ingevuld");
        }

    }

    private boolean textFieldNotFilledUp(TextField text)
    {
        return text.getText().trim().equals("");
    }

    private void resetError()
    {
        errorLabel.setVisible(false);
    }

    private void MaakNieuwAccount() throws ApplicationException, DBException
    {
        Account ac = new Account();
        ac.setEmailadres(adres.getText());
        ac.setGeslacht(Geslacht.valueOf(geslacht.getValue().toString()));
        ac.setLogin(login.getText());
        ac.setPaswoord(paswoord.getText());
        ac.setVoornaam(voornaam.getText());
        ac.setNaam(naam.getText());

        if (account == null)
        {
            AccountTrans tr = new AccountTrans();
            tr.accountToevoegen(ac);
            mainApp.laadLoginScherm();
        }
        else
        {
            if (account.getLogin().equals(ac.getLogin()))
            {
                AccountTrans tr = new AccountTrans();
                tr.accountWijzigen(ac);
                mainApp.setAccount(ac);
                mainApp.laadPostsScherm();
            }
            else
            {
                throw new ApplicationException("Gelieve uw login niet aan te passen");
            }
            //Account gaan aanpassen in database 
        }

    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
        adres.setText(account.getEmailadres());
        geslacht.setValue(account.getGeslacht().toString());
        login.setText(account.getLogin());
        paswoord.setText(account.getPaswoord());
        naam.setText(account.getNaam());
        voornaam.setText(account.getVoornaam());

    }

    public void setValues(Account account)
    {
        adres.setText(account.getEmailadres());
        geslacht.setValue(account.getGeslacht().toString());
        login.setText(account.getLogin());
        paswoord.setText(account.getPaswoord());
        naam.setText(account.getNaam());
        voornaam.setText(account.getVoornaam());
        login.setDisable(true);
        cancelButton.setVisible(false);

    }

}
