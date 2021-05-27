package ru.tusur.gazpromedatomsk.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tusur.gazpromedatomsk.model.NotificationEmail;
import ru.tusur.gazpromedatomsk.service.FileService;
import ru.tusur.gazpromedatomsk.service.UserService;
import ru.tusur.gazpromedatomsk.service.mail.MailService;


@Component
@Slf4j
@AllArgsConstructor
public class EventCreator {

  private final MenuParse menuParse;
  private final UserService userService;
  private final FileService fileService;
  private final MailService mailService;

  @Scheduled(cron = "0 0 6 * * MON-SAT", zone = "GMT+7:00")
  public void createMenu() {
    menuParse.parse();
    log.error("Parse Success!");
  }

  @Scheduled(cron = "0 0 9 * * MON-SAT", zone = "GMT+7:00")
  public void sendToUser() {
    userService.sendEmailToAllUser();
  }

  @Scheduled(cron = "0 0 12 * * MON-SAT", zone = "GMT+7:00")
  public void sendOrder() {
    fileService.sendEmailToDelivery();
    fileService.sendEmailToAdmin();
  }

  @Scheduled(cron = "0 0 8 * * MON-SAT", zone = "GMT+7:00")
  @Scheduled(cron = "0 0 9 * * MON-SAT", zone = "GMT+7:00")
  @Scheduled(cron = "0 0 12 * * MON-SAT", zone = "GMT+7:00")
  @Scheduled(cron = "0 0 0 * * MON-SAT", zone = "GMT+7:00")
  public void sendTestMail() {
    log.error("Send Email Test");
    mailService.sendMail(new NotificationEmail("Тестовое сообщение!",
        "devbybelousov@yandex.ru",
        "Сработал cron"));
  }
}
