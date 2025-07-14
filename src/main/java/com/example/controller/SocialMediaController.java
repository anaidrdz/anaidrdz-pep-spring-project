package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Optional<Account> created = accountService.register(account);
        if(created.isPresent()){
            return ResponseEntity.ok(created.get());
        }
        else if(accountService.usernameExists(account.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Optional<Account> result = accountService.login(account.getUsername(), account.getPassword());
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Optional<Message> created = messageService.createMessage(message);
        return created.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        Optional<Message> message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message.orElse(null));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId){
        Optional<Message> deleted = messageService.deleteMessageById(messageId);
        if(deleted.isPresent()) {
            return ResponseEntity.ok(1);
        }
        else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message){
        Optional<Message> updated = messageService.updateMessage(messageId, message.getMessageText());
        if(updated.isPresent()){
            return ResponseEntity.ok(1);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Integer accountId){
        List<Message> messages = messageService.getAllMessagesByAccount(accountId);
        return ResponseEntity.ok(messages);
    }
}
