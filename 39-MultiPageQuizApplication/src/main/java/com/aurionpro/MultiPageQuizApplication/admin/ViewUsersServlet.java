package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.aurionpro.MultiPageQuizApplication.Util.DBUtil;
import com.aurionpro.MultiPageQuizApplication.dao.UserDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/users")
public class ViewUsersServlet extends HttpServlet {
	private final UserDAO udao = new UserDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<!doctype html><html><head><meta charset='utf-8'><title>Users</title><link rel='stylesheet' href='"
				+ req.getContextPath() + "/css/styles.css'></head><body>");
		out.println("<div class='container'>");
		out.println("<h3>Registered Users</h3>");
		try (Connection c = DBUtil.getConnection(); ResultSet rs = udao.listAllRaw(c)) {
			out.println("<table class='table'><tr><th>ID</th><th>Username</th><th>Email</th><th>Joined</th><th>Action</th><th>Dashboard</th></tr>");
			while (rs.next()) {
			    int id = rs.getInt("id");
			    Timestamp ts = rs.getTimestamp("created_at"); 
			    String created_at = ts.toInstant()
			                         .atZone(ZoneId.of("UTC"))
			                         .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			    out.println("<tr><td>" + id + "</td><td>" + rs.getString("username") + "</td><td>"
			        + rs.getString("email") + "</td><td>" + created_at + "</td><td>"
			        + "<form method='post'><input type='hidden' name='uid' value='" + id
			        + "'><button>Remove</button></form></td>"
			        + "<td><form action='" + req.getContextPath() + "/admin/userResults' method='get'>"
			        + "<input type='hidden' name='uid' value='" + id + "'>"
			        + "<button>View</button></form></td></tr>");
			}
			out.println("</table>");
		} catch (Exception e) {
			out.println("<div class='error'>Unable to load users.</div>");
		}
		out.println("<a href='" + req.getContextPath() + "/admin/menu'>Back</a></div></body></html>");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			int uid = Integer.parseInt(req.getParameter("uid"));
			udao.deleteById(uid);
		} catch (Exception ignored) {
		}
		resp.sendRedirect(req.getContextPath() + "/admin/users");
	}
}
