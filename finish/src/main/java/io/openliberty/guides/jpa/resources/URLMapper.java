// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.jpa.resources;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;

@WebFilter(urlPatterns = "/*")
public class URLMapper implements Filter {
    Pattern[] endpoints = { Pattern.compile("/events.*") };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String endpoint = ((HttpServletRequest) request).getRequestURI();
            if (endpoint.equals("/event") || endpoint.startsWith("/event/")) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "No such an endpoint");
                return;
            }
            for (Pattern pattern : endpoints) {
                if (pattern.matcher(endpoint).matches()) {
                    request.getRequestDispatcher("/event" + endpoint).forward(request,
                        response);
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
