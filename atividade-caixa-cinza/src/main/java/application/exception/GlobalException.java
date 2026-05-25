package application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> runtimeException (RuntimeException erro){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro interno no servidor: " + erro.getMessage(), "success", false));
    }

    @ExceptionHandler(EmailUtilizadoException.class)
    public ResponseEntity<Map<String, Object>> emailUtilizadoException (EmailUtilizadoException erro){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", erro.getMessage(), "success", false));
    }

    @ExceptionHandler(EmailOuSenhaInvalidoException.class)
    public ResponseEntity<Map<String, Object>> emailOuSenhaInvalidoException (EmailOuSenhaInvalidoException erro){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", erro.getMessage(), "success", false));
    }

    @ExceptionHandler(ConflitodDeDadosException.class)
    public ResponseEntity<Map<String, Object>> conflitoDeDadosException (ConflitodDeDadosException erro){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", erro.getMessage(), "success", false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String mensagemErro = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Erro de validação nos campos.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", mensagemErro, "success", false));
    }
}