package database;

import bags.Account;
import bags.Vriendschap;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class VriendschapDB implements InterfaceVriendschapDB
{

    @Override
    public void toevoegenVriendschap(String account, String vriend) throws DBException
    {
        try(Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stat = conn.prepareStatement(
                "INSERT INTO vriendschap(accountlogin, accountvriendlogin) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS);)
            {

                  stat.setString(1, account);
                  stat.setString(2, vriend);
                  
                  stat.execute();
                  
                  stat.setString(1, vriend);
                  stat.setString(2, account);
                  
                  stat.execute();

            }catch (SQLException ex)
            {
                throw new DBException("SQL-exception in toevoegenVriendschap - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in toevoegenVriendschap - connection - " + ex);
        }
    }

    @Override
    public void verwijderenVriendschap(String account, String vriend) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement(
              "DELETE FROM vriendschap WHERE accountlogin = ? AND accountvriendlogin = ?");) {

                stat.setString(1, account);
                stat.setString(2, vriend);
                
                stat.execute();
                
                stat.setString(1, vriend);
                stat.setString(2, account);
                
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in verwijderenVriendschap - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in verwijderenVriendschap - connection - " + ex);
        }
    }

    @Override
    public Vriendschap zoekVriendschap(String account, String vriend) throws DBException
    {
        Vriendschap returnVriendschap = null;

        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement(
                    "SELECT accountlogin, accountvriendlogin FROM vriendschap WHERE accountlogin = ? AND accountvriendlogin = ?");)
            {
                stat.setString(1, account);
                stat.setString(2, vriend);

                stat.execute();
                
                try (ResultSet r = stat.getResultSet())
                {
                    Vriendschap vriendschap = new Vriendschap();

                    if (r.next())
                    {
                        vriendschap.setAccountlogin(r.getString("accountlogin"));
                        vriendschap.setAccountvriendlogin(r.getString("accountvriendlogin"));

                        returnVriendschap = vriendschap;
                    }

                    return returnVriendschap;
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekVriendschap - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekVriendschap - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekVriendschap - connection - " + ex);
        }
    }

    public ArrayList<Account> zoekVrienden(String login) throws DBException
    {
        ArrayList<Account> vrienden = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM account WHERE login IN (SELECT accountvriendlogin FROM vriendschap WHERE accountlogin = ?)");)
            {
                stmt.setString(1, login);
                stmt.execute();

                try (ResultSet r = stmt.getResultSet())
                {
                    while (r.next())
                    {
                        Account vriend = new Account();
                        vriend.setNaam(r.getString("naam"));
                        vriend.setVoornaam(r.getString("voornaam"));
                        vriend.setLogin(r.getString("login"));
                        vriend.setPaswoord(r.getString("paswoord"));
                        vriend.setEmailadres(r.getString("emailadres"));
                        vriend.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        vriend.setLastLogin(r.getTimestamp("lastLogin").toLocalDateTime());
                        vriend.setLastLogout(r.getTimestamp("lastLogout").toLocalDateTime());
                        vrienden.add(vriend);
                    }
                }
                catch (SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekVrienden - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekVrienden - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekVrienden - connection - " + ex);
        }
        return vrienden;
    }
}