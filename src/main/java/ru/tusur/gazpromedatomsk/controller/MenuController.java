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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.job.MenuParse;
import ru.tusur.gazpromedatomsk.model.Menu;
import ru.tusur.gazpromedatomsk.service.MenuService;

@RestController
@RequestMapping("/menu")
@AllArgsConstructor
@Slf4j
@Api(value = "menu", description = "API для операций с меню", tags = "Menu API")
public class MenuController {

  private final MenuService menuService;
  private final MenuParse menuParse;

  @ApiOperation(value = "Получить меню на сегодня")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok", response = List.class),
      @ApiResponse(code = 404, message = "Меню на сегодня не найдено")
  })
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Menu getAllMenu() {
    return menuService.getAllToday();
  }

  @ApiOperation(value = "Получить меню с сайта и внести в базу")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Ok", response = List.class)
  })
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void setMenu(){
    menuParse.parse();
  }
}
