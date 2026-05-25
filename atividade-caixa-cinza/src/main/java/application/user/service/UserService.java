package application.user.service;

import application.exception.ConflitodDeDadosException;
import application.exception.EmailOuSenhaInvalidoException;
import application.exception.EmailUtilizadoException;
import application.mapper.UserMapper;
import application.user.dto.UserLoginRequestDTO;
import application.user.dto.UserRequestDTO;
import application.user.dto.UserResponseDTO;
import application.user.model.UserModel;
import application.user.repositoy.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserModel userRegistration (UserRequestDTO dto){
        if (userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new EmailUtilizadoException("Email já utilizado");
        }

       UserModel model = userMapper.toModel(dto);
        model.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(model);
    }

    public UserResponseDTO loginUser (UserLoginRequestDTO dto){
        UserModel model = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EmailOuSenhaInvalidoException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getPassword(), model.getPassword())){
            throw new EmailOuSenhaInvalidoException("Email ou senha inválidos");
        }

        String message = switch (model.getRole()){
            case ADMIN -> "Olá administrador, "+model.getName();
            case GERENTE -> "Olá gerente, "+model.getName();
            case CLIENTE -> "Olá, "+model.getName();
        };

        UserResponseDTO response = userMapper.toResponseDTO(model);
        response.setMessage(message);
        return response;
    }
}
