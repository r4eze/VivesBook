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

    @Override
    public void accountWijzigen(Account teWijzigenAccount) throws ApplicationException
    {
        AccountDB acct = new AccountDB();
        try
        {
            acct.wijzigenAccount(teWijzigenAccount);
        }
        catch (DBException e)
        {
            throw new ApplicationException("Database problem " + e.getMessage());
        }
    }

    private boolean checkIfIngevuld(Account account)
    {
        return account.getEmailadres() != null && account.getGeslacht() != null && account.getLogin() != null && account.getNaam() != null
                && account.getVoornaam() != null;
    }

}
