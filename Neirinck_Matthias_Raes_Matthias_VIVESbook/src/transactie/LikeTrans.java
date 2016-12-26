package transactie;

import bags.Like;
import database.LikeDB;
import exception.ApplicationException;
import exception.DBException;
import java.util.ArrayList;

public class LikeTrans implements InterfaceLikesTrans
{
    @Override
    public void LikesToevoegen(Like like) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        checkIfLikeComplete(like);

        if(likeDB.zoekLike(like.getAccountlogin(), like.getPostid()) != null)
        {
            throw new ApplicationException("De post werd al geliket");
        }
        
        likeDB.toevoegenLike(like);
    }

    @Override
    public void likeVerwijderen(String account, Integer postID) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        likeDB.verwijderenLike(account, postID);
    }

    @Override
    public void likeWijzigen(Like like) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        checkIfLikeComplete(like);
        likeDB.wijzigenLike(like);
    }
    
    public ArrayList<Like> zoekAlleLikesVanPost(int postID) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        return likeDB.zoekAlleLikesVanPost(postID);
    }

    private void checkIfLikeComplete(Like like) throws ApplicationException
    {
        if(like.getAccountlogin() == null)
        {
            throw new ApplicationException("De werd geen login meegegeven");
        }
        if(like.getPostid() == null)
        {
            throw new ApplicationException("Er werd geen post ID meegegeven");
        }
        if(like.getType() == null)
        {
            throw new ApplicationException("Er werd geen type meegegeven");
        }
    }
}
