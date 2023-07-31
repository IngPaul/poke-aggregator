package com.alpha.pocfilter.controller.exampleDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String addressId;
    private String detail;
    private PhoneDTO phone;
}
