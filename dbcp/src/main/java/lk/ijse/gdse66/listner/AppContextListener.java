package lk.ijse.gdse66.listner;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource dbcp = new BasicDataSource(); //create a connection pool
        dbcp.setUsername("root");
        dbcp.setPassword("1234");
        dbcp.setUrl("jdbc:mysql://localhost:3306/thogakade1");
        dbcp.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dbcp.setInitialSize(2);
        dbcp.setMaxTotal(5);

        ServletContext sc = sce.getServletContext();
        sc.setAttribute("dbcp",dbcp);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        BasicDataSource dbcp = (BasicDataSource) sc.getAttribute("dbcp");
        try {
            dbcp.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
