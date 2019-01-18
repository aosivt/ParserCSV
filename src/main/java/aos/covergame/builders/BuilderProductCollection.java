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
import java.util.function.Function;
import java.util.stream.Stream;

public class BuilderProductCollection {

    private volatile ProductQueue products = new ProductQueue(100);

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
                    .forEach((bReader) -> {
                        try {
                            bReader.lines().skip(COUNT_SKIPPED_ROW_INSIDE_CSV)
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

    private Function<String, Product> mapToItem = (line) -> {
        final String[] rowCsv = line.split(DELIMITER_INSIDE_ROW);
        final Product product = new Product();

        Arrays.asList(BuilderProduct.values())
                .parallelStream()
                .forEach(builder -> builder.setFieldProduct(product, rowCsv));

        return product;
    };

    private synchronized void addToProductCollection(Product product) {
//        pollDataFromQueue(products);
        products.add(product);
    }

    public ProductQueue getProducts() {
        return products;
    }
    private static void pollDataFromQueue(Queue<Product> customerPriorityQueue) {
        while(true){
            Product cust = customerPriorityQueue.poll();
            if(cust == null) break;
            System.out.println("Обработка клиента с id=" + cust.getProductID());
        }
    }
    public static Comparator<Product> idComparator = new Comparator<Product>(){
        @Override
        public int compare(Product c1, Product c2) {
            return (int) (c1.getPrice() - c2.getPrice());
        }
    };
}
