package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.PasswordUtil;
import com.aurionpro.MultiPageQuizApplication.dao.AdminDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {
	private final AdminDAO dao = new AdminDAO();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String email = req.getParameter("email");
		String pw = req.getParameter("password");
		try {
			boolean ok = dao.validate(email, PasswordUtil.sha256(pw));
			if (!ok)
				throw new IllegalArgumentException("Invalid admin credentials.");
			HttpSession s = req.getSession(true);
			s.setAttribute("isAdmin", true);
			resp.sendRedirect(req.getContextPath() + "/admin/menu");
		} catch (Exception e) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<div class='error'>" + e.getMessage() + "</div><a href='admin-login.html'>Back</a>");
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(req.getContextPath() + "/admin-login.html");
	}
}