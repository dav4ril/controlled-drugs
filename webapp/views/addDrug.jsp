<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record Add Drug</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Create New Drug Profile</h2>
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
    <form action="addDrug.do" method="post" class="inline_form" autocomplete="off">
        Drug Name:<input name="name" type="text"><br>
        Strength:<input name="strength" type="text"><br>
        Units:<select name="units">
            <option value="tablets">Tablets</option>
            <option value="vials">Vials</option>
            <option value="ml">Millilitres</option>
            <option value="patches">Patches</option>
        <option value="syringes">Syringes</option>
        </select>
        <input type="submit" value="Add Drug">
    </form>
</div>
<script type="text/javascript" src="../scripts/addDrug.js"></script>
<%@include file="footer.jsp"%>
