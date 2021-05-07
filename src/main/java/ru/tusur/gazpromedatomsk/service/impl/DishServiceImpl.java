package ru.tusur.gazpromedatomsk.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tusur.gazpromedatomsk.exception.NotFoundException;
import ru.tusur.gazpromedatomsk.model.Description;
import ru.tusur.gazpromedatomsk.model.Dish;
import ru.tusur.gazpromedatomsk.model.FoodValue;
import ru.tusur.gazpromedatomsk.repository.DescriptionRepository;
import ru.tusur.gazpromedatomsk.repository.DishRepository;
import ru.tusur.gazpromedatomsk.repository.FoodValueRepository;
import ru.tusur.gazpromedatomsk.service.DishService;

@Service
@AllArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

  private final DishRepository dishRepository;
  private final FoodValueRepository foodValueRepository;
  private final DescriptionRepository descriptionRepository;

  @Override
  public Dish saveDish(Dish dish) {
    return dishRepository.save(dish);
  }

  @Override
  public Dish getDish(String id) {
    return dishRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Dish not found with id - " + id));
  }

  @Override
  public Boolean existDish(String id) {
    return dishRepository.existsById(id);
  }

  @Override
  public FoodValue saveFoodValue(FoodValue foodValue) {
    return foodValueRepository.save(foodValue);
  }

  @Override
  public Description saveDescription(Description description) {
    return descriptionRepository.save(description);
  }

}
