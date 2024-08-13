package com.nerdysoft.library.service.dto;

import com.nerdysoft.library.validation.AuthorName;
import com.nerdysoft.library.validation.BookTitle;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

  private UUID id;

  @BookTitle private String title;

  @AuthorName private String author;
  private Integer amount;
}
