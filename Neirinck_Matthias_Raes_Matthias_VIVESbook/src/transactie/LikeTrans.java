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
        PostTrans postTrans = new PostTrans();
        AccountTrans accountTrans = new AccountTrans();
        
        if(like == null)
        {
            throw new ApplicationException("Er werd geen like meegegeven");
        }
        
        checkAlleVeldenIngevuld(like);
        
        if(accountTrans.zoekAccountOpLogin(like.getAccountlogin()) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }
        if(postTrans.zoekPost(like.getPostid()) == null)
        {
            throw new ApplicationException("De post bestaat niet");
        }

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
        PostTrans postTrans = new PostTrans();
        AccountTrans accountTrans = new AccountTrans();
        
        if(account == null || account.trim().equals(""))
        {
            throw new ApplicationException("Er werd geen account ingevuld");
        }
        if(postID == null)
        {
            throw new ApplicationException("Er werd geen post ingevuld");
        }
        if(accountTrans.zoekAccountOpLogin(account) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }
        if(postTrans.zoekPost(postID) == null)
        {
            throw new ApplicationException("De post bestaat niet");
        }
        if(likeDB.zoekLike(account, postID) == null)
        {
            throw new ApplicationException("De like bestaat niet");
        }
        
        likeDB.verwijderenLike(account, postID);
    }

    @Override
    public void likeWijzigen(Like like) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        PostTrans postTrans = new PostTrans();
        AccountTrans accountTrans = new AccountTrans();
        
        if(like == null)
        {
            throw new ApplicationException("Er werd geen like meegegeven");
        }
        
        checkAlleVeldenIngevuld(like);
        
        // in principe niet nodig om te controleren of het account en de post bestaat want je kan een like pas toevoegen als het account en post bestaat
        
        if(accountTrans.zoekAccountOpLogin(like.getAccountlogin()) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }
        if(postTrans.zoekPost(like.getPostid()) == null)
        {
            throw new ApplicationException("De post bestaat niet");
        }
        if(likeDB.zoekLike(like.getAccountlogin(), like.getPostid()) == null)
        {
            throw new ApplicationException("De like bestaat niet");
        }
        
        likeDB.wijzigenLike(like);
    }
    
    public ArrayList<Like> zoekAlleLikesVanPost(int postID) throws DBException, ApplicationException
    {
        LikeDB likeDB = new LikeDB();
        PostTrans postTrans = new PostTrans();
        
        if(postTrans.zoekPost(postID) == null)
        {
            throw new ApplicationException("De post bestaat niet");
        }
        
        return likeDB.zoekAlleLikesVanPost(postID);
    }

    private void checkAlleVeldenIngevuld(Like like) throws ApplicationException
    {
        if(like.getAccountlogin() == null ||like.getAccountlogin().trim().equals(""))
        {
            throw new ApplicationException("De werd geen login ingevuld");
        }
        if(like.getPostid() == null)
        {
            throw new ApplicationException("Er werd geen post ID ingevuld");
        }
        if(like.getType() == null)
        {
            throw new ApplicationException("Er werd geen type ingevuld");
        }
    }
}
