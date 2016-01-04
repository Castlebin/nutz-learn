package com.heller.nutzbook.mvc;

import org.nutz.mvc.NutFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// 如果访问/druid或/rs下的路径,就无条件跳过NutFilter
public class NutzBookNutFilter extends NutFilter {

    protected Set<String> prefixs = new HashSet<>();

    @Override
    public void init(FilterConfig conf) throws ServletException {
        super.init(conf);
        prefixs.add(conf.getServletContext().getContextPath() + "/druid/");
        prefixs.add(conf.getServletContext().getContextPath() + "/rs/");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // 如果访问/druid或/rs下的路径,就无条件跳过NutFilter
        if (req instanceof HttpServletRequest) {
            String uri = ((HttpServletRequest)req).getRequestURI();
            for (String prefix : prefixs) {
                if (uri.startsWith(prefix)) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }

        super.doFilter(req, resp, chain);
    }
}
