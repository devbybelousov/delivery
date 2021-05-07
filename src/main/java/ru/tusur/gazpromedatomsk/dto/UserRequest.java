package ru.tusur.gazpromedatomsk.dto;

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
  private String name;
  @NotBlank
  @Size(max = 100)
  private String lastName;
  @NotBlank
  @Size(max = 100)
  @Email
  private String email;
}
