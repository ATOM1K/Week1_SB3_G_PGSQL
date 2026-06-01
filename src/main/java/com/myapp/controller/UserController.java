package com.myapp.controller;

import com.myapp.dto.UserDto;
import com.myapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST‑контроллер для управления пользователями
 * Предоставляет API‑эндпоинты для CRUD‑операций с пользователями
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получает список всех пользователей
     * @return ResponseEntity<List<UserDto>> — список пользователей в формате DTO
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Получает пользователя по ID
     * @param id ID пользователя
     * @return ResponseEntity<UserDto> — пользователь в формате DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Создаёт нового пользователя
     * @param userDto данные нового пользователя
     * @return ResponseEntity<UserDto> — созданный пользователь с заполненным ID
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    /**
     * Обновляет существующего пользователя
     * @param id ID пользователя для обновления
     * @param userDto новые данные пользователя
     * @return ResponseEntity<UserDto> — обновлённый пользователь
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Частичное обновление пользователя (PATCH)
     * @param id ID пользователя для обновления
     * @param userDto данные для частичного обновления
     * @return ResponseEntity<UserDto> — частично обновлённый пользователь
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Удаляет пользователя по ID
     * @param id ID пользователя для удаления
     * @return ResponseEntity<Void> — пустой ответ с кодом 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}