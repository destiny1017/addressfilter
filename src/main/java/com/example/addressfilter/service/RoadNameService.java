package com.example.addressfilter.service;

import com.example.addressfilter.data.AddressFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RoadNameService {

    private final int slicePoint = 5000;
    private final int maxThread = 20;
    private final FilterService filterService;
    private ExecutorService executorService;

    public AddressFilterDto addressFiltering(String address) {
        return filterService.addressFiltering(address);
    }

    public List<AddressFilterDto> addressListFiltering(List<String> addressList) {
        if(addressList.size() < slicePoint) {
            return filterService.addressListFiltering(addressList);
        } else {
            return addressListFilteringThread(addressList);
        }
    }

    private List<AddressFilterDto> addressListFilteringThread(List<String> addressList) {
        if(executorService == null) {
            executorService = Executors.newFixedThreadPool(maxThread);
        }
        // 분할한 리스트 순서대로 재조립하기위해 map사용
        HashMap<Integer, List<AddressFilterDto>> result = new HashMap<>();
        List<String> addressListTmp;
        List<AddressFilterDto> totalResult = new ArrayList<>();
        int task = (addressList.size() / slicePoint) + 1;
        for(int i = 1; i <= task; i++) {
            if(i == task) {
                addressListTmp = addressList.subList((i-1) * slicePoint, addressList.size()-1);
            } else {
                addressListTmp = addressList.subList((i-1) * slicePoint, i*slicePoint);
            }
            FilterServiceThread thread =
                    new FilterServiceThread(filterService.getRoadNameData(),filterService.getSpecialRoadNameData());
            thread.setThreadData(addressListTmp, result, i, executorService);
            executorService.submit(thread);
        }

        // 모든 스레드의 작업 종료 대기..
        try {
            while(!executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {}
        } catch(Exception e) {
            e.printStackTrace();
        }

        result.forEach((k,v) -> totalResult.addAll(v));

        return totalResult;
    }
}
