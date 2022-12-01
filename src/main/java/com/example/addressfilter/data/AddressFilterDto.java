package com.example.addressfilter.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AddressFilterDto {
    private String roadName;
    private String inputAddress;
    private String existRoadName;
    private List<String> addressList;

    @Builder
    public AddressFilterDto(String roadName, String inputAddress, String existRoadName, List<String> addressList) {
        this.roadName = roadName;
        this.inputAddress = inputAddress;
        this.existRoadName = existRoadName;
        this.addressList = addressList;
    }
}
