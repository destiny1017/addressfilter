package com.example.addressfilter.controller;

import com.example.addressfilter.data.AddressFilterDto;
import com.example.addressfilter.data.AddressFilterRequest;
import com.example.addressfilter.service.RoadNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoadNameController {

    private final RoadNameService roadNameService;

    @GetMapping("/api/address-filter")
    public AddressFilterDto filterAddress(@RequestParam String address) {
        AddressFilterDto addressFilterDto = roadNameService.addressFiltering(address);
        return addressFilterDto;
    }

    @PostMapping("/api/address-filter")
    public List<AddressFilterDto> filterAddress(@RequestBody AddressFilterRequest addressFilterRequest) {
        return roadNameService.addressListFiltering(addressFilterRequest.getAddressList());
    }

}
