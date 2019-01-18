package aos.covergame.builders;

import aos.covergame.model.Product;
import aos.covergame.model.ProductCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Stream;

public class BuilderProductCollection {

    private volatile ProductCollection products = new ProductCollection(100);

    private BuilderProductCollection(){}

    public static BuilderProductCollection build(String pathToFilesCsv) {

        ExecutorService pool = Executors.newFixedThreadPool(4);

        final BuilderProductCollection builderProducts = new BuilderProductCollection();

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
        products.add(product);
    }

    private Function<String, Product> mapToItem = (line) -> {

        final String[] rowCsv = line.split(";");
        final Product item = new Product();

        Arrays.asList(BuilderProduct.values())
                .parallelStream()
                .forEach(builder -> builder.setFieldProduct(item,rowCsv));

        return item;
    };

    public ProductCollection getProducts() {
        return products;
    }

}
