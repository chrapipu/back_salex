package com.app.product;

import com.app.category.Category;
import com.app.enums.ProductStatus;
import com.app.product_ordering.ProductOrdering;
import com.app.product_review.ProductReview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.app.util.Global.round;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "product_g")
    @SequenceGenerator(name = "product_g", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    private String name;
    private int count;
    private float price;
    private int discount;

    @Column(length = 1000)
    private String img = "";

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductOrdering> orderings = new ArrayList<>();

    public Product(String name, int count, float price, int discount) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.discount = discount;
    }

    public void update(Product update) {
        this.name = update.getName();
        this.count = update.getCount();
        this.price = update.getPrice();
        this.discount = update.getDiscount();
    }

    public int getScore() {
        if (reviews.isEmpty()) return 0;
        return reviews.stream().reduce(0, (i, review) -> i + review.getScore(), Integer::sum) / reviews.size();
    }

    public int getIncomeCount() {
        return this.orderings.stream().reduce(0, (i, ordering) -> i + ordering.getCount(), Integer::sum);
    }

    public float getIncomeSum() {
        return round(this.orderings.stream().reduce(0f, (i, ordering) -> i + ordering.getSum(), Float::sum));
    }

    public List<ProductReview> getReviews() {
        reviews.sort(Comparator.comparing(ProductReview::getId));
        Collections.reverse(reviews);
        return reviews;
    }
}