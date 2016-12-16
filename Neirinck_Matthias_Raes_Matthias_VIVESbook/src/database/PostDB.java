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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

public class PostDB implements InterfacePostDB
{

    @Override
    public ArrayList<Post> zoekAllePostsVanAccountEnVrienden(String login) throws DBException
    {
        ArrayList<Post> posts = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("select * from post where eigenaar = ? or eigenaar in (select accountvriendlogin from vriendschap where accountlogin = ?) order by datum DESC;");)
            {
                stat.setString(1, login);
                stat.setString(2, login);
                stat.execute();
                try (ResultSet r = stat.getResultSet())
                {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    while (r.next())
                    {
                        Post post = new Post();
                        Timestamp datum = r.getTimestamp("datum");
                        post.setDatum(datum.toLocalDateTime());
                        post.setEigenaar(r.getString("eigenaar"));
                        post.setId(r.getInt("id"));
                        post.setTekst(r.getString("tekst"));
                        posts.add(post);

                    }
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQLException in ZoekAccount - bij resultset");
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAccount - bij prepare statement");
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccount - bij connectie");

        }
        return posts;
    }

    @Override
    public void verwijderenPost(Integer id) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("delete from post where id=?");
                stat.setInt(1, id);
                stat.execute();
            }
            catch (SQLException e)
            {
                throw new DBException("Database probleem - bij prepare" + e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DBException("Database probleem - bij connectie");
        }
    }

    @Override
    public Integer toevoegenPost(Post post) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("insert into post(tekst, eigenaar) values(?,?)");
                stat.setString(1, post.getTekst());
                stat.setString(2, post.getEigenaar());
                stat.execute();
                ResultSet r = stat.getGeneratedKeys();
                post.setId(r.getInt(1));
            }
            catch (SQLException ex)
            {
                throw new DBException("SQLException in ZoekAccount - bij prepare");
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccount - bij connectie");
        }
        return post.getId();
    }

    public void wijzigenPost(Post post, Post nieuwePost) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("Update post set tekst=? where id=?");
                stat.setString(1, nieuwePost.getTekst());
                stat.setInt(2, post.getId());
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in wijzigenPost - bij prepare" + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccount - bij connectie");
        }
    }

    @Override
    public Post zoekPost(int id) throws DBException
    {
        Post post = new Post();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("select * from post where id=?");
                stat.setInt(1, id);
                stat.execute();
                try
                {
                    ResultSet r = stat.getResultSet();
                    if (r.next())
                    {

                        Timestamp datum = r.getTimestamp("datum");
                        post.setDatum(datum.toLocalDateTime());
                        post.setEigenaar(r.getString("eigenaar"));
                        post.setId(id);
                        post.setTekst(r.getString("tekst"));
                    }
                }
                catch (SQLException e)
                {
                    throw new DBException(e.getMessage());
                }
            }
            catch (SQLException e)
            {
                throw new DBException(e.getMessage());
            }
        }
        catch (SQLException e)
        {
            throw new DBException(e.getMessage());
        }
        return post;
    }
}
