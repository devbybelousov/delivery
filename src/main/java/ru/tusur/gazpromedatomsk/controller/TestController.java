package ru.tusur.gazpromedatomsk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.job.EdaTomskParse;
import ru.tusur.gazpromedatomsk.service.FileService;

@RequestMapping("/test")
@AllArgsConstructor
@RestController
@Api(value = "test", description = "API для тестовых операций", tags = "Test API")
public class TestController {

  private final EdaTomskParse edaTomskParse;
  private final FileService fileService;

  @ApiOperation(value = "Получить меню с сайта и внести в базу")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok")
  })
  @GetMapping("/menu/set")
  @ResponseStatus(HttpStatus.OK)
  public void setMenu() {
    edaTomskParse.parseEda();
  }

  @ApiOperation(value = "Отправить письмо с сформированным заказом в edatomsk.ru и админу (админ не работает)")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok")
  })
  @GetMapping("/order/send")
  @ResponseStatus(HttpStatus.OK)
  public void sendOrder() {
    //userService.sendEmailToAdmin();
    fileService.sendEmailToDelivery();
  }
}
