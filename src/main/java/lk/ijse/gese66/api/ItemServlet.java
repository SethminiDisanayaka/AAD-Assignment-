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

            resp.setContentType("application/json"); //set the MIME type of the content of the response (Thus, add response header called "Content-Type")

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemList,resp.getWriter());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
