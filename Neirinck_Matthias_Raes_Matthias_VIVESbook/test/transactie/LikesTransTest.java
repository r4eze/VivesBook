package transactie;

import bags.Account;
import bags.Post;
import bags.Likes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class LikesTransTest {
    
    Account account;
    AccountTrans accountTrans;
    
    Post post;
    PostTrans postTrans;
    
    LikesTrans likeTrans;
    Likes like;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public LikesTransTest() {
        accountTrans = new AccountTrans();
        postTrans = new PostTrans();
        likeTrans = new LikesTrans();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // Positieve test: like toevoegen
    @Test
    public void testLikesToevoegen(){
        
    }
}
