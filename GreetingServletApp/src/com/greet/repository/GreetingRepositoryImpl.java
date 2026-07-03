package com.greet.repository;

import com.greet.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GreetingRepositoryImpl implements GreetingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Greeting> greetingMapper = (rs, rowNum) -> {

        Greeting greeting = new Greeting();

        greeting.setId(rs.getInt("id"));
        greeting.setMessage(rs.getString("message"));
        greeting.setImagePath(rs.getString("image_path"));
        greeting.setCreatedById(rs.getInt("created_by_id"));
        greeting.setCreatedByName(rs.getString("username"));

        return greeting;
    };

    @Override
    public List<Greeting> findAll() {

        String sql =
                "SELECT g.*, u.username " +
                        "FROM greetings g " +
                        "LEFT JOIN users u ON g.created_by_id = u.id " +
                        "ORDER BY g.id DESC";

        return jdbcTemplate.query(sql, greetingMapper);
    }

    @Override
    public Greeting findById(int id) {

        String sql =
                "SELECT g.*, u.username " +
                        "FROM greetings g " +
                        "LEFT JOIN users u ON g.created_by_id = u.id " +
                        "WHERE g.id = ?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    greetingMapper,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean save(Greeting greeting) {

        String sql =
                "INSERT INTO greetings(message,image_path,created_by_id) VALUES(?,?,?)";

        int rows = jdbcTemplate.update(
                sql,
                greeting.getMessage(),
                greeting.getImagePath(),
                greeting.getCreatedById()
        );

        return rows > 0;
    }

    @Override
    public boolean update(Greeting greeting) {

        String sql =
                "UPDATE greetings SET message=?, image_path=? WHERE id=?";

        int rows = jdbcTemplate.update(
                sql,
                greeting.getMessage(),
                greeting.getImagePath(),
                greeting.getId()
        );

        return rows > 0;
    }

    @Override
    public boolean delete(int id) {

        String sql =
                "DELETE FROM greetings WHERE id=?";

        int rows = jdbcTemplate.update(sql, id);

        return rows > 0;
    }
}