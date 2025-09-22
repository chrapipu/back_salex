package com.app.product;

import com.app.product.converter.ProductDtoToProductConverter;
import com.app.product.converter.ProductToProductDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.MANAGER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;
    private final ProductToProductDtoConverter toDtoConverter;
    private final ProductDtoToProductConverter toConverter;

    @GetMapping
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({MANAGER})
    @GetMapping("/archive")
    public Result findAllArchive() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All Archive",
                service.findAllArchive().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public Result find(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.find(id))
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/status")
    public Result updateStatus(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Status",
                toDtoConverter.convert(service.status(id))
        );
    }

    @Secured({MANAGER})
    @PostMapping
    public Result save(@Valid @RequestBody ProductDto saveDto, @RequestParam String categoryId) {
        Product save = toConverter.convert(saveDto);
        Product saved = service.save(save, categoryId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(saved)
        );
    }

    @Secured({MANAGER})
    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @Valid @RequestBody ProductDto updateDto, @RequestParam String categoryId) {
        Product update = toConverter.convert(updateDto);
        Product updated = service.update(id, update, categoryId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @Secured({MANAGER})
    @PatchMapping("/{id}/img")
    public Result updateImg(@PathVariable String id, @RequestParam MultipartFile file) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Img",
                toDtoConverter.convert(service.updateImg(id, file))
        );
    }

    @Secured({MANAGER})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }

}
