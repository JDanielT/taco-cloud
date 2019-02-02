package br.com.zone.tacocloud.controller;

import br.com.zone.tacocloud.model.Ingredient;

import static br.com.zone.tacocloud.model.Ingredient.Type;

import br.com.zone.tacocloud.model.Taco;
import br.com.zone.tacocloud.repository.JdbcIngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    private JdbcIngredientRepository ingredientRepository;

    @Autowired
    public DesignTacoController(JdbcIngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public String showDesignForm(Model model) {

        List<Ingredient> ingredients = ingredientRepository.findAll();

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
        model.addAttribute("taco", new Taco());

        return "design";

    }

    @PostMapping
    public String processDesign(@Valid Taco taco, Errors errors){

        if(errors.hasErrors()){
            return "design";
        }

        log.info("Taco " +taco);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        return ingredients.stream().filter(i -> i.getType().equals(type)).collect(Collectors.toList());
    }

}
