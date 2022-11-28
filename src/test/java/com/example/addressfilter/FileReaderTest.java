package com.example.addressfilter;

import com.example.addressfilter.domain.RoadName;
import com.example.addressfilter.repository.RoadNameJdbcRepository;
import com.example.addressfilter.repository.RoadNameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@SpringBootTest
public class FileReaderTest {

    @Autowired
    private RoadNameRepository roadNameRepository;

    @Autowired
    private RoadNameJdbcRepository roadNameJdbcRepository;

//    @Test
    void readTest() throws Exception {
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath + "/data/도로명코드_전체.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringTokenizer st;
        String a;
        HashSet<String> roadSet = new HashSet<>();

        br.readLine(); // 헤더컬럼 삭제
        long start = System.currentTimeMillis();
        while(true) {
            String line = br.readLine();
            if(line == null) break;
            st = new StringTokenizer(line, "\t");
            st.nextToken();
            st.nextToken();
            roadSet.add(st.nextToken());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(roadSet.size());
    }

//    @Test
    void readTest2() throws Exception {
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath + "/data/도로명코드_전체.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String a;
        HashSet<String> roadSet = new HashSet<>();

        br.readLine(); // 헤더컬럼 삭제
        long start = System.currentTimeMillis();
        while(true) {
            String line = br.readLine();
            if(line == null) break;
            String[] str = line.split("\t");
            roadSet.add(str[2]);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(roadSet.size());


    }

    @Transactional
    @Test
    @Rollback()
    void batchInsert() throws Exception {
        long start;
        long end;
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath + "/data/도로명코드_전체.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
        String a;
        HashSet<String> roadStrSet = new HashSet<>();

        start = System.currentTimeMillis();
        br.readLine(); // 헤더컬럼 삭제
        while(true) {
            String line = br.readLine();
            if(line == null) break;
            String[] str = line.split("\t");
            roadStrSet.add(str[2]);
        }

        end = System.currentTimeMillis();
        System.out.println("도로명 추출 시간 : " + (end - start));

        ArrayList<String> roadList = new ArrayList<>(roadStrSet);
        roadNameJdbcRepository.batchInsert(roadList);
        end = System.currentTimeMillis();
        System.out.println("전체 수행 시간 : " + (end - start));
    }

}
