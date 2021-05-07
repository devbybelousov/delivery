package ru.tusur.gazpromedatomsk.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.tusur.gazpromedatomsk.dto.UserRequest;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.service.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void createUser(@RequestBody @Valid UserRequest userRequest) {
    userService.save(userRequest);
  }

  @PostMapping("/order")
  @ResponseStatus(HttpStatus.OK)
  public void addOrder(@RequestBody List<Order> orders, @RequestParam Long userId) {
    userService.addOrderToUser(orders, userId);
  }

}
