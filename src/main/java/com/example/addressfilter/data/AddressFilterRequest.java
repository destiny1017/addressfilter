package com.example.addressfilter.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class AddressFilterRequest {
    private List<String> addressList;
}
