package Service;

import Model.Message;
import DAO.MessageDAO;
import Service.MessageService;
import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */
 
public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    /**
     * Creates a new message if the input is valid
     * @param message The message to create
     * @return The created message with message_id if successful, null otherwise
     */
    public Message createMessage(Message message) {
        // Validate message_text is not blank and not over 255 characters
        if (message.getMessage_text() == null || 
            message.getMessage_text().trim().isEmpty() || 
            message.getMessage_text().length() > 255) {
            return null;
        }

        // Validate posted_by refers to an existing user
        if (accountService.getAccountById(message.getPosted_by()) == null) {
            return null;
        }

        // If all validations pass, create the message
        // Set the current time if not provided
        if (message.getTime_posted_epoch() == 0) {
            message.setTime_posted_epoch(System.currentTimeMillis());
        }

        return messageDAO.createMessage(message);
    }

    /**
     * Retrieves all messages
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its ID
     * @param messageId The ID of the message to retrieve
     * @return The message if found, null otherwise
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Deletes a message by its ID
     * @param messageId The ID of the message to delete
     * @return The deleted message if found and deleted, null otherwise
     */
    public Message deleteMessage(int messageId) {
        Message messageToDelete = messageDAO.getMessageById(messageId);
        
        if (messageToDelete != null) {
            messageDAO.deleteMessage(messageId);
            return messageToDelete;
        }
        
        return null;
    }

    /**
     * Updates a message text by its ID
     * @param messageId The ID of the message to update
     * @param newMessageText The new text for the message
     * @return The updated message if successful, null otherwise
     */
    public Message updateMessage(int messageId, String newMessageText) {
        // Validate the new message text
        if (newMessageText == null || 
            newMessageText.trim().isEmpty() || 
            newMessageText.length() > 255) {
            return null;
        }

        // Check if message exists
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage == null) {
            return null;
        }

        // Update the message text
        existingMessage.setMessage_text(newMessageText);
        return messageDAO.updateMessage(existingMessage);
    }

    /**
     * Retrieves all messages by a specific account
     * @param accountId The ID of the account
     * @return List of messages posted by the account
     */
    public List<Message> getMessagesByAccount(int accountId) {
        return messageDAO.getMessagesByAccount(accountId);
    }
    
}
