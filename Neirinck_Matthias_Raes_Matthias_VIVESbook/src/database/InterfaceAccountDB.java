/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Account;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceAccountDB {

    public Account zoekAccountOpLogin(String login) throws DBException;

    public Account zoekAccountOpEmail(String email) throws DBException;

    public void toevoegenAccount(Account account) throws DBException;

    public void wijzigenAccount(Account teWijzigenAccount) throws DBException;
}
