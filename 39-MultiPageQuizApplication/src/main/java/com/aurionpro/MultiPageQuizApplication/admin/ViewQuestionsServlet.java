package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.dao.CategoryDAO;
import com.aurionpro.MultiPageQuizApplication.dao.QuestionDAO;
import com.aurionpro.MultiPageQuizApplication.model.Category;
import com.aurionpro.MultiPageQuizApplication.model.Question;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/question/list")
public class ViewQuestionsServlet extends HttpServlet {
	private final CategoryDAO cdao = new CategoryDAO();
	private final QuestionDAO qdao = new QuestionDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		try {
			var cats = cdao.all();
			String sel = req.getParameter("category");
			out.println(
					"<!doctype html><html><head><meta charset='utf-8'><title>View Questions</title><link rel='stylesheet' href='"
							+ req.getContextPath() + "/css/styles.css'></head><body>");
			out.println("<div class='container'>");
			out.println("<h3>View Questions</h3>");
			out.println(
					"<form method='get'><select name='category' required><option value=''>--Select Category--</option>");
			for (Category c : cats)
				out.println("<option " + (String.valueOf(c.getId()).equals(sel) ? "selected" : "") + " value='"
						+ c.getId() + "'>" + HtmlUtil.esc(c.getName()) + "</option>");
			out.println("</select> <button>Load</button></form><hr>");

			if (sel != null && !sel.isBlank()) {
				int catId = Integer.parseInt(sel);
				var list = qdao.byCategory(catId);
				out.println(
						"<table class='table'><tr><th>ID</th><th>Question</th><th>Options</th><th>Correct</th><th>Actions</th></tr>");
				for (Question q : list) {
					out.println("<tr><td>" + q.getId() + "</td><td>" + HtmlUtil.esc(q.getText()) + "</td><td>" + "A) "
							+ HtmlUtil.esc(q.getOptA()) + "<br>B) " + HtmlUtil.esc(q.getOptB()) + "<br>C) "
							+ HtmlUtil.esc(q.getOptC()) + "<br>D) " + HtmlUtil.esc(q.getOptD()) + "</td><td>"
							+ q.getCorrect() + "</td><td>" + "<form style='display:inline' method='get' action='"
							+ req.getContextPath() + "/admin/question/edit'><input type='hidden' name='id' value='"
							+ q.getId() + "'><button>Edit</button></form> "
							+ "<form style='display:inline' method='post' action='" + req.getContextPath()
							+ "/admin/question/delete'><input type='hidden' name='id' value='" + q.getId()
							+ "'><button>Delete</button></form>" + "</td></tr>");
				}
				out.println("</table>");
			}
			out.println("<a href='" + req.getContextPath() + "/admin/menu'>Back</a></div></body></html>");
		} catch (Exception e) {
			out.println("<div class='error'>Load failed.</div>");
		}
	}
}
