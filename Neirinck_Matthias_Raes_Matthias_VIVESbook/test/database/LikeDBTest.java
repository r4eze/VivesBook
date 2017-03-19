package database;

import bags.Account;
import bags.Like;
import bags.Post;
import datatype.Geslacht;
import datatype.LikeType;
import exception.DBException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LikeDBTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Account account, vriend;
    private LikeDB likedb;
    private AccountDB accountdb;
    private PostDB postdb;
    private Post post;
    private Like like;

    public LikeDBTest() {
        likedb = new LikeDB();
        postdb = new PostDB();
        accountdb = new AccountDB();
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

        post = new Post();
        post.setEigenaar(account.getLogin());
        post.setTekst("Testpost");

        like = new Like();

    }

    @After
    public void tearDown() {
        try {
            likedb.verwijderenLike(vriend.getLogin(), post.getId());
            likedb.verwijderenLike(account.getLogin(), post.getId());
            postdb.verwijderenPost(post.getId());
            accountdb.verwijderenAccount(account);
            accountdb.verwijderenAccount(vriend);
        } catch (DBException ex) {
            System.out.println("Tear down - " + ex);
        }

    }

    //positieve test like toevoegen aan een post. 
    //positieve test like opzoeken van post
    @Test
    public void testLikeToevoegen() {
        try {
            accountdb.toevoegenAccount(account);
            accountdb.toevoegenAccount(vriend);
            post.setId(postdb.toevoegenPost(post));
            like.setAccountlogin(vriend.getLogin());
            like.setPostid(post.getId());
            like.setType(LikeType.leuk);
            likedb.toevoegenLike(like);

            Like teruglike = likedb.zoekLike(vriend.getLogin(), post.getId());
            assertEquals(teruglike.getAccountlogin(), vriend.getLogin());
            assertEquals(teruglike.getPostid(), post.getId());
            assertEquals(teruglike.getType(), like.getType());
        } catch (DBException ex) {
            System.out.println("testLikeToevoegen - " + ex);
        }
    }

    //negatieve test, like toevoegen zonder type. 
    @Test
    public void testLikeToevoegenZonderType() throws DBException {
        //We verwachten hier een nullPointer omdat de methode name() wordt opgeroepen in de LikeDB-klasse op het liketype. 
        //Vandaar ook dat we dan een nullPointerException krijgen vooraleer de database een exception kan opgooien. 
        thrown.expect(NullPointerException.class);
        accountdb.toevoegenAccount(account);
        accountdb.toevoegenAccount(vriend);
        post.setId(postdb.toevoegenPost(post));
        like.setAccountlogin(vriend.getLogin());
        like.setPostid(post.getId());
        likedb.toevoegenLike(like);
    }

    //negatieve test, like toevoegen zonder postID. 
    @Test
    public void testLikeToevoegenZonderPostId() throws DBException {
        //PostID wordt ook eruit gefilterd door de DBklasse d.m.v. een nullPointerException. 
        thrown.expect(NullPointerException.class);
        accountdb.toevoegenAccount(account);
        accountdb.toevoegenAccount(vriend);
        post.setId(postdb.toevoegenPost(post));
        like.setPostid(null);
        like.setAccountlogin(vriend.getLogin());
        like.setType(LikeType.leuk);
        likedb.toevoegenLike(like);
    }

    //negatieve test, like toevoegen zonder accountlogin 
    @Test
    public void testLikeToevoegenZonderAccountLogin() throws DBException {
        thrown.expect(DBException.class);
        accountdb.toevoegenAccount(account);
        accountdb.toevoegenAccount(vriend);
        post.setId(postdb.toevoegenPost(post));
        like.setPostid(post.getId());
        like.setAccountlogin(null);
        like.setType(LikeType.leuk);
        likedb.toevoegenLike(like);
    }

    //positieve test, like wijzigen
    @Test
    public void testLikeWijzigen() {
        try {
            accountdb.toevoegenAccount(account);
            accountdb.toevoegenAccount(vriend);
            post.setId(postdb.toevoegenPost(post));
            like.setAccountlogin(vriend.getLogin());
            like.setPostid(post.getId());
            like.setType(LikeType.leuk);
            likedb.toevoegenLike(like);

            Like tewijzigen = new Like();
            tewijzigen.setAccountlogin(vriend.getLogin());
            tewijzigen.setPostid(post.getId());
            tewijzigen.setType(LikeType.geweldig);
            likedb.wijzigenLike(tewijzigen);

            Like teruglike = likedb.zoekLike(vriend.getLogin(), post.getId());
            assertEquals(teruglike.getAccountlogin(), vriend.getLogin());
            assertEquals(teruglike.getPostid(), post.getId());
            assertEquals(teruglike.getType(), LikeType.geweldig);
        } catch (DBException ex) {
            System.out.println("testLikeToevoegen - " + ex);
        }
    }

    //negatieve test, like wijzigen die niet bestaat. 
    @Test
    public void testLikeWijzigenNietBestaat() throws DBException {
        //Hierbij blijf ik een account en post toevoegen zodat daar zeker geen foutmeldingen rond ontstaan. 
        accountdb.toevoegenAccount(account);
        accountdb.toevoegenAccount(vriend);
        post.setId(postdb.toevoegenPost(post));
        like.setPostid(post.getId());
        like.setAccountlogin(vriend.getLogin());
        like.setType(LikeType.leuk);

        Like tewijzigen = new Like();
        tewijzigen.setPostid(post.getId());
        tewijzigen.setAccountlogin(vriend.getLogin());
        tewijzigen.setType(LikeType.geweldig);

        likedb.wijzigenLike(tewijzigen);

        assertNull(likedb.zoekLike(tewijzigen.getAccountlogin(), tewijzigen.getPostid()));

    }

    //positieve test, like verwijderen
    @Test
    public void testLikeVerwijderen() {
        try {
            accountdb.toevoegenAccount(account);
            accountdb.toevoegenAccount(vriend);
            post.setId(postdb.toevoegenPost(post));
            like.setAccountlogin(vriend.getLogin());
            like.setPostid(post.getId());
            like.setType(LikeType.leuk);
            likedb.toevoegenLike(like);

            likedb.verwijderenLike(vriend.getLogin(), post.getId());

            Like teruglike = likedb.zoekLike(vriend.getLogin(), post.getId());
            assertNull(teruglike);
        } catch (DBException ex) {
            System.out.println("testLikeToevoegen - " + ex);
        }
    }

    

}
