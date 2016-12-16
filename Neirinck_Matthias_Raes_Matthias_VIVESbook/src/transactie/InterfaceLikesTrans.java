/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import bags.Likes;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceLikesTrans {

    public void LikesToevoegen(Likes like) throws DBException, ApplicationException;

    public void likeVerwijderen(String account, Integer postID) throws DBException, ApplicationException;

    public void likeWijzigen(Likes like) throws DBException, ApplicationException;

}
