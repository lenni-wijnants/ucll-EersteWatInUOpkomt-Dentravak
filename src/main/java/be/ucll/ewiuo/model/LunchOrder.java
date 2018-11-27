package be.ucll.ewiuo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class LunchOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID sandwichId;
    private String name;
    private String breadType;
    private LocalDateTime creationDate;
    private BigDecimal price;
    private String mobilePhoneNumber;

    public LunchOrder(){this.creationDate = LocalDateTime.now();}

    public LunchOrder(UUID sandwichId, String name, String breadType, BigDecimal price, String mobilePhoneNumber)
    {
        setSandwichId(sandwichId);
        setName(name);
        setBreadType(breadType);
        setPrice(price);
        setCreationDate(LocalDateTime.now());
        setMobilePhoneNumber(mobilePhoneNumber);
    }

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public UUID getSandwichId() {return sandwichId;}
    public void setSandwichId(UUID sandwichId) {this.sandwichId = sandwichId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getBreadType() {return breadType;}

    public void setBreadType(String breadType) {
        this.breadType = breadType;
    }

    public LocalDateTime getCreationDate() {return creationDate;}

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            this.creationDate = LocalDateTime.now();
        } else {
            this.creationDate = creationDate;
        }
    }

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public String getMobilePhoneNumber() {return mobilePhoneNumber;}

    public void setMobilePhoneNumber(String mobilePhoneNumber) {this.mobilePhoneNumber = mobilePhoneNumber;}
}