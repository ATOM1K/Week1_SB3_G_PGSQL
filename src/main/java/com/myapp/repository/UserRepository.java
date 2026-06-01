package com.myapp.repository;

import com.myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User
 * Наследует JpaRepository, предоставляя стандартные CRUD‑операции
 * и дополнительные методы для поиска пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Находит пользователя по имени пользователя (username)
     * @param username имя пользователя
     * @return Optional<User> — пользователь, если найден, иначе пустой Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет существование пользователя по ID
     * @param id ID пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsById(Long id);

    /**
     * Находит пользователя по адресу электронной почты (email)
     * @param email адрес электронной почты
     * @return Optional<User> — пользователь, если найден, иначе пустой Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Проверяет существование пользователя с указанным именем пользователя
     * @param username имя пользователя
     * @return true, если пользователь с таким именем существует, иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет существование пользователя с указанным адресом электронной почты
     * @param email адрес электронной почты
     * @return true, если пользователь с таким email существует, иначе false
     */
    boolean existsByEmail(String email);
}