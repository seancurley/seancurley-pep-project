package Controller;

import Service.*;
import Model.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController()
    {
        accountService = new AccountService();
        messageService = new MessageService();
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
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /*
     * Handler for registering a new user.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account toAdd = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(toAdd);
        if(addedAccount != null)
        {
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
        else
        {
            ctx.status(400);
        }
    }

    /*
     * Handler for login requests.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account toCheck = mapper.readValue(ctx.body(), Account.class);
        Account validAccount = accountService.checkValidUser(toCheck);
        if(validAccount != null)
        {
            ctx.json(mapper.writeValueAsString(validAccount));
        }
        else
        {
            ctx.status(401);
        }
    }

    /*
     * Handler for posting a new message.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message toAdd = mapper.readValue(ctx.body(), Message.class);
        Message validMessage = messageService.addMessage(toAdd);
        if(validMessage != null)
        {
            ctx.json(mapper.writeValueAsString(validMessage));
            ctx.status(200);
        }
        else
        {
            ctx.status(400);
        }
    }

    /*
     * Handler for retrieving all messages.
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException
    {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /*
     * Handler for getting messages by their ID. 
     * The message ID is taken from directly from the path parameter
     * instead of making a go-between Message object.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message toRet = messageService.getMessageById(messageID);
        if(toRet != null)
        {
            ctx.json(mapper.writeValueAsString(toRet));
        }
    }

    /*
     * Handler for deleting messages.
     * The message ID is taken directly from the path parameter
     * instead of making a go-between Message object.
     */
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message toRet = messageService.deleteMessageById(messageID);
        if(toRet != null)
        {
            ctx.json(mapper.writeValueAsString(toRet));
        }
    }

    /*
     * Handler for updating messages by message ID.
     * We only need the message_text info from the JSON object, so that string is
     * passed directly rather than passing a Message object to the MessageService.
     */
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        String newMessage = mapper.readValue(ctx.body(), Message.class).getMessage_text();
        Message toRet = messageService.updateMessageById(messageID, newMessage);
        if(toRet != null)
        {
            ctx.json(mapper.writeValueAsString(toRet));
        }
        else
        {
            ctx.status(400);
        }
    }

    /*
     * Handler for getting messages by account ID.
     */
    private void getMessagesByAccountHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> toRet = messageService.getMessagesByAccount(accountID);
        ctx.json(mapper.writeValueAsString(toRet));
    }
}