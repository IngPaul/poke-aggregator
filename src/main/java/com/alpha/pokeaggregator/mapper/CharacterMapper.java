package com.alpha.pokeaggregator.mapper;

import com.alpha.pokeaggregator.model.Character;
import com.alpha.pokeaggregator.model.CharacterAggregate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CharacterMapper {
    CharacterMapper INSTANCE = Mappers.getMapper(CharacterMapper.class);
    @Mapping(source = "origin.name", target = "originName")
    @Mapping(source = "location.name", target = "location")
    public CharacterAggregate parseOfCharacter(Character character);
}
