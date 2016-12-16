/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Likes;
import database.connect.ConnectionManager;
import datatype.LikeType;
import exception.ApplicationException;
import exception.DBException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikesDB implements InterfaceLikesDB
{

    @Override
    public void toevoegenLike(Likes like) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("insert into likes values(?,?,?)");
                stat.setString(1, like.getAccountlogin());
                System.out.println(like.getAccountlogin());
                stat.setInt(2, like.getPostid());
                stat.setString(3, like.getType().toString());
                stat.execute();
            }
            catch (SQLException e)
            {
                throw new DBException(e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DBException("database probleem - bij connectie");
        }
    }

    @Override
    public void wijzigenLike(Likes teWijzigenLike) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("update likes set type=? where accountlogin=? and postid=?");
                stat.setString(1, teWijzigenLike.getType().toString());
                stat.setString(2, teWijzigenLike.getAccountlogin());
                stat.setInt(3, teWijzigenLike.getPostid());
                stat.execute();
            }
            catch (SQLException e)
            {
                throw new DBException("database probleem - bij prepare");
            }
        }
        catch (SQLException e)
        {
            throw new DBException("Database probleem - bij connectie");
        }
    }

    @Override
    public void verwijderenLike(String login, Integer postid) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("delete from likes where accountlogin=? and postid=?");
                stat.setString(1, login);
                stat.setInt(2, postid);
                stat.execute();
            }
            catch (SQLException e)
            {
                throw new DBException("Database probleem - bij prepare" + e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DBException("database probleem - bij connectie");
        }
    }

    @Override
    public Likes zoekLike(String login, int postid) throws DBException
    {
        Likes like = null;
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("select * from likes where postid = ? and accountlogin = ?");
                stat.setInt(1, postid);
                stat.setString(2, login);
                stat.execute();
                try
                {
                    ResultSet r = stat.getResultSet();
                    if (r.next())
                    {
                        like.setAccountlogin(r.getString("accountlogin"));
                        like.setPostid(r.getInt("postid"));
                        like.setType(LikeType.valueOf(r.getString("type")));
                    }
                }
                catch (SQLException e)
                {
                    throw new DBException("Database probleem bij resultset");
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                throw new DBException("Database probleem bij prepare");

            }
        }
        catch (SQLException e)
        {
            throw new DBException("database probleem bij connectie");
        }
        return like;
    }

    @Override
    public ArrayList<Likes> zoekAlleLikesVanPost(int postID) throws DBException
    {
        ArrayList likeList = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("select * from likes where postid=?");
                stat.setInt(1, postID);
                stat.execute();
                ResultSet r = stat.getResultSet();
                try
                {
                    while (r.next())
                    {
                        Likes like = new Likes();
                        like.setAccountlogin(r.getString("accountlogin"));
                        like.setPostid(postID);
                        like.setType(LikeType.valueOf(r.getString("type")));
                        likeList.add(like);
                    }
                }
                catch (SQLException e)
                {
                    throw new DBException("database probleem - bij resultset" + e.getMessage());
                }
            }
            catch (SQLException e)
            {
                throw new DBException("database probleem - bij prepare" + e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DBException("database probleem - bij connectie " + e.getMessage());
        }

        return likeList;
    }

}
