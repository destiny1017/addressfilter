package com.example.addressfilter.service;

import com.example.addressfilter.data.AddressFilterDto;
import com.example.addressfilter.domain.RoadName;
import com.example.addressfilter.repository.RoadNameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoadNameServiceTest {

    @Autowired
    private RoadNameRepository roadNameRepository;

    @Test
    void addressFiltering() {

    }

    @Test
    @Transactional
    void addressListFiltering() {
//        ArrayList<String> addressList = new ArrayList<>();
//        addressList.add("성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!");
//        addressList.add("마포구 도화-2길 코끼리분식");
//        addressList.add("강서구 곰달래로 35길 31, 와이앤비");
//        addressList.add("강남구 테헤란로 326");
        ArrayList<String> addressList = sampleDataRead();

        String regex = "([가-힣][가-힣A-Za-z·\\d~\\-\\.\\s]{2,}(로|길))";

        ArrayList<AddressFilterDto> filteredAddressList = new ArrayList<>();

        for (String str : addressList) {
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(str);
            AddressFilterDto addressFilterDto = null;

            // ~로,~길 추출 정규식에 따라 추출된 문자열만큼 반복
            while(matcher.find()) {
                String fullAddress = matcher.group(1);
                String roadName = "";
                boolean existRoad = false;
                ArrayList<StringBuilder> sbList = new ArrayList<>();

                // 추출된 문자열을 공백으로 구분하고 특수문자 제거하여 도로명 탐색 리스트 담기
                Arrays.stream(fullAddress.split(" "))
                        .forEach(i -> sbList.add(new StringBuilder(i.replaceAll("[^\uAC00-\uD7A30-9a-zA-Z]", ""))));
                for (int i = 0; i < sbList.size(); i++) {
                    StringBuilder sb = sbList.get(i);
                    char lastChar = sb.charAt(sb.length()-1);
                    if('로' == lastChar) {
                        if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                            addStr(sb, i, sbList);
                        } else { // 이후 요소들에 몇번길로 끝나는 문자 있으면 붙이기
                            i = addStreet(sbList, i, sb);
                        }
                        existRoad = findRoadName(sb.toString());
                        roadName = sb.toString();
//                        System.out.println(sb + "->" + existRoad);
                        break;
                    } else if('길' == lastChar) {
                        if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                            addStr(sb, i, sbList);
                        } else { // 이전 요소에 도로로 끝나는 문자 있으면 붙이기
                            addRoad(sbList, i, sb);
                        }
                        existRoad = findRoadName(sb.toString());
                        roadName = sb.toString();
//                        System.out.println(sb + "->" + existRoad);
                        break;
                    }
                }
                addressFilterDto = AddressFilterDto.builder()
                        .inputAddress(fullAddress)
                        .roadName(roadName)
                        .existRoadName(existRoad ? "Y" : "N")
                        .build();

                System.out.println("입력값 : " + addressFilterDto.getInputAddress());
                System.out.println("도로명 : " + addressFilterDto.getRoadName());
                System.out.println("존재여부 : " + addressFilterDto.getExistRoadName());

                // 존재하는 걸로 확인되는 도로명이 발견되면 나머지는 탐색중지
                if(existRoad) break;

            }

            // ~로, ~길로 끝나는 문자열이 없는 경우
            if(addressFilterDto == null) {
                addressFilterDto = AddressFilterDto.builder()
                        .inputAddress(str)
                        .roadName("")
                        .existRoadName("N")
                        .build();
            }

            filteredAddressList.add(addressFilterDto);
            System.out.println("----------------------------------");
        }

    }

    private void addRoad(ArrayList<StringBuilder> sbList, int i, StringBuilder sb) {
        if(i != 0) {
            StringBuilder prevStr = sbList.get(i - 1);
            if('로' == prevStr.charAt(prevStr.length() - 1)) {
                sb.insert(0, prevStr);
            }
        }
    }

    private int addStreet(ArrayList<StringBuilder> sbList, int i, StringBuilder sb) {
        if(i < sbList.size()-1) {
            StringBuilder nextStr = sbList.get(i + 1);
            if('길' == nextStr.charAt(nextStr.length() - 1)) {
                sb.append(nextStr);
                i++; // 길은 이미 썼으니 i증가 시켜서 건너뛰기
            }
        }
        return i;
    }

    private void addStr(StringBuilder sb, int i, ArrayList<StringBuilder> sbList) {
        int cnt = 1;
        while(sb.length() < 3) {
            if(i - cnt < 0) break;
            sb.insert(0, sbList.get(i - cnt));
            cnt++;
        }
    }


    public boolean findRoadName(String roadName) {
        Optional<RoadName> findRoad = roadNameRepository.findById(roadName);
        if(findRoad.isPresent()) return true;
        else return false;
    }

    public ArrayList<String> sampleDataRead() {
        long start;
        long end;
        ArrayList<String> addressList = new ArrayList<>();
        try {
            String rootPath = System.getProperty("user.dir");
            File file = new File(rootPath + "/data/샘플주소.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while(true) {
                String line = br.readLine();
                if(line == null) break;
                addressList.add(line);
            }
//        addressMap.keySet().stream().forEach(v -> System.out.println(v));
//        System.out.println(addressMap.size());
        } catch(Exception e) {

        }
        return addressList;
    }
}