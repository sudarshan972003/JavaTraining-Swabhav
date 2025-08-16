package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.MultiPageQuizApplication.dao.CategoryDAO;
import com.aurionpro.MultiPageQuizApplication.model.Category;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/categories")
public class CategoryListServlet extends HttpServlet {
	private CategoryDAO dao = new CategoryDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		out.println("<!doctype html><html><head><meta charset='utf-8'><title>Categories</title>");
		out.println("<link rel='stylesheet' href='" + req.getContextPath() + "/css/styles.css'></head><body>");
		out.println("<div class='container'><h2>Categories</h2>");

		try {
			List<Category> cats = dao.all();
			out.println("<table class='table'><tr><th>ID</th><th>Name</th><th>Actions</th></tr>");
			for (Category c : cats) {
				out.println("<tr>");
				out.println("<td>" + c.getId() + "</td>");
				out.println("<td>" + c.getName() + "</td>");
				out.println("<td>");
				out.println("<form method='post' action='" + req.getContextPath()
						+ "/admin/categories' style='display:inline'>");
				out.println("<input type='hidden' name='id' value='" + c.getId() + "'/>");
				out.println("<input type='text' name='name' value='" + c.getName() + "'/>");
				out.println("<button type='submit' name='action' value='update'>Update</button>");
				out.println("</form>");
				out.println("<form method='post' action='" + req.getContextPath()
						+ "/admin/categories' style='display:inline' onsubmit=\"return confirm('Delete this category?');\">");
				out.println("<input type='hidden' name='id' value='" + c.getId() + "'/>");
				out.println("<button type='submit' name='action' value='delete'>Delete</button>");
				out.println("</form>");
				out.println("</td>");
				out.println("</tr>");
			}
			out.println("</table>");
		} catch (SQLException e) {
			out.println("<p>Error: " + e.getMessage() + "</p>");
		}

		out.println("<p><a href='" + req.getContextPath() + "/admin/menu'>Back to Admin Menu</a></p>");
		out.println("</div></body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String action = req.getParameter("action");
		int id = Integer.parseInt(req.getParameter("id"));

		try {
			if ("update".equals(action)) {
				String name = req.getParameter("name");
				Category c = new Category();
				c.setId(id);
				c.setName(name);
				dao.update(c);
			} else if ("delete".equals(action)) {
				dao.delete(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		resp.sendRedirect(req.getContextPath() + "/admin/categories");
	}
}
