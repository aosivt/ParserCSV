package aos.covergame.builders;

import aos.covergame.model.Product;
import aos.covergame.model.ProductCollection;
import aos.covergame.model.ProductQueue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class BuilderProductCollection {

    private volatile ProductQueue products = new ProductQueue();

    private static final Integer COUNT_SKIPPED_ROW_INSIDE_CSV = 1;

    private static final String DELIMITER_INSIDE_ROW = ";";

    private BuilderProductCollection() {
    }

    public static BuilderProductCollection build(String pathToFilesCsv) {

        Executors.newFixedThreadPool(4);

        final BuilderProductCollection builderProducts = new BuilderProductCollection();

        try (Stream<Path> pathFiles = Files.walk(Paths.get(pathToFilesCsv))) {

            pathFiles
                    .parallel()
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .map(builderProducts.bufferedReader)
                    .flatMap(builderProducts.add)
                    .forEach(builderProducts.closeBuffer)
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builderProducts;
    }

    private Function<BufferedReader, Stream<BufferedReader>> add = (bReader) -> {
        bReader.lines().skip(COUNT_SKIPPED_ROW_INSIDE_CSV)
                .parallel()
                .map(this.parserRowCsv)
                .forEach(this::addToProductCollection);
        return Stream.of(bReader);
    };

    private Consumer<BufferedReader> closeBuffer = (bReader) -> {
        try {
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private Function<File, BufferedReader> bufferedReader = (csvFile) -> {
        BufferedReader br = null;
        try {
            InputStream inputFS = new FileInputStream(csvFile);
            br = new BufferedReader(new InputStreamReader(inputFS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return br;
    };

    private Function<String, Product> parserRowCsv = (line) -> {
        final String[] rowCsv = line.split(DELIMITER_INSIDE_ROW);
        final Product product = new Product();

        Arrays.asList(BuilderProduct.values())
                .parallelStream()
                .forEach(builder -> builder.setFieldProduct(product, rowCsv));

        return product;
    };


    private synchronized void addToProductCollection(Product product) {
        products.add(product);
    }

    public ProductQueue getProducts() {
        return products;
    }
}
