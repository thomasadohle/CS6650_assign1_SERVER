import com.fasterxml.jackson.databind.ObjectMapper;
import models.Purchase;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String str = "{\n" +
                "\t\"items\": [{\n" +
                "\t\t\"itemID\": \"string\",\n" +
                "\t\t\"numberOfItems\": 0\n" +
                "\t}]\n" +
                "}";
        Purchase p = mapper.readValue(str, Purchase.class);
        System.out.println(p);
    }
}
