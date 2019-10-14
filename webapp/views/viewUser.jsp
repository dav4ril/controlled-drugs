<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record - View User</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4>View User Details : ${username}</h4>
<div class="custom-container">
    <table>
        <tr>
            <td>First Name:</td>
            <td>${first}</td>
        </tr>
        <tr>
            <td>Surname:</td>
            <td>${surname}</td>
        </tr>
        <tr>
            <td>User Role:</td>
            <td>${level}</td>
        </tr>
    </table>
</div>
<%
    if (Authorisation.checkAuthority("resetPassword", session.getAttribute("userLevel").toString())) {
%>
<h4>Reset User Password</h4>
<div class="custom-container">
    <p class="error">Warning:</p>
    <ul class="error">
        <li>Clicking this button will instantly reset the user's password.</li>
        <li>Please ensure you have permission from the user before doing this.</li>
        <li>This action will be logged.</li>
    </ul>
    <form method="post" action="resetPassword.do">
        <input type="hidden" value="${username}" name="userName">
        <div class="centreDiv">
            <input class="centreSubmit" type="submit" value="Reset Password">
        </div>
    </form>
</div>
<%
    }
%>
</body>
<%@include file="footer.jsp"%>
