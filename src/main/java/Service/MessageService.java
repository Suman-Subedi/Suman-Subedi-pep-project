package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

/*
 * Service Layer Class for Handling message-realted business logic
 * Acts as an intermediary between SocialMediaController and the MessageDAO class
 */

public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    // Creates and Validates a new message 
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255) {
            return null;
        }
        
        if (accountService.getAccountById(message.getPosted_by()) == null) {
            return null;
        }

        if(message.getTime_posted_epoch() == 0){
            message.setTime_posted_epoch(System.currentTimeMillis() / 1000);
        }
        return messageDAO.insertMessage(message);
    }

    // Returns all the messages from the system

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Returns specific messages based on the message_id

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    // Deletes specific messages based on message_id
    public Message deleteMessage(int messageId) {
      return messageDAO.deleteMessage(messageId);
    }

    // Updates the text of exisiting message

    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        
        return messageDAO.updateMessage(messageId, newMessageText);
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
