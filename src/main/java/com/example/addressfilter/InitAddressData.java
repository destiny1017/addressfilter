package com.example.addressfilter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InitAddressData {

    private HashSet<String> roadNameData;
    private HashMap<String, String> specialRoadNameData;
    private ArrayList<String> sampleData;

    public void readFileData() {

        HashSet<String> tmpRoadNameData = null;
        HashMap<String, String> tmpSpecialRoadNameData = null;

        try{
            String rootPath = System.getProperty("user.dir");
            File file = new File(rootPath + "/data/도로명코드_전체.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
            tmpRoadNameData = new HashSet<>(); // 도로명
            tmpSpecialRoadNameData = new HashMap<>(); // 특수문자 도로명
            br.readLine(); // 헤더컬럼 삭제

            while(true) {
                String line = br.readLine();
                if(line == null) break;
                String[] str = line.split("\t");
                if(str[2].matches("[가-힣0-9a-zA-Z]*")) { // 특문 없으면
                    tmpRoadNameData.add(str[2]);
                } else { // 특문 있으면
                    tmpSpecialRoadNameData.put(str[2].replaceAll("[^가-힣0-9a-zA-Z]", ""), str[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 도로명 insert
        roadNameData = tmpRoadNameData;
        specialRoadNameData = tmpSpecialRoadNameData;
    }

    public void sampleDataRead() {
        ArrayList<String> addressList = new ArrayList<>();
        try {
            String rootPath = System.getProperty("user.dir");
            File file = new File(rootPath + "/data/샘플주소_가공.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while(true) {
                String line = br.readLine();
                if(line == null) break;
                addressList.add(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        sampleData = addressList;
    }

    public HashSet<String> getRoadNameData() {
        if(roadNameData == null) {
            readFileData();
        }
        return roadNameData;
    }

    public HashMap<String, String> getSpecialRoadNameData() {
        if(specialRoadNameData == null) {
            readFileData();
        }
        return specialRoadNameData;
    }

    public ArrayList<String> getSampleData() {
        if(sampleData == null) {
            sampleDataRead();
        }
        return sampleData;
    }
}
