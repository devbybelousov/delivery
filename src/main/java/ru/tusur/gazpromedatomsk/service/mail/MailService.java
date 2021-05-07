package ru.tusur.gazpromedatomsk.service.mail;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tusur.gazpromedatomsk.exception.SpringMailException;
import ru.tusur.gazpromedatomsk.model.NotificationEmail;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

  private final JavaMailSender mailSender;
  private final MailContentBuilder mailContentBuilder;

  @Async
  public void sendMail(NotificationEmail notificationEmail) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom("no-reply@gazprom.ru");
      messageHelper.setTo(notificationEmail.getRecipient());
      messageHelper.setSubject(notificationEmail.getSubject());
      messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
    };
    try {
      mailSender.send(messagePreparator);
      log.info("Activation email sent!");
    } catch (MailException e) {
      throw new SpringMailException(
          "Exception occurred when sending mail to " + notificationEmail.getRecipient());
    }
  }

  @Async
  public void sendEmailWithAttachment(NotificationEmail notificationEmail) {
    MimeMessagePreparator messagePreparator = mimeMessage -> {
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
      messageHelper.setFrom("no-reply@gazprom.ru");
      messageHelper.setTo(notificationEmail.getRecipient());
      messageHelper.setSubject(notificationEmail.getSubject());
      messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
      FileSystemResource file
          = new FileSystemResource(new File(notificationEmail.getFileName()));
      messageHelper.addAttachment(notificationEmail.getFileName(), file);

    };
    try {
      mailSender.send(messagePreparator);
      log.info("Activation email sent!");
    } catch (MailException e) {
      e.printStackTrace();
      throw new SpringMailException(
          "Exception occurred when sending mail to " + notificationEmail.getRecipient());
    }
  }
}
