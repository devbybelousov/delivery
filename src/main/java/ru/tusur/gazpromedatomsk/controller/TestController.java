package ru.tusur.gazpromedatomsk.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.job.EdaTomskParse;
import ru.tusur.gazpromedatomsk.service.FileService;

@RequestMapping("/api/test")
@AllArgsConstructor
@RestController
public class TestController {

  private final EdaTomskParse edaTomskParse;
  private final FileService fileService;

  @GetMapping("/menu/set")
  @ResponseStatus(HttpStatus.OK)
  public void setMenu() {
    edaTomskParse.parseEda();
  }

  @GetMapping("/order/send")
  @ResponseStatus(HttpStatus.OK)
  public void sendOrder() {
    //userService.sendEmailToAdmin();
    fileService.sendEmailToDelivery();
  }
}
