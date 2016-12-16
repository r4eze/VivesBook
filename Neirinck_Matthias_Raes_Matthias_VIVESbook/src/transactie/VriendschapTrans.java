/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactie;

import database.VriendschapDB;
import exception.ApplicationException;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public class VriendschapTrans implements InterfaceVriendschapTrans
{

    @Override
    public void VriendschapToevoegen(String account, String vriend) throws ApplicationException
    {
        VriendschapDB vrdb = new VriendschapDB();
        try
        {
            vrdb.toevoegenVriendschap(account, vriend);
        }
        catch (DBException e)
        {
            throw new ApplicationException("Database probleem:" + e.getMessage());
        }
    }

    @Override
    public void vriendschapVerwijderen(String account, String vriend) throws ApplicationException
    {
        VriendschapDB vrdb = new VriendschapDB();
        try
        {
            vrdb.verwijderenVriendschap(account, vriend);
        }
        catch (DBException e)
        {
            throw new ApplicationException("Database probleem: " + e.getMessage());
        }
    }

}
