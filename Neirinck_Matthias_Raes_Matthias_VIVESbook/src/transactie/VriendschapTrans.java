package transactie;

import bags.Account;
import database.VriendschapDB;
import exception.ApplicationException;
import exception.DBException;
import java.util.ArrayList;

public class VriendschapTrans implements InterfaceVriendschapTrans
{

    @Override
    public void VriendschapToevoegen(String account, String vriend) throws DBException, ApplicationException
    {
        VriendschapDB vriendDB = new VriendschapDB();
        AccountTrans accTrans = new AccountTrans();
        
        if(account == null)
        {
            throw new ApplicationException("Er werd geen account meegegeven");
        }
        if(vriend == null)
        {
            throw new ApplicationException("Er werd geen vriend meegegven");
        }
        if(account.equals(vriend))
        {
            throw new ApplicationException("Je kan niet bevriend zijn met jezelf");
        }
        if(vriendDB.zoekVriendschap(account, vriend) != null && vriendDB.zoekVriendschap(vriend, account) != null)
        {
            throw new ApplicationException("Reeds bevriend");
        }
        if(accTrans.zoekAccountOpLogin(vriend) == null)
        {
            throw new ApplicationException("Vriend bestaat niet");
        }
        if(accTrans.zoekAccountOpLogin(account) == null)
        {
            throw new ApplicationException("Account bestaat niet");
        }
        
        vriendDB.toevoegenVriendschap(account, vriend);
    }

    @Override
    public void vriendschapVerwijderen(String account, String vriend) throws DBException, ApplicationException
    {
        VriendschapDB vriendDB = new VriendschapDB();
        AccountTrans accTrans = new AccountTrans();
        
        if(account == null)
        {
            throw new ApplicationException("Er werd geen account meegegeven");
        }
        if(vriend == null){
            throw new ApplicationException("Er werd geen vriend meegegven");
        }
        if(accTrans.zoekAccountOpLogin(vriend) == null)
        {
            throw new ApplicationException("Vriend bestaat niet");
        }
        if(accTrans.zoekAccountOpLogin(account) == null)
        {
            throw new ApplicationException("Account bestaat niet");
        }
        if(vriendDB.zoekVriendschap(account, vriend) == null && vriendDB.zoekVriendschap(vriend, account) == null)
        {
            throw new ApplicationException("De vriendschap bestaat niet");
        }
        
        vriendDB.verwijderenVriendschap(account, vriend);
    }

    public ArrayList<Account> zoekVrienden(String login) throws DBException, ApplicationException
    {
        VriendschapDB vriendDB = new VriendschapDB();
        return vriendDB.zoekVrienden(login);
    }
    
    /*public ArrayList<Account> zoekVrienden(String login, String zoek) throws DBException, ApplicationException
    {
        VriendschapDB vriendDB = new VriendschapDB();
        return vriendDB.zoekVrienden(login, zoek);
    }*/
}
