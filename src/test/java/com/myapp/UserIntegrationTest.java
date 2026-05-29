@SpringBootTest
@Testcontainers
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Test
    void testCreateAndGetUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setAge(30);

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("123 Main St");
        addressDto.setCity("New York");
        addressDto.setCountry("USA");

        userDto.setAddresses(List.of(addressDto));

        // When
        UserDto createdUser = userService.createUser(userDto);

        // Then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("John Doe");
        assertThat(createdUser.getEmail()).isEqualTo("john@example.com");
        assertThat(createdUser.getAge()).isEqualTo(30);
        assertThat(createdUser.getAddresses()).hasSize(1);
        assertThat(createdUser.getAddresses().get(0).getStreet()).isEqualTo("123 Main St");
    }

    @Test
    void testGetAllUsers() {
        // Given — создаём двух пользователей
        UserDto user1 = new UserDto();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setAge(25);

        UserDto user2 = new UserDto();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setAge(35);

        userService.createUser(user1);
        userService.createUser(user2);

        // When
        List<UserDto> allUsers = userService.getAllUsers();

        // Then
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers)
                .extracting(UserDto::getName)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void testUpdateUser() {
        // Given — создаём пользователя
        UserDto userDto = new UserDto();
        userDto.setName("Initial Name");
        userDto.setEmail("initial@example.com");
        userDto.setAge(40);

        UserDto createdUser = userService.createUser(userDto);
        Long userId = createdUser.getId();

        // Prepare update data
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");
        updateDto.setAge(45);

        AddressDto newAddress = new AddressDto();
        newAddress.setStreet("456 Oak Ave");
        newAddress.setCity("Boston");
        newAddress.setCountry("USA");
        updateDto.setAddresses(List.of(newAddress));

        // When
        UserDto updatedUser = userService.updateUser(userId, updateDto);

        // Then
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getAge()).isEqualTo(45);
        assertThat(updatedUser.getAddresses()).hasSize(1);
        assertThat(updatedUser.getAddresses().get(0).getStreet()).isEqualTo("456 Oak Ave");
    }

    @Test
    void testDeleteUser() {
        // Given — создаём пользователя
        UserDto userDto = new UserDto();
        userDto.setName("To Be Deleted");
        userDto.setEmail("delete@example.com");

        UserDto createdUser = userService.createUser(userDto);
        Long userId = createdUser.getId();

        // When
        userService.deleteUser(userId);

        // Then — проверяем, что пользователь больше не существует
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}