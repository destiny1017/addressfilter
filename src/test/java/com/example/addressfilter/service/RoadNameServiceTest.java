package com.example.addressfilter.service;

import com.example.addressfilter.domain.RoadName;
import com.example.addressfilter.repository.RoadNameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
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
    void addressListFiltering() {
        ArrayList<String> addressList = new ArrayList<>();
        addressList.add("성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!");
        addressList.add("마포구 도화-2길 코끼리분식");
        addressList.add("강서구 곰달래로 35길 31, 와이앤비");
        addressList.add("강남구 테헤란로 326");

        String regex = "([가-힣][가-힣A-Za-z·\\d~\\-\\.\\s]{2,}(로|길))";

        for (String str : addressList) {
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(str);
//            ArrayList<StringBuffer> matcherList = new ArrayList<>();
            while(matcher.find()) {
                ArrayList<StringBuilder> sbList = new ArrayList<>();
                Arrays.stream(matcher.group(1).split(" "))
                        .forEach(i -> sbList.add(new StringBuilder(i.replaceAll("[^\uAC00-\uD7A30-9a-zA-Z]", ""))));
                for (int i = 0; i < sbList.size(); i++) {
                    StringBuilder sb = sbList.get(i);
                    char lastChar = sb.charAt(sb.length()-1);
                    boolean findFlag = false;
                    if('로' == lastChar) {
                        if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                            int cnt = 1;
                            while(sb.length() < 3) {
                                if(i - cnt < 0) break;
                                sb.insert(0, sbList.get(i - cnt));
                                cnt++;
                            }
                        } else { // 이후 요소들에 몇번길로 끝나는 문자 있으면 붙이기
                            if(i < sbList.size()-1) {
                                StringBuilder nextStr = sbList.get(i + 1);
                                if('길' == nextStr.charAt(nextStr.length() - 1)) {
                                    sb.append(nextStr);
                                    i++; // 길은 이미 썼으니 i증가 시켜서 건너뛰기
                                }
                            }
                        }
//                        findRoadName(sb.toString());
                        findFlag = true;
                        System.out.println(sb + "->" + findRoadName(sb.toString()));
                    } else if('길' == lastChar) {
                        if(sb.length() < 3) { // 3글자보다 작으면 이전 요소 문자와 합치기
                            int cnt = 1;
                            while(sb.length() < 3) {
                                if(i - cnt < 0) break;
                                sb.insert(0, sbList.get(i - cnt));
                                cnt++;
                            }
                        } else { // 이전 요소에 도로로 끝나는 문자 있으면 붙이기
                            if(i != 0) {
                                StringBuilder prevStr = sbList.get(i - 1);
                                if('로' == prevStr.charAt(prevStr.length() - 1)) {
                                    sb.insert(0, prevStr);
                                }
                            }
                        }
                        //                        findRoadName(sb.toString());
                        findFlag = true;
                        System.out.println(sb + "->" + findRoadName(sb.toString()));
                    }
                    if(findFlag) break;
                }
                System.out.println();
            }
            System.out.println("----------------------------------");
        }


    }


    public boolean findRoadName(String roadName) {
        Optional<RoadName> findRoad = roadNameRepository.findById(roadName);
        if(findRoad.isPresent()) return true;
        else return false;
    }
}