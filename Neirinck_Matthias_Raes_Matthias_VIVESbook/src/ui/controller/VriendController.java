/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import bags.Account;
import database.VriendschapDB;
import exception.ApplicationException;
import exception.DBException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import transactie.VriendschapTrans;
import ui.VIVESbook;

/**
 * FXML Controller class
 *
 * @author raesm
 */
public class VriendController implements Initializable
{

    private Account account;

    private VIVESbook mainApp;

    @FXML
    private TextField vriendZoek;

    @FXML
    private ListView vriendView;

    @FXML
    private TextField vriendNaam;

    @FXML
    private Label errorLabel;

    private ArrayList<Account> vrienden;

    private int index;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        errorLabel.setVisible(false);
        vriendZoek.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                try
                {
                    changeListView(newValue);
                    index = -1;
                    vriendView.getSelectionModel().clearSelection();
                }
                catch (ApplicationException ex)
                {
                    errorLabel.setVisible(true);
                    errorLabel.setText(ex.getMessage());
                    System.out.println(ex.getMessage());
                }
            }

        });
        index = -1;
    }

    public void changeListView(String value) throws ApplicationException
    {
        VriendschapDB vrdb = new VriendschapDB();
        try
        {
            vrienden = vrdb.zoekVrienden(account.getLogin(), value);
            ObservableList view = FXCollections.observableArrayList();
            for (Account v : vrienden)
            {
                view.add(v.getVoornaam() + " " + v.getNaam() + " | " + v.getLogin());
            }
            vriendView.setItems(view);
        }
        catch (DBException e)
        {
            throw new ApplicationException("Database problem:" + e.getMessage());
        }

    }

    public void initializeListview() throws IOException
    {
        VriendschapDB vrdb = new VriendschapDB();
        try
        {
            vrienden = vrdb.zoekVrienden(account.getLogin());
            ObservableList view = FXCollections.observableArrayList();
            for (Account v : vrienden)
            {
                view.add(v.getVoornaam() + " " + v.getNaam() + " | " + v.getLogin());
            }
            vriendView.setItems(view);
        }
        catch (DBException e)
        {
            throw new IOException("Database problem:" + e.getMessage());
        }

    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public void setMainApp(VIVESbook mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void btnToevoeg(ActionEvent event) throws ApplicationException
    {
        VriendschapTrans tr = new VriendschapTrans();
        try
        {
            tr.VriendschapToevoegen(account.getLogin(), vriendNaam.getText());
            changeListView(vriendZoek.getText());
        }
        catch (ApplicationException e)
        {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
        catch(DBException ex)
        {
            
        }
    }

    @FXML
    private void btnVerwijder(ActionEvent event)
    {
        resetError();
        VriendschapTrans tr = new VriendschapTrans();
        try
        {
            tr.vriendschapVerwijderen(account.getLogin(), vrienden.get(index).getLogin());
            changeListView(vriendZoek.getText());
        }
        catch (ApplicationException e)
        {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
        catch(DBException ex)
        {
            
        }
    }

    private void resetError()
    {
        errorLabel.setVisible(false);
    }

    @FXML
    private void btnCancel(ActionEvent event)
    {
        mainApp.laadPostsScherm();
    }

    @FXML
    public void viewClicked(MouseEvent event)
    {
        index = vriendView.getSelectionModel().getSelectedIndex();
        //System.out.println(vriendView.getItems()); => debugging purpose
    }
}
