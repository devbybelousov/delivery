package ru.tusur.gazpromedatomsk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long menuId;

  @ManyToMany()
  @JoinTable(name = "menu_dish",
      joinColumns = @JoinColumn(name = "menuId"),
      inverseJoinColumns = @JoinColumn(name = "dishId")
  )
  private List<Dish> dishes;

  @JsonIgnore
  @Temporal(TemporalType.DATE)
  private Date createdAt;
}
