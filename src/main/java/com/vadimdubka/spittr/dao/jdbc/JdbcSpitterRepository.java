package com.vadimdubka.spittr.dao.jdbc;

import com.vadimdubka.spittr.dao.SpitterRepository;
import com.vadimdubka.spittr.model.Spitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcSpitterRepository implements SpitterRepository {
    
    private final static String INSERT_SPITTER = "insert into Spitter (username, password, first_name, last_name, email)" +
                                                     " values (?, ?, ?, ?, ?)";
    
    private static final String SELECT_SPITTER = "select id, username, password, first_name, last_name, fullname, email, updateByEmail " +
                                                     "from Spitter";
    
    private static final String SPITTER_UPDATE = "update Spitter set username=?, password=?, fullname=?, email=?, updateByEmail=? where id=?";
    
    
    private JdbcOperations jdbc;
    
    public JdbcSpitterRepository() {
    
    }
    
    @Autowired
    public JdbcSpitterRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }
    
    @Override
    public long count() {
        return jdbc.queryForObject("select count(id) from Spitter", Long.class);
    }
    
    
    @Override
    public Spitter findOne(long id) {
        return jdbc.queryForObject(SELECT_SPITTER + " where id=?", new SpitterRowMapper(), id);
    }
    
    public Spitter findByUsername(String username) {
        return jdbc.queryForObject(SELECT_SPITTER + " where username=?", new SpitterRowMapper(), username);
    }
    
    @Override
    public List<Spitter> findAll() {
        return jdbc.query("select id, username, password, fullname, email, updateByEmail from Spitter order by id", new SpitterRowMapper());
    }
    
    
    public Spitter save(Spitter spitter) {
        Long id = spitter.getId();
        if (id == null) {
            long spitterId = insertSpitterAndReturnId(spitter);
            spitter.setId(spitterId);
            return spitter;
        } else {
            update(spitter, id);
        }
        return spitter;
    }
    
    public void update(Spitter spitter, Long id) {
        jdbc.update(SPITTER_UPDATE,
                    spitter.getUsername(),
                    spitter.getPassword(),
                    spitter.getFullName(),
                    spitter.getFirstName(),
                    spitter.getLastName(),
                    spitter.getEmail(),
                    spitter.isUpdateByEmail(),
                    id);
    }
    
    
    /**
     * Inserts a spitter using SimpleJdbcInsert.
     * Involves no direct SQL and is able to return the ID of the newly created Spitter.
     *
     * @param spitter a Spitter to insert into the databse
     * @return the ID of the newly inserted Spitter
     */
    private long insertSpitterAndReturnId(Spitter spitter) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert((JdbcTemplate) jdbc).withTableName("Spitter");
        jdbcInsert.setGeneratedKeyName("id");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", spitter.getUsername());
        args.put("password", spitter.getPassword());
        args.put("fullname", spitter.getFullName());
        args.put("email", spitter.getEmail());
        args.put("updateByEmail", spitter.isUpdateByEmail());
        long spitterId = jdbcInsert.executeAndReturnKey(args).longValue();
        return spitterId;
    }
    
    /**
     * Inserts a spitter using a simple JdbcTemplate update() call.
     * Does not return the ID of the newly created Spitter.
     *
     * @param spitter a Spitter to insert into the database
     */
    @SuppressWarnings("unused")
    private void insertSpitter(Spitter spitter) {
        jdbc.update(INSERT_SPITTER,
                    spitter.getUsername(),
                    spitter.getPassword(),
                    spitter.getFullName(),
                    spitter.getEmail(),
                    spitter.isUpdateByEmail());
    }
    
    private static class SpitterRowMapper implements RowMapper<Spitter> {
        public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
            long    id            = rs.getLong("id");
            String  username      = rs.getString("username");
            String  password      = rs.getString("password");
            String  firstName = rs.getString("first_name");
            String  lastName = rs.getString("last_name");
            String  fullName      = rs.getString("fullname");
            String  email         = rs.getString("email");
            boolean updateByEmail = rs.getBoolean("updateByEmail");
            return new Spitter(id, username, password, firstName, lastName, fullName, email, updateByEmail);
        }
    }
}
