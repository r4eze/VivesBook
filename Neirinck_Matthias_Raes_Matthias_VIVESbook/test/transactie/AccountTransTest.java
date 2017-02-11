package transactie;

import bags.Account;
import database.AccountDB;
import datatype.Geslacht;
import exception.ApplicationException;
import exception.DBException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class AccountTransTest {
    
    private Account account;
    private AccountDB accountDB;
    private AccountTrans accountTrans;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public AccountTransTest() {
        accountDB = new AccountDB();
        accountTrans = new AccountTrans();
    }
    
    // Een account maken maar nog niet toevoegen
    @Before
    public void setUp() { 
        account = new Account();
        account.setNaam("Defoort");
        account.setVoornaam("Mieke");
        account.setLogin("miekedefoort");
        account.setPaswoord("wachtwoord123");
        account.setEmailadres("miekedefoort@hotmail.com");
        account.setGeslacht(Geslacht.V);
        // geen lastLogin en lastLogout toevoegen want dit gebeurt automatisch door de database
        // maar daarom kan je ook geen assertEquals ervan doen bij toevoegenAccount, zoekAccountOpLogin...
    }
    
     // Het account dat in de setup gemaakt werd, terug verwijderen
    @After
    public void tearDown() {
        try{
            accountDB.verwijderenAccount(account);
        }catch(DBException ex){
            System.out.println("-tearDown-" + ex);
        }
    }
    
    // Positieve test: account toevoegen met vrouwelijk geslacht
    // Positieve test: account zoeken op login
    @Test
    public void testAccountToevoegenGeslachtV(){
        try{
            accountTrans.accountToevoegen(account);
            Account ophaalAcc = accountTrans.zoekAccountOpLogin(account.getLogin());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testAccountToevoegen - " + ex);
        }
    }
    
    // Positieve test: account toevoegen met mannelijk geslacht
    // Positieve test: account zoeken op login
    @Test
    public void testAccountToevoegenGeslachtM(){
        try{
            account.setGeslacht(Geslacht.M);
            accountTrans.accountToevoegen(account);
            Account ophaalAcc = accountTrans.zoekAccountOpLogin(account.getLogin());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.M, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testAccountToevoegen - " + ex);
        }
    }
    
    // Negatieve test: 2 accounts toevoegen met dezelfde login
    @Test
    public void testAccountToevoegenZelfdeLogin() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);

            Account account2 = new Account();
            account2.setNaam("Petersen");
            account2.setVoornaam("Peter");
            account2.setLogin("miekedefoort");
            account2.setPaswoord("wachtwoord12345");
            account2.setEmailadres("peter@hotmail.com");
            account2.setGeslacht(Geslacht.M);

            accountTrans.accountToevoegen(account2);
        }catch(DBException ex){
            System.out.println("testAccountToevoegenZelfdeLogin - " + ex);
        }
    }
    
    // Negatieve test: 2 accounts toevoetgen met hetzelfde emailadres
    @Test
    public void testAccountToevoegenZelfdeEmailadres() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);

            Account account2 = new Account();
            account2.setNaam("Petersen");
            account2.setVoornaam("Peter");
            account2.setLogin("peterpetersen");
            account2.setPaswoord("wachtwoord12345");
            account2.setEmailadres("miekedefoort@hotmail.com");
            account2.setGeslacht(Geslacht.M);

            accountTrans.accountToevoegen(account2);
        }catch(DBException ex){
            System.out.println("testAccountToevoegenZelfdeEmailadres - " + ex);
        }
    }

    // Negatieve test: null object als account toe te voegen
    @Test
    public void testAccountToevoegenNull() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(null);
        }catch(DBException ex){
            System.out.println("testAccountToevoegenNull - " + ex);
        }
    }

    // Negatieve test: null object als account proberen te wijzigen
    @Test
    public void testAccountWijzigenNull() throws ApplicationException {
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountWijzigen(null);
        }catch(DBException ex){
            System.out.println("testAccountWijzigenNull - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder naam
    @Test
    public void testToevoegenAccountNaamNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setNaam(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountNaamNull - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen met lege naam
    @Test
    public void testToevoegenAccountNaamLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setNaam("   ");
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountNaamLeeg - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder voornaam
    @Test
    public void testToevoegenAccountVoornaamNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setVoornaam(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountVoornaamNull - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen met lege voornaam
    @Test
    public void testToevoegenAccountVoornaamLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setVoornaam("   ");
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountVoornaamLeeg - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder login
    @Test
    public void testToevoegenAccountLoginNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setLogin(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountLoginNull - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen met lege login
    @Test
    public void testToevoegenAccountLoginLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setLogin("   ");
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountLoginLeeg - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder paswoord
    @Test
    public void testToevoegenAccountPaswoordNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setPaswoord(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountPaswoordNull - " + ex);
        }
    }
    
    // Geen test om login te wijzigen want dat is niet mogelijk
    
    // Negatieve test: account toevoegen met lege paswoord
    @Test
    public void testToevoegenAccountPaswoordLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setPaswoord("   ");
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountPaswoordLeeg - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder emailadres
    @Test
    public void testToevoegenAccountEmailadresNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setEmailadres(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountEmailadresNull - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen met lege emailadres
    @Test
    public void testToevoegenAccountEmailadresLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setEmailadres("   ");
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountEmailadresLeeg - " + ex);
        }
    }
    
    // Negatieve test: account toevoegen zonder geslacht
    @Test
    public void testToevoegenAccountGeslachtNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        account.setGeslacht(null);
        
        try{
            accountTrans.accountToevoegen(account);
        }catch(DBException ex){
            System.out.println("testToevoegenAccountGeslachtNull - " + ex);
        }
    }
    
    // Positieve test: naam wijzigen
    @Test
    public void testWijzigenAccountNaam(){
        try{
            accountTrans.accountToevoegen(account);
            account.setNaam("Ford");
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Ford", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountNaam - " + ex);
        }
    }
    
    // Positieve test: voornaam wijzigen
    @Test
    public void testWijzigenAccountVoornaam(){
        try{
            accountTrans.accountToevoegen(account);
            account.setVoornaam("Katrien");
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Katrien", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountVoornaam - " + ex);
        }
    }
    
    // Positieve test: paswoord wijzigen
    @Test
    public void testWijzigenAccountPaswoord(){
        try{
            accountTrans.accountToevoegen(account);
            account.setPaswoord("paswoord123");
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("paswoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountPaswoord - " + ex);
        }
    }
    
    // Positieve test: emailadres wijzigen
    @Test
    public void testWijzigenAccountEmailadres(){
        try{
            accountTrans.accountToevoegen(account);
            account.setEmailadres("defoortmieke@gmail.com");
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("defoortmieke@gmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountEmailadres - " + ex);
        }
    }
    
    // Positieve test: Geslacht naar M wijzigen
    @Test
    public void testWijzigenAccountGeslachtM(){
        try{
            accountTrans.accountToevoegen(account);
            account.setGeslacht(Geslacht.M);
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.M, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountGeslachtM - " + ex);
        }
    }
    
    // Positieve test: Geslacht naar V wijzigen
    @Test
    public void testWijzigenAccountGeslachtV(){
        try{
            account.setGeslacht(Geslacht.M);
            accountTrans.accountToevoegen(account);
            account.setGeslacht(Geslacht.V);
            accountTrans.accountWijzigen(account);
            
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testWijzigenAccountGeslachtV - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder naam
    @Test
    public void testWijzigenAccountNaamNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setNaam(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountNaamNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder voornaam
    @Test
    public void testWijzigenAccountVoornaamNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setVoornaam(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountVoornaamNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder login
    @Test
    public void testWijzigenAccountLoginNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setLogin(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountLoginNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder paswoord
    @Test
    public void testWijzigenAccountPaswoordNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setPaswoord(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountPaswoordNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder emailadres
    @Test
    public void testWijzigenAccountEmailadresNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setEmailadres(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountEmailadresNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen zonder geslacht
    @Test
    public void testWijzigenAccountGeslachtNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setGeslacht(null);
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountGeslachtNull - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen met lege naam
    @Test
    public void testWijzigenAccountNaamLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setNaam("   ");
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountNaamLeeg - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen met lege voornaam
    @Test
    public void testWijzigenAccountVoornaamLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setVoornaam("   ");
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountVoornaamLeeg - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen met lege login
    @Test
    public void testWijzigenAccountLoginLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setLogin("   ");
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountLoginLeeg - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen met leeg paswoord
    @Test
    public void testWijzigenAccountPaswoordLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setPaswoord("   ");
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountPaswoordLeeg - " + ex);
        }
    }
    
    // Negatieve test: account wijzigen met leeg emailadres
    @Test
    public void testWijzigenAccountEmailadresLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            account.setEmailadres("   ");
            accountTrans.accountWijzigen(account);
        }catch(DBException ex){
            System.out.println("testWijzigenAccountEmailadresLeeg - " + ex);
        }
    }
    
    // Positieve test: account zoeken om email
    @Test
    public void testZoekAccountOpEmail(){
        try{
            accountTrans.accountToevoegen(account);
            Account ophaalAcc = accountTrans.zoekAccountOpEmail(account.getEmailadres());
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
        }catch(DBException | ApplicationException ex){
            System.out.println("testZoekAccountOpEmail - " + ex);
        }
    }
    
    // Negatieve test: account zoeken op email zonder email
    @Test
    public void testZoekAccountOpEmailNull() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            Account ophaalAcc = accountTrans.zoekAccountOpEmail(null);
        }catch(DBException ex){
            System.out.println("testZoekAccountOpEmailNull - " + ex);
        }
    }
    
    // Negatieve test: account zoeken op lege email
    @Test
    public void testZoekAccountOpEmailLeeg() throws ApplicationException{
        thrown.expect(ApplicationException.class);
        
        try{
            accountTrans.accountToevoegen(account);
            Account ophaalAcc = accountTrans.zoekAccountOpEmail("");
        }catch(DBException ex){
            System.out.println("testZoekAccountOpEmailLeeg - " + ex);
        }
    }
    
    // Postitieve test: account inloggen
    @Test
    public void testInloggenAccount()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogin(accountTrans.inloggenAccount(account.getLogin(), account.getPaswoord()));
            Account ophaalAcc = accountTrans.zoekAccountOpLogin("miekedefoort");
            
            assertEquals("Defoort", ophaalAcc.getNaam());
            assertEquals("Mieke", ophaalAcc.getVoornaam());
            assertEquals("miekedefoort", ophaalAcc.getLogin());
            assertEquals("wachtwoord123", ophaalAcc.getPaswoord());
            assertEquals("miekedefoort@hotmail.com", ophaalAcc.getEmailadres());
            assertEquals(Geslacht.V, ophaalAcc.getGeslacht());
            assertEquals(account.getLastLogin(), ophaalAcc.getLastLogin());
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testInloggenAccount - " + ex);
        }
    }
    
    // Negatieve test: account inloggen zonder login
    @Test
    public void testInloggenAccountLoginNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogin(accountTrans.inloggenAccount(null, account.getPaswoord()));
        }
        catch(DBException ex)
        {
            System.out.println("testInloggenAccountLoginNull - " + ex);
        }
    }
    
    // Negatieve test: account inloggen zonder paswoord
    @Test
    public void testInloggenAccountPaswoordNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogout(accountTrans.uitloggenAccount(account.getLogin(), null));
        }
        catch(DBException ex)
        {
            System.out.println("testInloggenAccountPaswoordNull - " + ex);
        }
    }
    
    // Negatieve test: account inloggen maar login klopt niet (of account bestaat niet)
    @Test
    public void testInloggenAccountLoginVerkeerd() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogin(accountTrans.inloggenAccount("verkeerdlogin", account.getPaswoord()));
        }
        catch(DBException ex)
        {
            System.out.println("testInloggenAccountLoginVerkeerd - " + ex);
        }
    }
    
    // Negatieve test: account inloggen maar paswoord klopt niet (of account bestaat niet)
    @Test
    public void testInloggenAccountPaswoordVerkeerd() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogin(accountTrans.inloggenAccount(account.getLogin(), "verkeerdpaswoord"));
        }
        catch(DBException ex)
        {
            System.out.println("testInloggenAccountPaswoordVerkeerd - " + ex);
        }
    }
    
    // Negatieve test: account uitloggen zonder login
    @Test
    public void testUitloggenAccountLoginNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogout(accountTrans.uitloggenAccount(null, account.getPaswoord()));
        }
        catch(DBException ex)
        {
            System.out.println("testUitloggenAccountLoginNull - " + ex);
        }
    }
    
    // Negatieve test: account uitloggen zonder paswoord
    @Test
    public void testUitloggenAccountPaswoordNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogout(accountTrans.uitloggenAccount(account.getLogin(), null));
        }
        catch(DBException ex)
        {
            System.out.println("testUitloggenAccountPaswoordNull - " + ex);
        }
    }
    
    // Negatieve test: account uitloggen maar verkeerd login (of account bestaat niet)
    @Test
    public void testUitloggenAccountLoginVerkeerd() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogout(accountTrans.uitloggenAccount("verkeerdlogin", account.getPaswoord()));
        }
        catch(DBException ex)
        {
            System.out.println("testUitloggenAccountLoginVerkeerd - " + ex);
        }
    }
    
    // Negatieve test: account uitloggen maar verkeerd paswoord (of account bestaat niet)
    @Test
    public void testUitloggenAccountPaswoordVerkeerd() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            account.setLastLogout(accountTrans.uitloggenAccount(account.getLogin(), "verkeerdpaswoord"));
        }
        catch(DBException ex)
        {
            System.out.println("testUitloggenAccountPaswoordVerkeerd - " + ex);
        }
    }
    
    // Positieve test: controleren als paswoord overeenkomt met login
    @Test
    public void testPasswordMatchesLogin()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            assertEquals(accountTrans.passwordMatchesLogin(account.getLogin(), account.getPaswoord()), true);
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testPasswordMatchesLogin - " + ex);
        }
    }
    
    // Positieve test: controleren als paswoord overeenkomt met login met verkeerd login
    @Test
    public void testPasswordMatchesLoginLoginVerkeerd()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            assertEquals(accountTrans.passwordMatchesLogin("verkeerdlogin", account.getPaswoord()), false);
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testPasswordMatchesLoginLoginVerkeerd - " + ex);
        }
    }
    
    // Positieve test: controleren als paswoord overeenkomt met login met verkeerd paswoord
    @Test
    public void testPasswordMatchesLoginPaswoordVerkeerd()
    {
        try
        {
            accountTrans.accountToevoegen(account);
            assertEquals(accountTrans.passwordMatchesLogin(account.getLogin(), "verkeerdpaswoord"), false);
        }
        catch(DBException | ApplicationException ex)
        {
            System.out.println("testPasswordMatchesLoginPaswoordVerkeerd - " + ex);
        }
    }
    
    // Negatieve test: controleren als paswoord overeenkomt met login zonder login
    @Test
    public void testPasswordMatchesLoginLoginNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.passwordMatchesLogin(null, account.getPaswoord());
        }
        catch(DBException ex)
        {
            System.out.println("testPasswordMatchesLoginLoginNull - " + ex);
        }
    }
    
    // Negatieve test: controleren als paswoord overeenkomt met login zonder paswoord
    @Test
    public void testPasswordMatchesLoginPaswoordNull() throws ApplicationException
    {
        thrown.expect(ApplicationException.class);
        
        try
        {
            accountTrans.accountToevoegen(account);
            accountTrans.passwordMatchesLogin(account.getLogin(), null);
        }
        catch(DBException ex)
        {
            System.out.println("testPasswordMatchesLoginPaswoordNull - " + ex);
        }
    }
}
