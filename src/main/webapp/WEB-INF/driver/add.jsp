<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Driver</title>
</head>
<body>
<h1>Hello! Please provide driver details</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/drivers/add">
    Please provide driver name: <input type="text" name="name">
    Please provide driver license: <input type="text" name="license">
    <button type="submit">Submit</button>
</form>
</body>
</html>
