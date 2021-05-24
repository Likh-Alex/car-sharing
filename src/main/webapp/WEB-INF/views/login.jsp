<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login page</h1>
<h3 style="color: red">${errorMessage}</h3>
<form method="post" action="${pageContext.request.contextPath}/login">
    Please provide driver login: <input type="text" name="login" required><br>
    Please provide driver password: <input type="password" name="password" required><br>
    <button type="submit">Login</button>
</form>
<td><a href="${pageContext.request.contextPath}/drivers/add">Register a driver</a></td>
</body>
</html>
