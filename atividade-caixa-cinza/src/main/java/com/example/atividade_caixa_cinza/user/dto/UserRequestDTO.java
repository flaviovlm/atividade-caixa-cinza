package com.example.atividade_caixa_cinza.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @Size(min = 3, max = 50, message = "O nome deve ter no mínimo 3 caracteres.")
    private String name;

    @Email(message = "Informe um email válido")
    private String email;
}
