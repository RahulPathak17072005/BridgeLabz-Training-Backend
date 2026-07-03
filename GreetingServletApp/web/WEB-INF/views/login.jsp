<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Greeting App - Login</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #f8fafc;
        }

        .container {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(16px);
            padding: 40px;
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            width: 400px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
        }

        h2 {
            text-align: center;
            margin-bottom: 24px;
            color: #38bdf8;
            font-size: 28px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            color: #94a3b8;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #475569;
            border-radius: 8px;
            background: rgba(15, 23, 42, 0.6);
            color: #fff;
            font-size: 16px;
            outline: none;
            transition: 0.3s;
        }

        input:focus {
            border-color: #38bdf8;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #38bdf8;
            border: none;
            border-radius: 8px;
            color: #0f172a;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.3s;
            margin-top: 10px;
        }

        button:hover {
            background: #0ea5e9;
        }

        .error {
            color: #f87171;
            text-align: center;
            margin-bottom: 15px;
            font-size: 14px;
        }

        .success {
            color: #4ade80;
            text-align: center;
            margin-bottom: 15px;
            font-size: 14px;
        }

        .link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #94a3b8;
        }

        .link a {
            color: #38bdf8;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Login</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success">
        <%= request.getAttribute("success") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="POST">

        <div class="form-group">
            <label>Username</label>
            <input type="text"
                   name="username"
                   required
                   autocomplete="off">
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password"
                   name="password"
                   required>
        </div>

        <button type="submit">Sign In</button>

    </form>

    <div class="link">
        Don't have an account?
        <a href="${pageContext.request.contextPath}/register">
            Register here
        </a>
    </div>

</div>

</body>
</html>