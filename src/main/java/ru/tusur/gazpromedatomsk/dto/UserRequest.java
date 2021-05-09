package ru.tusur.gazpromedatomsk.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  @NotBlank
  @Size(max = 100)
  @ApiModelProperty(notes = "Имя пользователя.",
      example = "Иван", required = true)
  private String name;

  @NotBlank
  @Size(max = 100)
  @ApiModelProperty(notes = "Фамилия пользователя.",
      example = "Иванов", required = true)
  private String lastName;

  @NotBlank
  @Size(max = 100)
  @Email
  @ApiModelProperty(notes = "Почта пользователя.",
      example = "ivanov@example.com", required = true)
  private String email;
}
