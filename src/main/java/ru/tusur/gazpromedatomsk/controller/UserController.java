package ru.tusur.gazpromedatomsk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.dto.UserRequest;
import ru.tusur.gazpromedatomsk.model.User;
import ru.tusur.gazpromedatomsk.service.UserService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Api(value = "user", description = "API для пользовательских операций", tags = "User API")
public class UserController {

  private final UserService userService;

  @PostMapping
  @ApiOperation(value = "Добавить пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Входные параметры неверные")
  })
  @ResponseStatus(HttpStatus.OK)
  public void createUser(@ApiParam("Пользователь") @RequestBody @Valid UserRequest user) {
    userService.save(user);
  }

  @GetMapping
  @ApiOperation(value = "Получить информацию о пользователе")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Входные параметры неверные"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @ResponseStatus(HttpStatus.OK)
  public User getUser(@ApiParam("Идентификатор пользователя") @RequestParam Long id){
    return userService.getUser(id);
  }


}
