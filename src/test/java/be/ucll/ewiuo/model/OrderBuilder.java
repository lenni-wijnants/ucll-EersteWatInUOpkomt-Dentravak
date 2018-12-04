package be.ucll.ewiuo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderBuilder {
    private UUID id;
    private UUID sandwichId;
    private String name;
    private String breadType;
    private LocalDateTime creationDate;
    private BigDecimal price;
    private String mobilePhoneNumber;

    private OrderBuilder(){}

    public static OrderBuilder anOrder() { return new OrderBuilder(); }

    public OrderBuilder forSandwich(Sandwich sandwich){
        this.sandwichId = sandwich.getId();
        this.name = sandwich.getName();
        this.price = sandwich.getPrice();
        return this;
    }

    public OrderBuilder withID(UUID id){
        this.id = id;
        return this;
    }

    public OrderBuilder withSandWichID(UUID sandwichId){
        this.sandwichId = sandwichId;
        return this;
    }

    public OrderBuilder withName(String name){
        this.name = name;
        return this;
    }
    public OrderBuilder withBreadType(String breadType){
        this.breadType = breadType;
        return this;
    }

    public OrderBuilder withCreationDate(LocalDateTime creationDate){
        this.creationDate = creationDate;
        return this;
    }

    public OrderBuilder withPrice(BigDecimal price){
        this.price = price;
        return this;
    }

    public OrderBuilder withMobilePhoneNumber(String mobilePhoneNumber){
        this.mobilePhoneNumber = mobilePhoneNumber;
        return this;
    }

    public LunchOrder build(){
        LunchOrder sandwichOrder = new LunchOrder();
        sandwichOrder.setSandwichId(sandwichId);
        sandwichOrder.setName(name);
        sandwichOrder.setBreadType(breadType);
        sandwichOrder.setCreationDate(creationDate);
        sandwichOrder.setMobilePhoneNumber(mobilePhoneNumber);
        sandwichOrder.setPrice(price);
        return sandwichOrder;
        //return new LunchOrder(this.sandwichId, this.name, this.breadType, this.price, this.mobilePhoneNumber);
    }
}
