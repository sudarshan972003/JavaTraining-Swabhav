package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.dao.CategoryDAO;
import com.aurionpro.MultiPageQuizApplication.model.Category;
import com.aurionpro.MultiPageQuizApplication.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/home")
public class UserHomeServlet extends HttpServlet {
	private final CategoryDAO cdao = new CategoryDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		User u = (User) req.getSession().getAttribute("user");
		try {
			var cats = cdao.all();
			out.println(
					"<!doctype html><html><head><meta charset='utf-8'><title>User Home</title><link rel='stylesheet' href='"
							+ req.getContextPath() + "/css/styles.css'></head><body>");
			out.println("<div class='container'>");
			out.println("<h3>Hi " + HtmlUtil.esc(u.getUsername()) + "!</h3>");
			out.println("<form method='get' action='" + req.getContextPath() + "/user/dashboard'>"
			        + "<button>Dashboard</button></form>");

			out.println("<form method='post' action='" + req.getContextPath() + "/user/quiz' class='card'>");
			out.println("<input type='hidden' name='action' value='start'>");
			out.println(
					"<label>Select Category<select name='categoryId' required><option value=''>--select--</option>");
			for (Category c : cats)
				out.println("<option value='" + c.getId() + "'>" + HtmlUtil.esc(c.getName()) + "</option>");
			out.println("</select></label><button>Start Test</button></form>");
			out.println(
					"<form method='get' action='" + req.getContextPath() + "/logout'><button>Logout</button></form>");
			out.println("</div>");
			out.println("</body></html>");
		} catch (Exception e) {
			out.println("<div class='error'>Unable to load categories.</div>");
		}
	}
}
