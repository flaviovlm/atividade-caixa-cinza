package application.user.controller;

import application.annotation.RateLimited;
import application.user.dto.UserLoginRequestDTO;
import application.user.dto.UserRequestDTO;
import application.user.dto.UserResponseDTO;
import application.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    @RateLimited(attempts = 10, minutes = 2)
    public ResponseEntity<Map<String, Object>> userRegister (@RequestBody @Valid UserRequestDTO dto){
        userService.userRegistration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message",  "User Registred", "success", true));
    }

    @PostMapping("/auth/login")
    @RateLimited(attempts = 3, minutes = 1)
        public ResponseEntity<Map<String, Object>>loginUser (@Valid @RequestBody UserLoginRequestDTO dto){
       UserResponseDTO response = userService.loginUser(dto);

       Map<String,Object> bodyResponse = Map.of(
               "success", true,
               "message", response.getMessage(),
               "data", response
       );

        return ResponseEntity.status(HttpStatus.OK).body(bodyResponse);
    }
}
