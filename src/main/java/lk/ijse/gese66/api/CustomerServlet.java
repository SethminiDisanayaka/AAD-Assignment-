package lk.ijse.gese66.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gese66.dto.CustomerDTO;
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

@WebServlet(name = "CustomerServlet" ,value = "/customers",loadOnStartup = 1)

public class CustomerServlet extends HttpServlet {
    BasicDataSource pool;

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        pool = (BasicDataSource) sc.getAttribute("dbcp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM customer");
            ResultSet rst = stm.executeQuery();

            ArrayList<CustomerDTO> customerList = new ArrayList<>();

            while (rst.next()){
                String id = rst.getString("id");
                String name = rst.getString("name");
                String address = rst.getString("address");
                System.out.printf("id=%s, name=%s, address=%s\n",id,name,address);

                customerList.add(new CustomerDTO(id, name, address));
            }

            resp.setContentType("application/json"); //set the MIME type of the content of the response (Thus, add response header called "Content-Type")

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customerList,resp.getWriter());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
