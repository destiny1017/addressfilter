package com.example.addressfilter.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class RoadName implements Persistable<String> {

    @Id
    private String name;

    @Builder
    public RoadName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
