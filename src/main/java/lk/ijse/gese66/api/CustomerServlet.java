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

@WebServlet(name = "CustomerServlet" ,urlPatterns = "/customers",loadOnStartup = 1)

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
                String contact = rst.getString("contact");
                System.out.printf("id=%s, name=%s, address=%s ,contact=%s\n",id,name,address,contact);

                customerList.add(new CustomerDTO(id, name, address,contact));
            }

            resp.setContentType("application/json"); //set the MIME type of the content of the response (Thus, add response header called "Content-Type")

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customerList,resp.getWriter());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);
        String id = customerDTO.getId();
        String name = customerDTO.getName();
        String address = customerDTO.getAddress();
        String contact =customerDTO.getContact();


        if(id==null || !id.matches("C\\d{3}")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is empty or invalid");
            return;
        } else if (name == null || !name.matches("[A-Za-z ]+")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name is empty or invalid");
            return;
        } else if (address == null || address.length() < 3) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address is empty or invalid");
            return;
        }

        System.out.printf("id=%s, name=%s, address=%s ,contact=%s\n", id,name,address,contact);


        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO customer(id, name, address,contact) VALUES (?,?,?,?)");

            stm.setString(1,id);
            stm.setString(2, name);
            stm.setString(3, address);
            stm.setString(4,contact);

            if (stm.executeUpdate() != 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Added customer successfully");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save the customer");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
