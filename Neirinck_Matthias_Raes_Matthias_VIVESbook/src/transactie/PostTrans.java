package transactie;

import bags.Post;
import database.PostDB;
import exception.ApplicationException;
import exception.DBException;
import java.util.ArrayList;

public class PostTrans implements InterfacePostTrans
{
    @Override
    public void postToevoegen(Post post) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        if (post == null)
        {
            throw new ApplicationException("Er werd geen post opgegeven");
        }

        postDB.toevoegenPost(post);
    }
    
    public Integer postToevoegenReturnId(Post post) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        if (post == null)
        {
            throw new ApplicationException("Er werd geen post opgegeven");
        }

        return postDB.toevoegenPost(post);
    }

    @Override
    public void postVerwijderen(Integer postID, String verwijderaar) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        Post post = postDB.zoekPost(postID);
        
        if (post == null)
        {
            throw new ApplicationException("De opgegeven post bestaat niet");
        }
        if(!post.getEigenaar().equals(verwijderaar))
        {
            throw new ApplicationException("U bent geen eigenaar van de post");
        }
        
        postDB.verwijderenPost(postID);
    }
    
    public ArrayList<Post> zoekAllePostsVanAccountEnVrienden(String login) throws DBException, ApplicationException
    {
        PostDB postDb = new PostDB();
        return postDb.zoekAllePostsVanAccountEnVrienden(login);
    }
}