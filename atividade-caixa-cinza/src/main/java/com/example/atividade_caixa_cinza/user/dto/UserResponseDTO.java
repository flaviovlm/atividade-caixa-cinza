package com.example.atividade_caixa_cinza.user.dto;

import com.example.atividade_caixa_cinza.role.Role;

import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String nome;
    private Role role;

    public UserResponseDTO(UUID id, String nome, Role role) {
        this.id = id;
        this.nome = nome;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
