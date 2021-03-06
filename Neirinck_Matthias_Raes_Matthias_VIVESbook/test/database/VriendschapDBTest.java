package database;

import bags.Account;
import bags.Vriendschap;
import datatype.Geslacht;
import exception.DBException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VriendschapDBTest {
    
    private Account account, vriend;
    private VriendschapDB vriendDB;
    private AccountDB accountDB;
    
    public VriendschapDBTest() {
        vriendDB = new VriendschapDB();
        accountDB = new AccountDB();
    }
    
    @Before
    public void setUp() {
        account = new Account();
        account.setNaam("Defoort");
        account.setVoornaam("Mieke");
        account.setLogin("miekedefoort");
        account.setPaswoord("wachtwoord123");
        account.setEmailadres("miekedefoort@hotmail.com");
        account.setGeslacht(Geslacht.V);
        
        vriend = new Account();
        vriend.setNaam("Petersen");
        vriend.setVoornaam("Peter");
        vriend.setLogin("peterpetersen");
        vriend.setPaswoord("wachtwoord12345");
        vriend.setEmailadres("peter@hotmail.com");
        vriend.setGeslacht(Geslacht.M);
    }
    
    @After
    public void tearDown() {
        try{
            accountDB.verwijderenAccount(account);
            accountDB.verwijderenAccount(vriend);
        }catch(DBException ex){
            System.out.println("-tearDown- " + ex);
        }
    }

    // Positieve test: vriendschap toevoegen
    // Positieve test: vriendschap zoeken
    @Test
    public void testToevoegenVriendschap() {
        try{
            accountDB.toevoegenAccount(account);
            accountDB.toevoegenAccount(vriend);
            vriendDB.toevoegenVriendschap(account.getLogin(), vriend.getLogin());
            
            Vriendschap ophaalVriendschap = vriendDB.zoekVriendschap(account.getLogin(), vriend.getLogin());
            Account ophaalAcc = accountDB.zoekAccountOpLogin(ophaalVriendschap.getAccountlogin());
            Account ophaalVriend = accountDB.zoekAccountOpLogin(ophaalVriendschap.getAccountvriendlogin());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
            assertEquals("Petersen", ophaalVriend.getNaam());
            assertEquals("Peter", ophaalVriend.getVoornaam());
            assertEquals("peterpetersen", ophaalVriend.getLogin());
            assertEquals("wachtwoord12345", ophaalVriend.getPaswoord());
            assertEquals("peter@hotmail.com", ophaalVriend.getEmailadres());
            assertEquals(Geslacht.M, ophaalVriend.getGeslacht());
            
            vriendDB.verwijderenVriendschap(account.getLogin(), vriend.getLogin());
        }catch(DBException ex){
            System.out.println("testToevoegenVriendschap - " + ex);
        }
    }
    
    // Positieve test: vriendschap verwijderen
    @Test
    public void testVerwijderenVriendschap(){
        try{
            accountDB.toevoegenAccount(account);
            accountDB.toevoegenAccount(vriend);
            vriendDB.toevoegenVriendschap(account.getLogin(), vriend.getLogin());
            
            vriendDB.verwijderenVriendschap(account.getLogin(), vriend.getLogin());
            
            Vriendschap ophaalVriendschap = vriendDB.zoekVriendschap(account.getLogin(), vriend.getLogin());
            assertNull(ophaalVriendschap);
            
        }catch(DBException ex){
            System.out.println("testVerwijderenVriendschap - " + ex);
        }
    }
}