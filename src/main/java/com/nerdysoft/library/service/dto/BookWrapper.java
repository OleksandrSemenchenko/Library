package com.nerdysoft.library.service.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class BookWrapper {

  private String userName;
  private UUID userId;
  private List<BookDto> books;
}
