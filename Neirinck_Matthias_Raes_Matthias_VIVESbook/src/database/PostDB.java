package database;

import bags.Account;
import bags.Post;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

public class PostDB implements InterfacePostDB
{
    @Override
    public Post zoekPost(int id) throws DBException
    {
        Post returnPost = null;
        
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("SELECT * FROM post WHERE id=?");
                
                stat.setInt(1, id);
                stat.execute();
                
                try
                {
                    try(ResultSet r = stat.getResultSet())
                    {
                        Post post = new Post();
                        
                        if (r.next())
                        {
                            post.setId(r.getInt("id"));
                            post.setDatum(r.getTimestamp("datum").toLocalDateTime());
                            post.setTekst(r.getString("tekst"));
                            post.setEigenaar(r.getString("eigenaar"));
                            returnPost = post;
                        }
                        
                        return returnPost;
                    }
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekPost - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekPost - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekPost - connection - " + ex);
        }
    }
    
    @Override
    public ArrayList<Post> zoekAllePostsVanAccountEnVrienden(String login) throws DBException
    {
        ArrayList<Post> posts = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement(
                    "SELECT * FROM post WHERE eigenaar = ? OR eigenaar IN (select accountvriendlogin from vriendschap where accountlogin = ?) ORDER BY datum DESC;");)
            {
                stat.setString(1, login);
                stat.setString(2, login);
                stat.execute();
                try (ResultSet r = stat.getResultSet())
                {
                    while (r.next())
                    {
                        Post post = new Post();
                        post.setId(r.getInt("id"));
                        post.setDatum(r.getTimestamp("datum").toLocalDateTime());
                        post.setEigenaar(r.getString("eigenaar"));
                        post.setTekst(r.getString("tekst"));
                        posts.add(post);
                    }
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekAllePostsVanAccountEnVrienden - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAllePostsVanAccountEnVrienden - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAllePostsVanAccountEnVrienden - connection - " + ex);

        }
        return posts;
    }

    @Override
    public void verwijderenPost(Integer id) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM post WHERE id = ?");)
            {
                if(id == null){
                    stmt.setNull(1, java.sql.Types.NULL);
                }else{
                    stmt.setInt(1, id);
                }
                
                stmt.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in verwijderenPost - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in verwijderenPost - connection - " + ex);
        }
    }

    @Override
    public Integer toevoegenPost(Post post) throws DBException
    {
        int primaryKey = -1;
        
        try(Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stat = conn.prepareStatement(
                    "INSERT INTO post(tekst, eigenaar) values(?,?)", Statement.RETURN_GENERATED_KEYS);)
            {
                stat.setString(1, post.getTekst());
                stat.setString(2, post.getEigenaar());
                stat.execute();
                
                ResultSet generatedKeys = stat.getGeneratedKeys();
                if(generatedKeys.next())
                {
                    primaryKey = generatedKeys.getInt(1);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in toevoegenPost - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in toevoegenPost - connection - " + ex);
        }
        return primaryKey;
    }
}
