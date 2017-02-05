package bags;


import datatype.Geslacht;
import java.time.LocalDateTime;

/**
 *
 * @author Katrien.Deleu
 */
public class Account implements Comparable<Account>{
    private String voornaam;
    private String naam;
    private String emailadres;
    private Geslacht geslacht;
    private String login;
    private String paswoord;
    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;

    public Account() {
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public void setGeslacht(Geslacht geslacht) {
        this.geslacht = geslacht;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPaswoord() {
        return paswoord;
    }

    public void setPaswoord(String paswoord) {
        this.paswoord = paswoord;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public LocalDateTime getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(LocalDateTime lastLogout) {
        this.lastLogout = lastLogout;
    }
    
    /*@Override
    public String toString() {
        return "Account{" + "voornaam=" + voornaam + ", naam=" + naam + ", emailadres=" + emailadres + ", geslacht=" + geslacht + ", login=" + login + ", paswoord=" + paswoord + '}';
    }*/
    
    public boolean isLoggedIn()
    {
        return lastLogin.isAfter(lastLogout);
    }
    
    @Override
    public String toString() {
        return voornaam + " " + naam + " | " + login;
    }

    // Eerst sorteren op voornaam, dan op naam
    @Override
    public int compareTo(Account a) {
        if(voornaam.compareTo(a.getVoornaam()) == 0)
        {
            return naam.compareTo(a.getNaam());
        }
        else
        {
            return voornaam.compareTo(a.getVoornaam());
        }
    }
}
