package CacheSmartCatalog.service.Product;

import CacheSmartCatalog.DTO.ProductDTO;
import CacheSmartCatalog.exception.ProductNotFoundException;
import CacheSmartCatalog.model.Product;
import CacheSmartCatalog.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;

    private final String CACHE_NAME = "Products";

    @CachePut(cacheNames = CACHE_NAME,key = "#result.id")
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepo.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

        return modelMapper.map(product, ProductDTO.class);
    }

    @CachePut(cacheNames = CACHE_NAME,key = "#pid")
    @Override
    public ProductDTO updateProduct(Map<String, Object> updates, Long pid) {

        Product product = productRepo.findById(pid)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));


        updates.forEach((field, value) -> {

            Field fieldTobeUpdated = ReflectionUtils.findField(Product.class, field);
            assert fieldTobeUpdated != null;
            fieldTobeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldTobeUpdated, product, value);


        });
        Product updatedProduct = productRepo.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("gettinmg all");
        return productRepo.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    @Override
    public List<ProductDTO> addAllProduct(List<ProductDTO> productsDTOs) {
        List<Product> products = productsDTOs.stream()
                .map(productDTO -> modelMapper.map(productDTO, Product.class))
                .toList();

        List<Product> savedProducts = productRepo.saveAll(products);

        return savedProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    @CacheEvict(cacheNames = CACHE_NAME,key = "#id")
    @Override
    public void deleteProduct(Long id) {


        productRepo.findById(id).ifPresentOrElse(productRepo::delete, () -> {
            throw new ProductNotFoundException("product not found");
        });
    }
}
