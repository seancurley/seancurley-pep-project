package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

/*
 * TODO:
 * Create new message
 * Get all messages
 * Get one message by message id
 * Delete a message by message id
 * Update message by message id
 */

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService()
    {
        this.messageDAO = new MessageDAO();
    }

    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByAccount(int id)
    {
        return messageDAO.getMessagesByAccount(id);
    }

    public Message addMessage(Message toAdd)
    {
        return messageDAO.addMessage(toAdd);
    }

    public Message getMessageById(int id)
    {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id)
    {
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessageById(int id, String update)
    {
        return messageDAO.updateMessageById(id, update);
    }
}
