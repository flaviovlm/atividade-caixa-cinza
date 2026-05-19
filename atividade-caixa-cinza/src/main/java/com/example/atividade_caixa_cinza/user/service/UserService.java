package com.example.atividade_caixa_cinza.user.service;

import com.example.atividade_caixa_cinza.user.repositoy.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public
}
