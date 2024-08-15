package com.nerdysoft.library.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.exceptionhandler.exceptions.BookAmountConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserBookRelationConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserNotFoundException;
import com.nerdysoft.library.mapper.UserMapper;
import com.nerdysoft.library.repository.UserRepository;
import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.UserDto;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final UUID NOT_EXISTING_USER_ID = UUID.randomUUID();
  private static final UUID BOOK_ID = UUID.randomUUID();
  private static final String USER_NAME = "John Doe";

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @Mock private BookService bookService;

  @BeforeEach
  void setUp() {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    ReflectionTestUtils.setField(userService, "userMapper", userMapper);
    ReflectionTestUtils.setField(userService, "maxBookQuantityForUser", 10);
  }

  @Test
  void updateUser_shouldUpdateUser_whenUserIsInDb() {
    UserDto userDto = TestDataGenerator.generateUserDto();
    User user = TestDataGenerator.generateUser();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    UserDto updatedUser = userService.updateUser(userDto);

    assertEquals(userDto, updatedUser);
  }

  @Test
  void updateUser_shouldThrowException_whenNoUserInDb() {
    UserDto userDto = TestDataGenerator.generateUserDto();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));
  }

  @Test
  void createUser_shouldCreate_whenRequested() {
    UserDto userDto = TestDataGenerator.generateUserDto();
    User user = TestDataGenerator.generateUser();

    when(userRepository.save(any(User.class))).thenReturn(user);

    UserDto createdUser = userService.createUser(userDto);

    UserDto expectedUserDto = TestDataGenerator.generateUserDto();
    assertEquals(expectedUserDto, createdUser);
  }

  @Test
  void deleteUser_shouldDeleteUser_whenUserIsInDb() {
    User user = TestDataGenerator.generateUser();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    userService.deleteUser(USER_ID);

    verify(userRepository).delete(user);
  }

  @Test
  void deleteUser_shouldThrowException_whenUserHasBorrowedBooks() {
    User user = TestDataGenerator.generateUser();
    int userBorrowedBook = 1;

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userRepository.countBookRelationsByUserId(USER_ID.toString()))
        .thenReturn(userBorrowedBook);

    assertThrows(UserBookRelationConflictException.class, () -> userService.deleteUser(USER_ID));
  }

  @Test
  void deleteUser_shouldThrowException_whenNoUserInDb() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(USER_ID));
  }

  @Test
  void borrowBookByUser_shouldCreateUserBookRelation_whenRequested() {
    User user = TestDataGenerator.generateUser();
    BookDto bookDto = TestDataGenerator.generateBookDto();
    int userBooksQuantity = 1;

    when(userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID)).thenReturn(false);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(bookService.getBookById(BOOK_ID)).thenReturn(bookDto);
    when(userRepository.countBookRelationsByUserId(USER_ID.toString()))
        .thenReturn(userBooksQuantity);

    userService.borrowBookByUser(USER_ID, BOOK_ID);

    verify(userRepository).createUserBookRelation(anyString(), anyString(), anyString());
    verify(bookService).decreaseBookAmountByOne(BOOK_ID);
  }

  @Test
  void borrowBookByUser_shouldThrowException_whenUserHasMaxBooksQuantity() {
    User user = TestDataGenerator.generateUser();
    BookDto bookDto = TestDataGenerator.generateBookDto();
    int maxBooksQuantity = 10;

    when(userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID)).thenReturn(false);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(bookService.getBookById(BOOK_ID)).thenReturn(bookDto);
    when(userRepository.countBookRelationsByUserId(USER_ID.toString()))
        .thenReturn(maxBooksQuantity);

    assertThrows(
        UserBookRelationConflictException.class,
        () -> userService.borrowBookByUser(USER_ID, BOOK_ID));
  }

  @Test
  void borrowBookByUser_shouldThrowException_whenNoAvailableBooksInDb() {
    User user = TestDataGenerator.generateUser();
    BookDto bookDto = TestDataGenerator.generateBookDto();
    bookDto.setAmount(0);
    int userBooksQuantity = 1;

    when(userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID)).thenReturn(false);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(bookService.getBookById(BOOK_ID)).thenReturn(bookDto);
    when(userRepository.countBookRelationsByUserId(USER_ID.toString()))
        .thenReturn(userBooksQuantity);

    assertThrows(
        BookAmountConflictException.class, () -> userService.borrowBookByUser(USER_ID, BOOK_ID));
  }

  @Test
  void borrowBookByUser_shouldThrowException_whenNoUserInDb() {
    when(userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID)).thenReturn(false);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.borrowBookByUser(USER_ID, BOOK_ID));
  }

  @Test
  void borrowBookByUser_shouldThrowException_whenUserBookRelationAlreadyExists() {
    when(userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID)).thenReturn(true);

    assertThrows(
        UserBookRelationConflictException.class,
        () -> userService.borrowBookByUser(USER_ID, BOOK_ID));
  }

  @Test
  void getUserById_shouldReturnUser_whenUserIsInDb() {
    User user = TestDataGenerator.generateUser();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    UserDto retrivedUserDto = userService.getUserById(USER_ID);

    assertEquals(user.getId(), retrivedUserDto.getId());
    assertEquals(user.getName(), retrivedUserDto.getName());
    assertEquals(user.getMembershipDate(), retrivedUserDto.getMembershipDate());
  }

  @Test
  void getUserById_shouldReturnUser_whenNoUserInDb() {
    Mockito.when(userRepository.findById(NOT_EXISTING_USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById(NOT_EXISTING_USER_ID));
  }
}
