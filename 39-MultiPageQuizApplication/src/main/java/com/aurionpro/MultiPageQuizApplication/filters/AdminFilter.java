package com.aurionpro.MultiPageQuizApplication.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName ="AdminFilter", urlPatterns="/admin/*")
public class AdminFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse p = (HttpServletResponse) res;
		if (r.getRequestURI().endsWith("/admin/login")) {
			chain.doFilter(req, res);
			return;
		}
		HttpSession s = r.getSession(false);
		if (s == null || s.getAttribute("isAdmin") == null) {
			p.sendRedirect(r.getContextPath() + "/admin-login.html");
			return;
		}
		chain.doFilter(req, res);
	}
}
