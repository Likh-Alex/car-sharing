<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Car</title>
</head>
<body>
<h1>Hello! Please provide car details</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/cars/add">
    Please provide car model: <input type="text" name="model" required>
    Please provide manufacturer ID: <input type="number" name="manufacturer_id" required>
    <button type="submit">Submit</button>
</form>
</body>
</html>
