package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;

import com.aurionpro.MultiPageQuizApplication.dao.QuestionDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/question/delete")
public class DeleteQuestionServlet extends HttpServlet {
	private final QuestionDAO qdao = new QuestionDAO();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			int id = Integer.parseInt(req.getParameter("id"));
			qdao.delete(id);
		} catch (Exception ignored) {
		}
		resp.sendRedirect(req.getContextPath() + "/admin/question/list");
	}
}
