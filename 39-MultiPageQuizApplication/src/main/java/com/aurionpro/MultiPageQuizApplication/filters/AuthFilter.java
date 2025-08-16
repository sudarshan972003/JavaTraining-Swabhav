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

@WebFilter(filterName ="AuthFilter", urlPatterns="/user/*")
public class AuthFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse p = (HttpServletResponse) res;
		HttpSession s = r.getSession(false);
		if (s == null || s.getAttribute("user") == null) {
			p.sendRedirect(r.getContextPath() + "/user-login.html");
			return;
		}
		chain.doFilter(req, res);
	}
}
