import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/insertEmployee")
public class EmployeeInsertServlet extends HttpServlet {

    private static final String DB_URL =
            "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Sample values (YOU CAN CHANGE)
        String empName = "Vivek";
        String email = "vivek@gmail.com";
        double salary = 60000.50;
        String[] skills = {"Java", "Spring", "PostgreSQL"};
        String address = "Ahmedabad";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = """
                INSERT INTO employee (emp_name, email, salary, skills, address)
                VALUES (?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, empName);
            ps.setString(2, email);
            ps.setDouble(3, salary);

            Array skillArray = conn.createArrayOf("text", skills);
            ps.setArray(4, skillArray);

            ps.setString(5, address);

            int rows = ps.executeUpdate();

            out.println("<h2>Employee Inserted Successfully</h2>");
            out.println("Rows affected: " + rows);

            ps.close();
            conn.close();

        } catch (Exception e) {
            out.println("<b>Error:</b> " + e.getMessage());
        }
    }
}
