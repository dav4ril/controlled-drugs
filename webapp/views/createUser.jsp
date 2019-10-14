<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Authorisation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record - Create User</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Create User</h2>
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
    <form action="createUser.do" method="post" class="inline_form" autocomplete="off">
        First name: <input name="first" type="text">
        Surname: <input name="surname" type="text">
        Password (at least 8 characters):<br>
        <input name="pass1" type="password"><br>
        Repeat password:<br>
        <input name="pass2" type="password"><br>
        User Type:<br>
        <select name="level">
            <%
                String userLevel = session.getAttribute("userLevel").toString();
                if (Authorisation.checkAuthority("createAdmin", userLevel)) {
            %>
            <option value="Admin">Admin</option>
            <%
                }
            %>
            <option value="Sister">Sister/Charge Nurse</option>
            <option value="Nurse">Staff Nurse</option>
            <option value="Odp">ODP</option>
            <option value="Student">Student</option>
            <option value="Pharmacist">Pharmacist</option>
            <option value="Tech">Pharmacy Technician</option>
            <option value="Doctor">Doctor</option>
        </select>
        <input type="submit" value="Create User">
    </form>
</div>
<%
    if(Authorisation.checkAuthority("resetPassword", session.getAttribute("userLevel").toString())) {
%>
<h2>Search Users</h2>
<% if (request.getAttribute("searchErrorMessage") != null) { %>
<div class="custom-container">
    <ul class="error">${searchErrorMessage}</ul>
</div>
<% } %>
<div class="custom-container">
    <form action="searchUser.do" method="post" class="inline_form" autocomplete="off">
        Search by Username: <input type="radio" id="searchTypeUserName" name="method" value="username" checked>
        Other names: <input type="radio" id="searchTypeOtherNames" name="method" value="names">
        <br><br>
        <div id="userNameInput">
            Username: <input type="text" name="username">
        </div>
        <div id="otherNamesInput">
            First Name: <input type="text" name="first">
            Surname: <input type="text" name="surname">
        </div>
        <input type="submit" value="Search">
    </form>
</div>
<script type="text/javascript" src="../scripts/searchUser.js"></script>
<%
    }
%>
<%@include file="footer.jsp"%>
