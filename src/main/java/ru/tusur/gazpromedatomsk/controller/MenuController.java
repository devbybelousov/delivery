package ru.tusur.gazpromedatomsk.controller;

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
@RequestMapping("/api/menu")
@AllArgsConstructor
@Slf4j
public class MenuController {

  private final MenuService menuService;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Menu getAllMenu() {
    return menuService.getAllToday();
  }
}
