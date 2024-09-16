package CacheSmartCatalog.service.Product;


import CacheSmartCatalog.DTO.ProductDTO;

import java.util.List;
import java.util.Map;

public interface IProductService {

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Map<String,Object> updates,Long pid);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> addAllProduct(List<ProductDTO> productsDTOs);
    void  deleteProduct(Long id);
}
