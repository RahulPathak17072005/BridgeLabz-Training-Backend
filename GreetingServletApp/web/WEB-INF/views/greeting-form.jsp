<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.greet.model.Greeting" %>

<%
    Greeting greeting = (Greeting) request.getAttribute("greeting");
    String title = (String) request.getAttribute("title");
    String actionUrl = (String) request.getAttribute("actionUrl");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= title %></title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: #0f172a;
            color: #e2e8f0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            background: #1e293b;
            border-radius: 12px;
            border: 1px solid #334155;
            padding: 40px;
            width: 500px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }

        h2 {
            color: #38bdf8;
            margin-bottom: 25px;
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

        input[type="text"],
        input[type="file"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #475569;
            border-radius: 8px;
            background: rgba(15, 23, 42, 0.6);
            color: #fff;
            font-size: 16px;
            outline: none;
        }

        input[type="file"] {
            cursor: pointer;
        }

        .img-preview {
            max-width: 100%;
            height: 150px;
            object-fit: cover;
            border-radius: 8px;
            margin-top: 10px;
            border: 1px solid #475569;
        }

        .actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            transition: 0.3s;
        }

        .btn-submit {
            background: #38bdf8;
            color: #0f172a;
        }

        .btn-submit:hover {
            background: #0ea5e9;
        }

        .btn-cancel {
            background: #475569;
            color: white;
        }

        .btn-cancel:hover {
            background: #334155;
        }
    </style>
</head>

<body>

<div class="container">

    <h2><%= title %></h2>

    <form action="<%= request.getContextPath() + "/" + actionUrl %>"
          method="POST"
          enctype="multipart/form-data">

        <input type="hidden"
               name="existingImagePath"
               value="<%= greeting != null && greeting.getImagePath() != null ? greeting.getImagePath() : "" %>" />

        <div class="form-group">
            <label>Greeting Message</label>

            <input type="text"
                   name="message"
                   value="<%= greeting != null && greeting.getMessage() != null ? greeting.getMessage() : "" %>"
                   placeholder="Enter greeting message"
                   required
                   autocomplete="off" />
        </div>

        <div class="form-group">
            <label>Upload Greeting Image</label>

            <input type="file"
                   name="imageFile"
                   accept="image/*" />

            <%
                if (greeting != null &&
                        greeting.getImagePath() != null &&
                        !greeting.getImagePath().isEmpty()) {
            %>

            <label style="margin-top:15px;">
                Current Image:
            </label>

            <img
                    src="<%= request.getContextPath() + greeting.getImagePath() %>"
                    class="img-preview"
                    alt="Current Image">

            <%
                }
            %>

        </div>

        <div class="actions">

            <button type="submit"
                    class="btn btn-submit">
                Save Greeting
            </button>

            <a href="<%= request.getContextPath() %>/greetings"
               class="btn btn-cancel">
                Cancel
            </a>

        </div>

    </form>

</div>

</body>
</html>