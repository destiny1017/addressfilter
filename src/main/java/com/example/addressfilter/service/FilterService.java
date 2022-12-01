package com.example.addressfilter.service;

import com.example.addressfilter.data.AddressFilterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Getter
@RequiredArgsConstructor
public class FilterService {

    private final Set<String> roadNameData;
    private final Map<String, String> specialRoadNameData;

    // 여러주소 필터링
    public ArrayList<AddressFilterDto> addressListFiltering(List<String> addressList) {
        ArrayList<AddressFilterDto> filteredAddressList = new ArrayList<>();

        for (String str : addressList) {
            AddressFilterDto addressFilterDto = addressFiltering(str);
            filteredAddressList.add(addressFilterDto);
        }
        return filteredAddressList;
    }

    // 단일주소 필터링
    public AddressFilterDto addressFiltering(String str) {
        String regex = "([가-힣][가-힣A-Za-z·\\d~\\-\\.\\s]{2,}(로|길|\\d))";
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
                    .forEach(i -> sbList.add(new StringBuilder(i.replaceAll("[^가-힣0-9a-zA-Z]", ""))));
            for (int i = 0; i < sbList.size(); i++) {
                StringBuilder sb = sbList.get(i);
                char lastChar = sb.charAt(sb.length()-1);
                boolean isRoad = false;
                boolean nextNum = false;
                if('로' == lastChar) {
                    isRoad = true;
                    if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                        addStr(sb, i, sbList);
                    } else { // 이후 요소들에 '~길'로 끝나는 문자 있으면 붙이기
//                        char prevStr = sbList.get(i + 1).charAt(sbList.get(i + 1).length() - 1);
//                        if(((int) prevStr) >= 48 && ((int) prevStr) <= 57) {
//                            nextNum = true;
//                        }
                        addStreet(sbList, i, sb);
                    }
                } else if('길' == lastChar) {
                    isRoad = true;
                    if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                        addStr(sb, i, sbList);
                    } else { // 이전 요소에 '~로'로 끝나는 문자 있으면 붙이기
                        addRoad(sbList, i, sb);
                    }
                }

                if(isRoad) {
                    roadName = sb.toString();
                    if(roadNameData.contains(roadName)) {
                        existRoad = true;
                    } else {
                        // 특수문자 포함된 도로명 데이터에 존재할 경우 특문 제거 이전의 도로명으로 교체
                        String specialRoadName = specialRoadNameData.get(roadName);
                        if(specialRoadName != null) {
                            roadName = specialRoadName;
                            existRoad = true;
                        }
                    }
                    break;
                }
            }
            addressFilterDto = AddressFilterDto.builder()
                    .inputAddress(fullAddress)
                    .roadName(roadName)
                    .existRoadName(existRoad ? "Y" : "N")
                    .build();

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

        return addressFilterDto;
    }


    private void addRoad(ArrayList<StringBuilder> sbList, int i, StringBuilder sb) {
        if(i != 0) {
            StringBuilder prevStr = sbList.get(i - 1);
            if('로' == prevStr.charAt(prevStr.length() - 1)) {
                sb.insert(0, prevStr);
            }
        }
    }

    private void addStreet(ArrayList<StringBuilder> sbList, int i, StringBuilder sb) {
        if(i < sbList.size()-1) {
            StringBuilder nextStr = sbList.get(i + 1);
            char nextLastChar = nextStr.charAt(nextStr.length() - 1);
            if('길' == nextLastChar) {
                sb.append(nextStr);
            }
        }
    }

    private void addStr(StringBuilder sb, int i, ArrayList<StringBuilder> sbList) {
        int cnt = 1;
        while(sb.length() < 3) {
            if(i - cnt < 0) break;
            sb.insert(0, sbList.get(i - cnt));
            cnt++;
        }
    }

}
