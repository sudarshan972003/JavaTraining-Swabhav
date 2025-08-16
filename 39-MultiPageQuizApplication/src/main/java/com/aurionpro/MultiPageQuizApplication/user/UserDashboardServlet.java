package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.dao.ResultDAO;
import com.aurionpro.MultiPageQuizApplication.model.Result;
import com.aurionpro.MultiPageQuizApplication.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
    private final ResultDAO rdao = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
    	resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        User u = (User) req.getSession().getAttribute("user");

        try {
            Result[] results = rdao.findByUser(u.getId());
            out.println("<!doctype html><html><head><meta charset='utf-8'><title>User Dashboard</title>"
                    + "<link rel='stylesheet' href='" + req.getContextPath() + "/css/styles.css'></head><body>");
            out.println("<div class='container'>");
            out.println("<h2>Dashboard for " + HtmlUtil.esc(u.getUsername()) + "</h2>");

            if (results.length == 0) {
                out.println("<p>No quiz attempts yet.</p>");
            } else {
                out.println("<table class='table'><tr><th>Category</th><th>Score</th><th>Total</th><th>Taken At</th></tr>");
                for (Result r : results) {
                    String takenAtStr = r.getTakenAt() != null 
                            ? fmt.format(r.getTakenAt().toInstant()) 
                            : "-";
                    out.println("<tr><td>" + HtmlUtil.esc(r.getCategoryName()) + "</td>"
                            + "<td>" + r.getScore() + "</td>"
                            + "<td>" + r.getTotal() + "</td>"
                            + "<td>" + takenAtStr + "</td></tr>");
                }
                out.println("</table>");
            }

            out.println("<br><form method='get' action='" + req.getContextPath() + "/user/home'>"
                    + "<button>Back to Home</button></form>");
            out.println("</div></body></html>");
        } catch (Exception e) {
            out.println("<div class='error'>Unable to load dashboard.</div>");
        }
    }
}
