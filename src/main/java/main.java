import com.fasterxml.jackson.databind.ObjectMapper;
import models.Purchase;
import models.PurchaseItem;

import javax.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        //JsonObject empObject = Json.createObjectBuilder();

        PurchaseItem itemOne = new PurchaseItem();
        itemOne.setItemID("1");
        itemOne.setNumberOfItems(4);

        PurchaseItem itemTwo = new PurchaseItem();
        itemTwo.setNumberOfItems(5);
        itemTwo.setItemID("2");

        Purchase purchase = new Purchase();
        List<PurchaseItem> items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);
        purchase.setItems(items);

        String purch = mapper.writeValueAsString(purchase);

        System.out.println(purch);




    }
}
