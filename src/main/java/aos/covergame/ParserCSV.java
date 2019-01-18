package aos.covergame;

import aos.covergame.builders.BuilderProductCollection;
import aos.covergame.model.ProductCollection;
import aos.covergame.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class ParserCSV {

    public static void main(String[] args){
        final String pathToCsv = args.length > 0 ? args[0] : "csv";

        ProductCollection products = BuilderProductCollection.build(pathToCsv).getProducts();

        if (products.size() < products.getMaxCount()) products.sort(Product::compareTo);

        createCsv(products);

     }
     private static void createCsv(final Collection<Product> products){
         try {
             Files.write(Paths.get("result.txt"), products.stream().map(Product::toString)
                                    .reduce((p1,p2)->String.format("%s\n%s",p1,p2)).get().getBytes());
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

}
