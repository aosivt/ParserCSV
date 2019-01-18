package aos.covergame.model;

import aos.covergame.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class ProductCollection extends ArrayList<Product> {

    private static final Integer DEFAULT_MAX_COUNT = 1000;
    private static final Integer DEFAULT_IDENTICAL_OBJECTS = 20;

    private Integer maxCount = DEFAULT_MAX_COUNT;
    private Integer countIdenticalObject = DEFAULT_IDENTICAL_OBJECTS;

    public ProductCollection() {
    }

    public ProductCollection(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public ProductCollection(Integer maxCount, Integer countIdenticalObject) {
        this.maxCount = maxCount;
        this.countIdenticalObject = countIdenticalObject;
    }

    @Override
    public boolean add(Product product) {
        return Collections.frequency(this, product) == countIdenticalObject
                || (size() == maxCount ? addAfterMaxCount(product) : super.add(product));
    }

    private boolean addAfterMaxCount(Product product) {
        boolean returnValue = super.add(product);
        this.sort(Product::compareTo);
        super.remove(this.size() - 1);
        return returnValue;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

}
