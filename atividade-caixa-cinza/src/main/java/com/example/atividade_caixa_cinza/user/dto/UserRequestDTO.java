package com.example.atividade_caixa_cinza.user.dto;

import com.example.atividade_caixa_cinza.role.Role;
import jakarta.validation.constraints.*;

public class UserRequestDTO {

    @Size(min = 3, max = 50, message = "O nome deve ter no mínimo 3 caracteres.")
    @NotBlank(message = "É obrigatório colocar o nome")
    private String name;

    @Email(message = "Informe um email válido")
    @NotBlank(message = "É obrigatório informar o email")
    private String email;


    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%&*]).{10,12}$", message = "A senha deve ter pelo menos: 1 caractere especial, letra maiuscula e minuscula, e números")
    private String password;

    @NotNull
    private Role role;

    public UserRequestDTO(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
