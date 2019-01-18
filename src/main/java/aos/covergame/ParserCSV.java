package aos.covergame;

import aos.covergame.builders.BuilderProductCollection;
import aos.covergame.model.ProductCollection;
import aos.covergame.model.Product;
import aos.covergame.model.ProductQueue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.function.Function;

public class ParserCSV {

    private static final String FORMAT_STRING_RESULT_CSV_FILE = "%s\n%s";

    private static final String DEFAULT_PATH_TO_CSV_FILES = "csv";

    private static final String DEFAULT_NAME_RESULT_FILE = "result.txt";

    public static void main(String[] args) {
        final String pathToCsv = args.length > 0 ? args[0] : DEFAULT_PATH_TO_CSV_FILES;

        ProductQueue products = BuilderProductCollection.build(pathToCsv).getProducts();

        createCsv(products);

    }

    private static void createCsv(final Collection<Product> products) {

        products.stream()
                .map(Product::toString)
                .forEach(row ->
                        {
                            try {
                                Files.write(Paths.get(DEFAULT_NAME_RESULT_FILE), row.getBytes(), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }
}
