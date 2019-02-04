package br.com.zone.tacocloud.controller;

import br.com.zone.tacocloud.model.Ingredient;
import br.com.zone.tacocloud.model.Order;
import br.com.zone.tacocloud.model.Taco;
import br.com.zone.tacocloud.repository.IngredientRepository;
import br.com.zone.tacocloud.repository.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.zone.tacocloud.model.Ingredient.Type;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private IngredientRepository ingredientRepository;
    private TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository,
                                TacoRepository tacoRepository){
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @ModelAttribute("order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute("taco")
    public Taco taco(){
        return new Taco();
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
    public String processDesign(@Valid Taco taco, Errors errors, Order order){

        if(errors.hasErrors()){
            return "design";
        }

        Taco saved = tacoRepository.save(taco);
        order.getTacos().add(saved);


        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        return ingredients.stream().filter(i -> i.getType().equals(type)).collect(Collectors.toList());
    }

}
