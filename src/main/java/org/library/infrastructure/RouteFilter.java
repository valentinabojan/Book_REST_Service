package org.library.infrastructure;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RouteFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String path = req.getRequestURI();
        System.out.println(path);
        if (path.startsWith("/"))
            path = path.substring(1);

        if (!path.isEmpty() && !path.startsWith("api") && !path.endsWith(".js") && !path.endsWith(".css")) {
            System.out.println("Redirected " + path);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("");
            requestDispatcher.forward(request, response);
        }

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    public void destroy() {
        //we can close resources here
    }
}
