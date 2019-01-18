package aos.covergame.builders;

import aos.covergame.model.Product;

public enum BuilderProduct {

    PRODUCT_ID{
        @Override
        public void setFieldProduct(Product product,String[] rowCsv) {
            product.setProductID(getValue(rowCsv[ordinal()],Integer.class));
        }
    },
    NAME{
        @Override
        public void setFieldProduct(Product product,String[] rowCsv) {
            product.setName(getValue(rowCsv[ordinal()],String.class));
        }
    },
    CONDITION{
        @Override
        public void setFieldProduct(Product product,String[] rowCsv) {
            product.setCondition(getValue(rowCsv[ordinal()],String.class));
        }
    },
    STATE{
        @Override
        public void setFieldProduct(Product product,String[] rowCsv) {
            product.setState(getValue(rowCsv[ordinal()],String.class));
        }
    },
    PRICE{
        @Override
        public void setFieldProduct(Product product,String[] rowCsv) {
            product.setPrice(getValue(rowCsv[ordinal()],Float.class));
        }
    };

    public abstract void setFieldProduct(Product product,String[] rowCsv);

    protected <T> T getValue(String value, Class<T> typeKey){
        return  tryParseInt(value)?typeKey.cast(Integer.parseInt(value)):
                tryParseFloat(value)?typeKey.cast(Float.parseFloat(value)):
                typeKey.cast(value);
    }

    protected boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    protected boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
