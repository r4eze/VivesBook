package bags;


import datatype.Geslacht;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katrien.Deleu
 */
public class Account {
    private String voornaam;
    private String naam;
    private String emailadres;
    private Geslacht geslacht;
    private String login;
    private String paswoord;

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
    
    @Override
    public String toString() {
        return "Account{" + "voornaam=" + voornaam + ", naam=" + naam + ", emailadres=" + emailadres + ", geslacht=" + geslacht + ", login=" + login + ", paswoord=" + paswoord + '}';
    }
}
