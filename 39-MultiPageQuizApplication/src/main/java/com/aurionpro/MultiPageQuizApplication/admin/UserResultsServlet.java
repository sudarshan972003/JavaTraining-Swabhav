package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.aurionpro.MultiPageQuizApplication.dao.ResultDAO;
import com.aurionpro.MultiPageQuizApplication.model.Result;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/userResults")
public class UserResultsServlet extends HttpServlet {
	private final ResultDAO rdao = new ResultDAO();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		int uid = Integer.parseInt(req.getParameter("uid"));

		out.println("<!doctype html><html><head><title>User Results</title>");
		out.println("<link rel='stylesheet' href='" + req.getContextPath() + "/css/styles.css'>");
		out.println("</head><body><div class='container'>");
		out.println("<h3>User " + uid + " Results</h3>");

		try {
			Result[] results = rdao.findByUser(uid);
			if (results.length == 0) {
				out.println("<p>No results found.</p>");
			} else {
				out.println(
						"<table class='table'><tr><th>ID</th><th>Category ID</th><th>Category Name</th><th>Score</th><th>Total</th><th>Taken At</th></tr>");
				for (Result r : results) {
					String takenAtFormatted = r.getTakenAt().toInstant().atZone(ZoneId.of("UTC")).format(fmt);
					
					out.println("<tr><td>" + r.getId() + "</td><td>" + r.getCategoryId() + "</td><td>" + r.getCategoryName() + "</td><td>" + r.getScore()
							+ "</td><td>" + r.getTotal() + "</td><td>" + takenAtFormatted + "</td></tr>");
				}
				out.println("</table>");
			}
		} catch (Exception e) {
			out.println("<div class='error'>Error loading results.</div>");
			e.printStackTrace();
		}

		out.println("<a href='" + req.getContextPath() + "/admin/users'>Back to Users</a></div></body></html>");
	}
}
