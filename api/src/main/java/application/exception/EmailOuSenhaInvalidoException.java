package application.exception;

public class EmailOuSenhaInvalidoException extends RuntimeException {
    public EmailOuSenhaInvalidoException(String message) {
        super(message);
    }
}
