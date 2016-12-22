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
            try(PreparedStatement stat = conn.prepareStatement("INSERT INTO likes values(?,?,?)"))
            {
                stat.setString(1, like.getAccountlogin());
                stat.setInt(2, like.getPostid());
                stat.setString(3, like.getType().toString());
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in toevoegenLike - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in toevoegenLike - connection - " + ex);
        }
    }

    @Override
    public void wijzigenLike(Likes teWijzigenLike) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stat = conn.prepareStatement("UPDATE likes SET type=? WHERE accountlogin=? AND postid=?"))
            {
                stat.setString(1, teWijzigenLike.getType().name());
                stat.setString(2, teWijzigenLike.getAccountlogin());
                stat.setInt(3, teWijzigenLike.getPostid());
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in wijzigenLike - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in wijzigenLike - connection - " + ex);
        }
    }

    @Override
    public void verwijderenLike(String login, Integer postid) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("DELETE FROM likes WHERE accountlogin=? AND postid=?");
                stat.setString(1, login);

                if(postid == null){
                    stat.setNull(2, java.sql.Types.NULL);
                }else{
                    stat.setInt(2, postid);
                }
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in verwijderenLike - statement" + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in verwijderenLike - connection - " + ex);
        }
    }

    @Override
    public Likes zoekLike(String login, int postid) throws DBException
    {
        Likes returnLike = null;
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("SELECT * FROM likes WHERE postid = ? AND accountlogin = ?");
                
                stat.setInt(1, postid);
                stat.setString(2, login);
                stat.execute();
                
                try(ResultSet r = stat.getResultSet())
                {
                    Likes like = new Likes();
                    
                    if (r.next())
                    {
                        like.setAccountlogin(r.getString("accountlogin"));
                        like.setPostid(r.getInt("postid"));
                        like.setType(LikeType.valueOf(r.getString("type")));
                        returnLike = like;
                    }
                    
                    return returnLike;
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekLike - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekLike - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekLike - connection - " + ex);
        }
    }

    @Override
    public ArrayList<Likes> zoekAlleLikesVanPost(int postID) throws DBException
    {
        ArrayList likeList = new ArrayList<>();
        
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stat = conn.prepareStatement("SELECT * FROM likes WHERE postid=?"))
            {
                stat.setInt(1, postID);
                stat.execute();

                try(ResultSet r = stat.getResultSet())
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
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekAlleLikesVanPost - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAlleLikesVanPost - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAlleLikesVanPost - connection - " + ex);
        }

        return likeList;
    }

}
