package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.PasswordUtil;
import com.aurionpro.MultiPageQuizApplication.Util.ValidationUtil;
import com.aurionpro.MultiPageQuizApplication.dao.UserDAO;
import com.aurionpro.MultiPageQuizApplication.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth/register")
public class UserRegisterServlet extends HttpServlet {
	private final UserDAO dao = new UserDAO();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String username = req.getParameter("username");
			String email = req.getParameter("email");
			String pw = req.getParameter("password");
			String cpw = req.getParameter("confirm");
			ValidationUtil.require(username, "Username");
			ValidationUtil.password(pw);
			if (!pw.equals(cpw))
				throw new IllegalArgumentException("Passwords do not match.");
			if (dao.findByEmail(email) != null)
				throw new IllegalArgumentException("Email already exists.");
			User u = new User();
			u.setUsername(username.trim());
			u.setEmail(email.trim());
			u.setPasswordHash(PasswordUtil.sha256(pw));
			int id = dao.create(u);
			u.setId(id);
			HttpSession s = req.getSession(true);
			s.setAttribute("user", u);
			resp.sendRedirect(req.getContextPath() + "/user/home");
		} catch (Exception e) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<div class='error'>" + e.getMessage() + "</div><a href='" + req.getContextPath()
					+ "/user-register.html'>Back</a>");
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(req.getContextPath() + "/user-register.html");
	}
}
