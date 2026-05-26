package application.user.service;

import application.exception.EmailOuSenhaInvalidoException;
import application.exception.EmailUtilizadoException;
import application.mapper.UserMapper;
import application.role.Role;
import application.user.dto.UserLoginRequestDTO;
import application.user.dto.UserRequestDTO;
import application.user.dto.UserResponseDTO;
import application.user.model.UserModel;
import application.user.repositoy.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso quando o email não estiver em uso")
    void userRegistration_ComSucesso() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("flavio@gmail.com");
        dto.setPassword("senha123");

        UserModel modelSemSenhaCripto = new UserModel();
        modelSemSenhaCripto.setEmail("flavio@gmail.com");

        UserModel modelSalvo = new UserModel(UUID.randomUUID(), "Flavio", "flavio@gmail.com", "senhaCriptografada", Role.CLIENTE);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toModel(dto)).thenReturn(modelSemSenhaCripto);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("senhaCriptografada");
        when(userRepository.save(modelSemSenhaCripto)).thenReturn(modelSalvo);

        UserModel resultado = userService.userRegistration(dto);

        assertNotNull(resultado);
        assertEquals(modelSalvo.getId(), resultado.getId());
        assertEquals("senhaCriptografada", modelSemSenhaCripto.getPassword()); // Garante que a senha foi setada no modelo
        verify(userRepository, times(1)).save(modelSemSenhaCripto);
    }

    @Test
    @DisplayName("Deve lançar EmailUtilizadoException ao tentar cadastrar email duplicado")
    void userRegistration_ErroEmailDuplicado() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("flavio@gmail.com");

        UserModel usuarioExistente = new UserModel();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuarioExistente));

        assertThrows(EmailUtilizadoException.class, () -> {
            userService.userRegistration(dto);
        });

        verify(userMapper, never()).toModel(any());
        verify(userRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve lançar EmailOuSenhaInvalidoException se o email não for encontrado no login")
    void loginUser_ErroEmailNaoEncontrado() {
        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setEmail("invalido@gmail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(EmailOuSenhaInvalidoException.class, () -> {
            userService.loginUser(dto);
        });
    }

    @Test
    @DisplayName("Deve lançar EmailOuSenhaInvalidoException se a senha estiver incorreta")
    void loginUser_ErroSenhaIncorreta() {
        // Arrange
        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setEmail("flavio@gmail.com");
        dto.setPassword("senhaErrada");

        UserModel usuarioDoBanco = new UserModel(UUID.randomUUID(), "Flavio", "flavio@gmail.com", "hashCorreto", Role.CLIENTE);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuarioDoBanco));
        when(passwordEncoder.matches(dto.getPassword(), usuarioDoBanco.getPassword())).thenReturn(false);

        assertThrows(EmailOuSenhaInvalidoException.class, () -> {
            userService.loginUser(dto);
        });
    }

    @Test
    @DisplayName("Deve logar com sucesso e saudar corretamente o perfil ADMINISTRADOR")
    void loginUser_SucessoAdmin() {

        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setEmail("admin@gmail.com");
        dto.setPassword("123");

        UserModel adminMock = new UserModel(UUID.randomUUID(), "Flavio", "admin@gmail.com", "hash", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(); // DTO vazio simulando o mapper

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(adminMock));
        when(passwordEncoder.matches(dto.getPassword(), adminMock.getPassword())).thenReturn(true);
        when(userMapper.toResponseDTO(adminMock)).thenReturn(responseDTO);

        UserResponseDTO resultado = userService.loginUser(dto);
        assertEquals("Olá administrador, Flavio", resultado.getMessage());
    }

    @Test
    @DisplayName("Deve logar com sucesso e saudar corretamente o perfil GERENTE")
    void loginUser_SucessoGerente() {
        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setEmail("gerente@gmail.com");
        dto.setPassword("123");

        UserModel gerenteMock = new UserModel(UUID.randomUUID(), "Vieira", "gerente@gmail.com", "hash", Role.GERENTE);
        UserResponseDTO responseDTO = new UserResponseDTO();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(gerenteMock));
        when(passwordEncoder.matches(dto.getPassword(), gerenteMock.getPassword())).thenReturn(true);
        when(userMapper.toResponseDTO(gerenteMock)).thenReturn(responseDTO);
        UserResponseDTO resultado = userService.loginUser(dto);
        assertEquals("Olá gerente, Vieira", resultado.getMessage());
    }

    @Test
    @DisplayName("Deve logar com sucesso e saudar corretamente o perfil CLIENTE")
    void loginUser_SucessoCliente() {

        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setEmail("cliente@gmail.com");
        dto.setPassword("123");

        UserModel clienteMock = new UserModel(UUID.randomUUID(), "Lima", "cliente@gmail.com", "hash", Role.CLIENTE);
        UserResponseDTO responseDTO = new UserResponseDTO();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(clienteMock));
        when(passwordEncoder.matches(dto.getPassword(), clienteMock.getPassword())).thenReturn(true);
        when(userMapper.toResponseDTO(clienteMock)).thenReturn(responseDTO);
        UserResponseDTO resultado = userService.loginUser(dto);
        assertEquals("Olá, Lima", resultado.getMessage());
    }
}