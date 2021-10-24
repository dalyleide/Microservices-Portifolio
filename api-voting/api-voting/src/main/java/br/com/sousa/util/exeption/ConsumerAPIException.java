package br.com.sousa.util.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConsumerAPIException extends RuntimeException {

    private static final long serialVersionUID = -2023713351285264189L;

    public ConsumerAPIException(String exception){
        super(exception);
    }
}
