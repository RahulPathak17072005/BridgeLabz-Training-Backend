<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Greeting Application</title>
</head>
<body>

<h2>Spring Core Greeting Application</h2>
<% if (request.getAttribute("lastUser") != null) { %>

<p>
    Welcome back!
    Last greeted:
    <strong>${lastUser}</strong>
</p>

<% } %>
<form action="greet" method="post">

    <label>Name:</label>
    <input
            type="text"
            id="userName"
            name="userName"
            value="${savedName}"
            placeholder="e.g. Rahul"
            required/>

    <br><br>

    <input
            type="submit"
            value="Greet"/>

</form>

</body>
</html>