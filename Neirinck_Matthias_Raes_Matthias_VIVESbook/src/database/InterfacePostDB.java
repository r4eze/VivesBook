/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Post;
import exception.DBException;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfacePostDB {
    
    public Post zoekPost(int id) throws DBException;

    public ArrayList<Post> zoekAllePostsVanAccountEnVrienden(String login) throws
      DBException;

    public void verwijderenPost(Integer id) throws DBException;

    public Integer toevoegenPost(Post post) throws DBException;

}
