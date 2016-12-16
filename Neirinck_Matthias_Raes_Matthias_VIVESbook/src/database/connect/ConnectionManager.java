package database.connect;

/**
 * Write a description of class Connectionmanager here.
 *
 * @author Johan
 * @version 0.9
 */

import exception.DBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    public static Connection getConnection() throws DBException {

        try {
            //driver laden
            Class.forName(DBProp.getDriver()).newInstance();
            Connection conn = DriverManager.getConnection(DBProp.getDbUrl(), DBProp.getLogin(), DBProp.getPaswoord());
            return conn;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            throw new DBException("Connectie met de database mislukt: " + ex);
        }
    }
}
