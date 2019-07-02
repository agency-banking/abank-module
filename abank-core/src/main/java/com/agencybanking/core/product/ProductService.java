package com.agencybanking.core.product;

import com.agencybanking.core.data.SearchRequest;
import com.agencybanking.core.services.Crud;
import com.agencybanking.core.utils.BeanUtil;
import com.agencybanking.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.notEmpty;

/**
 * @author dubic
 */
@Service
@Slf4j
@CacheConfig(cacheNames={"module"})
public class ProductService implements Crud<Product, String> {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        throw new UnsupportedOperationException("Cannot product from here");
//        return this.productRepository.save(product);
    }
    @Override
    public Product update(Product product) {
        Product found = this.productRepository.findById(product.getCode()).orElseThrow(() -> new IllegalStateException("Resource not found"));
        found.copyForUpdate(product);
        return this.productRepository.save(found);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> query(Product product, PageRequest p) {
        if (ObjectUtils.isEmpty(product)) {
            return productRepository.findAll(p);
        }
        return productRepository.findAll(Example.of(product), p);
    }

    @Override
    @Cacheable
    public Collection<Product> list() {
        return productRepository.findAll(new Sort(Sort.Direction.ASC, "code"));
    }

    @Override
    public boolean exists(Product product, String id) {
        return productRepository.exists(Example.of(product));
    }

    @Cacheable
    public Object getServiceBean(String productCode) {
        Product product = getProduct(productCode);
        //Get Bean
        return getServiceBean(product);
    }

    private Product getProduct(String productCode) {
        return this.productRepository.findById(productCode)
                .orElseThrow(() -> new IllegalArgumentException("Could not find product :" + productCode));
    }

    @Cacheable
    public Object getServiceBean(Product product) {
        String s = product.getServices();
        hasLength(s, "No services defined for '" + product.getName() + "' product. Consider defining a service class in Product table.");
        String[] services = s.split(",");
        notEmpty(services, "No first service class defined in Products table for '" + product.getName() + "'");

        String beanName = Utils.simpleName(services[0], true);
        log.debug("Found bean name for product service: {}", beanName);
        //Get Bean
        return BeanUtil.getBean(beanName);
    }

    @Cacheable
    public String[] getDomains(String productCode) {
        Product product = getProduct(productCode);
        Assert.hasLength(product.getDomains(), "Product has no domains :"+productCode);
        String[] domains = product.getDomains().split(",");
        Assert.notEmpty(domains, "No domain class found for '"+productCode+"'.Consider adding domain class(es) in product.sql");
        return domains;
    }

    @Override
    @Cacheable
    public Page<Product> singleSearch(SearchRequest request, PageRequest p) {
        QProduct qProduct = QProduct.product;

        if (StringUtils.isEmpty(request.getSearchItem())) {
            return productRepository.findAll(p);
        }
        return productRepository.findAll(
                qProduct.name.containsIgnoreCase(request.getSearchItem())
                        .or(qProduct.description.containsIgnoreCase(request.getSearchItem()))
                        .or(qProduct.name.stringValue().containsIgnoreCase(request.getSearchItem()))
                        .or(qProduct.code.stringValue().containsIgnoreCase(request.getSearchItem()))
                        .and(qProduct.module.name.containsIgnoreCase(request.getSearchItem())), p);
    }


    @Override
    @Cacheable
    public Page<Product> single(String... args) {
        PageRequest request = PageRequest.of(0, 10);
        String[] codes = !StringUtils.isEmpty(args[1]) ? args[1].split(",") : new String[0];
        return codes.length > 0 ? productRepository.findByCodeNotIn(Arrays.asList(codes), request) : singleSearch(new SearchRequest(args[0], null), request);
    }
}
