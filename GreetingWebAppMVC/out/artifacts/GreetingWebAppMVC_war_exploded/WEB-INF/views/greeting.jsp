<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Greeting Result</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .card {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            padding: 48px 40px;
            border-radius: 20px;
            width: 420px;
            text-align: center;
        }
        .emoji { font-size: 64px; margin-bottom: 16px; }
        h1 { color: rgba(255,255,255,0.7); font-size: 16px; letter-spacing: 2px; marginbottom: 24px; }
        .message-box {
            background: linear-gradient(135deg, #e94560, #c23152);
            color: white;
            padding: 24px 32px;
            border-radius: 14px;
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 32px;
            word-wrap: break-word;
        }
        a {
            display: inline-block;
            padding: 12px 28px;
            background: rgba(255,255,255,0.08);
            color: rgba(255,255,255,0.7);
            text-decoration: none;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 600;
            border: 1px solid rgba(255,255,255,0.1);
            transition: background 0.3s;
        }
        a:hover { background: rgba(255,255,255,0.15); }
        .footer { color: rgba(255,255,255,0.3); font-size: 12px; margin-top: 24px; }
    </style>
</head>
<body>
<div class="card">
    <div class="emoji">👋</div>
    <h1>YOUR GREETING</h1>
    <!--
 ${greeting.message}:
 - "greeting" was added by controller: model.addAttribute("greeting", greetingObj)
 - ".message" calls greeting.getMessage() via JSP EL
 - Spring MVC automatically makes all model attributes available in JSP
 -->
    <div class="message-box">
        ${greeting.message}
    </div>
    <a href="greet">← Try Another Name</a>
    <div class="footer">
        Spring MVC 6.1.6 | Tomcat 10.1 | Java 26
    </div>
</div>
</body>
</html>