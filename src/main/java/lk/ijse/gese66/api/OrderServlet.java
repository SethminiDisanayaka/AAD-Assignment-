package lk.ijse.gese66.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gese66.dto.ItemDTO;
import lk.ijse.gese66.dto.OrderDTO;
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

@WebServlet(name = "OrderServlet" ,urlPatterns = "/orders",loadOnStartup = 1)


public class OrderServlet extends HttpServlet {

    BasicDataSource pool;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        pool = (BasicDataSource) sc.getAttribute("dbcp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM order");
            ResultSet rst = stm.executeQuery();

            ArrayList<OrderDTO> orderList = new ArrayList<>();

            while (rst.next()){
                String orderID = rst.getString("orderID");
                String  orderDate= rst.getString("orderDate");
                String id = rst.getString("id");

                System.out.printf("orderID=%s, orderDate=%s, id=%s\n",orderID,orderDate,id);

                orderList.add(new OrderDTO(orderID, orderDate, id));
            }

            resp.setContentType("application/json");

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(orderList,resp.getWriter());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
