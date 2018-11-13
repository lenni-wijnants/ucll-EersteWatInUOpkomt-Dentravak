package be.ucll.ewiuo.Model;

import java.math.BigDecimal;
import java.util.UUID;

public class Sandwich {
    private String name;
    private String ingredients;
    private BigDecimal price;

    public Sandwich(String name, String ingredients, BigDecimal price) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIngredients() { return ingredients; }

    public void setIngredients(String ingredients)
    {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public static class SandwichBuilder {
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

        public SandwichBuilder WithIngedients(String ingredients){
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
}
