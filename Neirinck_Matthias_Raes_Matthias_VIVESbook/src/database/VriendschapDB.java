/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Katrien.Deleu
 */
public class VriendschapDB
{

    public void toevoegenVriendschap(String account, String vriend) throws DBException
    {
        if (account.equals(vriend))
        {
            throw new DBException("Je kan jezelf niet toevoegen als vriend!");
        }
        if (zoekVriendschap(account, vriend) == null)
        {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();)
            {
                // preparedStatement opstellen (en automtisch sluiten)
                try (PreparedStatement stmt = conn.
                        prepareStatement(
                                "INSERT INTO vriendschap(accountlogin, accountvriendlogin) VALUES(?,?)",
                                Statement.RETURN_GENERATED_KEYS);)
                {

                    stmt.setString(1, account);
                    stmt.setString(2, vriend);

                    // execute voert elke sql-statement uit, executeQuery enkel de select
                    stmt.execute();
                    PreparedStatement stmet = conn.prepareStatement("insert into vriendschap(accountlogin, accountvriendlogin) values(?,?)");
                    stmet.setString(1, vriend);
                    stmet.setString(2, account);
                    stmet.execute();

                }
                catch (SQLException sqlEx)
                {
                    //System.out.println(sqlEx.getMessage()); => debugging purpose
                    throw new DBException("Vriend bestaat niet");

                }
            }
            catch (SQLException sqlEx)
            {
                throw new DBException(
                        "SQL-exception in toevoegenFriend - connection" + sqlEx);
            }
        }
        else
        {
            throw new DBException("Deze persoon staat al tussen jouw vrienden");
        }
    }

    public void verwijderenVriendschap(String account, String vriend) throws DBException
    {
        if (zoekVriendschap(account, vriend) == null)
        {
            throw new DBException("Vriendschap bestaat niet");
        }
        else
        {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();)
            {
                // preparedStatement opstellen (en automtisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "delete from vriendschap where accountlogin = ? and accountvriendlogin = ?");)
                {

                    stmt.setString(1, account);
                    stmt.setString(2, vriend);
                    // execute voert elke sql-statement uit, executeQuery enkel de select
                    stmt.execute();

                    PreparedStatement stmet = conn.prepareStatement("delete from vriendschap where accountlogin = ? and accountvriendlogin = ?");
                    stmet.setString(1, vriend);
                    stmet.setString(2, account);
                    stmet.execute();
                }
                catch (SQLException sqlEx)
                {
                    throw new DBException("SQL-exception in verwijderenFriend - statement" + sqlEx);
                }
            }
            catch (SQLException sqlEx)
            {
                throw new DBException(
                        "SQL-exception in verwijderenFriend - connection" + sqlEx);
            }
        }
    }

    public Vriendschap zoekVriendschap(String account, String vriend) throws DBException
    {
        Vriendschap returnVriendschap = null;
        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();)
        {
            // preparedStatement opstellen (en automtisch sluiten)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "select accountlogin, accountvriendlogin from vriendschap where accountlogin = ? and accountvriendlogin = ?");)
            {
                stmt.setString(1, account);
                stmt.setString(2, vriend);
                // execute voert het SQL-statement uit
                stmt.execute();
                // result opvragen (en automatisch sluiten)
                try (ResultSet r = stmt.getResultSet())
                {
                    // van de post uit de database een Post-object maken
                    Vriendschap vriendschap = new Vriendschap();

                    // er werd een vriendschap gevonden
                    if (r.next())
                    {
                        vriendschap.setAccountlogin(r.getString("accountlogin"));
                        vriendschap.setAccountvriendlogin(r.getString("accountvriendlogin"));

                        returnVriendschap = vriendschap;
                    }

                    return returnVriendschap;
                }
                catch (SQLException sqlEx)
                {
                    throw new DBException("SQL-exception in zoekFriend - resultset" + sqlEx);
                }
            }
            catch (SQLException sqlEx)
            {
                throw new DBException("SQL-exception in zoekFriend - statement" + sqlEx);
            }
        }
        catch (SQLException sqlEx)
        {
            throw new DBException(
                    "SQL-exception in zoekFriend - connection");
        }
    }

    public ArrayList<Account> zoekVrienden(String login) throws DBException
    {
        ArrayList<Account> vrienden = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();)
        {
            // preparedStatement opstellen (en automtisch sluiten)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "select * from account where login in (select accountvriendlogin from vriendschap where accountlogin = ?) order by voornaam, naam");)
            {
                stmt.setString(1, login);
                // execute voert het SQL-statement uit
                stmt.execute();
                // result opvragen (en automatisch sluiten)
                try (ResultSet r = stmt.getResultSet())
                {
                    // van de post uit de database een Post-object maken
                    while (r.next())
                    {
                        Account vriend = new Account();
                        vriend.setLogin(r.getString("login"));
                        vriend.setEmailadres(r.getString("emailadres"));
                        vriend.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        vriend.setNaam(r.getString("naam"));
                        vriend.setVoornaam(r.getString("voornaam"));
                        vrienden.add(vriend);
                    }

                    // er werd een vriendschap gevonden
                }
                catch (SQLException sqlEx)
                {
                    throw new DBException("SQL-exception in zoekFriend - resultset" + sqlEx);
                }
            }
            catch (SQLException sqlEx)
            {
                throw new DBException("SQL-exception in zoekFriend - statement" + sqlEx);
            }
        }
        catch (SQLException sqlEx)
        {
            throw new DBException(
                    "SQL-exception in zoekFriend - connection");
        }
        return vrienden;
    }

    public ArrayList<Account> zoekVrienden(String login, String zoek) throws DBException
    {
        ArrayList<Account> vrienden = new ArrayList<>();
        if (zoek == null || zoek.equals(""))
        {
            vrienden = zoekVrienden(login);
        }
        else
        {
            try (Connection conn = ConnectionManager.getConnection();)
            {
                // preparedStatement opstellen (en automtisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "select * from account where login in (select accountvriendlogin from vriendschap where accountlogin = ? and accountvriendlogin in (select login from account where voornaam like ? or naam like ?)) order by voornaam, naam");)
                {
                    stmt.setString(1, login);
                    stmt.setString(2, "%" + zoek + "%");
                    stmt.setString(3, "%" + zoek + "%");
                    //System.out.println(stmt.toString()); => debugging purpose
                    // execute voert het SQL-statement uit
                    stmt.execute();
                    // result opvragen (en automatisch sluiten)
                    try (ResultSet r = stmt.getResultSet())
                    {
                        // van de post uit de database een Post-object maken
                        while (r.next())
                        {
                            Account vriend = new Account();
                            vriend.setLogin(r.getString("login"));
                            vriend.setEmailadres(r.getString("emailadres"));
                            vriend.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                            vriend.setNaam(r.getString("naam"));
                            vriend.setVoornaam(r.getString("voornaam"));
                            vrienden.add(vriend);
                        }

                        // er werd een vriendschap gevonden
                    }
                    catch (SQLException sqlEx)
                    {
                        throw new DBException("SQL-exception in zoekFriend - resultset" + sqlEx);
                    }
                }
                catch (SQLException sqlEx)
                {
                    throw new DBException("SQL-exception in zoekFriend - statement" + sqlEx);
                }
            }
            catch (SQLException sqlEx)
            {
                throw new DBException(
                        "SQL-exception in zoekFriend - connection");
            }
        }
        return vrienden;
    }
}
