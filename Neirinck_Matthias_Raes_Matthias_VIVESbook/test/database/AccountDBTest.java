package database;

import bags.Account;
import datatype.Geslacht;
import exception.DBException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class AccountDBTest {
    
    private Account account;
    private AccountDB accountDB;
    
    public AccountDBTest() {
        accountDB = new AccountDB();
    }
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() {
            account = new Account();
            account.setNaam("Defoort");
            account.setVoornaam("Mieke");
            account.setLogin("miekedefoort");
            account.setPaswoord("wachtwoord123");
            account.setEmailadres("miekedefoort@hotmail.com");
            account.setGeslacht(Geslacht.V);
    }
    
    @After
    public void tearDown() {
        try{
            accountDB.verwijderenAccount(account);
        }catch(DBException ex){
            System.out.println("-tearDown- " + ex);
        }
    }

    // Positieve test: account toe te voegen
    // Positieve test: account zoeken op login
    @Test
    public void testToevoegenAccount(){
        try{
            accountDB.toevoegenAccount(account);
            Account ophaalAcc = accountDB.zoekAccountOpLogin(account.getLogin());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException ex){
            System.out.println("-testToevoegenAccount- " + ex);
        }
    }
    
    
    
    // Negatieve test: 2 accounts toevoegen met dezelfde login
    @Test
    public void testToevoegenAccountZelfdeLogin() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        
        Account account2 = new Account();
        account2.setNaam("Petersen");
        account2.setVoornaam("Peter");
        account2.setLogin("miekedefoort");
        account2.setPaswoord("wachtwoord12345");
        account2.setEmailadres("peter@hotmail.com");
        account2.setGeslacht(Geslacht.M);
        
        accountDB.toevoegenAccount(account2);
    }
    
    // Negatieve test: 2 accounts toevoegen met dezelfde email
    @Test
    public void testToevoegenAccountZelfdeEmail() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        
        Account account2 = new Account();
        account2.setNaam("Petersen");
        account2.setVoornaam("Peter");
        account2.setLogin("peterpetersen");
        account2.setPaswoord("wachtwoord12345");
        account2.setEmailadres("miekedefoort@hotmail.com");
        account2.setGeslacht(Geslacht.M);
        
        accountDB.toevoegenAccount(account2);
    }
    
    // Negatieve test: account toevoegen zonder naam
    @Test
    public void testToevoegenAccountNaamNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setNaam(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Negatieve test: account toevoegen zonder voornaam
    
    @Test
    public void testToevoegenAccountVoornaamNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setVoornaam(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Negatieve test: account toevoegen zonder login
    @Test
    public void testToevoegenAccountLoginNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setLogin(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Negatieve test: account toevoegen zonder passwoord
    @Test
    public void testToevoegenAccountPaswoordNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setPaswoord(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Negatieve test: account toevoegen zonder emailadres
    @Test
    public void testToevoegenAccountEmailadresNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setEmailadres(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Negatieve test: account toevoegen zonder geslacht
    @Test
    public void testToevoegenAccountGeslachtNull() throws DBException{
        thrown.expect(DBException.class);
        
        account.setGeslacht(null);
        
        accountDB.toevoegenAccount(account);
    }
    
    // Positiveve test: account zoeken op email
    @Test
    public void testZoekAccountOpEmail(){
        try{
            accountDB.toevoegenAccount(account);
            Account ophaalAcc = accountDB.zoekAccountOpEmail(account.getEmailadres());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException ex){
            System.out.println("-testToevoegenAccount-" + ex);
        }
    }
    
    /*
    // Negatieve test: account zoeken op email zonder email
    @Test
    public void testZoekAccountOpEmailNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        Account ophaalAcc = accountDB.zoekAccountOpEmail(null);
    }*/
    
    // Positieve test: naam van account wijzigen
    @Test
    public void testWijzigenAccountNaam(){
        try{
            accountDB.toevoegenAccount(account);
            account.setNaam("Ford");
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Ford", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountNaam - " + ex);
        }
    }
    
    // Positieve test: voornaam van account wijzigen
    @Test
    public void testWijzigenAccountVoornaam(){
        try{
            accountDB.toevoegenAccount(account);
            account.setVoornaam("Katrien");
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Katrien", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountVoornaam - " + ex);
        }
    }
    
    // Er werd geen test voor login te wijzigen toegevoegd want de wijzigenAccount() methode kan het login niet wijzigen
    
    // Positieve test: passwoord van account wijzgen
    @Test
    public void testWijzigenAccountPasswoord(){
        try{
            accountDB.toevoegenAccount(account);
            account.setPaswoord("paswoord123");
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("paswoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountPasswoord - " + ex);
        }
    }
    
    // Positieve test: emailadres van account wijzigen
    @Test
    public void testWijzigenAccountEmailadres(){
        try{
            accountDB.toevoegenAccount(account);
            account.setEmailadres("defoortmieke@gmail.com");
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("defoortmieke@gmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountEmailadres - " + ex);
        }
    }
    
    // Positieve test: geslacht van account wijzigen naar M
    @Test
    public void testWijzigenAccountGeslachtM(){
        try{
            accountDB.toevoegenAccount(account);
            account.setGeslacht(Geslacht.M);
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.M, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountGeslachtM - " + ex);
        }
    }
    
    // Positieve test: geslacht van account wijzigen naar V
    @Test
    public void testWijzigenAccountGeslachtV(){
        try{
            account.setGeslacht(Geslacht.M);
            accountDB.toevoegenAccount(account);
            account.setGeslacht(Geslacht.V);
            accountDB.wijzigenAccount(account);
            
            Account ophaalAcc = accountDB.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
        }catch(DBException ex){
            System.out.println("testWijzigenAccountGeslachtV - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder naam
    @Test
    public void testWijzigenAccountNaamNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        account.setNaam(null);
        accountDB.wijzigenAccount(account);
    }
    
    // Negatieve test: account wijzigen zonder voornaam
    @Test
    public void testWijzigenAccountVoornaamNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        account.setVoornaam(null);
        accountDB.wijzigenAccount(account);
    }
    
    // Negatieve test: account wijzigen zonder paswoord
    @Test
    public void testWijzigenAccountPaswoordNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        account.setPaswoord(null);
        accountDB.wijzigenAccount(account);
    }
    
    // Negatieve test: account wijzigen zonder emailadres
    @Test
    public void testWijzigenAccountEmailadresNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        account.setEmailadres(null);
        accountDB.wijzigenAccount(account);
    }
    
    // Negatieve test: account wijzigen zonder geslacht
    @Test
    public void testWijzigenAccountGeslachtNull() throws DBException{
        thrown.expect(DBException.class);
        
        accountDB.toevoegenAccount(account);
        account.setGeslacht(null);
        accountDB.wijzigenAccount(account);
    }
    
    // Positieve test: account verwijderen
    @Test
    public void testVerwijderenAccount(){
        try{
            accountDB.toevoegenAccount(account);

            Account ophaalAcc = accountDB.zoekAccountOpLogin(account.getLogin());

            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            
            accountDB.verwijderenAccount(account);
            
            ophaalAcc = accountDB.zoekAccountOpLogin(account.getLogin());
            assertNull(ophaalAcc);
        }catch(DBException ex){
            System.out.println("testVerwijderenAccount - " + ex);
        }
    }
    
    // Geen negatieve test om account te verwijderen want een account proberen verwijderen dat niet bestaat geeft geen errors?
}