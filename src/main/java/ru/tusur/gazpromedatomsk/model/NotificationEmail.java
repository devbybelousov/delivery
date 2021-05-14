package ru.tusur.gazpromedatomsk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {

  private String subject;

  private String recipient;

  private String body;

  private String fileName;

  public NotificationEmail(String subject, String recipient, String body) {
    this.subject = subject;
    this.recipient = recipient;
    this.body = body;
  }
}
