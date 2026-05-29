package com.myapp.repository;

import com.myapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setAge(30);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void shouldFindUserById() {
        // Given
        User user = new User();
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        // When
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("Jane Smith");
    }

    @Test
    void shouldFindUserByName() {
        // Given
        User user1 = new User();
        user1.setName("Alice Johnson");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setName("Bob Wilson");
        user2.setEmail("bob@example.com");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // When
        var users = userRepository.findByNameContaining("Alice");

        // Then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).contains("Alice");
    }

    @Test
    void shouldDeleteUser() {
        // Given
        User user = new User();
        user.setName("To Be Deleted");
        user.setEmail("delete@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        // When
        userRepository.deleteById(savedUser.getId());

        // Then
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }
}