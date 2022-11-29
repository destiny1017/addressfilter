package com.example.addressfilter.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RoadNameJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void roadNameBatchInsert(List<String> roadList) {
        jdbcTemplate.batchUpdate("insert into road_name(name) values(?)"
        ,new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, roadList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return roadList.size();
                    }
                });

    }
}
