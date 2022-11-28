package com.example.addressfilter.service;

import com.example.addressfilter.data.AddressFilterDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoadNameService {

    public AddressFilterDto addressFiltering(String address) {
        // 정규표현식으로 도로명 필터링

        return null;
    }

    public List<AddressFilterDto> addressListFiltering(ArrayList<String> addressList) {
        return null;
    }
}
