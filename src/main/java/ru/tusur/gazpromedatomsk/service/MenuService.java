package ru.tusur.gazpromedatomsk.service;

import ru.tusur.gazpromedatomsk.model.Menu;

public interface MenuService {

  Menu getAllToday();

  Menu getMenuIfNotExistGetNewMenu();

  void save(Menu menu);
}
