package com.example.atividade_caixa_cinza.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN ("Administrador"),
    GERENTE("Gerente"),
    CLIENTE("Cliente");

    private String texto;

    Role(String texto) {
        this.texto = texto;
    }

    @JsonValue
    public String getTexto() {
        return texto;
    }

    @JsonCreator
    public static Role fromTexto(String value){
        for (Role role : Role.values()){
            if (role.texto.equalsIgnoreCase(value)){
                return role;
            }
        }
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored){}

        throw new IllegalArgumentException("Cargo inválido: "+value);
    }
}
