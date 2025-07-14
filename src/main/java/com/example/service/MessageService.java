package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    public Optional<Message> addNewMessage(Message message) {
    if (message == null ||
        message.getMessageText() == null || message.getMessageText().isBlank() ||
        message.getMessageText().length() > 255 ||
        message.getPostedBy() == null ||
        !accountRepository.existsById(message.getPostedBy())) {
        return Optional.empty();
    }
    return Optional.of(messageRepository.save(message));
}

    public Optional<Message> createMessage(Message message) {
        if(message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return Optional.empty();
        }
        if(!accountRepository.existsById(message.getPostedBy())){
            return Optional.empty();
        }
        
        return Optional.of(messageRepository.save(message));
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id){
        return messageRepository.findById(id);
    }

    public Optional<Message> deleteMessageById(Integer id){
        Optional<Message> existing = messageRepository.findById(id);
        existing.ifPresent(messageRepository::delete);
        return existing;
    }

    public Optional<Message> updateMessage(Integer id, String newMessageText) {
        if(newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return Optional.empty();
        }
        Optional<Message> existing = messageRepository.findById(id);
        if(existing.isPresent()){
            Message msg = existing.get();
            msg.setMessageText(newMessageText);
            messageRepository.save(msg);
            return Optional.of(msg);
        }

        return Optional.empty();
    }

    public List<Message> getAllMessagesByAccount(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
