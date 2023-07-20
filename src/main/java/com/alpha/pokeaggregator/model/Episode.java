package com.alpha.pokeaggregator.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Episode {
    private Long id;
    private String name;
    private String airDate;
    private String episode;
    private List<String> characters;
}
