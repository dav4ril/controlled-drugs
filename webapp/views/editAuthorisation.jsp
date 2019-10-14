<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Authorisation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: Edit Authorisation</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4>Edit User Level: <%out.print(request.getParameter("level"));%></h4>
<% if (request.getAttribute("errorMessage") != null) { %>
<div class="container">
    <ul class="error">${errorMessage}</ul>
</div>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<%
    Authorisation authorisation = Authorisation.getAuthorisation(request.getParameter("level"));
%>
<div class=custom-container>
    <form method="post" action="editAuthorisation.do">
        <input type="hidden" name="level" value="<%out.print(request.getParameter("level"));%>">
        <table>
            <tr>
                <th>Action</th>
                <th>Option</th>
            </tr>
        <%
            ArrayList<String> columnLabels = Authorisation.getColumnLabels();
            for (String columnLabel : columnLabels) {
        %>
            <tr>
                <td><%out.print(Authorisation.getActionDesc(columnLabel));%></td>
                <td>
                    <%
                        if (authorisation.getAction(columnLabel)) {
                    %>
                    <input type="radio" name="<%out.print(columnLabel);%>" value="yes" checked>Yes<br>
                    <input type="radio" name="<%out.print(columnLabel);%>" value="no">No
                    <%
                        } else {
                    %>
                    <input type="radio" name="<%out.print(columnLabel);%>" value="yes">Yes<br>
                    <input type="radio" name="<%out.print(columnLabel);%>" value="no"checked>No
                    <%
                        }
                    %>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="submit" value="Submit Changes">
    </form>
</div>
<%@include file="footer.jsp"%>
