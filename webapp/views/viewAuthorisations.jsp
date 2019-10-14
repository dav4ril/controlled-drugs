<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Authorisation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: View Authorisations</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Authorisations</h2>
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
<div class=authorContainer>
<table>
    <tr>
        <th>User Level</th>
        <th>Create Admin</th>
        <th>Create Nurse</th>
        <th>Create ODP</th>
        <th>Create Pharmacist</th>
        <th>Create Student</th>
        <th>Create Pharmacy Technician</th>
        <th>Create Doctor</th>
        <th>Create Sister</th>
        <th>Create Entry</th>
        <th>Check Entry</th>
        <th>Create Ward</th>
        <th>Create Drug</th>
        <th>Allocate Drug</th>
        <th>Perform Full Check</th>
        <th>Set Alert Level</th>
        <th>Give Drug</th>
        <th>Reset Passwords</th>
        <th>Edit</th>
    </tr>
<%
    ArrayList<Authorisation> authorisationsList = Authorisation.getAuthorisations();
    for (Authorisation authorisation : authorisationsList) {
        if (authorisation.getLevel().equals("Admin")) {
            continue;
        }
%>
    <tr>
        <td><%out.print(authorisation.getLevel());%></td>
        <%
            out.print(Authorisation.cellCreation(authorisation.canCreateAdmin()));
            out.print(Authorisation.cellCreation(authorisation.canCreateNurse()));
            out.print(Authorisation.cellCreation(authorisation.canCreateOdp()));
            out.print(Authorisation.cellCreation(authorisation.canCreatePharmacist()));
            out.print(Authorisation.cellCreation(authorisation.canCreateStudent()));
            out.print(Authorisation.cellCreation(authorisation.canCreateTech()));
            out.print(Authorisation.cellCreation(authorisation.canCreateDoctor()));
            out.print(Authorisation.cellCreation(authorisation.canCreateSister()));
            out.print(Authorisation.cellCreation(authorisation.canCreateEntry()));
            out.print(Authorisation.cellCreation(authorisation.canCheckEntry()));
            out.print(Authorisation.cellCreation(authorisation.canCreateWard()));
            out.print(Authorisation.cellCreation(authorisation.canCreateDrug()));
            out.print(Authorisation.cellCreation(authorisation.canAllocateDrug()));
            out.print(Authorisation.cellCreation(authorisation.canPerformCheck()));
            out.print(Authorisation.cellCreation(authorisation.canSetAlert()));
            out.print(Authorisation.cellCreation(authorisation.canGiveDrug()));
            out.print(Authorisation.cellCreation(authorisation.canResetPassword()));
        %>
        <td>
            <form method="get" action="editAuthorisation.do">
                <input type="hidden" name="level" value="<%out.print(authorisation.getLevel());%>">
                <input type="submit" value="Edit">
            </form>
        </td>
    </tr>
<%
    }
%>
</table>
</div>
<%@include file="footer.jsp"%>
