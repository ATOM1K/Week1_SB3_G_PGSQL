package com.myapp.mapper;

import com.myapp.dto.AddressDto;
import com.myapp.dto.UserDto;
import com.myapp.entity.Address;
import com.myapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует DTO в сущность User, игнорируя коллекцию адресов.
     * Адреса будут обрабатываться отдельно.
     */
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserDto userDto);

    /**
     * Обновляет существующую сущность User данными из DTO.
     * Коллекция адресов игнорируется — будет обновляться отдельно.
     */
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true) // ID не должен меняться при обновлении
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);

    /**
     * Преобразует сущность User в DTO, включая связанные адреса.
     */
    @Mapping(source = "addresses", target = "addresses")
    UserDto toDto(User user);

    /**
     * Маппинг списка сущностей User в список DTO.
     */
    List<UserDto> toUserDtos(List<User> users);

    /**
     * Преобразует DTO адреса в сущность Address.
     */
    Address toAddressEntity(AddressDto addressDto);

    /**
     * Преобразует сущность Address в DTO адреса.
     */
    AddressDto toAddressDto(Address address);

    /**
     * Маппинг списка DTO адресов в список сущностей Address.
     */
    List<Address> toAddressEntities(List<AddressDto> addressDtos);

    /**
     * Маппинг списка сущностей Address в список DTO адресов.
     */
    List<AddressDto> toAddressDtos(List<Address> addresses);
}