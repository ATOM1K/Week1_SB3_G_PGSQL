@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       AddressRepository addressRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.userMapper = userMapper;
    }

    // Создание пользователя
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Сохраняем пользователя и его адреса
        User savedUser = userRepository.save(user);

        // Обновляем связь в адресах
        if (userDto.getAddresses() != null) {
            for (Address address : savedUser.getAddresses()) {
                address.setUser(savedUser);
            }
            addressRepository.saveAll(savedUser.getAddresses());
        }

        return userMapper.toDto(savedUser);
    }

    // Получение пользователя по ID
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    // Обновление пользователя
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setAge(userDto.getAge());
        existingUser.setUpdatedAt(LocalDateTime.now());

        // Обработка адресов
        if (userDto.getAddresses() != null) {
            // Удаляем старые адреса
            addressRepository.deleteByUserId(id);

            // Создаём новые адреса
            List<Address> addresses = userDto.getAddresses().stream()
                    .map(addressDto -> {
                        Address address = userMapper.toAddressEntity(addressDto);
                        address.setUser(existingUser);
                        return address;
                    }).collect(Collectors.toList());

            existingUser.setAddresses(addresses);
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        addressRepository.deleteByUserId(id); // Сначала удаляем связанные адреса
        userRepository.deleteById(id);     // Затем удаляем пользователя
    }

    // Получение всех пользователей
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}