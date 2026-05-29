package com.myapp.repository;

import com.myapp.entity.Address;
import com.myapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void shouldSaveAddressWithUser() {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        Address address = new Address();
        address.setUser(savedUser);
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setCountry("USA");

        // When
        Address savedAddress = addressRepository.save(address);

        // Then
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(savedAddress.getCity()).isEqualTo("New York");
    }

    @Test
    void shouldFindAddressesByUserId() {
        // Given
        User user = new User();
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        Address address1 = new Address();
        address1.setUser(savedUser);
        address1.setStreet("456 Oak Ave");
        address1.setCity("Boston");
        address1.setCountry("USA");

        Address address2 = new Address();
        address2.setUser(savedUser);
        address2.setStreet("789 Pine Rd");
        address2.setCity("Boston");
        address2.setCountry("USA");

        entityManager.persist(address1);
        entityManager.persist(address2);
        entityManager.flush();

        // When
        List<Address> addresses = addressRepository.findByUserId(savedUser.getId());

        // Then
        assertThat(addresses).hasSize(2);
        assertThat(addresses)
                .extracting(Address::getCity)
                .containsOnly("Boston");
    }

    @Test
    void shouldDeleteAddressesByUserId() {
        // Given
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        Address address = new Address();
        address.setUser(savedUser);
        address.setStreet("Temporary St");
        address.setCity("Test City");
        address.setCountry("Test Country");
        Address savedAddress = entityManager.persistAndFlush(address);

        // When
        addressRepository.deleteByUserId(savedUser.getId());

        // Then
        assertThat(addressRepository.findByUserId(savedUser.getId())).isEmpty();
        assertThat(entityManager.find(Address.class, savedAddress.getId())).isNull();
    }

    @Test
    void shouldUpdateAddress() {
        // Given
        User user = new User();
        user.setName("Update Test");
        user.setEmail("update@example.com");
        User savedUser = entityManager.persistAndFlush(user);

        Address address = new Address();
        address.setUser(savedUser);
        address.setStreet("Old Street");
        address.setCity("Old City");
        address.setCountry("Old Country");
        Address savedAddress = addressRepository.save(address);

        // When
        savedAddress.setStreet("New Street");
        savedAddress.setCity("New City");
        Address updatedAddress = addressRepository.save(savedAddress);

        // Then
        assertThat(updatedAddress.getStreet()).isEqualTo("New Street");
        assertThat(updatedAddress.getCity()).isEqualTo("New City");
    }
}