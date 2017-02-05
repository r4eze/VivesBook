package transactie;

import bags.Account;
import database.AccountDB;
import exception.ApplicationException;
import exception.DBException;

public class AccountTrans implements InterfaceAccountTrans
{

    @Override
    public void accountToevoegen(Account acc) throws ApplicationException, DBException
    {
        AccountDB accountDB = new AccountDB();
        
        if(acc == null)
        {
            throw new ApplicationException("Er werd geen account ingevuld");
        }
        
        checkAlleVeldenIngevuld(acc);
        
        if (accountDB.zoekAccountOpLogin(acc.getLogin()) != null)
        {       
            throw new ApplicationException("Er bestaat al een account met dezelfde login");
        }  
        
        if (accountDB.zoekAccountOpEmail(acc.getEmailadres()) != null)
        {
            throw new ApplicationException("Er bestaat al een account met hetzelfde emailadres");
        }
        
        accountDB.toevoegenAccount(acc);
    }
    
    public Account zoekAccountOpLogin(String login) throws DBException, ApplicationException {
        if(login == null || login.trim().equals(""))
        {
            throw new ApplicationException("Login niet ingevuld");
        }

        AccountDB accDB = new AccountDB();
        return accDB.zoekAccountOpLogin(login);
    }

    public Account zoekAccountOpEmail(String email) throws DBException, ApplicationException {
        if(email == null || email.trim().equals(""))
        {
            throw new ApplicationException("Email niet ingevuld");
        }

        AccountDB accDB = new AccountDB();
        return accDB.zoekAccountOpEmail(email);
    }

    @Override
    public void accountWijzigen(Account teWijzigenAccount) throws DBException, ApplicationException
    {
        if(teWijzigenAccount == null)
        {
            throw new ApplicationException("Er werd geen account ingevuld");
        }
        
        AccountDB accountDB = new AccountDB();
        
        checkAlleVeldenIngevuld(teWijzigenAccount);
        
        Account accViaEmail = accountDB.zoekAccountOpEmail(teWijzigenAccount.getEmailadres());
        
        // Exception als het account met hetzelfde emailadres verschillend is van het account dat moet gewijzigd worden
        if(accViaEmail != null && !accViaEmail.getLogin().equals(teWijzigenAccount.getLogin()))
        {
            throw new ApplicationException("Er bestaat al een account met hetzelfde emailadres");
        }
        
        accountDB.wijzigenAccount(teWijzigenAccount);
    }
    
    public Account inloggenAccount(String login, String paswoord) throws DBException, ApplicationException{
        if(login == null || login.trim().equals(""))
        {
            throw new ApplicationException("Login niet ingevuld");
        }
        else if(paswoord == null || paswoord.trim().equals(""))
        {
            throw new ApplicationException("Paswoord niet ingevuld");
        }
        
        AccountDB accDB = new AccountDB();
        return accDB.inloggenAccount(login, paswoord);
    }
    
    public void uitloggenAccount(String login) throws DBException, ApplicationException{
        AccountDB accDB = new AccountDB();
        
        if(login == null || login.trim().equals(""))
        {
            throw new ApplicationException("Login niet ingevuld");
        }
        if(accDB.zoekAccountOpLogin(login) == null)
        {
            throw new ApplicationException("Het account bestaat niet");
        }  
        
        accDB.uitloggenAccount(login);
    }

    private void checkAlleVeldenIngevuld(Account acc) throws ApplicationException{
        if(acc.getNaam() == null || acc.getNaam().trim().equals(""))
        {
            throw new ApplicationException("Naam niet ingevuld");
        }
        if(acc.getVoornaam() == null || acc.getVoornaam().trim().equals(""))
        {
            throw new ApplicationException("Voornaam niet ingevuld");
        }
        if(acc.getLogin() == null || acc.getLogin().trim().equals(""))
        {
            throw new ApplicationException("Login niet ingevuld");
        }
        if(acc.getPaswoord() == null || acc.getPaswoord().trim().equals(""))
        {
            throw new ApplicationException("Paswoord niet ingevuld");
        }
        if(acc.getEmailadres() == null || acc.getEmailadres().trim().equals(""))
        {
            throw new ApplicationException("Emailadres niet ingevuld");
        }
        if(acc.getGeslacht() == null){
            throw new ApplicationException("Geslacht niet ingevuld");
        }
    }
}
