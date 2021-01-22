<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Driver</title>
</head>
<body>
<h1>Hello! Please provide driver details</h1>
<h3 style="color: red">${errorMessage}</h3>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/drivers/add">
    Please provide driver name: <input type="text" name="name" required><br>
    Please provide driver license: <input type="text" name="license" required><br>
    Please provide driver login: <input type="text" name="login" required><br>
    Please provide driver password: <input type="password" name="password" required><br>
    Please repeat driver password: <input type="password" name="password-repeat" required><br>
    <button type="submit">Submit</button>
</form>
</body>
</html>
