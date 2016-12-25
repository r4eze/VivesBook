/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Like;
import exception.DBException;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceLikesDB {

    public void toevoegenLike(Like like) throws DBException;

    public void wijzigenLike(Like teWijzigenLike) throws DBException;

    public void verwijderenLike(String login, Integer postid) throws DBException;

    public Like zoekLike(String login, int postid) throws DBException;

    public ArrayList<Like> zoekAlleLikesVanPost(int postID) throws DBException;
}
