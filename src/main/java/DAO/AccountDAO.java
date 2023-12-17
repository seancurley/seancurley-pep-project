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
    
    public Account addAccount(Account toAdd)
    {
        Connection connection = ConnectionUtil.getConnection();
        if(toAdd.getUsername().equals("") || toAdd.getPassword().length() < 4)
        {
            return null;
        }
        try{
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, toAdd.getUsername());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return null;
            }

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
        return null;
    }

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
                System.out.println("Error: " + e.getMessage());
            }
            return null;

    }

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
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }
}
