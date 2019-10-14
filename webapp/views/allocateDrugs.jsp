<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record Allocate Drugs to Ward</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Allocate Drug to Area: <%out.print(session.getAttribute("wardName").toString());%></h2>
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
    <form action="allocateDrug.do" method="post" class="inline_form" autocomplete="off">
        <select name="drug">
            <%
                ArrayList<Drug> allDrugs = Drug.getDrugs();
                for (Drug drug : allDrugs) {
            %>
            <option value="<%out.print(drug.getId());%>"><%out.print(drug.toString());%></option>
            <%
                }
            %>
        </select>
        <input type="hidden" name="wardId" value="<%out.print(wardId);%>">
        <input type="submit" value="Alocate Drug">
    </form>
</div>
<%@include file="footer.jsp"%>
