package bags;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katrien.Deleu
 */
public class Vriendschap {
    private String accountlogin;
    private String accountvriendlogin;

    public Vriendschap() {
    }

    public String getAccountlogin() {
        return accountlogin;
    }

    public void setAccountlogin(String accountlogin) {
        this.accountlogin = accountlogin;
    }

    public String getAccountvriendlogin() {
        return accountvriendlogin;
    }

    public void setAccountvriendlogin(String accountvriendlogin) {
        this.accountvriendlogin = accountvriendlogin;
    }

    @Override
    public String toString() {
        return "Friend{" + "accountlogin=" + accountlogin + ", accountvriendlogin=" + accountvriendlogin + '}';
    }

    
    
    
}
