import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/requestDemo")
public class RequestAllMethodsDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body><h2>HttpServletRequest Methods Demo</h2>");

        // 1. Cookies
        Cookie[] cookies = request.getCookies();
        out.println("<h3>Cookies</h3>");
        if (cookies != null) {
            for (Cookie c : cookies) {
                out.println(c.getName() + " = " + c.getValue() + "<br>");
            }
        }

        // 2. Attribute Names
        out.println("<h3>Attributes</h3>");
        Enumeration<String> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String name = attrNames.nextElement();
            out.println(name + " = " + request.getAttribute(name) + "<br>");
        }

        // 3. Header Names
        out.println("<h3>Headers</h3>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String h = headerNames.nextElement();
            out.println(h + " = " + request.getHeader(h) + "<br>");
        }

        // 4. Parameter Names
        out.println("<h3>Parameters</h3>");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String p = paramNames.nextElement();
            out.println(p + " = " + Arrays.toString(request.getParameterValues(p)) + "<br>");
        }

        // 5 & 6. Session
        HttpSession session = request.getSession();
        out.println("<h3>Session ID: " + session.getId() + "</h3>");

        // 7. Locale
        out.println("Locale: " + request.getLocale() + "<br>");

        // 8. getAttribute
        out.println("Sample Attribute (test): " + request.getAttribute("test") + "<br>");

        // 9. Input Stream
        out.println("InputStream available<br>");

        // 10. Auth Type
        out.println("Auth Type: " + request.getAuthType() + "<br>");

        // 11. Character Encoding
        out.println("Character Encoding: " + request.getCharacterEncoding() + "<br>");

        // 12. Content Type
        out.println("Content Type: " + request.getContentType() + "<br>");

        // 13. Context Path
        out.println("Context Path: " + request.getContextPath() + "<br>");

        // 14. Single Header
        out.println("User-Agent: " + request.getHeader("User-Agent") + "<br>");

        // 15. HTTP Method
        out.println("Method: " + request.getMethod() + "<br>");

        // 16 & 26. Parameters
        out.println("Parameter(name): " + request.getParameter("name") + "<br>");

        // 17. Path Info
        out.println("Path Info: " + request.getPathInfo() + "<br>");

        // 18. Protocol
        out.println("Protocol: " + request.getProtocol() + "<br>");

        // 19. Query String
        out.println("Query String: " + request.getQueryString() + "<br>");

        // 20 & 21. Remote info
        out.println("Remote Addr: " + request.getRemoteAddr() + "<br>");
        out.println("Remote Host: " + request.getRemoteHost() + "<br>");

        // 22. Remote User
        out.println("Remote User: " + request.getRemoteUser() + "<br>");

        // 23. Request URI
        out.println("Request URI: " + request.getRequestURI() + "<br>");

        // 24. Requested Session ID
        out.println("Requested Session ID: " + request.getRequestedSessionId() + "<br>");

        // 25. Servlet Path
        out.println("Servlet Path: " + request.getServletPath() + "<br>");

        // 27. Secure
        out.println("Is Secure: " + request.isSecure() + "<br>");

        // 28. Content Length
        out.println("Content Length: " + request.getContentLength() + "<br>");

        // 29. Int Header
        out.println("Content-Length Header: " + request.getIntHeader("Content-Length") + "<br>");

        // 30. Server Port
        out.println("Server Port: " + request.getServerPort() + "<br>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
