package com.nerdysoft.library.service.impl;

import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.MAX_USER_BOOKS_QUANTITY;
import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.USER_BOOK_RELATION_ALREADY_EXISTS;
import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.USER_BORROWED_BOOKS;

import com.nerdysoft.library.exceptionhandler.ExceptionMessages;
import com.nerdysoft.library.exceptionhandler.exceptions.BookAmountConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserBookRelationConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserNotFoundException;
import com.nerdysoft.library.mapper.UserMapper;
import com.nerdysoft.library.repository.UserRepository;
import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.UserService;
import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.UserDto;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private static final String GET_USER_BY_ID_CACHE = "getUserByIdCache";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BookService bookService;

  @Value("${application.max-book-quantity-for-user}")
  private int maxBookQuantityForUser;

  @Override
  @Transactional
  @CachePut(value = GET_USER_BY_ID_CACHE, key = "#userDto.id")
  public UserDto updateUser(UserDto userDto) {
    User user = findUserById(userDto.getId());
    User updatedUser = userMapper.mergeWithDto(userDto, user);
    User savedUser = userRepository.save(updatedUser);
    return userMapper.toDto(savedUser);
  }

  @Override
  @Transactional
  @CacheEvict(value = GET_USER_BY_ID_CACHE, allEntries = true)
  public UserDto createUser(UserDto userDto) {
    userDto.setId(null);
    userDto.setMembershipDate(null);
    User newUser = userMapper.toEntity(userDto);
    User savedUser = userRepository.save(newUser);
    return userMapper.toDto(savedUser);
  }

  /**
   * Deletes a user that has no borrowed books.
   *
   * @param userId - a user ID
   */
  @Override
  @Transactional
  @CacheEvict(value = GET_USER_BY_ID_CACHE, key = "#userId")
  public void deleteUser(UUID userId) {
    User user = findUserById(userId);
    int userBookQuantity = userRepository.countBookRelationsByUserId(userId.toString());

    if (userBookQuantity != 0) {
      log.debug(USER_BORROWED_BOOKS.formatted(userId, userBookQuantity));
      throw new UserBookRelationConflictException(
          USER_BORROWED_BOOKS.formatted(userId, userBookQuantity));
    }
    userRepository.delete(user);
  }

  /**
   * Creates a relation between a user and a book if the book amount is greater than zero and the
   * user has fewer books than the maximum allowed book quantity. When the relation is created the
   * book amount decreases by one.
   *
   * @param userId - a user ID
   * @param bookId - a book ID
   */
  @Override
  @Transactional
  @CacheEvict(value = GET_USER_BY_ID_CACHE, key = "#userId")
  public void borrowBookByUser(UUID userId, UUID bookId) {
    verifyIfUserBookRelationAlreadyExists(userId, bookId);
    verifyIfUserExists(userId);
    BookDto bookDto = bookService.getBookById(bookId);
    int userBookQuantity = userRepository.countBookRelationsByUserId(userId.toString());

    if (bookDto.getAmount() == 0) {
      log.debug(ExceptionMessages.NO_BOOKS);
      throw new BookAmountConflictException(ExceptionMessages.NO_BOOKS);
    } else if (userBookQuantity >= maxBookQuantityForUser) {
      log.debug(MAX_USER_BOOKS_QUANTITY.formatted(maxBookQuantityForUser));
      throw new UserBookRelationConflictException(
          MAX_USER_BOOKS_QUANTITY.formatted(maxBookQuantityForUser));
    } else {
      createUserBookRelation(userId, bookId);
      bookService.decreaseBookAmountByOne(bookId);
    }
  }

  private void verifyIfUserBookRelationAlreadyExists(UUID userId, UUID bookId) {
    if (userRepository.existsByIdAndBooksId(userId, bookId)) {
      throw new UserBookRelationConflictException(
          USER_BOOK_RELATION_ALREADY_EXISTS.formatted(userId, bookId));
    }
  }

  private void verifyIfUserExists(UUID userId) {
    findUserById(userId);
  }

  private void createUserBookRelation(UUID userId, UUID bookId) {
    String relationId = UUID.randomUUID().toString();
    userRepository.createUserBookRelation(userId.toString(), bookId.toString(), relationId);
  }

  @Override
  @Cacheable(value = GET_USER_BY_ID_CACHE, key = "#userId")
  public UserDto getUserById(UUID userId) {
    User user = findUserById(userId);
    return userMapper.toDto(user);
  }

  private User findUserById(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new UserNotFoundException(
                    ExceptionMessages.USER_NOT_FOUND_BY_ID.formatted(userId)));
  }
}
