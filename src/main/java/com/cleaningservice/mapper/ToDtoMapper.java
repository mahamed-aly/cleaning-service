package com.cleaningservice.mapper;

public interface ToDtoMapper<Entity, Dto> {
    Dto toDto(Entity entity);
}
