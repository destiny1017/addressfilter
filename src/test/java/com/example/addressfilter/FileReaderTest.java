package com.example.addressfilter;

import com.example.addressfilter.domain.RoadName;
import com.example.addressfilter.repository.RoadNameJdbcRepository;
import com.example.addressfilter.repository.RoadNameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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
    @Rollback(false)
    void batchInsert() throws Exception {
        long start;
        long end;
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath + "/data/도로명코드_전체.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
        String a;
        HashSet<String> roadStrSet = new HashSet<>(); //도로명
//        HashSet<String> cityStrSet = new HashSet<>(); //시도명
//        HashSet<String> regionStrSet = new HashSet<>(); //시군구
//        HashSet<String> provinceStrSet = new HashSet<>(); //읍면동

        start = System.currentTimeMillis();
//        br.readLine(); // 헤더컬럼 삭제
        while(true) {
            String line = br.readLine();
            if(line == null) break;
            String[] str = line.split("\t");
            roadStrSet.add(str[2]);
//            cityStrSet.add(str[5]);
//            regionStrSet.add(str[6]);
//            provinceStrSet.add(str[9]);
//            for (int i = 0; i < str.length; i++) {
//                String s = str[i];
//                System.out.print("[" + i + "] " + s + " | ");
//            }
            System.out.println();
        }

        end = System.currentTimeMillis();
        System.out.println("도로명 추출 시간 : " + (end - start));

        // 도로명 insert
        ArrayList<String> roadList = new ArrayList<>(roadStrSet);
        roadNameJdbcRepository.roadNameBatchInsert(roadList);
        end = System.currentTimeMillis();
        System.out.println("전체 수행 시간 : " + (end - start));
    }

    @Test
    void sampleDataRead() {
        long start;
        long end;
        ArrayList<String> addressList = new ArrayList<>();
        try {
            String rootPath = System.getProperty("user.dir");
            File file = new File(rootPath + "/data/샘플주소.txt");
            File file2 = new File(rootPath + "/data/샘플주소_가공.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            FileWriter fw = new FileWriter(file2);
            BufferedWriter writer = new BufferedWriter(fw);

            while(true) {
                StringBuilder line = new StringBuilder(br.readLine());
                if(line == null || line.length() == 0) break;
                Random r = new Random();
                char str[] = new char[10];
                for (int i = 0; i < str.length; i++) {
                    if(i == 0 || i ==  4) str[i] = ' ';
                    else {
                        int num = r.nextInt(11171) + 44032;
                        str[i] = (char) num;
                    }
                }
                line.append(String.valueOf(str) + "\n");
                writer.write(line.toString());
            }

        } catch(Exception e) {

        }
    }

    @Test
    void fileWriteTest() throws Exception {
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath + "/data/fileWriter.txt");
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);

        if (!file.exists()) {
            file.createNewFile();
        }

        writer.write("line1\n");
        writer.write("line2\n");
        writer.write("line3\n");
        writer.write("line4\n");

        writer.close();

    }

    @Test
    void uniTest() {
        char a = '가';
        char b = '힣';
        System.out.println("(int) a = " + (int) a);
        System.out.println("(int) b = " + (int) b);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int num = r.nextInt(11171) + 44032;
            System.out.println(num + " = " + ((char) num));
        }
    }

}
