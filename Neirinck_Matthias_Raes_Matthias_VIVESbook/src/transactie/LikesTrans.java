/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import bags.Likes;
import database.LikesDB;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public class LikesTrans implements InterfaceLikesTrans
{

    @Override
    public void LikesToevoegen(Likes like) throws ApplicationException, DBException
    {
        LikesDB ldb = new LikesDB();
        CheckIfLikeComplete(like);
        Likes lik = ldb.zoekLike(like.getAccountlogin(), like.getPostid());
        if (lik != null)
        {
            throw new ApplicationException("Like bestaat al");
        }
        ldb.toevoegenLike(like);
    }

    @Override
    public void likeVerwijderen(String account, Integer postID) throws ApplicationException
    {
        LikesDB ldb = new LikesDB();
        try
        {
            ldb.verwijderenLike(account, postID);
        }
        catch (DBException e)
        {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public void likeWijzigen(Likes like) throws ApplicationException
    {
        LikesDB ldb = new LikesDB();
        try
        {
            ldb.wijzigenLike(like);
        }
        catch (DBException e)
        {
            throw new ApplicationException(e.getMessage());
        }
    }

    private void CheckIfLikeComplete(Likes like) throws ApplicationException
    {
        if (like.getAccountlogin() == null || like.getPostid() == null || like.getType() == null)
        {
            throw new ApplicationException("De like werd niet ingevuld");
        }

    }

}
