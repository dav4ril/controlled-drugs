<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record - View Drug</title>
</head>
<body>
<%@include file="header.jsp"%>
<% if (request.getAttribute("alertErrorMessage") != null) { %>
<div class="custom-container">
    <p class="error">${errorMessage}</p>
    <a href="viewDrug.do?drug=${drugId}"><button value="Back">Back</button></a>
</div>
<% } %>
<% if (request.getAttribute("alertSuccessMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
    <a href="viewDrug.do?drug=${drugId}"><button value="Back">Back</button></a>
</div>
<% } %>
<%@include file="footer.jsp"%>
