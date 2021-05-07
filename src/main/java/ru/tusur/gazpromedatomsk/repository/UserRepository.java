package ru.tusur.gazpromedatomsk.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByIsAdmin(Boolean isAdmin);
}
