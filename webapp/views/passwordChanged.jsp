<%@ page import="java.text.DecimalFormat" %>
<%@ page import="webapp.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: Password Change</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Password Change</h2>
<% if (request.getAttribute("errorMessage") != null) { %>
<div class="checkContainer">
    <p class="error">${errorMessage}</p>
</div>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<%
    }
%>
<%@include file="footer.jsp"%>
