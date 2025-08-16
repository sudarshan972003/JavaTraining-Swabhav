package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.PasswordUtil;
import com.aurionpro.MultiPageQuizApplication.Util.ValidationUtil;
import com.aurionpro.MultiPageQuizApplication.dao.UserDAO;
import com.aurionpro.MultiPageQuizApplication.model.User;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth/login")
public class UserLoginServlet extends HttpServlet {
	private final UserDAO dao = new UserDAO();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String email = req.getParameter("email");
			String pw = req.getParameter("password");
			ValidationUtil.password(pw);
			User u = dao.findByEmail(email);
			if (u == null || !u.getPasswordHash().equals(PasswordUtil.sha256(pw)))
				throw new IllegalArgumentException("Invalid email or password.");
			HttpSession s = req.getSession(true);
			s.setAttribute("user", u);
			resp.sendRedirect(req.getContextPath() + "/user/home");
		} catch (Exception e) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<div class='error'>" + e.getMessage() + "</div><a href='" + req.getContextPath()
					+ "/user-login.html'>Back</a>");
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(req.getContextPath() + "/user-login.html");
	}
}
