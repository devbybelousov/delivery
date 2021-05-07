package ru.tusur.gazpromedatomsk.service;

import java.util.List;
import ru.tusur.gazpromedatomsk.dto.UserRequest;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.model.User;

public interface UserService {

  void sendEmailToAllUser();

  void save(UserRequest userRequest);

  void addOrderToUser(List<Order> orders, Long userId);

  List<User> getAllUser();
}
