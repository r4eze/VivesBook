package transactie;

import bags.Account;
import bags.Post;
import exception.ApplicationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PostTransTest {
    
    Account account;
    AccountTrans accountTrans;
    
    Post post;
    PostTrans postTrans;
    
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public PostTransTest() {
        accountTrans = new AccountTrans();
        postTrans = new PostTrans();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // Positieve test: post toevoegen
    @Test
    public void testPostToevoegen(){
        
    }
    
    // Negatieve test: null object als post toe te voegen
    @Test
    public void testPostToevoegenNull() throws ApplicationException{
        
    }
}
