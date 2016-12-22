package transactie;

import bags.Account;
import database.AccountDB;
import datatype.Geslacht;
import exception.DBException;
import org.junit.After;
import org.junit.Before;
import transactie.VriendschapTrans;

public class VriendschapTransTest {
    
    private Account account, vriend;
    private VriendschapTrans vriendTrans;
    private AccountDB accountDB;
    
    public VriendschapTransTest() {
        vriendTrans = new VriendschapTrans();
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
        vriend.setLogin("miekedefoort");
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

        // jezelfd toevoegen als vriend
}
