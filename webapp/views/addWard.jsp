<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record Add Ward</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4>Add Area</h4>
<% if (request.getAttribute("errorMessage") != null) { %>
<div class="custom-container">
    <ul class="error">${errorMessage}</ul>
</div>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<div class="custom-container">
    <form action="addWard.do" method="post" class="inline_form" autocomplete="off">
        Area Name:<input name="name" type="text"><br>
        <input type="submit" value="Add Area">
    </form>
</div>
<%@include file="footer.jsp"%>
