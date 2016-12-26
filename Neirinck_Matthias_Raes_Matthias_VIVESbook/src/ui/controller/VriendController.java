package ui.controller;

import bags.Account;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import transactie.AccountTrans;
import transactie.VriendschapTrans;
import ui.VIVESbook;

public class VriendController implements Initializable
{
    private VIVESbook mainApp;
    
    private Account loggedInAccount;

    @FXML
    private TextField tfZoekVriend;

    @FXML
    private ListView lvVriend;

    @FXML
    private TextField tfVriendNaam;

    @FXML
    private Label laErrorMessage;
    
    private ObservableList<Account> obsVrienden;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setVisible(false);
        obsVrienden = FXCollections.observableArrayList();
        
        tfZoekVriend.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                changeListView(oldValue, newValue);
            }

        });
    }
    
    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }
    
    public void setData(Account account)
    {
        this.loggedInAccount = account;
        initializeListview();
    }
    
    public void initializeListview()
    {
        VriendschapTrans vriendTrans = new VriendschapTrans();
        try
        {
            ArrayList<Account> vrienden = vriendTrans.zoekVrienden(loggedInAccount.getLogin());
            
            for (Account a : vrienden)
            {
                obsVrienden.add(a);
                lvVriend.getItems().add(a);
            }
        }
        catch (DBException e)
        {
            System.out.println("Contacteer uw beheerder");
        }
        catch(ApplicationException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void changeListView(String oldValue, String newValue)
    {
        if(oldValue != null && (newValue.length() < oldValue.length()))
        {
            lvVriend.setItems(obsVrienden);
        }

        ObservableList<Account> filteredList = FXCollections.observableArrayList();
        for(Object entry : lvVriend.getItems())
        {
            Account a = (Account) entry;
            if(a.getNaam().toLowerCase().contains(newValue.toLowerCase()) || a.getVoornaam().toLowerCase().contains(newValue.toLowerCase()))
            {
                filteredList.add(a);
            }
        }
        
        lvVriend.setItems(filteredList);
    }   
    
    @FXML
    private void buVriendToevoegenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        
        VriendschapTrans vriendTrans = new VriendschapTrans();
        try
        {
            vriendTrans.VriendschapToevoegen(loggedInAccount.getLogin(), tfVriendNaam.getText());
            lvVriend.getItems().add(new AccountTrans().zoekAccountOpLogin(tfVriendNaam.getText()));
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

    @FXML
    private void buVerwijderVriendClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        VriendschapTrans vriendTrans = new VriendschapTrans();
        try
        {
            vriendTrans.vriendschapVerwijderen(loggedInAccount.getLogin(), ((Account) lvVriend.getItems().get(lvVriend.getSelectionModel().getSelectedIndex())).getLogin());
            lvVriend.getItems().remove(lvVriend.getSelectionModel().getSelectedIndex());
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

    @FXML
    private void buCancelClicked(ActionEvent event)
    {
        mainApp.laadHomeScherm(loggedInAccount);
    }
}
