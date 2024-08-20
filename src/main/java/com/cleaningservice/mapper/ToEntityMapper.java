package com.cleaningservice.mapper;

public interface ToEntityMapper<Entity, Dto> {
    Entity toEntity(Dto dto);
}
