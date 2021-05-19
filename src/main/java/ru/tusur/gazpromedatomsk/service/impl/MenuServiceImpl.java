package ru.tusur.gazpromedatomsk.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tusur.gazpromedatomsk.exception.NotFoundException;
import ru.tusur.gazpromedatomsk.model.Menu;
import ru.tusur.gazpromedatomsk.repository.MenuRepository;
import ru.tusur.gazpromedatomsk.service.MenuService;

@Service
@AllArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;

  @Override
  public Menu getAllToday() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try {
      return menuRepository.findByCreatedAt(sdf.parse(sdf.format(new Date())))
          .orElseThrow(() -> new NotFoundException("Menu not found by date"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return new Menu();
  }

  @Override
  public Menu getMenuIfNotExistGetNewMenu() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date today = null;
    try {
      today = sdf.parse(sdf.format(new Date()));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    log.warn("Today: " + today);

    return menuRepository.findByCreatedAt(today)
        .orElse(new Menu());
  }

  @Override
  public void save(Menu menu) {
    menuRepository.save(menu);
  }
}
