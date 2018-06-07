package io.openliberty.guides.eventapp.resources;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class URLMapper implements Filter {
  Pattern[] endpoints = { Pattern.compile("/events.*"),
                          Pattern.compile("/users.*")
                        };

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      String endpoint = ((HttpServletRequest) request).getRequestURI();
      if (endpoint.equals("/event") || endpoint.startsWith("/event/")) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "No such an endpoint");
        return;
      }
      for (Pattern pattern : endpoints) {
        if (pattern.matcher(endpoint).matches()) {
          request.getRequestDispatcher("/event" + endpoint).forward(request,response);
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

}