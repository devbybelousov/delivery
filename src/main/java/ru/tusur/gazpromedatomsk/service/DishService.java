package ru.tusur.gazpromedatomsk.service;

import ru.tusur.gazpromedatomsk.model.Description;
import ru.tusur.gazpromedatomsk.model.Dish;
import ru.tusur.gazpromedatomsk.model.FoodValue;

public interface DishService {

  Dish saveDish(Dish dish);

  Dish getDish(String id);

  Boolean existDish(String id);

  FoodValue saveFoodValue(FoodValue foodValue);

  Description saveDescription(Description description);
}
