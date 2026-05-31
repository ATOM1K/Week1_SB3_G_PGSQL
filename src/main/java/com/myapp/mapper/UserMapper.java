package com.myapp.mapper;

import com.myapp.dto.AddressDto;
import com.myapp.dto.UserDto;
import com.myapp.entity.Address;
import com.myapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "addresses", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "addresses", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    User toEntity(UserDto userDto);

    List<UserDto> toUserDtos(List<User> users);

    @Mappings({
            @Mapping(target = "user", ignore = true)
    })
    AddressDto toAddressDto(Address address);

    @Mappings({
            @Mapping(target = "user", ignore = true)
    })
    Address toAddressEntity(AddressDto addressDto);

    List<AddressDto> toAddressDtos(List<Address> addresses);

    List<Address> toAddressEntities(List<AddressDto> addressDtos);

    /**
     * Обновление полей сущности User из DTO, игнорируя связи и временные метки.
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "addresses", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}