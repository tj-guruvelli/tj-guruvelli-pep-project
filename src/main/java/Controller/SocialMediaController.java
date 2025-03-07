package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;


    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    

    /**
     * Configures and starts the API with all required endpoints
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // Account endpoints
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);
        
        // Message endpoints
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler for registering a new account
     * POST localhost:8080/register
     */
    private void registerAccountHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        
        // Check if account registration is valid
        Account registeredAccount = accountService.registerAccount(account);
        
        if (registeredAccount != null) {
            context.status(200);
            context.json(registeredAccount);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for account login
     * POST localhost:8080/login
     */
    private void loginAccountHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        
        // Check if login is valid
        Account loggedInAccount = accountService.login(account);
        
        if (loggedInAccount != null) {
            context.status(200);
            context.json(loggedInAccount);
        } else {
            context.status(401);
        }
    }

    /**
     * Handler for creating a new message
     * POST localhost:8080/messages
     */
    private void createMessageHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);
        
        // Attempt to create the message
        Message createdMessage = messageService.createMessage(message);
        
        if (createdMessage != null) {
            context.status(200);
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for retrieving all messages
     * GET localhost:8080/messages
     */
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * Handler for retrieving a message by its ID
     * GET localhost:8080/messages/{message_id}
     */
    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        
        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
    }

    /**
     * Handler for deleting a message by its ID
     * DELETE localhost:8080/messages/{message_id}
     */
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        
        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.json("");
        }
    }

    /**
     * Handler for updating a message text by its ID
     * PATCH localhost:8080/messages/{message_id}
     */
    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        
        // Parse the new message text from the request body
        Message updatedMessageInfo = context.bodyAsClass(Message.class);
        String newMessageText = updatedMessageInfo.getMessage_text();
        
        // Attempt to update the message
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        
        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler for retrieving all messages by a specific account
     * GET localhost:8080/accounts/{account_id}/messages
     */
    private void getMessagesByAccountHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getMessagesByAccount(accountId));
    }


}