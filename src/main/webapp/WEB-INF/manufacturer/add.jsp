<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add manufacturer</title>
</head>
<body>
<h1>Hello! Please provide manufacturer data</h1>
<a href="${pageContext.request.contextPath}/">Back to main page</a><br>
<form method="post" action="${pageContext.request.contextPath}/manufacturers/add">
    Please provide manufacturer name: <input type="text" name="name">
    Please provide manufacturer country: <input type="text" name="country">
    <button type="submit">Submit</button>
</form>
</body>
</html>
