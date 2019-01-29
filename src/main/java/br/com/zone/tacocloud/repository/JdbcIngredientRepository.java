package br.com.zone.tacocloud.repository;

import br.com.zone.tacocloud.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query("SELECT id, name, type FROM Ingredient", this::rowMapper);
    }

    @Override
    public Ingredient findOne(String id) {
        return jdbcTemplate.queryForObject("SELECT id, name, type FROM Ingredient WHER id = ?", this::rowMapper, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
         jdbcTemplate.update("INSERT INTO Ingredient (id, name, type) VALUES (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
         return ingredient;
    }

    private Ingredient rowMapper(ResultSet rs, int row) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type")));
    }

}
