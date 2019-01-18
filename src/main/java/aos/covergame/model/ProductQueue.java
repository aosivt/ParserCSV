package aos.covergame.model;

import java.util.*;

public class ProductQueue extends PriorityQueue<Product> {

    private static final Integer DEFAULT_MAX_COUNT = 1000;
    private static final Integer DEFAULT_IDENTICAL_OBJECTS = 20;

    private Integer maxCount = DEFAULT_MAX_COUNT;
    private Integer countIdenticalObject = DEFAULT_IDENTICAL_OBJECTS;

    public ProductQueue() {
        super(DEFAULT_MAX_COUNT);
    }

    public ProductQueue(Integer maxCount) {
        super(maxCount);
        this.maxCount = maxCount;
    }

    public ProductQueue(Integer maxCount, Integer countIdenticalObject) {
        super(maxCount);
        this.maxCount = maxCount;
        this.countIdenticalObject = countIdenticalObject;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    @Override
    public Comparator<? super Product> comparator() {
        return idComparator;
    }

    private static Comparator<Product> idComparator = new Comparator<Product>(){
        @Override
        public int compare(Product c1, Product c2) {
            return (int) (c1.getProductID() - c2.getProductID());
        }
    };
}
