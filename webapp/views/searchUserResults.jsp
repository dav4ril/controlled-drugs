<%@ page import="java.text.DecimalFormat" %>
<%@ page import="webapp.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: Search User Results</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Search Users</h2>
<h2>First name: "<%out.print(request.getParameter("first"));%>"
    Surname: "<%out.print(request.getParameter("surname"));%>"</h2>
<% if (request.getAttribute("errorMessage") != null) { %>
<div class="checkContainer">
    <p class="error">${errorMessage}</p>
</div>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<%
    ArrayList<User> userList = User.getUsersCalled(request.getParameter("first"),request.getParameter("surname"));
    if (userList.isEmpty()) {
%>
<div class="custom-container">
    <p class="error">No users found</p>
</div>
<%
    } else {
%>

<div class="checkContainer">
    <table>
        <tr>
            <th>Username</th>
            <th>First Name</th>
            <th>Surname</th>
            <th>Role</th>
            <th>Created On</th>
            <th>Created By</th>
            <th>View User Page</th>
        </tr>
        <%
            for (User user : userList) {
        %>
        <tr>
            <td><%out.print(user.getUserName());%></td>
            <td><%out.print(user.getFirstName());%></td>
            <td><%out.print(user.getSurname());%></td>
            <td><%out.print(user.getLevel());%></td>
            <td>
                <%out.print(user.getCreatedOn().getDayOfMonth());%>/
                <%out.print(user.getCreatedOn().getMonthValue());%>/
                <%out.print(user.getCreatedOn().getYear());%>
            </td>
            <td><%out.print(user.getCreatedBy());%></td>
            <td class="centreCell">
                <a href="viewUser.do?username=<%out.print(user.getUserName());%>">
                    <button>View User</button>
                </a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
<%
    }
%>
<%@include file="footer.jsp"%>
