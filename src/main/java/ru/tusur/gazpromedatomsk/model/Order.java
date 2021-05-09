package ru.tusur.gazpromedatomsk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long orderId;

  @ApiModelProperty(notes = "Идентификатор блюда.",
      example = "234", required = true)
  private String dishId;

  @ApiModelProperty(notes = "Количество блюда.",
      example = "2", required = true)
  private int count;

  @JsonIgnore
  @Temporal(TemporalType.DATE)
  private Date createdAt;
}
