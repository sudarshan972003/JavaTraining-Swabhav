package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.dao.CategoryDAO;
import com.aurionpro.MultiPageQuizApplication.dao.SettingDAO;
import com.aurionpro.MultiPageQuizApplication.model.Category;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/settings")
public class SetQuizCountServlet extends HttpServlet {
	private final SettingDAO sdao = new SettingDAO();
	private final CategoryDAO cdao = new CategoryDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		try {
			var cats = cdao.all();
			out.println(
					"<!doctype html><html><head><meta charset='utf-8'><title>Set Count</title><link rel='stylesheet' href='"
							+ req.getContextPath() + "/css/styles.css'></head><body>");
			out.println("<div class='container'>");
			out.println("<h3>Set Number of Questions (per category)</h3>");
			out.println(
					"<form method='post' class='card'><label>Category<select name='categoryId' required><option value=''>--select--</option>");
			for (Category c : cats)
				out.println("<option value='" + c.getId() + "'>" + HtmlUtil.esc(c.getName()) + "</option>");
			out.println(
					"</select></label><label>Count <input type='number' min='1' name='num' required></label><button>Save</button></form>");
			out.println("<a href='" + req.getContextPath() + "/admin/menu'>Back</a></div></body></html>");
		} catch (Exception e) {
			out.println("<div class='error'>Load failed.</div>");
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			int cid = Integer.parseInt(req.getParameter("categoryId"));
			int n = Integer.parseInt(req.getParameter("num"));
			if (n <= 0)
				throw new IllegalArgumentException("Number must be > 0.");
			sdao.upsert(cid, n);
			resp.sendRedirect(req.getContextPath() + "/admin/settings");
		} catch (Exception e) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<div class='error'>" + e.getMessage() + "</div><a href='" + req.getContextPath()
					+ "/admin/settings'>Back</a>");
		}
	}
}
