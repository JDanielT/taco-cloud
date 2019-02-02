package br.com.zone.tacocloud.repository;

import br.com.zone.tacocloud.model.Ingredient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository {

    Iterable<Ingredient> findAll();
    Ingredient findOne(String id);
    Ingredient save(Ingredient ingredient);

}
