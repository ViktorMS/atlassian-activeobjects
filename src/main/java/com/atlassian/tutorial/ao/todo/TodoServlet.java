package com.atlassian.tutorial.ao.todo;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.google.common.base.Preconditions.*;

@Scanned
public final class TodoServlet extends HttpServlet
{
    private final TodoService todoService;
    @ComponentImport
    private final UserManager userManager;

    @Inject
    public TodoServlet(TodoService todoService, UserManager userManager)
    {
        this.todoService = checkNotNull(todoService);
        this.userManager = checkNotNull(userManager);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        if (!enforceLoggedIn(req, res))
        {
            return;
        }

        final PrintWriter w = res.getWriter();
        w.printf("<h1>Todos (%s)</h1>", userManager.getRemoteUser().getUsername());

        // the form to post more TODOs
        w.write("<form method=\"post\">");
        w.write("<input type=\"text\" name=\"task\" size=\"25\"/>");
        w.write("&nbsp;&nbsp;");
        w.write("<input type=\"submit\" name=\"submit\" value=\"Add\"/>");
        w.write("</form>");

        w.write("<ol>");

        for (Todo todo : todoService.all())
        {
            w.printf("<li><%2$s> %s </%2$s></li>", todo.getDescription(), todo.isComplete() ? "strike" : "strong");
        }

        w.write("</ol>");
        w.write("<script language='javascript'>document.forms[0].elements[0].focus();</script>");

        w.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        if (!enforceLoggedIn(req, res))
        {
            return;
        }

        final String description = req.getParameter("task");
        todoService.add(description);
        res.sendRedirect(req.getContextPath() + "/plugins/servlet/todo/list");
    }

    private boolean enforceLoggedIn(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        if (userManager.getRemoteUser() == null)
        {
            res.sendRedirect(req.getContextPath() + "/plugins/servlet/login");
            return false;
        }
        return true;
    }
}