<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Ward" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs - Login</title>
    <link rel="stylesheet" href="../styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<h3>Controlled Drugs Record</h3>
<h4>Please login below:</h4>
<% if (request.getAttribute("errorMessage") != null) { %>
<div class="custom-container">
    <p class="error">${errorMessage}</p>
</div>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<div class="custom-container">
    <form method="post" action="home.do">
        User name: <input type="text" name="username">
        Password: <input type="password" name="password">
        Choose Area: <select name="wardId">
        <%
            ArrayList<Ward> wardList = Ward.getWardList();
            for (Ward ward : wardList) {

        %>
                    <option value="<%out.print(ward.getId());%>"><%out.print(ward.getName());%></option>
        <%
            }
        %>
                </select>
        <input type="submit" value="Login">
    </form>
</div>
<%@include file="footer.jsp"%>
