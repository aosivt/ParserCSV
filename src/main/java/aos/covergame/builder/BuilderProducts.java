package aos.covergame.builder;

import aos.covergame.model.Product;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class BuilderProducts {

    private volatile ProductCollection products = new ProductCollection(100);

    private BuilderProducts(){}

    public static BuilderProducts build(String pathToFilesCsv) {

        final BuilderProducts builderProducts = new BuilderProducts();

        try(Stream<Path> pathFiles = Files.walk(Paths.get(pathToFilesCsv))){

            pathFiles
                    .parallel()
                                .map(Path::toFile)
                                .filter(File::isFile)
                                .map(builderProducts.bufferedReader)
                                .forEach((bReader) -> {
                                    try {
                                        bReader.lines().skip(1)
                                                .parallel()
                                                .map(builderProducts.mapToItem)
                                                .forEach(builderProducts::addToProductCollection);
                                        bReader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builderProducts;
    }

    private Function<File,BufferedReader> bufferedReader = (csvFile) ->{
        BufferedReader br = null;
        try {
            InputStream inputFS = new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(inputFS));
        } catch (IOException e){
            e.printStackTrace();
        }
        return br;
    };

    private synchronized void addToProductCollection(Product product){
        if(Collections.frequency(products,product) == 20){
            return;
        }
        products.add(product);
    }

    private Function<String, Product> mapToItem = (line) -> {
        try{
            String[] p = line.split(";");
            Product item = new Product();
            item.setProductID(Integer.parseInt(p[0]));
            item.setName(p[1]);
            item.setCondition(p[2]);
            item.setState(p[3]);
            item.setPrice(Float.parseFloat(p[4]));
            return item;
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return null;
    };

    public ProductCollection getProducts() {
        return products;
    }

}
