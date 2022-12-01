package com.example.addressfilter.data;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddressFilterDto {
    private String roadName;
    private String inputAddress;
    private String existRoadName;
    private List<String> addressList = new ArrayList<>();

    @Builder
    public AddressFilterDto(String roadName, String inputAddress, String existRoadName, List<String> addressList) {
        this.roadName = roadName;
        this.inputAddress = inputAddress;
        this.existRoadName = existRoadName;
        this.addressList = addressList;
    }
}
