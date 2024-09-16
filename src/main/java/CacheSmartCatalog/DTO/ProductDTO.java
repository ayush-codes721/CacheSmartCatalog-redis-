package CacheSmartCatalog.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDTO  implements Serializable {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String category;

    private Integer stockQuantity;

    private String brand;

    private boolean isAvailable;
}
