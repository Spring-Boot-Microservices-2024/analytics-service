package org.naukma.analytics.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException() {
        super();
    }
    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public BookingNotFoundException(String message) {
        super(message);
    }
    public BookingNotFoundException(Throwable cause) {super(cause);}
}
