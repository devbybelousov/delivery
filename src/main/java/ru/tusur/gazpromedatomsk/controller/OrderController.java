package ru.tusur.gazpromedatomsk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.service.FileService;
import ru.tusur.gazpromedatomsk.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
@Api(value = "order", description = "API для операций с заказами", tags = "Order API")
public class OrderController {

  private final UserService userService;
  private final FileService fileService;

  @PostMapping()
  @ApiOperation(value = "Добавить заказ пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Входные параметры неверные"),
      @ApiResponse(code = 404, message = "Пользователь или блюдо не найдены.")

  })
  @ResponseStatus(HttpStatus.OK)
  public void addOrder(@ApiParam("Список заказов пользователя.") @RequestBody List<Order> orders,
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) {
    userService.addOrderToUser(orders, id);
  }

  @ApiOperation(value = "Отправить письмо с сформированным заказом в edatomsk.ru и админу")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok")
  })
  @GetMapping("/send")
  @ResponseStatus(HttpStatus.OK)
  public void sendOrder() {
    fileService.sendEmailToAdmin();
    fileService.sendEmailToDelivery();
  }
}
