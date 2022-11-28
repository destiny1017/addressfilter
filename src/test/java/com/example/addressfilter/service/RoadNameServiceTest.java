package com.example.addressfilter.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoadNameServiceTest {

    @Test
    void addressFiltering() {

    }

    @Test
    void addressListFiltering() {
        ArrayList<String> addressList = new ArrayList<>();
        addressList.add("성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!");
        addressList.add("마포구 도화-2길 코끼리분식");
        addressList.add("강서구 곰달래로 35길 31, 와이앤비");
        addressList.add("강남구 테헤란로 326");

    }
}