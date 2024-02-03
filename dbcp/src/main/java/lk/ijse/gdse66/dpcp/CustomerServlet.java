package lk.ijse.gdse66.dpcp;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "Customer", value = "/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext sc = getServletContext();
        BasicDataSource dbcp = (BasicDataSource) sc.getAttribute("dbcp");

        try(Connection connection =dbcp.getConnection()) {
            System.out.println(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
