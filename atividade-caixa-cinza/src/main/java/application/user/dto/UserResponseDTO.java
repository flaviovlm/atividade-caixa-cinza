package application.user.dto;

import application.role.Role;

import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String name;
    private Role role;
    private String message;

    public UserResponseDTO() {
    }

    public UserResponseDTO(UUID id, String name, Role role, String message) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
