/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import bags.Post;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfacePostTrans {

    public Post postToevoegen(Post post) throws DBException, ApplicationException;

    public void postVerwijderen(Integer postID, String verwijderaar) throws DBException, ApplicationException;
}
