package be.ucll.ewiuo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class SandwichOrderTestBuilder {

    private UUID sandwichId;
    private String name;
    private String breadType;
    private LocalDateTime creationDate;
    private BigDecimal price;
    private String mobilePhoneNumber;
    private String test;

    private SandwichOrderTestBuilder() {
    }

    public static SandwichOrderTestBuilder aSandwichOrder() {
        return new SandwichOrderTestBuilder();
    }

    public SandwichOrderTestBuilder forSandwich(Sandwich sandwich) {
        this.sandwichId = sandwich.getId();
        this.name = sandwich.getName();
        this.price = sandwich.getPrice();
        return this;
    }

    public SandwichOrderTestBuilder withSandwichId(UUID sandwichId) {
        this.sandwichId = sandwichId;
        return this;
    }

    public SandwichOrderTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SandwichOrderTestBuilder withBreadType(String breadType) {
        this.breadType = breadType;
        return this;
    }

    public SandwichOrderTestBuilder withCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public SandwichOrderTestBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public SandwichOrderTestBuilder withMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
        return this;
    }

    public LunchOrder build() {
        LunchOrder sandwichOrder = new LunchOrder();
        sandwichOrder.setSandwichId(sandwichId);
        sandwichOrder.setName(name);
        sandwichOrder.setBreadType(breadType);
        sandwichOrder.setCreationDate(creationDate);
        sandwichOrder.setMobilePhoneNumber(mobilePhoneNumber);
        sandwichOrder.setPrice(price);
        return sandwichOrder;
    }
}
