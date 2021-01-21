<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All cars</title>
</head>
<body>
<h1>All cars page</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Model</th>
        <th>Manufacturer ID</th>
        <th>Manufacturer Name</th>
        <th>Drivers</th>
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
                <c:out value="${car.manufacturer.id}"/>
            </td>
            <td>
                <c:out value="${car.manufacturer.name}"/>
            </td>
            <td>
                <table>
                    <c:choose>
                        <c:when test="${car.drivers.size() > 0}">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>License Number</th>
                            </tr>
                            <c:forEach var="driver" items="${car.drivers}">
                                <tr>
                                    <td>
                                        <c:out value="${driver.id}"/>
                                    </td>
                                    <td>
                                        <c:out value="${driver.name}"/>
                                    </td>
                                    <td>
                                        <c:out value="${driver.licenseNumber}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            No drivers
                        </c:otherwise>
                    </c:choose>
                </table>
            </td>
            <td><a href="${pageContext.request.contextPath}/cars/drivers/add">Add driver</a></td>
            <td><a href="${pageContext.request.contextPath}/cars/delete?id=${car.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
