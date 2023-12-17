package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/*
 * Data access object for the Account table.
 * Account structure in the database is as follows:
 *      id int primary key auto-increment
 *      username varchar(255) unique
 *      password varchar(255)
 */

public class AccountDAO {
    
    /*
     * Method for adding a new account.
     */
    public Account addAccount(Account toAdd)
    {
        Connection connection = ConnectionUtil.getConnection();
        //Sanity check for username and password
        if(toAdd.getUsername().equals("") || toAdd.getPassword().length() < 4)
        {
            return null;
        }
        try{
            //Sanity check to make sure we're not repeating usernames
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, toAdd.getUsername());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return null;
            }

        //Add the new account, similarly to how Books were added in the Library project
           sql = "INSERT INTO Account(username, password) VALUES (?, ?)";
           ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ps.setString(1, toAdd.getUsername());
           ps.setString(2, toAdd.getPassword());
           ps.executeUpdate();
           ResultSet pkeyResultSet = ps.getGeneratedKeys();
           if(pkeyResultSet.next())
           {
                int generatedID = (int)pkeyResultSet.getLong(1);
                Account toRet = new Account(generatedID, toAdd.getUsername(), toAdd.getPassword());
                return toRet;
           }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //If we get here, something has gone wrong.
        return null;
    }

    /*
     * Method for validating an existing user's login and password.
     */
    public Account checkValidUser(Account toCheck)
    {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, toCheck.getUsername());
            ps.setString(2, toCheck.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Account toRet = new Account(rs.getInt("account_id"),
                                            rs.getString("username"),
                                            rs.getString("password"));
                return toRet;
            }
        }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            //If we get here, there was no existing username/password combination in the database.
            return null;

    }

    /*
     * Method for retrieving all user accounts.
     */
    public List<Account> getAllAccounts()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<Account>();
        try{
            String sql = "SELECT * FROM account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), 
                                            rs.getString("username"), 
                                            rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }
}
