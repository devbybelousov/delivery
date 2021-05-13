package ru.tusur.gazpromedatomsk.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tusur.gazpromedatomsk.exception.NotFoundException;

@ControllerAdvice
@Slf4j
public class RestErrorHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleNotFoundException(NotFoundException ex) {
    log.debug("handling 404 error");
    log.debug(ex.getMessage());
  }
}
