/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import bags.Vriendschap;
import exception.DBException;

/**
 *
 * @author Katrien.Deleu
 */
public interface InterfaceVriendschapDB {

    public void toevoegenVriendschap(String account, String vriend) throws DBException;

    public void verwijderenVriendschap(String account, String vriend) throws DBException;

}
