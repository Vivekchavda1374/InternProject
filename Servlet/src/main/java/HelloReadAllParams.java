import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/readAllParams")
public class HelloReadAllParams extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Read All Parameters</title>
                <style>
                    table { border-collapse: collapse; width: 60%; }
                    th, td { border: 1px solid black; padding: 8px; }
                    th { background-color: #ccc; }
                </style>
            </head>
            <body>
                <h2>All Request Parameters</h2>
                <table>
                    <tr>
                        <th>Parameter Name</th>
                        <th>Parameter Value(s)</th>
                    </tr>
        """);

        Map<String, String[]> paramMap = request.getParameterMap();

        if (paramMap.isEmpty()) {
            out.println("<tr><td colspan='2'>No Parameters Found</td></tr>");
        } else {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                out.println("<tr>");
                out.println("<td>" + entry.getKey() + "</td>");
                out.println("<td>");

                String[] values = entry.getValue();
                if (values.length == 1) {
                    out.println(values[0]);
                } else {
                    out.println("<ul>");
                    for (String v : values) {
                        out.println("<li>" + v + "</li>");
                    }
                    out.println("</ul>");
                }

                out.println("</td>");
                out.println("</tr>");
            }
        }

        out.println("""
                </table>
            </body>
            </html>
        """);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response); // reuse logic
    }
}
