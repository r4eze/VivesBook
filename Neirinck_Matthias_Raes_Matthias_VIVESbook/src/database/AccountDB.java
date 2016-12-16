package database;

import bags.Account;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import exception.DBException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDB implements InterfaceAccountDB
{

    @Override
    public Account zoekAccountOpLogin(String login) throws DBException
    {
        Account returnacc = null;

        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("select * from Account where login = ?");)
            {
                stat.setString(1, login);
                stat.execute();
                try (ResultSet r = stat.getResultSet())
                {
                    Account acc = new Account();
                    acc.setLogin(login);
                    if (r.next())
                    {
                        acc.setNaam(r.getString("naam"));
                        acc.setVoornaam(r.getString("voornaam"));
                        acc.setPaswoord(r.getString("paswoord"));
                        acc.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        acc.setEmailadres(r.getString("emailadres"));
                        returnacc = acc;
                    }

                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                    throw new DBException("SQLException in ZoekAccount - bij resultset");

                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAccount - bij prepare statement \n" + ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccount - bij connectie");

        }
        return returnacc;
    }

    @Override
    public Account zoekAccountOpEmail(String email) throws DBException
    {
        Account ac = null;
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("select * from account where emailadres=?");
                stat.setString(1, email);
                stat.execute();
                ResultSet r = stat.getResultSet();
                if (r.next())
                {
                    ac.setEmailadres(r.getString("emailadres"));
                    ac.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                    ac.setLogin(r.getString("login"));
                    ac.setPaswoord(r.getString("paswoord"));
                    ac.setNaam(r.getString("naam"));
                    ac.setVoornaam(r.getString("voornaam"));
                }
            }
            catch (SQLException e)
            {
                throw new DBException("SQLException in zoekaccount bij prepare");
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekaccount - bij connectie");
        }
        return ac;
    }

    @Override
    public void toevoegenAccount(Account account) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("insert into account values(?,?,?,?,?,?)");)
            {
                stat.setString(1, account.getNaam());
                stat.setString(2, account.getVoornaam());
                stat.setString(3, account.getLogin());
                stat.setString(4, account.getPaswoord());
                stat.setString(5, account.getEmailadres());
                stat.setString(6, account.getGeslacht().toString());
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in AccountToevoegen - at prepare");
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in Toevoegen Account  - at connectie");
        }
    }

    @Override
    public void wijzigenAccount(Account acc) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("Select * from account where emailadres = ?");
                stat.setString(1, acc.getEmailadres());
                stat.execute();
                ResultSet r = stat.getResultSet();
                System.out.println(checkIfEmailValid(acc, r));
                r.beforeFirst();
                if (this.checkIfEmailValid(acc, r))
                {
                    stat = conn.prepareStatement("UPDATE account SET naam=?,voornaam=?,emailadres=?,geslacht=?,paswoord=? where login=?");
                    stat.setString(1, acc.getNaam());
                    stat.setString(2, acc.getVoornaam());
                    stat.setString(3, acc.getEmailadres());
                    stat.setString(4, acc.getGeslacht().toString());
                    stat.setString(5, acc.getPaswoord());
                    stat.setString(6, acc.getLogin());
                    stat.execute();
                }
                else
                {
                    throw new DBException("Emailadres bestaat al");
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in Toevoegen Account - at prepare statement");
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in Toevoegen Account  - at connectie");

        }
    }

    private boolean checkIfEmailValid(Account account, ResultSet r) throws DBException
    {
        ArrayList<String> emails = new ArrayList<>();
        try
        {
            while (r.next())
            {
                if (r.getString("login").equals(account.getLogin()))
                {

                }
                else
                {
                    emails.add(r.getString("emailadres"));
                    //System.out.println(r.getString("emailadres")); debugging purpose
                }
            }
        }
        catch (SQLException e)
        {
            throw new DBException("Problem with resultset");
        }
        return emails.isEmpty();
    }

    private boolean checkIfLoginValid(String login, ResultSet r) throws DBException
    {
        ArrayList<String> logins = new ArrayList<>();

        try
        {
            r.beforeFirst();
            while (r.next())
            {
                logins.add(r.getString("login"));
                System.out.println(r.getString("login"));
            }
        }
        catch (SQLException e)
        {
            throw new DBException("Problem with resultset");
        }
        System.out.println(logins);
        return !logins.contains(login);
    }

}
