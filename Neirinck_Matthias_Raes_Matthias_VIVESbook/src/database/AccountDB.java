package database;

import bags.Account;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccountDB implements InterfaceAccountDB
{

    @Override
    public Account zoekAccountOpLogin(String login) throws DBException
    {
        Account returnAcc = null;

        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("SELECT * FROM Account WHERE login = ?");)
            {
                stat.setString(1, login);
                stat.execute();
                
                try (ResultSet r = stat.getResultSet())
                {
                    Account acc = new Account();

                    if (r.next())
                    {
                        acc.setNaam(r.getString("naam"));
                        acc.setVoornaam(r.getString("voornaam"));
                        acc.setLogin(r.getString("login"));
                        acc.setPaswoord(r.getString("paswoord"));
                        acc.setEmailadres(r.getString("emailadres"));
                        acc.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        acc.setLastLogin(r.getTimestamp("lastLogin").toLocalDateTime());
                        acc.setLastLogout(r.getTimestamp("lastLogout").toLocalDateTime());
                        returnAcc = acc;
                    }
                    
                    return returnAcc;
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                    throw new DBException("SQL-exception in ZoekAccountOpLogin - resultset - " + ex);

                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAccountOpLogin - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccountOpLogin - connection - " + ex);

        }
    }

    @Override
    public Account zoekAccountOpEmail(String email) throws DBException
    {
        Account returnAcc = null;
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stat = conn.prepareStatement("SELECT * FROM account WHERE emailadres=?");
                
                stat.setString(1, email);
                stat.execute();
                
                try(ResultSet r = stat.getResultSet()){
                    Account a = new Account();
                    
                    if(r.next()){
                        a.setNaam(r.getString("naam"));
                        a.setVoornaam(r.getString("voornaam"));
                        a.setLogin(r.getString("login"));
                        a.setPaswoord(r.getString("paswoord"));
                        a.setEmailadres(r.getString("emailadres"));
                        a.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        a.setLastLogin(r.getTimestamp("lastLogin").toLocalDateTime());
                        a.setLastLogout(r.getTimestamp("lastLogout").toLocalDateTime());
                        returnAcc = a;
                    }
                    
                    return returnAcc;
                }
                catch(SQLException ex)
                {
                    throw new DBException("SQL-exception in zoekAccountOpEmail - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in zoekAccountOpEmail - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in zoekAccountOpEmail - connection - " + ex);
        }
    }

    @Override
    public void toevoegenAccount(Account account) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("INSERT INTO account(naam, voornaam, login, paswoord, emailadres, geslacht) values(?,?,?,?,?,?)");)
            {
                stat.setString(1, account.getNaam());
                stat.setString(2, account.getVoornaam());
                stat.setString(3, account.getLogin());
                stat.setString(4, account.getPaswoord());
                stat.setString(5, account.getEmailadres());
                
                if(account.getGeslacht() == null){
                    stat.setNull(6, java.sql.Types.NULL);
                }else{
                    stat.setString(6, account.getGeslacht().name()); // Geen toString() want deze kan aangepast worden, bv "Man" en "Vrouw" ipv "M" en "V"
                }
                
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in toevoegenAccount - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in toevoegenAccount - connection - " + ex);
        }
    }

    @Override
    public void wijzigenAccount(Account teWijzigenAccount) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try (PreparedStatement stat = conn.prepareStatement("UPDATE account SET naam = ?, "
                    + "voornaam = ?, "
                    + "paswoord = ?, "
                    + "emailadres = ?, "
                    + "geslacht = ? WHERE login = ?");)
            {
                stat.setString(1, teWijzigenAccount.getNaam());
                stat.setString(2, teWijzigenAccount.getVoornaam());
                stat.setString(3, teWijzigenAccount.getPaswoord());
                stat.setString(4, teWijzigenAccount.getEmailadres());
                
                if(teWijzigenAccount.getGeslacht() == null){
                    stat.setNull(5, java.sql.Types.NULL);
                }else{
                    stat.setString(5, teWijzigenAccount.getGeslacht().name());
                }
                
                stat.setString(6, teWijzigenAccount.getLogin());
                
                stat.execute();
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in wijzigenAccount - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in wijzigenAccount - connection - " + ex);

        }
    }
    
    public void verwijderenAccount(Account teVerwijderenAccount) throws DBException
    {
        try(Connection conn = ConnectionManager.getConnection();)
        {
            try(PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM account WHERE login = ?");)
            {  
                stmt.setString(1, teVerwijderenAccount.getLogin());
                stmt.execute();
                
            }
            catch(SQLException ex)
            {
                throw new DBException("SQL-Exception in verwijderenAccount - statement - " + ex);
            }
        }
        catch(SQLException ex)
        {
            throw new DBException("SQL-exception in verwijderenAccount - connection - " + ex);
        }
    }
    
    // past de lastLogin van het account aan en returnt dit
    public LocalDateTime inloggenAccount(String login, String paswoord) throws DBException{
        LocalDateTime lastLogin = null;
        
        try(Connection conn = ConnectionManager.getConnection();){
            try(PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Account SET lastLogin = NOW() WHERE login = ? AND paswoord = ?");){
                
                stmt.setString(1, login);
                stmt.setString(2, paswoord);
                stmt.execute();
            }catch(SQLException sqlEx){
                throw new DBException("SQL-Exception in inloggenAccount - statement" + sqlEx);
            }
            
            try (PreparedStatement stmt = conn.prepareStatement("SELECT lastLogin FROM Account WHERE login = ? AND paswoord = ?");)
            {
                stmt.setString(1, login);
                stmt.setString(2, paswoord);
                stmt.execute();
                
                try (ResultSet r = stmt.getResultSet())
                {
                    if (r.next())
                    {
                        lastLogin = r.getTimestamp("lastLogin").toLocalDateTime();
                    }
                    
                    return lastLogin;
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                    throw new DBException("SQL-exception in inloggenAccount - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in inloggenAccount - statement - " + ex);
            }
        }catch(SQLException sqlEx){
            throw new DBException("SQL-exception in inloggenAccount - connection" + sqlEx);
        }   
    }
    
    // past de lastLogout van het account aan en returnt dit
    public LocalDateTime uitloggenAccount(String login, String paswoord) throws DBException
    {
        LocalDateTime lastLogout = null;
        
        try(Connection conn = ConnectionManager.getConnection();){
            try(PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Account SET lastLogout = NOW() WHERE login = ? AND paswoord = ?");){
                        
                stmt.setString(1, login);
                stmt.setString(2, paswoord);
                stmt.execute();
                
            }catch(SQLException sqlEx){
                throw new DBException("SQL-Exception in uitloggenAccount - statement" + sqlEx);
            }
            
            try (PreparedStatement stmt = conn.prepareStatement("SELECT lastLogout FROM Account WHERE login = ? AND paswoord = ?");)
            {
                stmt.setString(1, login);
                stmt.setString(2, paswoord);
                stmt.execute();
                
                try (ResultSet r = stmt.getResultSet())
                {
                    if (r.next())
                    {
                        lastLogout = r.getTimestamp("lastLogout").toLocalDateTime();
                    }
                    
                    return lastLogout;
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                    throw new DBException("SQL-exception in uitloggenAccount - resultset - " + ex);

                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in uitloggenAccount - statement - " + ex);
            }
        }catch(SQLException sqlEx){
            throw new DBException("SQL-exception in uitloggenAccount - connection" + sqlEx);
        }    
    }
    
    // returnt 1 als het paswoord overeenkomt met de login
    // returnt 0 als dit niet zo is
    public boolean passwordMatchesLogin(String login, String paswoord) throws DBException
    {
        try (Connection conn = ConnectionManager.getConnection();)
        {
            try
            {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE login=? AND paswoord = ?");
                
                stmt.setString(1, login);
                stmt.setString(2, paswoord);
                stmt.execute();
                
                try(ResultSet r = stmt.getResultSet()){
                    return r.next();
                }
                catch(SQLException ex)
                {
                    throw new DBException("SQL-exception in passwordMatchesLogin - resultset - " + ex);
                }
            }
            catch (SQLException ex)
            {
                throw new DBException("SQL-exception in passwordMatchesLogin - statement - " + ex);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException("SQL-exception in passwordMatchesLogin - connection - " + ex);
        }
    }
}