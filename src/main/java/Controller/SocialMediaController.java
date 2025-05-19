package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    ObjectMapper objectMapper;
   
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);



        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

     // Handlers for Each Activity
    private void registerHandler(Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account registeredAccount = accountService.register(account);

            if(registeredAccount == null){
                ctx.status(400);

            } else{
                ctx.json(registeredAccount);
            }
        } catch (JsonProcessingException e) {
            // TODO: handle exception
            ctx.status(400);
        }
       
       
    }

    private void loginHandler(Context ctx){
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.login(account);
            if(loggedInAccount == null){
                ctx.status(401);
            } else{
                ctx.json(loggedInAccount);
            }

        } catch (JsonProcessingException e) {
            // TODO: handle exception
            ctx.status(400);
        }
    }


    private void createMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(createdMessage);
            }
        } catch (JsonProcessingException e) {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message);
            } else {
                ctx.result("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
            if (deletedMessage != null) {
                ctx.json(deletedMessage);
            } else {
                ctx.result("");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void updateMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message messageUpdate = objectMapper.readValue(ctx.body(), Message.class);
            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate.getMessage_text());
            if (updatedMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(updatedMessage);
            }
        } catch (JsonProcessingException | NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByUser(accountId));
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }



}