package com.nerdysoft.library.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nerdysoft.library.validation.BookTitle;
import com.nerdysoft.library.validation.Name;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {

  private UUID id;

  @BookTitle private String title;

  @Name private String author;

  @Positive private Integer amount;
}
