package com.example.atividade_caixa_cinza.user.service;

import com.example.atividade_caixa_cinza.user.dto.UserRequestDTO;
import com.example.atividade_caixa_cinza.user.model.UserModel;
import com.example.atividade_caixa_cinza.user.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel userRegistration (UserRequestDTO dto){
        if (userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Esse email já está em uso");
        }
        UserModel model = new UserModel();
        model.setEmail(dto.getEmail());
        model.setName(dto.getName());
        model.setPassword(passwordEncoder.encode(dto.getPassword()));
        model.setRole(dto.getRole());

        return userRepository.save(model);
    }}
