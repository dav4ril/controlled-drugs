<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: Change Password</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4>Change Password for User: <%out.print(session.getAttribute("user").toString());%></h4>
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
    <form method="post" action="changePassword.do" autocomplete="off">
        Old Password: <input type="password" name="old">
        New Password (8 or more characters): <input type="password" name="pass1">
        Repeat New Password: <input type="password" name="pass2">
        <input type="hidden" name="userName" value="<%out.print(session.getAttribute("user").toString());%>">
        <input type="submit" value="Change Password">
    </form>
</div>
<%@include file="footer.jsp"%>
