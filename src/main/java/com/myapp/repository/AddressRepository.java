@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Поиск всех адресов пользователя
    List<Address> findByUserId(Long userId);

    // Удаление всех адресов пользователя
    @Modifying
    @Query("DELETE FROM Address a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}