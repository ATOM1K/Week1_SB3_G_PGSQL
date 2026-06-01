package com.myapp.service;

import com.myapp.dto.UserDto;
import com.myapp.entity.User;
import com.myapp.mapper.UserMapper;
import com.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Получает всех пользователей из БД и преобразует их в DTO
     * @return список UserDto
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDtos(users);
    }

    /**
     * Находит пользователя по ID и преобразует в DTO
     * @param id ID пользователя
     * @return UserDto
     * @throws RuntimeException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    /**
     * Создаёт нового пользователя
     * @param userDto данные нового пользователя
     * @return созданный UserDto с заполненным ID
     */
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // Преобразуем DTO в сущность
        User user = userMapper.toEntity(userDto);

        // Устанавливаем временные метки
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(null);

        // Сохраняем в БД
        User savedUser = userRepository.save(user);

        // Возвращаем DTO с заполненным ID
        return userMapper.toDto(savedUser);
    }

    /**
     * Обновляет существующего пользователя
     * @param id ID пользователя для обновления
     * @param userDto новые данные пользователя
     * @return обновлённый UserDto
     * @throws RuntimeException если пользователь не найден
     */
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        // Находим существующего пользователя
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Обновляем поля из DTO (кроме ID и временных меток)
        userMapper.updateUserFromDto(userDto, existingUser);

        // Обновляем временную метку
        existingUser.setUpdatedAt(java.time.LocalDateTime.now());

        // Сохраняем изменения в БД
        User updatedUser = userRepository.save(existingUser);

        // Возвращаем обновлённый DTO
        return userMapper.toDto(updatedUser);
    }

    /**
     * Удаляет пользователя по ID
     * @param id ID пользователя для удаления
     * @throws RuntimeException если пользователь не найден
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}