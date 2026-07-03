<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.greet.model.Greeting" %>
<%@ page import="com.greet.model.User" %>

<%
    User currentUser = (User) session.getAttribute("currentUser");
    List<Greeting> greetings = (List<Greeting>) request.getAttribute("greetings");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Greeting Dashboard</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: #0f172a;
            color: #e2e8f0;
            padding: 40px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #334155;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }

        h1 {
            color: #38bdf8;
        }

        .user-info {
            font-size: 16px;
            color: #94a3b8;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .role-badge {
            background: #1e293b;
            color: #38bdf8;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            border: 1px solid #0ea5e9;
        }

        .role-badge.admin {
            color: #ef4444;
            border-color: #ef4444;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #38bdf8;
            color: #0f172a;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
        }

        .btn:hover {
            background: #0ea5e9;
        }

        .btn-danger {
            background: #ef4444;
            color: white;
        }

        .btn-danger:hover {
            background: #dc2626;
        }

        .btn-edit {
            background: #fbbf24;
            color: #0f172a;
        }

        .btn-edit:hover {
            background: #f59e0b;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .card {
            background: #1e293b;
            border-radius: 12px;
            border: 1px solid #334155;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            display: flex;
            flex-direction: column;
        }

        .card-img-wrapper {
            height: 200px;
            background: #0f172a;
            display: flex;
            align-items: center;
            justify-content: center;
            border-bottom: 1px solid #334155;
        }

        .card-img-wrapper img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .card-img-placeholder {
            color: #475569;
            font-size: 14px;
        }

        .card-content {
            padding: 20px;
        }

        .message {
            font-size: 18px;
            font-weight: 500;
            color: #f1f5f9;
            margin-bottom: 15px;
        }

        .meta {
            font-size: 13px;
            color: #64748b;
            margin-bottom: 20px;
        }

        .actions {
            display: flex;
            gap: 10px;
        }

        .actions a {
            flex: 1;
            text-align: center;
            padding: 8px 12px;
            font-size: 14px;
        }

        .no-records {
            text-align: center;
            grid-column: 1 / -1;
            padding: 60px 0;
            color: #64748b;
            font-size: 18px;
        }
    </style>
</head>
<body>

<div class="header">
    <div>
        <h1>🌿 Greeting Application</h1>
    </div>

    <div class="user-info">
        <span>
            Welcome,
            <strong><%= currentUser != null ? currentUser.getUsername() : "Guest" %></strong>
        </span>

        <span class="role-badge <%= currentUser != null && "ADMIN".equals(currentUser.getRole()) ? "admin" : "" %>">
            <%= currentUser != null ? currentUser.getRole() : "" %>
        </span>

        <a href="<%= request.getContextPath() %>/logout"
           class="btn btn-danger"
           style="padding:8px 14px;">
            Logout
        </a>
    </div>
</div>

<div style="display:flex; justify-content:space-between; align-items:center;">
    <h2>Dashboard</h2>

    <% if(currentUser != null && "ADMIN".equals(currentUser.getRole())) { %>
    <a href="<%= request.getContextPath() %>/greetings/new"
       class="btn">
        + Create Greeting
    </a>
    <% } %>
</div>

<div class="grid">

    <%
        if (greetings != null && !greetings.isEmpty()) {

            for (Greeting greet : greetings) {
    %>

    <div class="card">

        <div class="card-img-wrapper">
            <% if(greet.getImagePath() != null && !greet.getImagePath().isEmpty()) { %>

            <img src="<%= request.getContextPath() + greet.getImagePath() %>"
                 alt="Greeting Image">

            <% } else { %>

            <span class="card-img-placeholder">
                    No Image Available
                </span>

            <% } %>
        </div>

        <div class="card-content">

            <div class="message">
                <%= greet.getMessage() %>
            </div>

            <div class="meta">
                Created by:
                <%= greet.getCreatedByName() != null
                        ? greet.getCreatedByName()
                        : "Deleted User" %>
            </div>

            <% if(currentUser != null && "ADMIN".equals(currentUser.getRole())) { %>

            <div class="actions">

                <a href="<%= request.getContextPath() %>/greetings/edit?id=<%= greet.getId() %>"
                   class="btn btn-edit">
                    Edit
                </a>

                <a href="<%= request.getContextPath() %>/greetings/delete?id=<%= greet.getId() %>"
                   class="btn btn-danger"
                   onclick="return confirm('Delete this greeting?');">
                    Delete
                </a>

            </div>

            <% } %>

        </div>

    </div>

    <%
        }
    } else {
    %>

    <div class="no-records">
        No greetings found. Go ahead and create one!
    </div>

    <%
        }
    %>

</div>

</body>
</html>