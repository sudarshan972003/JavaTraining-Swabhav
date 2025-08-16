package com.aurionpro.MultiPageQuizApplication.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import com.aurionpro.MultiPageQuizApplication.Util.HtmlUtil;
import com.aurionpro.MultiPageQuizApplication.dao.QuestionDAO;
import com.aurionpro.MultiPageQuizApplication.dao.ResultDAO;
import com.aurionpro.MultiPageQuizApplication.dao.SettingDAO;
import com.aurionpro.MultiPageQuizApplication.model.Question;
import com.aurionpro.MultiPageQuizApplication.model.Result;
import com.aurionpro.MultiPageQuizApplication.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/quiz")
public class QuizServlet extends HttpServlet {

    private final QuestionDAO qdao = new QuestionDAO();
    private final SettingDAO sdao = new SettingDAO();
    private final ResultDAO rdao = new ResultDAO();

    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession();
        String action = req.getParameter("action");

        try {
            if ("start".equals(action)) {
                startQuiz(req, resp, s);
            } else if ("previous".equals(action)) {
                saveCurrentAnswer(req, s);
                int index = (int) s.getAttribute("quiz.index");
                if (index > 0) index--;
                s.setAttribute("quiz.index", index);
                renderQuestion(req, resp, index);
            } else if ("answer".equals(action)) {
                saveCurrentAnswer(req, s);

                int index = (int) s.getAttribute("quiz.index");
                List<Question> qs = (List<Question>) s.getAttribute("quiz.questions");
                Map<Integer, Integer> remainingMap = (Map<Integer, Integer>) s.getAttribute("quiz.remaining");
                Question currentQ = qs.get(index);
                int remaining = remainingMap.getOrDefault(currentQ.getId(), 60);

                String choice = req.getParameter("choice");
                if ((choice == null || choice.isBlank()) && remaining > 0) {
                    renderQuestion(req, resp, index, "You must select an option to proceed.");
                    return; 
                }

                index++;
                if (index >= qs.size()) finish(req, resp);
                else {
                    s.setAttribute("quiz.index", index);
                    renderQuestion(req, resp, index);
                }
            }else if ("finish".equals(action)) {
                saveCurrentAnswer(req, s);

                int index = (int) s.getAttribute("quiz.index");
                List<Question> qs = (List<Question>) s.getAttribute("quiz.questions");
                Map<Integer, Integer> remainingMap = (Map<Integer, Integer>) s.getAttribute("quiz.remaining");
                Question currentQ = qs.get(index);
                int remaining = remainingMap.getOrDefault(currentQ.getId(), 60);

                String choice = req.getParameter("choice");
                if ((choice == null || choice.isBlank()) && remaining > 0) {
                    renderQuestion(req, resp, index, "You must select an option before submitting.");
                    return; 
                }

                finish(req, resp);
            } else if ("startNew".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/user/home");
            } else {
                resp.sendError(400);
            }
        } catch (Exception e) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<div class='error'>" + HtmlUtil.esc(e.getMessage()) + "</div>"
                    + "<a href='" + req.getContextPath() + "/user/home'>Back</a>");
        }
    }

    @SuppressWarnings("unchecked")
    private void saveCurrentAnswer(HttpServletRequest req, HttpSession s) {
        List<Question> qs = (List<Question>) s.getAttribute("quiz.questions");
        Map<Integer, Character> answers = (Map<Integer, Character>) s.getAttribute("quiz.answers");
        Map<Integer, List<Character>> optOrder = (Map<Integer, List<Character>>) s.getAttribute("quiz.optOrder");
        Map<Integer, Integer> remainingMap = (Map<Integer, Integer>) s.getAttribute("quiz.remaining");

        int index = (int) s.getAttribute("quiz.index");
        Question q = qs.get(index);
        int qid = q.getId();

        int elapsed = 0;
        try {
            String el = req.getParameter("elapsed");
            if (el != null && !el.isBlank()) elapsed = Integer.parseInt(el);
        } catch (NumberFormatException ignore) {}
        int remaining = Math.max(0, remainingMap.getOrDefault(qid, 60) - elapsed);
        remainingMap.put(qid, remaining);

        String choice = req.getParameter("choice");
        if (choice != null && !choice.isBlank() && remaining > 0) {
            char display = Character.toUpperCase(choice.charAt(0));
            int displayIndex = display - 'A';
            List<Character> originals = optOrder.get(qid);
            if (originals != null && displayIndex >= 0 && displayIndex < 4) {
                char originalLetter = originals.get(displayIndex);
                answers.put(qid, originalLetter);
            }
        }
    }

    private void startQuiz(HttpServletRequest req, HttpServletResponse resp, HttpSession s) throws Exception {
        int catId = Integer.parseInt(req.getParameter("categoryId"));
        List<Question> all = qdao.byCategory(catId);
        if (all.isEmpty()) throw new IllegalArgumentException("No questions in this category.");

        Integer max = sdao.getNumQuestions(catId);
        int total = (max == null) ? all.size() : Math.min(max, all.size());

        List<Question> shuffled = new ArrayList<>(all);
        Collections.shuffle(shuffled, new Random(System.nanoTime()));
        List<Question> chosen = shuffled.subList(0, total);

        Map<Integer, List<Character>> optOrder = new HashMap<>();
        Map<Integer, Integer> remainingMap = new HashMap<>();

        for (Question q : chosen) {
            List<Character> originals = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D'));
            Collections.shuffle(originals, new Random(System.nanoTime()));
            optOrder.put(q.getId(), originals);
            remainingMap.put(q.getId(), 60);
        }

        s.setAttribute("quiz.categoryId", catId);
        s.setAttribute("quiz.questions", chosen);
        s.setAttribute("quiz.optOrder", optOrder);
        s.setAttribute("quiz.answers", new HashMap<Integer, Character>());
        s.setAttribute("quiz.remaining", remainingMap);
        s.setAttribute("quiz.index", 0);

        renderQuestion(req, resp, 0);
    }

    @SuppressWarnings("unchecked")
    private void renderQuestion(HttpServletRequest req, HttpServletResponse resp, int index) throws Exception {
        renderQuestion(req, resp, index, null);
    }

    @SuppressWarnings("unchecked")
    private void renderQuestion(HttpServletRequest req, HttpServletResponse resp, int index, String error) throws Exception {
        HttpSession s = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        List<Question> qs = (List<Question>) s.getAttribute("quiz.questions");
        Map<Integer, List<Character>> optOrder = (Map<Integer, List<Character>>) s.getAttribute("quiz.optOrder");
        Map<Integer, Integer> remainingMap = (Map<Integer, Integer>) s.getAttribute("quiz.remaining");
        Map<Integer, Character> answers = (Map<Integer, Character>) s.getAttribute("quiz.answers");

        Question q = qs.get(index);
        List<Character> originals = optOrder.get(q.getId());
        int remaining = remainingMap.getOrDefault(q.getId(), 60);
        remainingMap.putIfAbsent(q.getId(), remaining);

        boolean timeOver = remaining <= 0;

        out.println("<!doctype html><html><head><meta charset='utf-8'><title>Quiz</title>"
                + "<link rel='stylesheet' href='" + req.getContextPath() + "/css/styles.css'>");
        out.println("<script>");
        out.println("let remain=" + remaining + ";");
        out.println("let elapsed=0;");
        out.println("function tick(){");
        out.println("  if(remain <= 0){");
        out.println("    document.getElementById('elapsed').value = elapsed;");
        out.println("    document.querySelectorAll('#quizForm input[type=radio]').forEach(r => r.disabled = true);");
        out.println("    document.getElementById('timer').textContent = 0;");
        out.println("    return;");
        out.println("  }");
        out.println("  document.getElementById('timer').textContent = remain;");
        out.println("  elapsed++; remain--; document.getElementById('elapsed').value = elapsed;");
        out.println("  setTimeout(tick, 1000);");
        out.println("}");
        out.println("window.addEventListener('load', tick);");
        out.println("</script>");
        out.println("</head><body><div class='container'>");
        out.println("<h3>Question " + (index + 1) + " / " + qs.size() + "</h3>");
        out.println("<div class='notice'>Time left: <b><span id='timer'>" + remaining + "</span>s</b></div>");
        if (error != null) out.println("<div class='error'>" + HtmlUtil.esc(error) + "</div>");
        out.println("<p><b>" + HtmlUtil.esc(q.getText()) + "</b></p>");

        out.println("<form id='quizForm' method='post' class='card'>");
        out.println("<input type='hidden' name='elapsed' id='elapsed' value='0'>");

        Character selected = answers.get(q.getId());
        for (int i = 0; i < 4; i++) {
            char displayLabel = (char) ('A' + i);
            char orig = originals.get(i);
            String text = (orig == 'A') ? q.getOptA()
                        : (orig == 'B') ? q.getOptB()
                        : (orig == 'C') ? q.getOptC()
                        : q.getOptD();
            boolean checked = (selected != null && selected == orig);
            out.println("<label class='quiz-option'><input type='radio' name='choice' value='" + displayLabel + "'"
                    + (checked ? " checked" : "")
                    + (timeOver ? " disabled" : "")
                    + "> " + displayLabel + ") " + HtmlUtil.esc(text) + "</label>");
        }

        if (timeOver) {
            out.println("<div class='error'>Time is over for this question. You cannot change your answer.</div>");
        }

        out.println("<div class='actions'>");
        if (index > 0) out.println("<button type='submit' name='action' value='previous'>Previous</button>");
        if (index == qs.size() - 1) out.println("<button type='submit' name='action' value='finish'>Submit</button>");
        else out.println("<button type='submit' name='action' value='answer'>Next</button>");
        out.println("</div></form></div></body></html>");

        s.setAttribute("quiz.index", index);
    }

    @SuppressWarnings("unchecked")
    private void finish(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession s = req.getSession();
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        List<Question> qs = (List<Question>) s.getAttribute("quiz.questions");
        Map<Integer, Character> answers = (Map<Integer, Character>) s.getAttribute("quiz.answers");
        int catId = (int) s.getAttribute("quiz.categoryId");
        User u = (User) s.getAttribute("user");

        int correct = 0;
        for (Question q : qs) {
            Character given = answers.get(q.getId());
            if (given != null && q.getCorrect().charAt(0) == given) correct++;
        }

        Result r = new Result();
        r.setUserId(u.getId());
        r.setCategoryId(catId);
        r.setScore(correct);
        r.setTotal(qs.size());
        rdao.save(r);

        s.removeAttribute("quiz.questions");
        s.removeAttribute("quiz.optOrder");
        s.removeAttribute("quiz.answers");
        s.removeAttribute("quiz.index");
        s.removeAttribute("quiz.remaining");

        out.println("<!doctype html><html><head><meta charset='utf-8'><title>Final Score</title>"
                + "<link rel='stylesheet' href='" + req.getContextPath() + "/css/styles.css'></head><body>");
        out.println("<div class='container'><h2>Final Score</h2>");
        out.println("<div class='notice'>Score: <b>" + correct + "</b> / <b>" + qs.size() + "</b></div>");

        out.println("<h3>Quiz Summary</h3>");
        for (Question q : qs) {
            Character selected = answers.get(q.getId());
            out.println("<div class='card'><p><b>Q: " + HtmlUtil.esc(q.getText()) + "</b></p>");
            Map<Character, String> opts = Map.of(
                    'A', q.getOptA(),
                    'B', q.getOptB(),
                    'C', q.getOptC(),
                    'D', q.getOptD()
            );
            for (Map.Entry<Character, String> e : opts.entrySet()) {
                char opt = e.getKey();
                String text = e.getValue();
                String style = "";
                if (opt == q.getCorrect().charAt(0)) style = "color:green; font-weight:bold;";
                if (selected != null && selected == opt && opt != q.getCorrect().charAt(0)) style = "color:red;";
                out.println("<p style='" + style + "'>" + opt + ") " + HtmlUtil.esc(text)
                        + (selected != null && selected == opt ? " &larr; Your choice" : "") + "</p>");
            }
            out.println("</div>");
        }

        out.println("<div class='button-group'>");
        out.println("<form method='post'><input type='hidden' name='action' value='start'>"
                + "<input type='hidden' name='categoryId' value='" + catId + "'><button>Reattempt Same Category</button></form>");
        out.println("<form method='post'><input type='hidden' name='action' value='startNew'><button>Start New Test</button></form>");
        out.println("<form method='get' action='" + req.getContextPath() + "/logout'><button>Logout</button></form>");
        out.println("</div></div></body></html>");
    }
}
