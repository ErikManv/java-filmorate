package ru.yandex.practicum.filmorate.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.servlet.ServletException;


@ControllerAdvice
public class Handler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleNullPointer(final NullPointerException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleValidation(final ValidationException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleThrowable(final Throwable e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Произошла непредвиденная ошибка", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleNullPointer(final ServletException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }
}
