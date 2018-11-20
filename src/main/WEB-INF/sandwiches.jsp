<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <title>First Web Application</title>
</head>

<body>

<p>All sandwiches are:</p>
<c:forEach var="sandwich" items="${sandwichList}">
    <p><c:out value="${sandwich.name}"/></p>
    <p><c:out value="${sandwich.ingredients}"/></p>
    <p><c:out value="${sandwich.price}"/></p>
</c:forEach>
<p><c:out value="${sandwichList}"/></p>

</body>

</html>