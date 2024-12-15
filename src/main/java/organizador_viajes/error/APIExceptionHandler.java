package organizador_viajes.error;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import organizador_viajes.error.exception.*;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessageForClient handleBadRequest(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageForClient handleNotFound(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({DuplicateException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessageForClient handleDuplicate(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({NotAuthorizedException.class, AuthenticationException.class, HttpClientErrorException.Forbidden.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessageForClient handleNotAuthorized(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({GenericInternalException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessageForClient handleGeneric(HttpServletRequest request, Exception e) {
        return new ErrorMessageForClient(e.getMessage(), request.getRequestURI());
    }

}