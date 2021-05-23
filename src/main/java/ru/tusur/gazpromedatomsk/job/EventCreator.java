package ru.tusur.gazpromedatomsk.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tusur.gazpromedatomsk.service.FileService;
import ru.tusur.gazpromedatomsk.service.UserService;


@Component
@Slf4j
@AllArgsConstructor
public class EventCreator {

  private final MenuParse menuParse;
  private final UserService userService;
  private final FileService fileService;

  @Scheduled(cron = "0 0 6 * * ?", zone = "GMT+7:00")
  @Scheduled(cron = "0 36 17 * * ?", zone = "GMT+7:00")
  public void createMenu() {
    menuParse.parse();
    log.debug("Parse Success!");
  }

  @Scheduled(cron = "0 0 9 * * ?", zone = "GMT+7:00")
  public void sendToUser() {
    userService.sendEmailToAllUser();
  }

  @Scheduled(cron = "0 0 12 * * ?", zone = "GMT+7:00")
  public void sendOrder() {
    fileService.sendEmailToDelivery();
    fileService.sendEmailToAdmin();
  }
}
