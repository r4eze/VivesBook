/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import bags.Account;
import database.AccountDB;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public class AccountTrans implements InterfaceAccountTrans
{

    @Override
    public void accountToevoegen(Account acc) throws ApplicationException, DBException
    {
        AccountDB acct = new AccountDB();
        Account ac = null;
        if (checkIfIngevuld(acc))
        {
            ac = acct.zoekAccountOpLogin(acc.getLogin());
            if (ac == null)
            {
                ac = acct.zoekAccountOpEmail(acc.getEmailadres());
                if (ac == null)
                {

                    acct.toevoegenAccount(acc);
                }
                else
                {
                    throw new ApplicationException("email bestaat al");
                }
            }
            else
            {
                throw new ApplicationException("Login bestaat al");
            }

        }
        else
        {
            throw new ApplicationException("Gelieve alle velden in te vullen");
        }

    }
    
    public Account zoekAccountOpLogin(String login) throws DBException, ApplicationException {
        if (login == null || login.trim().equals("")) {
            throw new ApplicationException("Login niet ingevuld");
        }

        AccountDB accDB = new AccountDB();
        return accDB.zoekAccountOpLogin(login);
    }

    public Account zoekAccountOpEmail(String email) throws DBException, ApplicationException {
        if (email == null || email.trim().equals("")) {
            throw new ApplicationException("Email niet ingevuld");
        }

        AccountDB accDB = new AccountDB();
        return accDB.zoekAccountOpEmail(email);
    }

    @Override
    public void accountWijzigen(Account teWijzigenAccount) throws DBException, ApplicationException
    {
        AccountDB acct = new AccountDB();
        acct.wijzigenAccount(teWijzigenAccount);
        
        /*
        Dit is niet goed want de gebruiker mag geen DBExceptions zien
        try
        {
            acct.wijzigenAccount(teWijzigenAccount);
        }
        catch (DBException e)
        {
            throw new ApplicationException("Database problem " + e.getMessage());
        }*/
    }

    private boolean checkIfIngevuld(Account account)
    {
        return account.getEmailadres() != null && account.getGeslacht() != null && account.getLogin() != null && account.getNaam() != null
                && account.getVoornaam() != null;
    }

    private void checkAlleVeldenIngevuld(Account a) throws ApplicationException{
        if(a.getNaam() == null || a.getNaam().trim().equals("")){
            throw new ApplicationException("Naam niet ingevuld");
        }
        if(a.getVoornaam() == null || a.getVoornaam().trim().equals("")){
            throw new ApplicationException("Voornaam niet ingevuld");
        }
        if(a.getLogin() == null || a.getLogin().trim().equals("")){
            throw new ApplicationException("Login niet ingevuld");
        }
        if(a.getPaswoord() == null || a.getPaswoord().trim().equals("")){
            throw new ApplicationException("Paswoord niet ingevuld");
        }
        if(a.getEmailadres() == null || a.getEmailadres().trim().equals("")){
            throw new ApplicationException("Emailadres niet ingevuld");
        }
        if(a.getGeslacht() == null){
            throw new ApplicationException("Geslacht niet ingevuld");
        }
    }
}
