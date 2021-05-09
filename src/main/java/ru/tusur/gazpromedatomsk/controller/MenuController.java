package ru.tusur.gazpromedatomsk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.model.Menu;
import ru.tusur.gazpromedatomsk.service.MenuService;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
@Slf4j
@Api(value = "menu", description = "API для операций с меню", tags = "Menu API")
public class MenuController {

  private final MenuService menuService;

  @ApiOperation(value = "Получить меню на сегодня")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok", response = List.class)
  })
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Menu getAllMenu() {
    return menuService.getAllToday();
  }
}
