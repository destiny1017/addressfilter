package com.example.addressfilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

public class FileReaderTest {
    
    @Test
    void adasd() {
        char a = '0';
        char b = '9';
        char c = 'a';
        System.out.println("((int) a == 50)  = " + ((int) a == 50) );
        System.out.println(((int) a) >= 48 && ((int) a) <= 57);
        System.out.println(((int) b) >= 48 && ((int) b) <= 57);
        System.out.println(((int) c) >= 48 && ((int) c) <= 57);
    }

    @Test
    void sampleDataWriter() {
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


}
