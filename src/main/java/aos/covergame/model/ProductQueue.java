package aos.covergame.model;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class ProductQueue extends PriorityBlockingQueue<Product> {

    private static final Integer DEFAULT_IDENTICAL_OBJECTS = 20;

    private Integer countIdenticalObject = DEFAULT_IDENTICAL_OBJECTS;

    public ProductQueue() {}

    public ProductQueue(Integer countIdenticalObject) {
        this.countIdenticalObject = countIdenticalObject;
    }

    @Override
    public boolean add(Product product) {
        return Collections.frequency(this, product) != countIdenticalObject && super.add(product);
    }

}
