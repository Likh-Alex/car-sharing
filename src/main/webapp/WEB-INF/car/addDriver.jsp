<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add driver to car</title>
</head>
<body>
<h1>Please enter Car Id and Driver Id:</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/cars/drivers/add">
    Please provide car ID: <input type="number" name="car_id">
    Please provide driver ID: <input type="number" name="driver_id">
    <button type="submit">Submit</button>
</form>
</body>
</html>
