<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add driver to car</title>
</head>
<body>
<h1>Please enter Car Id and Driver Id:</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/cars/drivers/add">
    Please provide car ID: <input type="number" name="car_id" required><br>
    Please provide driver ID: <input value="${driver_id}" type="number" name="driver_id" required>
    <br>
    <button type="submit">Submit</button>
</form>
<h1>Available cars:</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Model</th>
        <th>Manufacturer Name</th>
    </tr>
    <c:forEach var="car" items="${cars}">
        <tr>
            <td>
                <c:out value="${car.id}"/>
            </td>
            <td>
                <c:out value="${car.model}"/>
            </td>
            <td>
                <c:out value="${car.manufacturer.name}"/>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
