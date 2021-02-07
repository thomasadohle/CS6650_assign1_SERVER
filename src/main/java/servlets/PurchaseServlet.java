package servlets;


//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import models.Purchase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class PurchaseServlet extends HttpServlet {

    private String msg;

    public void init() throws ServletException {
        // Initialization
        msg = "Welcome to PurchaseServlet";
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (! urlValid(request)){
            //Return 400 response
            response.setStatus(400);
            return;
        }
        if (! postDataValid(request)){
            response.setStatus(400);
            return;
        }

        // Set response content type to text
        response.setContentType("application/json");
        // Send the response
        PrintWriter out = response.getWriter();
        out.println("<h1>" + msg + "</h1>");
    }

    private boolean postDataValid(HttpServletRequest req) throws IOException {
        Purchase purchase = new Gson().fromJson(req.getReader(), Purchase.class);
        return false;
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
            Integer.parseInt(paramMap.get("storeId"));
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


    public void destroy() {
        // nothing to do here
    }
}
