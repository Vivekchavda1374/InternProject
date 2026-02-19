import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;

@WebServlet("/responseDemo")
public class ResponseAllMethodsDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Basic response setup
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setLocale(Locale.US);
        response.setStatus(HttpServletResponse.SC_OK);

        // Cookie
        Cookie cookie = new Cookie("username", "Vivek");
        response.addCookie(cookie);

        // Headers
        response.addHeader("App-Name", "ResponseDemo");
        response.addIntHeader("Version", 1);
        response.addDateHeader("Server-Time", new Date().getTime());

        PrintWriter out = response.getWriter();

        out.println("""
            <html>
            <head><title>Response Methods Demo</title></head>
            <body>
            <h2>HttpServletResponse Methods (Visible)</h2>
        """);

        // URL encoding
        out.println("encodeURL: "
                + response.encodeURL("home") + "<br>");
        out.println("encodeRedirectURL: "
                + response.encodeRedirectURL("next") + "<br>");

        // Header check
        out.println("containsHeader(App-Name): "
                + response.containsHeader("App-Name") + "<br>");

        // Committed status
        out.println("isCommitted: "
                + response.isCommitted() + "<br>");

        out.println("""
            <hr>
            <a href="responseDemo?action=redirect">Send Redirect</a><br>
            <a href="responseDemo?action=error">Send Error</a>
            </body>
            </html>
        """);

        // Handle actions
        String action = request.getParameter("action");

        if ("redirect".equals(action)) {
            response.sendRedirect("https://www.google.com");
        }

        if ("error".equals(action)) {
            response.sendError(404, "Custom Error from Servlet");
        }
    }
}
