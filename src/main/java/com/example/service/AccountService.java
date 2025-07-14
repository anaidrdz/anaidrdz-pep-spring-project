package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Optional<Account> register(Account account){
        if(account == null || account.getUsername() == null || account.getUsername().isBlank() || account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public boolean usernameExists(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }
}
