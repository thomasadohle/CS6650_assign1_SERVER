import com.fasterxml.jackson.databind.ObjectMapper;
import models.Purchase;
import models.PurchaseItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException, SQLException {

//        ObjectMapper mapper = new ObjectMapper();
//        String str = "{\n" +
//                "\t\"items\": [{\n" +
//                "\t\t\"itemID\": \"string\",\n" +
//                "\t\t\"numberOfItems\": 0\n" +
//                "\t}]\n" +
//                "}";
//        Purchase p = mapper.readValue(str, Purchase.class);
//
//        //JsonObject empObject = Json.createObjectBuilder();
//
//        PurchaseItem itemOne = new PurchaseItem();
//        itemOne.setItemID("1");
//        itemOne.setNumberOfItems(4);
//
//        PurchaseItem itemTwo = new PurchaseItem();
//        itemTwo.setNumberOfItems(5);
//        itemTwo.setItemID("2");
//
//        Purchase purchase = new Purchase();
//        List<PurchaseItem> items = new ArrayList<>();
//        items.add(itemOne);
//        items.add(itemTwo);
//        purchase.setItems(items);
//
//        String purch = mapper.writeValueAsString(purchase);
//
//        System.out.println(purch);

//        Connection cxn = DriverManager.getConnection("jdbc:mysql://localhost:3307/testdb", "root", "password");
//        Statement statement = cxn.createStatement();
//        ResultSet res = statement.executeQuery("SELECT * FROM purchase");
//
//        while (res.next()){
//            System.out.println(res.getString(2));
//        }
//        Logger logger = LogManager.getLogger(main.class);
//        logger.error("hello error");

    }
}
