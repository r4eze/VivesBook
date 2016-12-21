package transactie;

import bags.Likes;
import database.LikesDB;
import exception.ApplicationException;
import exception.DBException;

public class LikesTrans implements InterfaceLikesTrans
{

    @Override
    public void LikesToevoegen(Likes like) throws DBException, ApplicationException
    {
        LikesDB likeDB = new LikesDB();
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
        LikesDB likeDB = new LikesDB();
        likeDB.verwijderenLike(account, postID);
    }

    @Override
    public void likeWijzigen(Likes like) throws DBException, ApplicationException
    {
        LikesDB likeDB = new LikesDB();
        checkIfLikeComplete(like);
        likeDB.wijzigenLike(like);
    }

    private void checkIfLikeComplete(Likes like) throws ApplicationException
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
