<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.Authorisation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record: View Authorisations</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4>Authorisations</h4>
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
<table class="table-active">
    <tr>
        <th class="table-secondary">User Level</th>
        <th class="table-secondary">Create Admin</th>
        <th class="table-secondary">Create Nurse</th>
        <th class="table-secondary">Create ODP</th>
        <th class="table-secondary">Create Pharmacist</th>
        <th class="table-secondary">Create Student</th>
        <th class="table-secondary">Create Pharmacy Technician</th>
        <th class="table-secondary">Create Doctor</th>
        <th class="table-secondary">Create Sister</th>
        <th class="table-secondary">Create Entry</th>
        <th class="table-secondary">Check Entry</th>
        <th class="table-secondary">Create Ward</th>
        <th class="table-secondary">Create Drug</th>
        <th class="table-secondary">Allocate Drug</th>
        <th class="table-secondary">Perform Full Check</th>
        <th class="table-secondary">Set Alert Level</th>
        <th class="table-secondary">Give Drug</th>
        <th class="table-secondary">Reset Passwords</th>
        <th class="table-secondary">Edit</th>
    </tr>
<%
    ArrayList<Authorisation> authorisationsList = Authorisation.getAuthorisations();
    for (Authorisation authorisation : authorisationsList) {
        if (authorisation.getLevel().equals("Admin")) {
            continue;
        }
%>
    <tr>
        <td class="table-secondary"><%out.print(authorisation.getLevel());%></td>
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
<%@include file="footer.jsp"%>
