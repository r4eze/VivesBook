package database;

import bags.Account;
import bags.Post;
import datatype.Geslacht;
import exception.DBException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PostDBTest {
    
    private PostDB postdb;
    private AccountDB accountdb;
    private Account account;
    private Post post;
    @Rule
    public ExpectedException thrown;
    public PostDBTest() {
        postdb = new PostDB();
        account = new Account();
        post = new Post();
        accountdb = new AccountDB();
    }
    
    @Before
    public void setUp() {
        account.setNaam("Defoort");
        account.setVoornaam("Mieke");
        account.setLogin("miekedefoort");
        account.setPaswoord("wachtwoord123");
        account.setEmailadres("miekedefoort@hotmail.com");
        account.setGeslacht(Geslacht.V);
        try{
        accountdb.toevoegenAccount(account);
        }
        catch(DBException ex){
            System.out.println("setUp - " + ex);
        }
        

    }
    
    @After
    public void tearDown() {
        try{
            postdb.verwijderenPost(post.getId());
            accountdb.verwijderenAccount(account);
        }
        catch(DBException e){
            System.out.println("tearDown - " + e);
        }
    }
    
    //Positieve test: post toevoegen
    //Positieve test: post opzoeken
    @Test
    public void testPostToevoegen(){
        post.setEigenaar(account.getLogin());
        post.setTekst("Post om te testen");
        try{
            post.setId(postdb.toevoegenPost(post));
            Post terug = postdb.zoekPost(post.getId());
            assertEquals(terug.getTekst(),post.getTekst());
            assertEquals(post.getEigenaar(), account.getLogin());
        }
        catch(DBException ex){
            System.out.println("testPostToevoegen - " + ex);
        }
    }
    
    //Positieve test: verwijderen post
    @Test
    public void testPostVerwijderen(){
        post.setEigenaar(account.getLogin());
        post.setTekst("Post om te testen");
        try{
            post.setId(postdb.toevoegenPost(post));
            postdb.verwijderenPost(post.getId());
            Post terug = postdb.zoekPost(post.getId());
            assertNull(terug);
        }
        catch(DBException ex){
            System.out.println("testPostVerwijderen - " + ex);
        }
    }

}
