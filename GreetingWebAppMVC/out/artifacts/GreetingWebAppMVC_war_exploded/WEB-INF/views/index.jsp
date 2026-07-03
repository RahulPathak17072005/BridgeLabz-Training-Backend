<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>
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
        .badge {
            display: inline-block;
            background: linear-gradient(90deg, #e94560, #0f3460);
            color: white;
            font-size: 12px;
            padding: 4px 14px;
            border-radius: 20px;
            margin-bottom: 20px;
            letter-spacing: 1px;
        }
        h1 { color: #fff; font-size: 28px; margin-bottom: 8px; }
        p { color: rgba(255,255,255,0.5); margin-bottom: 32px; font-size: 15px; }
        label {
            display: block;
            text-align: left;
            color: rgba(255,255,255,0.7);
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 8px;
            letter-spacing: 0.5px;
        }
        input[type="text"] {
            width: 100%;
            padding: 14px 18px;
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.15);
            border-radius: 10px;
            color: white;
            font-size: 15px;
            margin-bottom: 20px;
            outline: none;
            transition: border-color 0.3s;
        }
        input[type="text"]:focus { border-color: #e94560; }
        input[type="text"]::placeholder { color: rgba(255,255,255,0.3); }
        button {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #e94560, #c23152);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(233, 69, 96, 0.4);
        }
        .footer { color: rgba(255,255,255,0.3); font-size: 12px; margin-top: 24px; }
    </style>
</head>
<body>
<div class="card">
    <div class="badge">SPRING MVC</div>
    <h1>🌿 Greeting App</h1>
    <p>Enter your name to receive a personalized greeting</p>
    <!--
   SpringMVCImplementationPlanGreetingApplication.md 2026-06-21
   10 / 19
    form action="greet" → POST /GreetingWebApp/greet
    Handled by GreetingController.handleGreet()
    -->
    <form action="greet" method="POST">
        <label for="userName">YOUR NAME</label>
        <input type="text"
               id="userName"
               name="userName"
               placeholder="e.g. Rahul"
               required
               autofocus />
        <button type="submit">Get Greeting →</button>
    </form>
    <div class="footer">
        Spring MVC 6.1.6 | Tomcat 10.1 | Java 26
    </div>
</div>
</body>
</html>