package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.Util.ValidationUtil;
import com.aurionpro.MultiPageQuizApplication.dao.CategoryDAO;
import com.aurionpro.MultiPageQuizApplication.dao.QuestionDAO;
import com.aurionpro.MultiPageQuizApplication.model.Category;
import com.aurionpro.MultiPageQuizApplication.model.Question;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/question/add")
public class AddQuestionServlet extends HttpServlet {
	private final CategoryDAO cdao = new CategoryDAO();
	private final QuestionDAO qdao = new QuestionDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		try {
			var cats = cdao.all();
			out.println(
					"<!doctype html><html><head><meta charset='utf-8'><title>Add Question</title><link rel='stylesheet' href='"
							+ req.getContextPath() + "/css/styles.css'></head><body>");
			 out.println("<div class='container'>");
			out.println("<h3>Add Question</h3>");
			out.println("<form method='post' class='card'>");
			out.println(
					"<label>Category (existing)</label><select name='categoryId'><option value=''>--select--</option>");
			for (Category c : cats)
				out.println("<option value='" + c.getId() + "'>" + HtmlUtil.esc(c.getName()) + "</option>");
			out.println("</select>");
			out.println("<label>Or new category <input name='newCategory'></label>");
			out.println("<label>Question <textarea name='q' required></textarea></label>");
			out.println("<label>A <input name='a' required></label>");
			out.println("<label>B <input name='b' required></label>");
			out.println("<label>C <input name='c' required></label>");
			out.println("<label>D <input name='d' required></label>");
			out.println("<label>Correct (A/B/C/D) <input name='correct' maxlength='1' required></label>");
			out.println("<button>Save</button></form>");
			out.println("<a href='" + req.getContextPath() + "/admin/menu'>Back</a></div></body></html>");
		} catch (Exception e) {
			out.println("<div class='error'>Load failed.</div>");
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			String catIdStr = req.getParameter("categoryId");
			String newCat = req.getParameter("newCategory");
			String qt = req.getParameter("q");
			String a = req.getParameter("a");
			String b = req.getParameter("b");
			String c = req.getParameter("c");
			String d = req.getParameter("d");
			String correct = req.getParameter("correct");
			ValidationUtil.require(qt, "Question");
			ValidationUtil.require(a, "Option A");
			ValidationUtil.require(b, "Option B");
			ValidationUtil.require(c, "Option C");
			ValidationUtil.require(d, "Option D");
			if (!("A".equalsIgnoreCase(correct) || "B".equalsIgnoreCase(correct) || "C".equalsIgnoreCase(correct)
					|| "D".equalsIgnoreCase(correct)))
				throw new IllegalArgumentException("Correct must be A, B, C or D.");

			int catId;
			if (newCat != null && !newCat.trim().isEmpty()) {
				catId = new CategoryDAO().upsertByName(newCat.trim());
			} else {
				catId = Integer.parseInt(catIdStr);
			}

			Question q = new Question();
			q.setCategoryId(catId);
			q.setText(qt.trim());
			q.setOptA(a.trim());
			q.setOptB(b.trim());
			q.setOptC(c.trim());
			q.setOptD(d.trim());
			q.setCorrect(correct.toUpperCase());
			qdao.create(q);
			resp.sendRedirect(req.getContextPath() + "/admin/question/add");
		} catch (Exception e) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<div class='error'>" + e.getMessage() + "</div><a href='" + req.getContextPath()
					+ "/admin/question/add'>Back</a>");
		}
	}
}
