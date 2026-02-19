import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Date;

@WebFilter("/*")
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        System.out.println("LogFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();
        System.out.println("IP: " + ip + " | Time: " + new Date());

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("LogFilter destroyed");
    }
}
