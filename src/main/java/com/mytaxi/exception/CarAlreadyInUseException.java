package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Car already in use")
public class CarAlreadyInUseException extends Exception
{
    static final long serialVersionUID = -3387516997894229948L;


    public CarAlreadyInUseException(String message)
    {
        super(message);
    }
}
