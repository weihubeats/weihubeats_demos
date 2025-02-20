package com.xiaozou.security.demo.dao;

import com.xiaozou.security.demo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : wh
 * @date : 2025/2/19 15:43
 * @description:
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
