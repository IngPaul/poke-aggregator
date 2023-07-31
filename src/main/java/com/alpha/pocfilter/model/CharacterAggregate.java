package com.alpha.pocfilter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class CharacterAggregate {
    private Long id;
    private String name;
    private String status;
    private String gender;
    private String originName;
    private String location;
    private String images;
    private Boolean isPresent;
}
