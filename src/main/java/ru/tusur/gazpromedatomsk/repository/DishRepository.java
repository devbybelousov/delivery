package ru.tusur.gazpromedatomsk.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, String> {
  List<Dish> findAllByOrderByTypeAsc();
}
