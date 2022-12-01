package com.example.addressfilter;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Configuration
public class InitialBeanConfig {

    private InitAddressData initAddressData;

    public InitialBeanConfig() {
        initAddressData = new InitAddressData();
    }

    @Bean
    public HashSet<String> roadNameData() {
        return initAddressData.getRoadNameData();
    }

    @Bean
    public HashMap<String, String> specialRoadNameData() {
        return initAddressData.getSpecialRoadNameData();
    }

    @Bean
    public List<String> sampleData() {
        return initAddressData.getSampleData();
    }
}
