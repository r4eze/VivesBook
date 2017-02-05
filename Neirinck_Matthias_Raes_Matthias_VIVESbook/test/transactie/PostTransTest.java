package transactie;

import bags.Account;
import bags.Post;
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

public class PostTransTest {
    
    Account account;
    AccountTrans accountTrans;
    AccountDB accountDB;
    
    Post post;
    PostTrans postTrans;
    
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public PostTransTest() {
        accountTrans = new AccountTrans();
        accountDB = new AccountDB();
        postTrans = new PostTrans();
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
        
        post = new Post();
        post.setEigenaar(account.getLogin());
        post.setTekst("test");
    }
    
    @After
    public void tearDown() 
    {
        try{
            accountDB.verwijderenAccount(account);
        }
        catch(DBException ex)
        {
            System.out.println("-tearDown- " + ex);
        }
    }

    // Positieve test: post toevoegen
    @Test
    public void testPostToevoegen()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            post = postTrans.postToevoegen(post); // het ID instellen van de post
            
            Post ophaalPost =  postTrans.zoekPost(post.getId());
            
            assertEquals(post.getId(), ophaalPost.getId());
            assertEquals(post.getEigenaar(), ophaalPost.getEigenaar());
            assertEquals(post.getTekst(), ophaalPost.getTekst());
            
            postTrans.postVerwijderen(post.getId(), account.getLogin());
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testPostToevoegen - " + ex);
        }
    }
    
    // Negatieve test: null object als post toe te voegen
    @Test
    public void testPostToevoegenNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            postTrans.postToevoegen(null);
        }
        catch(DBException ex)
        {
            System.out.println("testPostToevoegenNull - " + ex);
        }
    }
    
    // Negatieve test: post toevoegen maar account zit niet in database
    @Test
    public void testPostToevoegenAccountBestaatNiet() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            postTrans.postToevoegen(post);
        }
        catch(DBException ex)
        {
            System.out.println("testPostToevoegen - " + ex);
        }
    }
    
    
}
