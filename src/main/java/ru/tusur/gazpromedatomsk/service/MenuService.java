package ru.tusur.gazpromedatomsk.service;

import ru.tusur.gazpromedatomsk.model.Menu;

public interface MenuService {

  Menu getAllToday();

  void save(Menu menu);
}
