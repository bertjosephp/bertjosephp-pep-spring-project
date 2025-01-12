package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        String messageText = message.getMessageText();
        Integer posterAccountId = message.getPostedBy();
        boolean isValidMessageText = (messageText != null) && (!messageText.isEmpty()) && (messageText.length() <= 255);
        boolean isPosterAccountExisting = (posterAccountId != null) && accountRepository.existsById(posterAccountId);

        if (isValidMessageText && isPosterAccountExisting) {
            messageRepository.save(message);
            return message;
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessageById(Integer id) {
        messageRepository.deleteById(id);
    }

    public Message updateMessageById(Integer id, String messageText) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message == null) {
            return null;
        }

        Integer posterAccountId = message.getPostedBy();
        boolean isValidMessageText = (messageText != null) && (!messageText.isEmpty()) && (messageText.length() <= 255);
        boolean isPosterAccountExisting = (posterAccountId != null) && accountRepository.existsById(posterAccountId);

        if (isValidMessageText && isPosterAccountExisting) {
            message.setMessageText(messageText);
            messageRepository.save(message);
            return message;
        }

        return null;
    }

    public List<Message> getAllMessagesByAccount(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

}
