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
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Function;
import java.util.stream.Stream;

public class ParserCSV {

    private static final String FORMAT_STRING_RESULT_CSV_FILE = "%s\n%s";

    private static final String DEFAULT_PATH_TO_CSV_FILES = "csv";

    private static final String DEFAULT_NAME_RESULT_FILE = "result.txt";

    private static final String HEAD_CSV_FILE = "product ID;Name;Condition;State;Price;\n";

    static {
        try {
            Files.delete(Paths.get(DEFAULT_NAME_RESULT_FILE));
            Files.createFile(Paths.get(DEFAULT_NAME_RESULT_FILE));
            ParserCSV.write(HEAD_CSV_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        final String pathToCsv = args.length > 0 ? args[0] : DEFAULT_PATH_TO_CSV_FILES;

        ProductQueue products = BuilderProductCollection.build(pathToCsv).getProducts();

        createCsv(products);
    }

    private static void createCsv(final Collection<Product> products) {
        products.stream()
                .map(Product::toString).forEach(ParserCSV::write);
    }

    private static void createCsv(final ProductQueue products) {
        drainToStream(products)
                .limit(1000)
                .map(Product::toString).forEach(ParserCSV::write);
    }

    private static void write(String row){
        try {
            Files.write(Paths.get(DEFAULT_NAME_RESULT_FILE), row.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static <T> Stream<T> drainToStream(PriorityBlockingQueue<T> queue) {
        Objects.requireNonNull(queue);
        return Stream.generate(queue::poll).limit(queue.size());
    }

}
