package model;

import be.ucll.ewiuo.Model.Sandwich;

import java.math.BigDecimal;
import java.util.UUID;

public class SandwichBuilder {
    private UUID id;
    private String name;
    private String ingredients;
    private BigDecimal price;

    private SandwichBuilder(){}

    public static SandwichBuilder aSandwich() {
        return new SandwichBuilder();
    }
    public SandwichBuilder WithName(String name){
        this.name = name;
        return this;
    }

    public SandwichBuilder WithID(UUID id){
        this.id = id;
        return this;
    }

    public SandwichBuilder WithIngredients(String ingredients){
        this.ingredients = ingredients;
        return this;
    }

    public SandwichBuilder WithPrice(BigDecimal price){
        this.price = price;
        return this;
    }

    public Sandwich build(){
        return new Sandwich(name, ingredients, price);
        //throw new RuntimeException();
    }
}
