package transactie;

import bags.Post;
import database.PostDB;
import exception.ApplicationException;
import exception.DBException;

public class PostTrans implements InterfacePostTrans
{
    @Override
    public void postToevoegen(Post post) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        if (post != null)
        {
            throw new ApplicationException("Er werd geen post opgegeven");
        }

        postDB.toevoegenPost(post);
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

    public void postWijzigen(Post post, Post nieuwPost) throws DBException, ApplicationException
    {
        PostDB postDB = new PostDB();
        
        if(post == null)
        {
            throw new ApplicationException("Er werd geen post meegegeven");
        }
        if(nieuwPost == null)
        {
            throw new ApplicationException("Er werd geen nieuwe post meegegeven");
        }
        if (postDB.zoekPost(post.getId()) == null)
        {
            throw new ApplicationException("De te wijzigen post bestaat niet");
        }

        postDB.wijzigenPost(post, nieuwPost);
    }
}