package ru.tusur.gazpromedatomsk.repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

  Optional<Menu> findByCreatedAt(Date createdAt);
}
