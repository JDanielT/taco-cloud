package br.com.zone.tacocloud.repository;

import br.com.zone.tacocloud.model.Ingredient;
import br.com.zone.tacocloud.model.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbcTemplate;
    private JdbcIngredientRepository ingredientRepository;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbcTemplate, JdbcIngredientRepository ingredientRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);

        taco.getIngredients().forEach(i -> {
            saveIngredientToTaco(ingredientRepository.findOne(i), tacoId);
        });

        return taco;

    }

    private long saveTacoInfo(Taco taco){

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory("INSERT INTO Taco (name, createdAt) VALUES (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP);

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory
                .newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        return keyHolder.getKey().longValue();

    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId){
        jdbcTemplate.update("INSERT INTO Taco_Ingredients (taco, ingredient) VALUES (?, ?)", tacoId, ingredient.getId());
    }

}
