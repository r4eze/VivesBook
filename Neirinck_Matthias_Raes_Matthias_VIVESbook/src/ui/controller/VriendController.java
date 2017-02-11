package ui.controller;

import bags.Account;
import exception.ApplicationException;
import exception.DBException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
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
    private ListView lvVrienden;

    @FXML
    private TextField tfVriendNaam;
    
    @FXML
    private Button buVerwijderVriend;

    @FXML
    private Label laErrorMessage;
    
    // Is nodig om bij te houden door het sorteren
    private ObservableList<Account> obsVrienden;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        laErrorMessage.setText(null);
        buVerwijderVriend.setDisable(true);
        obsVrienden = FXCollections.observableArrayList();
        
        // Vrienden zoeken in de listview a.d.h.v. het textfield
        tfZoekVriend.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                changeListView(oldValue, newValue);
            }
        });
        
        // De button om een vriend te verwijderen enkel beschikbaar maken als er een vriend geselecteerd is
        lvVrienden.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {       
                if(newValue != null)
                {
                    buVerwijderVriend.setDisable(false);
                }
                else
                {
                    buVerwijderVriend.setDisable(true);
                }
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
    
    // De listview lvVrienden opvullen met alle vrienden van het ingelogde account
    public void initializeListview()
    {
        try
        {
            VriendschapTrans vriendTrans = new VriendschapTrans();
            
            ArrayList<Account> vrienden = vriendTrans.zoekVrienden(loggedInAccount.getLogin());
            Collections.sort(vrienden); // sorteren op voornaam, naam

            for (Account a : vrienden)
            {
                obsVrienden.add(a);
                lvVrienden.getItems().add(a);
            }
            
            updateListViewColors();
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

    // De listview aanpassen zodat het enkel vrienden bevat waarvan de "newValue" (= waarde van textfield) voorkomt in de naam of voornaam
    public void changeListView(String oldValue, String newValue)
    {
        // Als de nieuwe waarde korter is dan de oude waarde, moet de listview opnieuw gevuld worden met de originele waarden
        // Want normaal zijn er meer zoekresultaten bij een kortere waarde
        if(oldValue != null && (newValue.length() < oldValue.length()))
        {
            lvVrienden.setItems(obsVrienden);
        }

        ObservableList<Account> filteredList = FXCollections.observableArrayList();
        
        for(Object entry : lvVrienden.getItems())
        {
            Account a = (Account) entry;
            
            if(a.getNaam().toLowerCase().contains(newValue.toLowerCase()) || a.getVoornaam().toLowerCase().contains(newValue.toLowerCase()))
            {
                filteredList.add(a);
            }
        }
        
        lvVrienden.setItems(filteredList);
        updateListViewColors();
    }
    
    // De achtergrondkleur van de cellen in listview lvVriend instellen via een css klasse
    // Als de vriend ingelogd is: groen
    // Als de vriend niet ingelogd is: rood
    public void updateListViewColors()
    {
        lvVrienden.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>(){
            @Override
            public ListCell<Account> call(ListView<Account> p) {

                ListCell<Account> cell = new ListCell<Account>() {

                    @Override
                    protected void updateItem(Account t, boolean bln) {
                        super.updateItem(t, bln);

                        if (t != null) {
                            setText(t.toString());

                            if (t.isLoggedIn()) {
                                getStyleClass().add("loggedIn");
                            } else {
                                getStyleClass().add("loggedOut");
                            }
                        } else {
                            setText("");
                        }
                    }
                };

                return cell;
            }
        });
    }
    
    @FXML
    private void buVriendToevoegenClicked(ActionEvent event)
    {
        laErrorMessage.setText(null);
        
        VriendschapTrans vriendTrans = new VriendschapTrans();
        try
        {
            vriendTrans.VriendschapToevoegen(loggedInAccount.getLogin(), tfVriendNaam.getText());
            lvVrienden.getItems().add(new AccountTrans().zoekAccountOpLogin(tfVriendNaam.getText()));
            Collections.sort(lvVrienden.getItems()); // zodat de pas toegevoegde vriend niet onderaan de lijst staat
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
        
        try
        {
            VriendschapTrans vriendTrans = new VriendschapTrans();
            
            vriendTrans.vriendschapVerwijderen(loggedInAccount.getLogin(), ((Account) lvVrienden.getItems().get(lvVrienden.getSelectionModel().getSelectedIndex())).getLogin());
            lvVrienden.getItems().remove(lvVrienden.getSelectionModel().getSelectedIndex());
            updateListViewColors();
            lvVrienden.getSelectionModel().clearSelection();
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
