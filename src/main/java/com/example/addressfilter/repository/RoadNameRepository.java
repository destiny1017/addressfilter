package com.example.addressfilter.repository;

import com.example.addressfilter.domain.RoadName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadNameRepository extends JpaRepository<RoadName, String> {

}
