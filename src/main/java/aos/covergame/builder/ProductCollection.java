package aos.covergame.builder;

import aos.covergame.model.Product;

import java.util.ArrayList;
import java.util.Collections;

public class ProductCollection extends ArrayList<Product> {
    public ProductCollection(){}
    public ProductCollection(Integer maxCount){
        this.maxCount = maxCount;
    }

    private Integer maxCount = 1000;

    @Override
    public boolean add(Product product) {
        return size() == maxCount ? addAfterMaxCount(product):super.add(product);
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
