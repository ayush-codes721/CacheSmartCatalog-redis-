package CacheSmartCatalog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the product

    private String description; // Brief description of the product

    private BigDecimal price; // Price of the product

    private String category; // Product category, e.g., Electronics, Clothing

    private Integer stockQuantity; // Available stock

    private String brand; // Brand name

    private boolean isAvailable; // Availability status

}
