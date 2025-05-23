package com.auta.server.adapter.out.persistence.user;

import com.auta.server.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);
//
//
//    public static UserEntity toEntity(User user) {
//        return UserEntity.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .username(user.getUsername())
//                .address(user.getAddress())
//                .phoneNumber(user.getPhoneNumber())
//                .build();
//    }
//
//    public static User toDomain(UserEntity entity) {
//        return User.builder()
//                .id(entity.getId())
//                .email(entity.getEmail())
//                .password(entity.getPassword())
//                .username(entity.getUsername())
//                .address(entity.getAddress())
//                .phoneNumber(entity.getPhoneNumber())
//                .build();
//    }
}
