package com.vadimdubka.spittr.dao;

import com.vadimdubka.spittr.model.Spittle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcSpittleRepository implements SpittleRepository {
    boolean isDataInserted = false;
    
    private JdbcOperations jdbc;
    
    public JdbcSpittleRepository() {
    
    }
    
    @Autowired
    public JdbcSpittleRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }
    
    public List<Spittle> findRecentSpittles() {
        /*return jdbc.query(
            "select id, message, created_at, latitude, longitude" +
                " from Spittle" +
                " order by created_at desc limit 20",
            new SpittleRowMapper());*/
        return createSpittleList(20);
    }
    
    public List<Spittle> findSpittles(long max, int count) {
        /*return jdbc.query(
            "select id, message, created_at, latitude, longitude" +
                " from Spittle" +
                " where id < ?" +
                " order by created_at desc limit 20",
            new SpittleRowMapper(), max);*/
        return createSpittleList(count);
    }
    
    public Spittle findOne(long id) {
        /*List<Spittle> spittles = jdbc.query(
            "select id, message, created_at, latitude, longitude" +
                " from Spittle" +
                " where id = ?",
            new SpittleRowMapper(), id);
        return spittles.size() > 0 ? spittles.get(0) : null;*/
        return new Spittle("Spittle " + id, new Date());
    }
    
    public void save(Spittle spittle) {
        jdbc.update(
            "insert into Spittle (message, created_at, latitude, longitude)" +
                " values (?, ?, ?, ?)",
            spittle.getMessage(),
            spittle.getTime(),
            spittle.getLatitude(),
            spittle.getLongitude());
    }
    
    private static class SpittleRowMapper implements RowMapper<Spittle> {
        public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Spittle(
                rs.getLong("id"),
                rs.getString("message"),
                rs.getDate("created_at"),
                rs.getDouble("longitude"),
                rs.getDouble("latitude"));
        }
    }
    
    private List<Spittle> createSpittleList(long count) {
        List<Spittle> spittles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            spittles.add(new Spittle("Spittle " + i, new Date()));
        }
        return spittles;
    }
}