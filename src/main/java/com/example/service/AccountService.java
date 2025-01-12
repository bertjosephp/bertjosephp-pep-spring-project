package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidRegistrationInputException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        boolean isValidUsername = (username != null) && (!username.isEmpty());
        boolean isValidPassword = password.length() >= 4;
        if (!isValidUsername || !isValidPassword) {
            throw new InvalidRegistrationInputException("Invalid username or password.");
        }

        boolean isUsernameTaken = accountRepository.existsByUsername(username);
        if (isUsernameTaken) {
            throw new DuplicateUsernameException("Username is already taken.");
        }

        accountRepository.save(account);
        return account;
    }

    public Account login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password).orElse(null);
    }

}
