package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidRegistrationInputException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        Account newAccount = accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(newAccount);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateUsername(DuplicateUsernameException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidRegistrationInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPasswordOrAccountAlreadyExists(InvalidRegistrationInputException e) {
        return e.getMessage();
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authenticatedAccount = accountService.login(account.getUsername(), account.getPassword());
        if (authenticatedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(authenticatedAccount);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.addMessage(message);
        if (createdMessage != null) {
            return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages != null) {
            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
        
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message newMessage) {
        Message updatedMessage = messageService.updateMessageById(messageId, newMessage.getMessageText());
        if (updatedMessage != null) {
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getAllMessagesByAccount(accountId);
        if (messages != null) {
            return ResponseEntity.status(HttpStatus.OK).body(messages);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
