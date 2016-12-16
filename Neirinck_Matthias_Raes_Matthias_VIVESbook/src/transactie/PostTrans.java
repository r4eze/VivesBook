/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import bags.Post;
import database.PostDB;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public class PostTrans implements InterfacePostTrans
{

    @Override
    public void postToevoegen(Post post) throws ApplicationException, DBException
    {
        PostDB pdb = new PostDB();
        if (post != null)
        {
            pdb.toevoegenPost(post);
        }
        else
        {
            throw new ApplicationException("Er werd geen post opgegeven");
        }
    }

    @Override
    public void postVerwijderen(Integer postID, String verwijderaar) throws ApplicationException, DBException
    {
        PostDB pdb = new PostDB();
        Post post = pdb.zoekPost(postID);
        if (post != null && post.getEigenaar().equals(verwijderaar))
        {
            pdb.verwijderenPost(postID);
        }
        else
        {
            throw new ApplicationException("De opgegeven post bestaat niet");
        }

    }

    public void postWijzigen(Post post, Post nieuwPost) throws ApplicationException, DBException
    {
        PostDB pdb = new PostDB();
        Post p = pdb.zoekPost(post.getId());
        if (p == null)
        {
            throw new ApplicationException("De te wijzigen post bestaat niet");
        }
        else
        {
            pdb.wijzigenPost(post, nieuwPost);
        }

    }

}
