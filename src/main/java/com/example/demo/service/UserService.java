package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    };

    @Transactional
    public void transferMoney(String sender, String receiver, Double amount) {
        deduct(sender, amount);
        deposit(receiver, amount);
    }

    public void deduct(String sender, double amount) {
        User fromUser = userRepository.findUserByUsername(sender);
        double credit = fromUser.getCredit();
        if (credit<amount){
            throw new IllegalStateException("Not enough money");
        } else {
            fromUser.setCredit(credit - amount);
            userRepository.save(fromUser);
        }
    }

    public void deposit(String receiver, double amount) {
        User toUser = userRepository.findUserByUsername(receiver);
        toUser.setCredit(toUser.getCredit() + amount);
        userRepository.save(toUser);
        if (new Random().nextBoolean()) {
            throw new RuntimeException("DummyException: this should cause rollback of both inserts!");
        }
    }
}
