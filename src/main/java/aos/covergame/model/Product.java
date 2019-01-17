package aos.covergame.model;

public class Product implements Comparable{

    private Integer productID;
    private String  name, condition, state;
    private Float   price;

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s",productID,name,condition,state,price);
    }

    @Override
    public boolean equals(Object o) {
        return ((Product)o).getProductID().equals(this.getProductID());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return (int)(price * 100 - ((Product) o).getPrice() * 100);
    }
}
