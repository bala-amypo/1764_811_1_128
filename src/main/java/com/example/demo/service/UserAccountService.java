package com.example.demo.service;

import com.example.demo.entity.UserAccount;

public interface UserAccountService {
    UserAccount registerUser(UserAccount user);
    UserAccount getUserByEmail(String email);
}
