package transactie;

import bags.Account;
import bags.Vriendschap;
import database.AccountDB;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import java.util.ArrayList;
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
    
    // Negatieve test: vriendschap toevoegen zonder vriend
    @Test
    public void testVriendschapToevoegenVriendNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), null);
        }catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenVriendNull - " + ex);
        }
    }
    
    // Negatieve test: vriendschap toevoegen als account niet bestaat
    @Test
    public void testVriendschapToevoegenAccountBestaatNiet() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenAccountBestaatNiet - " + ex);
        }
    }
    
    // Negatieve test: vriendschap toevoegen als vriend niet bestaat
    @Test
    public void testVriendschapToevoegenVriendBestaatNiet() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapToevoegenVriendBestaatNiet - " + ex);
        }
    }
    
    // Postieve test: vriendschap verwijderen
    @Test
    public void testVriendschapVerwijderen()
    {     
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
        }catch(DBException | ApplicationException ex)
        {
            System.out.println("testVriendschapVerwijderen - " + ex);
        }
    }
    
    // Negatieve test: vriendschap verwijderen zonder account
    @Test
    public void testVriendschapVerwijderenAccountNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            vriendTrans.vriendschapVerwijderen(null, vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapVerwijderenAccountNull - " + ex);
        }catch(ApplicationException ex)
        {
            try
            {
                vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
            }
            catch(DBException ex1)
            {
                System.out.println("testVriendschapVerwijderenAccountNull - " + ex1);
            }
            
            // Exception opnieuw opgooien zodat de test slaagt
            throw ex;
        }
    }
    
    // Negatieve test: vriendschap verwijderen zonder vriend
    @Test
    public void testVriendschapVerwijderenVriendNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            vriendTrans.vriendschapVerwijderen(account.getLogin(), null);
        }catch(DBException ex)
        {
            System.out.println("testVriendschapVerwijderenVriendNull - " + ex);
        }catch(ApplicationException ex)
        {
            try
            {
                vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
            }
            catch(DBException ex1)
            {
                System.out.println("testVriendschapVerwijderenVriendNull - " + ex1);
            }
            
            // Exception opnieuw opgooien zodat de test slaagt
            throw ex;
        }
    }
    
    // Negatieve test: vriendschap verwijderen als account niet bestaat
    // Lukt niet want je kan geen vriendschap toevoegen als het account niet bestaat,
    // en je kan een account niet verwijderen als er reeds een vriendschap bestaat
    
    // Negatieve test: vriendschap verwijderen als vriend niet bestaat
    // Lukt niet want je kan geen vriendschap toevoegen als de vriend niet bestaat,
    // en je kan een account niet verwijderen als er reeds een vriendschap bestaat
    
    // Negatieve test: vriendschap verwijderen die niet bestaat
    @Test
    public void testVriendschapVerwijderenBestaatNiet() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            
            vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testVriendschapVerwijderenBestaatNiet - " + ex);
        }
    }
    
    // Positieve test: vriendschap zoeken die niet bestaat
    @Test
    public void testZoekVriendschapBestaatNiet()
    {
        try
        {
            assertEquals(null, vriendTrans.zoekVriendschap(account.getLogin(), vriend.getLogin()));
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testZoekVriendschapBestaatNiet - " + ex);
        }
    }
    
    // Negatieve test: vriendschap zoeken zonder account
    @Test
    public void testZoekVriendschapAccountNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            Vriendschap ophaalVriendschap = vriendTrans.zoekVriendschap(null, vriend.getLogin());
        }catch(DBException ex)
        {
            System.out.println("testZoekVriendschapAccountNull - " + ex);
        }
    }
    
    // Negatieve test: vriendschap zoeken zonder vriend
    @Test
    public void testZoekVriendschapVriendNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            Vriendschap ophaalVriendschap = vriendTrans.zoekVriendschap(account.getLogin(), null);
        }catch(DBException ex)
        {
            System.out.println("testZoekVriendschapVriendNull - " + ex);
        }
    }
    
    // Positieve test: vrienden zoeken
    @Test
    public void testZoekVrienden()
    {
        Account vriend2 = new Account();
        vriend2.setNaam("Jansens");
        vriend2.setVoornaam("Jan");
        vriend2.setLogin("janjansens");
        vriend2.setPaswoord("geheim123");
        vriend2.setEmailadres("janjansens@hotmail.com");
        vriend2.setGeslacht(Geslacht.M);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.accountToevoegen(vriend);
            accountTrans.accountToevoegen(vriend2);
            
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend.getLogin());
            vriendTrans.VriendschapToevoegen(account.getLogin(), vriend2.getLogin());
            
            ArrayList<Account> vrienden = vriendTrans.zoekVrienden(account.getLogin());
            
            Account ophaalVriend = vrienden.get(1);
            Account ophaalVriend2 = vrienden.get(0);
            
            assertEquals("Petersen", ophaalVriend.getNaam());
            assertEquals("Peter", ophaalVriend.getVoornaam());
            assertEquals("peterpetersen", ophaalVriend.getLogin());
            assertEquals("wachtwoord12345", ophaalVriend.getPaswoord());
            assertEquals("peter@hotmail.com", ophaalVriend.getEmailadres());
            assertEquals(Geslacht.M, ophaalVriend.getGeslacht());
            
            assertEquals("Jansens", ophaalVriend2.getNaam());
            assertEquals("Jan", ophaalVriend2.getVoornaam());
            assertEquals("janjansens", ophaalVriend2.getLogin());
            assertEquals("geheim123", ophaalVriend2.getPaswoord());
            assertEquals("janjansens@hotmail.com", ophaalVriend2.getEmailadres());
            assertEquals(Geslacht.M, ophaalVriend2.getGeslacht());
            
            vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend.getLogin());
            vriendTrans.vriendschapVerwijderen(account.getLogin(), vriend2.getLogin());
            accountDB.verwijderenAccount(vriend2);
        }catch(DBException | ApplicationException ex){
            System.out.println("testZoekVrienden - " + ex);
        }
    }
    
    // Negatieve test: vrienden zoeken zonder account
    @Test
    public void testZoekVriendenAccountNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            ArrayList<Account> vrienden = vriendTrans.zoekVrienden(null);
        }catch(DBException ex)
        {
            System.out.println("testZoekVriendenAccountNull - " + ex);
        }
    }
}
