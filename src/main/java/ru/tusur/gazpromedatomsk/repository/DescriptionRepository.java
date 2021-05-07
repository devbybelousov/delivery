package ru.tusur.gazpromedatomsk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tusur.gazpromedatomsk.model.Description;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {

}
