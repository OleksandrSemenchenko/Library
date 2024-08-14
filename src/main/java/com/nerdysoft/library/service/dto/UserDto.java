package com.nerdysoft.library.service.dto;

import com.nerdysoft.library.validation.Name;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private UUID id;

  @Name private String name;
  private LocalDate membershipDate;
}
