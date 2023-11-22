package com.example.testapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InviteMapper {
    InviteMapper INSTANCE = Mappers.getMapper(InviteMapper.class);

}
