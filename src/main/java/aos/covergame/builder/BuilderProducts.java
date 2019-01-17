package aos.covergame.builder;

import aos.covergame.model.Product;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BuilderProducts {

    private static String TYPE_FILE = "csv";

//    final PriorityQueue<Product> products = new PriorityQueue<>();
    private volatile ProductCollection products = new ProductCollection(100);

    private BuilderProducts(){}

    public static BuilderProducts build(String pathToFilesCsv) {

        final BuilderProducts builderProducts = new BuilderProducts();

        try(Stream<Path> pathFiles = Files.walk(Paths.get(pathToFilesCsv))){

            pathFiles
                    .parallel()
                                .map(Path::toFile)
                                .filter(File::isFile)
                                .filter(builderProducts::isCsv)
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

    private boolean isCsv(File file) {
        boolean isCsv = false;
        try {
            isCsv = FilenameUtils.getExtension(file.getName()).equals(TYPE_FILE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return isCsv;
    }
    private void closeBufferedReader (BufferedReader br){
        try {
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private Function<String, Product> mapToItem = (line) -> {
        String[] p = line.split(";");
        Product item = new Product();
        item.setProductID(Integer.parseInt(p[0]));
        item.setName(p[1]);
        item.setCondition(p[2]);
        item.setState(p[3]);
        item.setPrice(Float.parseFloat(p[4]));
        return item;
    };

    public ProductCollection getProducts() {
        return products;
    }

}
