package com.myapp.repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Дополнительные методы
}