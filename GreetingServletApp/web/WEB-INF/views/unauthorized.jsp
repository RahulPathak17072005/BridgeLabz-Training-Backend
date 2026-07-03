<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>403 - Unauthorized Access</title>
    <style>
        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: #0f172a;
            color: #f8fafc;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
        }
        .container {
            background: #1e293b;
            padding: 50px;
            border-radius: 12px;
            border: 1px solid #ef4444;
            max-width: 500px;
            box-shadow: 0 4px 20px rgba(239, 68, 68, 0.15);
        }
        h1 { color: #ef4444; font-size: 72px; margin-bottom: 10px; }
        h2 { font-size: 24px; margin-bottom: 20px; color: #e2e8f0; }
        p { color: #94a3b8; margin-bottom: 30px; line-height: 1.6; }
        btn {
            display: inline-block;
            padding: 12px 24px;
            background: #38bdf8;
            color: #0f172a;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: 0.3s;
        }
        .btn:hover { background: #0ea5e9; }
    </style>
</head>
<body>
<div class="container">
    h1>403</h1>
    <h2>Access Denied</h2>
    <p>You do not have the required permissions to access this feature. Admin
        privilege is required to modify greetings.</p>
    <a href="${pageContext.request.contextPath}/greetings" class="btn">Return
        to Dashboard</a>
</div>
</body>
</html>
