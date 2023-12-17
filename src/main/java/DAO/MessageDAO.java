package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/*
 * Data access object for the Message table.
 * Message structure in the database is as follows:
 *      message_id int primary key auto_increment
        posted_by int
        message_text varchar(255)
        time_posted_epoch bigint
        foreign key (posted_by) references account(account_id)
 */
public class MessageDAO {
    
    public Message addMessage(Message toAdd)
    {
        Connection connection = ConnectionUtil.getConnection();
        if(toAdd.getMessage_text().length() <= 0 || toAdd.getMessage_text().length() > 255)
        {
            return null;
        }

        try{
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, toAdd.getPosted_by());
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
            {
                return null;
            }

           sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
           ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           ps.setInt(1, toAdd.getPosted_by());
           ps.setString(2, toAdd.getMessage_text());
           ps.setLong(3, toAdd.getTime_posted_epoch());
           ps.executeUpdate();
           ResultSet pkeyResultSet = ps.getGeneratedKeys();
           while(pkeyResultSet.next())
           {
                int generatedID = (int)pkeyResultSet.getLong(1);
                return new Message(generatedID, toAdd.getPosted_by(), toAdd.getMessage_text(), toAdd.getTime_posted_epoch());
           }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message getMessageById(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

           while(rs.next())
           {
                return new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
           }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByAccount(int account_id)
    {
        List<Message> messages = new ArrayList<Message>();
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

           while(rs.next())
           {
                Message toAdd = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));

                messages.add(toAdd);
           }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message deleteMessage(int id)
    {
        Message toRet = getMessageById(id);

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?";
           PreparedStatement ps = connection.prepareStatement(sql);
           ps.setInt(1, id);
           ps.executeUpdate();

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return toRet;
    }

    public Message updateMessageById(int id, String update)
    {
        if(update.length() <= 0 || update.length() > 255)
        {
            return null;
        }
        if(getMessageById(id) == null)
        {
            return null;
        }
        Connection connection = ConnectionUtil.getConnection();
        try{
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
           PreparedStatement ps = connection.prepareStatement(sql);
           ps.setString(1, update);
           ps.setInt(2, id);
           ps.executeUpdate();

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return getMessageById(id);
    }

    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Message toAdd = new Message(rs.getInt("message_id"), 
                                                rs.getInt("posted_by"), 
                                                rs.getString("message_text"),
                                                rs.getLong("time_posted_epoch"));
                messages.add(toAdd);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


}
