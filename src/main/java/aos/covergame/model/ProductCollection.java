package aos.covergame.model;

import aos.covergame.model.Product;

import java.util.ArrayList;
import java.util.Collections;

public class ProductCollection extends ArrayList<Product> {

    public static final Integer DEFAULT_MAX_COUNT = 1000;
    public static final Integer DEFAULT_IDENTIONAL_OBJECTS = 20;

    public ProductCollection(){}
    public ProductCollection(Integer maxCount){
        this.maxCount = maxCount;
    }

    public ProductCollection(Integer maxCount, Integer countIdentionalObject){
        this.maxCount = maxCount;
        this.countIdenticalObject = countIdentionalObject;
    }

    private Integer maxCount = DEFAULT_MAX_COUNT;
    private Integer countIdenticalObject = DEFAULT_IDENTIONAL_OBJECTS;

    @Override
    public boolean add(Product product) {
        return Collections.frequency(this, product) == countIdenticalObject
                || (size() == maxCount ? addAfterMaxCount(product) : super.add(product));
    }
    private boolean addAfterMaxCount(Product product){
        boolean returnValue = super.add(product);
        this.sort(Product::compareTo);
        super.remove(this.size()-1);
        return returnValue;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

}
