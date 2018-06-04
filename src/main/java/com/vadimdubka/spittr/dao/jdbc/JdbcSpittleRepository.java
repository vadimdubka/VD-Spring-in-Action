package com.vadimdubka.spittr.dao.jdbc;

import com.vadimdubka.spittr.dao.SpittleRepository;
import com.vadimdubka.spittr.model.Spitter;
import com.vadimdubka.spittr.model.Spittle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcSpittleRepository implements SpittleRepository {
    boolean isDataInserted = false;
    
    private static final String SELECT_SPITTLE                = "select sp.id, s.id as spitterId, s.username, s.password, s.fullname, s.email, s.updateByEmail, sp.message, sp.postedTime from Spittle sp, Spitter s where sp.spitter = s.id";
    private static final String SELECT_SPITTLE_BY_ID          = SELECT_SPITTLE + " and sp.id=?";
    private static final String SELECT_SPITTLES_BY_SPITTER_ID = SELECT_SPITTLE + " and s.id=? order by sp.postedTime desc";
    private static final String SELECT_RECENT_SPITTLES        = SELECT_SPITTLE + " order by sp.postedTime desc limit ?";
    
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
    
    private List<Spittle> createSpittleList(long count) {
        List<Spittle> spittles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            spittles.add(new Spittle("Spittle " + i, new Date()));
        }
        return spittles;
    }
    
    public long count() {
        return jdbc.queryForLong("select count(id) from Spittle");
    }
    
    public List<Spittle> findRecent() {
        return findRecent(10);
    }
    
    public List<Spittle> findRecent(int count) {
        return jdbc.query(SELECT_RECENT_SPITTLES, new SpittleRowMapper(), count);
    }
    
    public Spittle findOne(long id) {
        try {
            return jdbc.queryForObject(SELECT_SPITTLE_BY_ID, new SpittleRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public List<Spittle> findBySpitterId(long spitterId) {
        return jdbc.query(SELECT_SPITTLES_BY_SPITTER_ID, new SpittleRowMapper(), spitterId);
    }
    
    public Spittle save(Spittle spittle) {
        long spittleId = insertSpittleAndReturnId(spittle);
        return new Spittle(spittleId, spittle.getSpitter(), spittle.getMessage(), spittle.getPostedTime());
    }
    
    private long insertSpittleAndReturnId(Spittle spittle) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert((JdbcTemplate) jdbc).withTableName("Spittle");
        jdbcInsert.setGeneratedKeyName("id");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("spitter", spittle.getSpitter().getId());
        args.put("message", spittle.getMessage());
        args.put("postedTime", spittle.getPostedTime());
        long spittleId = jdbcInsert.executeAndReturnKey(args).longValue();
        return spittleId;
    }
    
    public void delete(long id) {
        jdbc.update("delete from Spittle where id=?", id);
    }
    
    private static final class SpittleRowMapper implements RowMapper<Spittle> {
        public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
            long    id            = rs.getLong("id");
            String  message       = rs.getString("message");
            Date    postedTime    = rs.getTimestamp("postedTime");
            long    spitterId     = rs.getLong("spitterId");
            String  username      = rs.getString("username");
            String  password      = rs.getString("password");
            String  fullName      = rs.getString("fullname");
            String  email         = rs.getString("email");
            boolean updateByEmail = rs.getBoolean("updateByEmail");
            Spitter spitter       = new Spitter(spitterId, username, password, fullName, email, updateByEmail);
            return new Spittle(id, spitter, message, postedTime);
        }
    }
}