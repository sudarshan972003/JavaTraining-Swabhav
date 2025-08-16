package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.Util.ValidationUtil;
import com.aurionpro.MultiPageQuizApplication.dao.QuestionDAO;
import com.aurionpro.MultiPageQuizApplication.model.Question;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/question/edit")
public class EditQuestionServlet extends HttpServlet {
    private final QuestionDAO qdao = new QuestionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println(
				"<!doctype html><html><head><meta charset='utf-8'><title>Edit Questions</title><link rel='stylesheet' href='"
						+ req.getContextPath() + "/css/styles.css'></head><body>");
        out.println("<div class='container'>");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Question q = qdao.find(id);
            if (q == null) {
                out.println("<div style='color:red'>Question not found.</div>");
                out.println("<a href='" + req.getContextPath() + "/admin/question/list'>Back</a>");
                out.println("</div></body></html>");
                return;
            }

            out.println("<h3>Edit Question #" + id + "</h3>");
            out.println("<form method='post'>");
            out.println("<input type='hidden' name='id' value='" + id + "'>");
            out.println("Category ID: <input type='number' name='categoryId' value='" + q.getCategoryId() + "' required><br>");
            out.println("Question: <textarea name='question' required>" + HtmlUtil.esc(q.getText()) + "</textarea><br>");
            out.println("A: <input name='optA' value='" + HtmlUtil.esc(q.getOptA()) + "' required><br>");
            out.println("B: <input name='optB' value='" + HtmlUtil.esc(q.getOptB()) + "' required><br>");
            out.println("C: <input name='optC' value='" + HtmlUtil.esc(q.getOptC()) + "' required><br>");
            out.println("D: <input name='optD' value='" + HtmlUtil.esc(q.getOptD()) + "' required><br>");
            out.println("Correct (A/B/C/D): <input name='correct' value='" + HtmlUtil.esc(q.getCorrect()) + "' maxlength='1' required><br>");
            out.println("<button type='submit'>Update</button></form>");
        } catch (Exception e) {
            out.println("<div style='color:red'>Error loading question.</div>");
        }
        out.println("<br><a href='" + req.getContextPath() + "/admin/question/list'>Back</a>");
        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            String qt = req.getParameter("question");
            String a = req.getParameter("optA");
            String b = req.getParameter("optB");
            String c = req.getParameter("optC");
            String d = req.getParameter("optD");
            String correct = req.getParameter("correct");

            ValidationUtil.require(qt, "Question");
            ValidationUtil.require(a, "Option A");
            ValidationUtil.require(b, "Option B");
            ValidationUtil.require(c, "Option C");
            ValidationUtil.require(d, "Option D");
            if (!("A".equalsIgnoreCase(correct) || "B".equalsIgnoreCase(correct) ||
                  "C".equalsIgnoreCase(correct) || "D".equalsIgnoreCase(correct))) {
                throw new IllegalArgumentException("Correct option must be A, B, C, or D.");
            }

            Question q = new Question();
            q.setId(id);
            q.setCategoryId(categoryId);
            q.setText(qt.trim());
            q.setOptA(a.trim());
            q.setOptB(b.trim());
            q.setOptC(c.trim());
            q.setOptD(d.trim());
            q.setCorrect(correct.toUpperCase());

            qdao.update(q);

            resp.sendRedirect(req.getContextPath() + "/admin/question/list");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Update failed.");
            doGet(req, resp);
        }
    }
}
