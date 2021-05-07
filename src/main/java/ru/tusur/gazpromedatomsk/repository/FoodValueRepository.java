package ru.tusur.gazpromedatomsk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.FoodValue;

@Repository
public interface FoodValueRepository extends JpaRepository<FoodValue, Long> {

}
