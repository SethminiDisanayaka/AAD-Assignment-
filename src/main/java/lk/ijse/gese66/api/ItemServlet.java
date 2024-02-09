package lk.ijse.gese66.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gese66.dto.CustomerDTO;
import lk.ijse.gese66.dto.ItemDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ItemServlet" ,urlPatterns = "items" , loadOnStartup = 1)
public class ItemServlet extends HttpServlet {

    BasicDataSource pool;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        pool = (BasicDataSource) sc.getAttribute("dbcp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item");
            ResultSet rst = stm.executeQuery();

            ArrayList<ItemDTO> itemList = new ArrayList<>();

            while (rst.next()){
                String itemId = rst.getString("itemId");
                String description = rst.getString("description");
                String unitPrice = rst.getString("unitPrice");
                String quantity = rst.getString("quantity");
                System.out.printf("itemId=%s, description=%s, unitPrice=%s ,quantity=%s\n",itemId,description,unitPrice,quantity);

                itemList.add(new ItemDTO(itemId, description, unitPrice,quantity));
            }

            resp.setContentType("application/json");

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemList,resp.getWriter());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
        String itemId = itemDTO.getItemID();
        String description = itemDTO.getDescription();
        String unitPrice = itemDTO.getUnitPrice();
        String quantity =itemDTO.getQuantity();


        if(itemId==null || !itemId.matches("I\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is empty or invalid");
            return;
        } else if (description == null || !description.matches("[A-Za-z ]+")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is empty or invalid");
            return;
        }

        System.out.printf("itemId=%s, description=%s, unitPrice=%s ,quantity=%s\n", itemId,description,unitPrice,quantity);


        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO item(itemId, description, unitPrice,quantity) VALUES (?,?,?,?)");

            stm.setString(1,itemId);
            stm.setString(2, description);
            stm.setString(3, unitPrice);
            stm.setString(4,quantity);

            if (stm.executeUpdate() != 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Added customer successfully");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the item");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemId = req.getParameter("itemId");

        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("DELETE FROM item WHERE itemId=?");
            stm.setString(1,itemId);

            if(stm.executeUpdate() != 0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete the item!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);
        String itemId = itemDTO.getItemID();
        String description = itemDTO.getDescription();
        String unitPrice = itemDTO.getUnitPrice();
        String quantity =itemDTO.getQuantity();


        System.out.printf("itemId=%s, description=%s, unitPrice=%s ,quantity=%s\n", itemId,description,unitPrice,quantity);

        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("UPDATE item SET description=?, unitPrice=? ,quantity=? WHERE itemId=?");

            stm.setString(1,itemId);
            stm.setString(2, description);
            stm.setString(3, unitPrice);
            stm.setString(4,quantity);
            if (stm.executeUpdate() != 0) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else{
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update the customer");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
