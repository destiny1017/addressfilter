package com.example.addressfilter.service;

import com.example.addressfilter.data.AddressFilterDto;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class FilterServiceThread extends FilterService implements Runnable {

    private int idx;
    private HashMap<Integer, List<AddressFilterDto>> result;
    private List<String> addressList;
    private ExecutorService executorService;

    public FilterServiceThread(Set<String> roadNameData, Map<String, String> specialRoadNameData) {
        super(roadNameData, specialRoadNameData);
    }

    public void setThreadData(List<String> addressList, HashMap<Integer, List<AddressFilterDto>> result, int idx,
                              ExecutorService executorService) {
        this.addressList = addressList;
        this.result = result;
        this.idx = idx;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        ArrayList<AddressFilterDto> addressDtoList = super.addressListFiltering(addressList);
        result.put(idx, addressDtoList);
        executorService.shutdown();
    }
}
