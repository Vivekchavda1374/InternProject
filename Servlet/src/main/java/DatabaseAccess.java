import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/employees")
public class DatabaseAccess extends HttpServlet {

    private static final String DB_URL =
            "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("""
            <!DOCTYPE html>
            <html>
            <head><title>Employee Data</title></head>
            <body bgcolor="#f0f0f0">
            <h2 align="center">Employee Table Data</h2>
            <hr>
        """);

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT emp_id, emp_name, email, salary, skills, address FROM employee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("emp_id");
                String name = rs.getString("emp_name");
                String email = rs.getString("email");
                double salary = rs.getDouble("salary");
                Array skillsArray = rs.getArray("skills");
                String address = rs.getString("address");

                String[] skills = (String[]) skillsArray.getArray();

                out.println("<b>ID:</b> " + id + "<br>");
                out.println("<b>Name:</b> " + name + "<br>");
                out.println("<b>Email:</b> " + email + "<br>");
                out.println("<b>Salary:</b> " + salary + "<br>");
                out.println("<b>Skills:</b> ");
                for (String s : skills) {
                    out.println(s + " ");
                }
                out.println("<br>");
                out.println("<b>Address:</b> " + address + "<br><hr>");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<b>Error:</b> " + e.getMessage());
        }

        out.println("</body></html>");
    }
}
