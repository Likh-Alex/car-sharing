<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
  <h1>Welcome to taxi service</h1>
  <a href="${pageContext.request.contextPath}/drivers/add">Add new driver</a><br>
  <a href="${pageContext.request.contextPath}/drivers/">Display all drivers</a><br>
  <a href="${pageContext.request.contextPath}/manufacturers/add">Add new manufacturer</a><br>
  <a href="${pageContext.request.contextPath}/manufacturers/">Display all manufacturers</a><br>
  <a href="${pageContext.request.contextPath}/cars/add">Add new car</a><br>
  <a href="${pageContext.request.contextPath}/cars/">Display all cars</a><br>
  </body>
</html>
