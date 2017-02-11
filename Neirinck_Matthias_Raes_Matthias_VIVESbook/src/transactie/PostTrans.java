package transactie;

import bags.Post;
import transactie.AccountTrans;
import database.PostDB;
import exception.ApplicationException;
import exception.DBException;
import java.util.ArrayList;

public class PostTrans implements InterfacePostTrans
{
    @Override
    public Post postToevoegen(Post post) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        AccountTrans accountTrans = new AccountTrans();
        
        if (post == null)
        {
            throw new ApplicationException("Er werd geen post opgegeven");
        }
        
        checkAlleVeldenIngevuld(post);
        
        if(accountTrans.zoekAccountOpLogin(post.getEigenaar()) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }
        if(post.getId() != null)
        {
            throw new ApplicationException("Id wordt automatisch bepaald en mag dus niet opgegeven worden");
        }
        if(accountTrans.zoekAccountOpLogin(post.getEigenaar()) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }

        post.setId(postDB.toevoegenPost(post));
        
        return post;
    }

    @Override
    public void postVerwijderen(Integer postID, String verwijderaar) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        if(postID == null)
        {
            throw new ApplicationException("Er werd geen post meegegeven");
        }
        
        Post post = postDB.zoekPost(postID);
        
        if (post == null)
        {
            throw new ApplicationException("De opgegeven post bestaat niet");
        }
        if(verwijderaar == null || verwijderaar.trim().equals(""))
        {
            throw new ApplicationException("Er werd geen account ingevuld");
        }
        if(!post.getEigenaar().equals(verwijderaar))
        {
            throw new ApplicationException("U bent geen eigenaar van de post");
        }
        
        LikeTrans likeTrans = new LikeTrans();
        if(!likeTrans.zoekAlleLikesVanPost(postID).isEmpty())
        {
            throw new ApplicationException("Je kan geen post verwijderen met likes");
        }
        
        postDB.verwijderenPost(postID);
    }
    
    public Post zoekPost(int id) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        // id is een int dus kan niet null zijn, dus geen if hiervoor
        
        return postDB.zoekPost(id);
    }
    
    public ArrayList<Post> zoekAllePostsVanAccountEnVrienden(String login) throws DBException, ApplicationException
    {
        PostDB postDb = new PostDB();
        AccountTrans accountTrans = new AccountTrans();
        
        if(login == null || login.trim().equals(""))
        {
            throw new ApplicationException("Login niet ingevuld");
        }
        if(accountTrans.zoekAccountOpLogin(login) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }
        
        return postDb.zoekAllePostsVanAccountEnVrienden(login);
    }
    
    private void checkAlleVeldenIngevuld(Post post) throws ApplicationException
    {
        if(post.getEigenaar() == null || post.getEigenaar().trim().equals(""))
        {
            throw new ApplicationException("Eigenaar niet ingevuld");
        }
        if(post.getTekst() == null || post.getTekst().trim().equals(""))
        {
            throw new ApplicationException("Tekst niet ingevuld");
        }
        
        // Geen check voor ID en datum want deze worden automatisch door de database ingesteld
    }
}