package ru.tusur.gazpromedatomsk.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "dishes")
public class Dish {

  @Id
  private String dishId;

  private String type;

  private String title;

  private String weight;

  private int price;

  private String image;

  @OneToOne
  private FoodValue foodValue;

  @OneToMany
  private List<Description> description;
}
