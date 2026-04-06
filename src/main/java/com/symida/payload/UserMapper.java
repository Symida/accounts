package com.symida.payload;

import com.symida.entity.User;
import com.symida.payload.request.UserCreateRequest;
import com.symida.payload.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

	UserResponse entityToResponse(User user);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "role", ignore = true)
	User requestToEntity(UserCreateRequest request);

}
