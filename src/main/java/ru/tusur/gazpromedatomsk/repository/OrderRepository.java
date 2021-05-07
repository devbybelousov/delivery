package ru.tusur.gazpromedatomsk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
