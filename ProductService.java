package com.app.product;

import com.app.category.CategoryService;
import com.app.enums.ProductStatus;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.app.util.Global.saveFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryService categoryService;

    public List<Product> findAll() {
        return repository.findAllByStatus(ProductStatus.ACTIVE, Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Product> findAllArchive() {
        return repository.findAllByStatus(ProductStatus.ARCHIVE, Sort.by(Sort.Direction.DESC, "id"));
    }

    public Product find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден товар по ИД: " + id));
    }

    public Product status(String id) {
        Product product = find(id);
        switch (product.getStatus()) {
            case ACTIVE -> product.setStatus(ProductStatus.ARCHIVE);
            case ARCHIVE -> product.setStatus(ProductStatus.ACTIVE);
        }
        return repository.save(product);
    }

    public Product save(Product save, String categoryId) {
        save.setCategory(categoryService.find(categoryId));
        return repository.save(save);
    }

    public Product update(String id, Product update, String categoryId) {
        Product old = find(id);
        old.update(update);
        old.setCategory(categoryService.find(categoryId));
        return repository.save(old);
    }

    public Product updateImg(String id, MultipartFile img) {
        Product product = find(id);
        try {
            product.setImg(saveFile(img, "product"));
        } catch (IOException e) {
            if (product.getImg().isEmpty()) repository.deleteById(product.getId());
            throw new BadRequestException("Некорректное изображение");
        }
        return repository.save(product);
    }

    public void delete(String id) {
        repository.deleteById(find(id).getId());
    }

}
