package servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Purchase;
import models.PurchaseItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
//import com.google.gson.Gson;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class PurchaseServlet extends HttpServlet {

    private String successMsg;
    private String failureMsg;
    private Map<String, String> purchaseParamMap;
    private Connection cnxn;
    int purchaseId;
    private Purchase purchase;
    private SQLException exception = null;
    private Logger logger = LogManager.getLogger(PurchaseServlet.class);

    public void init() throws ServletException {
        // Initialization
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        successMsg = "Successful request";
        failureMsg = "Unsuccessful request";
        purchaseParamMap = new HashMap<>();
        try {
            cnxn = DriverManager.getConnection("jdbc:mysql://thomas-db.c64ylwt4ybkn.us-east-1.rds.amazonaws.com:3306/purchase", "admin", "icecoldnewenglandipa");
        } catch (SQLException e){
            e.printStackTrace();
            exception = e;
            logger.error(e);
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (exception != null){
            response.setStatus(401);
            response.getWriter().write(exception.toString());
            return;
        }
        boolean validRequest = true;
        if (! urlValid(request)){
            //Return 400 response
            response.setStatus(400);
            validRequest = false;
        }
        if (! postDataValid(request)){
            response.setStatus(400);
            validRequest = false;
        }
        String message;
        if (validRequest){
            message = this.successMsg;
            try {
                storePurchase();
                storePurchaseItems();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            message = this.failureMsg;
        }

        // Set response content type to text
        response.setContentType("application/json");
        // Send the response
        PrintWriter out = response.getWriter();
        String jsonString = new JSONObject().put("message", message).toString();
        out.println(jsonString);
    }

    private void throwException() throws Exception {
        if (exception != null){
            throw new Exception(exception);
        }
    }

    private boolean postDataValid(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Purchase p = mapper.readValue(req.getReader(), Purchase.class);
            purchase = p;

        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean urlValid(HttpServletRequest req){
        String path = req.getPathInfo();
        String[] urlParams = path.split("/");
        if (urlParams.length != 6){
            return false;
        }
        Map<String, String> paramMap = mapUrl(urlParams);
        if (! paramsValid(paramMap)){
            return false;
        }
        return true;
    }

    private boolean paramsValid(Map<String, String> paramMap){
        // Validate storeID is an Integer
        try{
            int storeId = Integer.parseInt(paramMap.get("storeId"));
            purchaseParamMap.put("storeId", paramMap.get("storeId"));
        } catch (NumberFormatException e){
            return false;
        }
        // Must equal "customerTitle"
        String customerTitle = paramMap.get("customerTitle");
        if (! customerTitle.equals("customer")){
            return false;
        }
        // Must be an Integer
        try{
            Integer.parseInt(paramMap.get("customerId"));
            purchaseParamMap.put("customerId", paramMap.get("customerId"));
        } catch (NumberFormatException e){
            return false;
        }
        // Must equal "date"
        String dateTitle = paramMap.get("dateTitle");
        if (! dateTitle.equals("date")){
            return false;
        }
        // Must be a valid date in yyyyMMdd format
        String date = paramMap.get("date");
        try{
            new SimpleDateFormat("yyyyDDmm").parse(date);
            purchaseParamMap.put("date", date);
        } catch(ParseException e){
            return false;
        }
        return true;
    }

    private Map<String, String> mapUrl(String[] urlParams) {
        Map <String, String> paramMap = new HashMap<>();
        paramMap.put("storeId", urlParams[1]);
        paramMap.put("customerTitle", urlParams[2]);
        paramMap.put("customerId", urlParams[3]);
        paramMap.put("dateTitle", urlParams[4]);
        paramMap.put("date", urlParams[5]);
        return paramMap;
    }

    private void storePurchase() throws SQLException {
        Statement statement = cnxn.createStatement();
        String query = "INSERT INTO purchase (store_id, customer_id, purchase_date) VALUES(";
        query += purchaseParamMap.get("storeId") + ",";
        query += purchaseParamMap.get("customerId") + ",";
        query += purchaseParamMap.get("date") + ");";
        statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet keys = statement.getGeneratedKeys();
        keys.first();
        purchaseId = keys.getInt(1);
        statement.close();
    }

    private void storePurchaseItems() throws SQLException {
        String query = "INSERT INTO purchase_item (purchase_id, item_id, num_items) VALUES (?,?,?)";
        PreparedStatement statement = cnxn.prepareStatement(query);
        for (PurchaseItem item: purchase.getItems()){
            statement.setInt(1, purchaseId);
            statement.setString(2, item.getItemID());
            statement.setInt(3, item.getNumberOfItems());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }


    public void destroy() {
        try {
            cnxn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
