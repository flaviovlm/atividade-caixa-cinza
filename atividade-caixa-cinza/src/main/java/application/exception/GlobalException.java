package application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> runtimeException (RuntimeException erro){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("messsage", "Erro interno no servidor: " +erro.getMessage(), "success", false));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> emailUtilizadoException (EmailUtilizadoException erro){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("messsage", erro.getMessage(), "success", false));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> emailOuSenhaInvalidoException (EmailOuSenhaInvalidoException erro){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("messsage", erro.getMessage(), "success", false));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> conflitoDeDadosException (ConflitodDeDadosException erro){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("messsage", erro.getMessage(), "success", false));
    }
}
