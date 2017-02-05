package transactie;

import bags.Account;
import bags.Post;
import bags.Like;
import database.AccountDB;
import datatype.Geslacht;
import datatype.LikeType;
import exception.ApplicationException;
import exception.DBException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class LikeTransTest {
    
    Account account;
    AccountTrans accountTrans;
    AccountDB accountDB;
    
    Post post;
    PostTrans postTrans;
    
    Like like;
    LikeTrans likeTrans;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public LikeTransTest() {
        accountTrans = new AccountTrans();
        accountDB = new AccountDB();
        postTrans = new PostTrans();
        likeTrans = new LikeTrans(); 
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
        
        like = new Like();
        like.setAccountlogin(account.getLogin());
    }
    
    @After
    public void tearDown() 
    {
        try
        {
            accountDB.verwijderenAccount(account);
        }
        catch(DBException ex)
        {
            System.out.println("-tearDown-" + ex);
        }
    }

    // Positieve test: like toevoegen met type leuk
    @Test
    public void testLikesToevoegenTypeLeuk()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            post = postTrans.postToevoegen(post); // het ID instellen van de post
            
            like.setType(LikeType.leuk);
            like.setPostid(post.getId());
            
            likeTrans.LikesToevoegen(like);
            
            Like ophaalLike = likeTrans.zoekAlleLikesVanPost(post.getId()).get(0);
            
            assertEquals(like.getAccountlogin(), ophaalLike.getAccountlogin());
            assertEquals(like.getPostid(), ophaalLike.getPostid());
            assertEquals(like.getType(), ophaalLike.getType());

            likeTrans.likeVerwijderen(account.getLogin(), post.getId());
            postTrans.postVerwijderen(post.getId(), account.getLogin());
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testPostToevoegen - " + ex);
        }
    }
    
    // Positieve test: like toevoegen met type geweldig
    @Test
    public void testLikesToevoegenTypeGeweldig()
    {
        
    }
    
    // Negatieve test: like toevoegen als post niet bestaat
    @Test
    public void testLikesToevoegenPostBestaatNiet()
    {
        
    }
}
