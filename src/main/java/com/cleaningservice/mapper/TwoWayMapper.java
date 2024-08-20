package com.cleaningservice.mapper;

public interface TwoWayMapper<Entity, Dto> extends ToEntityMapper<Entity, Dto>, ToDtoMapper<Entity, Dto> {
}
