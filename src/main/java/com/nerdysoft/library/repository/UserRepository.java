package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {}
