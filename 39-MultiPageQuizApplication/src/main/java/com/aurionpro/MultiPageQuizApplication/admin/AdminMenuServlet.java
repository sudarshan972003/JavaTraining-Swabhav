package com.aurionpro.MultiPageQuizApplication.admin;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/menu")
public class AdminMenuServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<!doctype html><html><head><meta charset='utf-8'><title>Admin</title><link rel='stylesheet' href='"
				+ req.getContextPath() + "/css/styles.css'></head><body>");
		out.println("<div class='container'>");
		out.println("<h2>Admin Dashboard</h2>");
		out.println("<div class='actions'>");
		out.println("<form method='get' action='" + req.getContextPath()
				+ "/admin/question/add'><button>Add Question</button></form>");
		out.println("<form method='get' action='" + req.getContextPath()
				+ "/admin/question/list'><button>View Questions</button></form>");
		out.println("<form method='get' action='" + req.getContextPath()
        + "/admin/categories'><button>View Categories</button></form>");
		out.println("<form method='get' action='" + req.getContextPath()
				+ "/admin/users'><button>View Registered Users</button></form>");
		out.println("<form method='get' action='" + req.getContextPath()
				+ "/admin/settings'><button>Set Number of Questions</button></form>");
		out.println("<form method='get' action='" + req.getContextPath() + "/logout'><button>Logout</button></form>");
		out.println("</div></div></body></html>");
	}
}
