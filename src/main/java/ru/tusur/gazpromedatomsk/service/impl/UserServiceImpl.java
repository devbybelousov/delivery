package ru.tusur.gazpromedatomsk.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tusur.gazpromedatomsk.dto.UserRequest;
import ru.tusur.gazpromedatomsk.exception.NotFoundException;
import ru.tusur.gazpromedatomsk.model.NotificationEmail;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.model.User;
import ru.tusur.gazpromedatomsk.repository.OrderRepository;
import ru.tusur.gazpromedatomsk.repository.UserRepository;
import ru.tusur.gazpromedatomsk.service.UserService;
import ru.tusur.gazpromedatomsk.service.mail.MailService;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final MailService mailService;
  private final OrderRepository orderRepository;

  @Override
  public void sendEmailToAllUser() {
    userRepository.findAllByIsAdmin(false).stream().map(
        user -> {
          mailService.sendMail(new NotificationEmail("Время кушать!",
              user.getEmail(),
              "Чтобы посмотреть меню нажмите на <a href=\"http://localhost:8080/api/menu\">ссылку</a></br>"
                  + "Это автоматическая рассылка, отвечать на нее не нужно."
          ));
          return user;
        }
    );
  }

  @Override
  @Transactional
  public void save(UserRequest userRequest) {
    User user = new User().toBuilder()
        .email(userRequest.getEmail())
        .name(userRequest.getName())
        .lastName(userRequest.getLastName())
        .isAdmin(false)
        .build();
    userRepository.save(user);
  }

  @Override
  public void addOrderToUser(List<Order> orders, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found with id - " + userId));
    user.setOrders(orderRepository.saveAll(orders));
    log.error(user.getOrders().toString());
    userRepository.save(user);
  }

  @Override
  public List<User> getAllUser() {
    return userRepository.findAllByIsAdmin(false);
  }

}
