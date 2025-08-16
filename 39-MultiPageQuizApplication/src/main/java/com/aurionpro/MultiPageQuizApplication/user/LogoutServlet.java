package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession(false);
		if (s != null)
			s.invalidate();
		resp.sendRedirect(req.getContextPath() + "/index.html");
	}
}
