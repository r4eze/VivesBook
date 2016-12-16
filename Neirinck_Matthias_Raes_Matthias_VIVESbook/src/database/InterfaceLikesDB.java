/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Likes;
import exception.DBException;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceLikesDB {

    public void toevoegenLike(Likes like) throws DBException;

    public void wijzigenLike(Likes teWijzigenLike) throws DBException;

    public void verwijderenLike(String login, Integer postid) throws DBException;

    public Likes zoekLike(String login, int postid) throws DBException;

    public ArrayList<Likes> zoekAlleLikesVanPost(int postID) throws DBException;
}
