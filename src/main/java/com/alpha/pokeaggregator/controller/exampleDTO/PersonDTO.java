package com.alpha.pokeaggregator.controller.exampleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String id;
    private String name;
    private int age;
    private List<AddressDTO> address;
}