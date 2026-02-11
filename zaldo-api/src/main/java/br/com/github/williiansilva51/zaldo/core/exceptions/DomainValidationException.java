package br.com.github.williiansilva51.zaldo.core.exceptions;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
