import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/employee-data")
public class EmployeeDataServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"data\":[");

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT emp_id, emp_name, email, salary, address FROM employee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                        .append("\"emp_id\":").append(rs.getInt("emp_id")).append(",")
                        .append("\"emp_name\":\"").append(rs.getString("emp_name")).append("\",")
                        .append("\"email\":\"").append(rs.getString("email")).append("\",")
                        .append("\"salary\":").append(rs.getDouble("salary")).append(",")
                        .append("\"address\":\"").append(rs.getString("address")).append("\"")
                        .append("}");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        json.append("]}");
        out.print(json.toString());
    }
}
