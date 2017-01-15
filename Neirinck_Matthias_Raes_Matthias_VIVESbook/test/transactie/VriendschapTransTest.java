package transactie;

import bags.Account;
import bags.Vriendschap;
import database.AccountDB;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class VriendschapTransTest {
    
    private Account account, vriend;
    private VriendschapTrans vriendTrans;
    private AccountTrans accountTrans;
    private AccountDB accountDB;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public VriendschapTransTest() {
        vriendTrans = new VriendschapTrans();
        accountTrans = new AccountTrans();
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
    public void testVriendschapToevoegen()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            Vriendschap ophaalVriendschap = vriendTrans.zoekVriendschap(account.getLogin(), vriend.getLogin());
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin(ophaalVriendschap.getAccountlogin());
            Account ophaalVriend = accountTrans.zoekAccountOpLogin(ophaalVriendschap.getAccountvriendlogin());
            
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
            
            vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
        }catch(DBException | ApplicationException ex){
            System.out.println("testVriendschapToevoegen - " + ex);
        }
    }
    
    // Negatieve test: vriendschap toevoegen met 2 dezelfde logins
    @Test
    public void testVriendschapToevoegenDezelfdeLogin() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            vriend.setLogin(account.getLogin());
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
        
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenMetJezelf - " + ex);
        }
    }
    
    // Negatieve test: de vriendschap bestaat al
    @Test
    public void testVriendschapToevoegenBestaatAl() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);

            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
        }
        catch(ApplicationException ex)
        {
            try
            {
                vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
            }
            catch(DBException ex1)
            {
                System.out.println("testVriendschapToevoegenBestaatAl - " + ex1);
            }
            
            // Exception opnieuw opgooien zodat de test slaagt
            throw ex;
        }
        catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenBestaatAl - " + ex);
        }  
    }
    
    // Negatieve test: vriendschap toevoegen zonder account
    @Test
    public void testVriendschapToevoegenAccountNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(null, vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenAccountNull - " + ex);
        }
    }
}
