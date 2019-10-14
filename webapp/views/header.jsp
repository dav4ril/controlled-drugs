<%@ page import="java.util.ArrayList" %>
<%@ page import="webapp.DbOp" %>
<%@ page import="webapp.Drug" %>
<%@ page import="webapp.Ward" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="webapp.Authorisation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="../styles/styles.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

<%
    int wardId = Integer.parseInt(session.getAttribute("wardId").toString());
    String wardName = session.getAttribute("wardName").toString();
%>

    <h1 class="header">Controlled Drug Record | Area: <%out.print(wardName);%></h1><br>
    <h2 class="header">Logged in as: <%out.print(session.getAttribute("user"));%> | User Level: <%out.print(session.getAttribute("userLevel"));%></h2>

    <%
        int selectedId = -1;
        if (request.getParameter("drugId") != null) {
            selectedId = Integer.parseInt(request.getParameter("drugId"));
        }
        ArrayList<Drug> drugList = Drug.getWardDrugs(wardId);
    %>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="home.do">Home</a></li>
                <%if (Authorisation.checkAuthority("performCheck",session.getAttribute("userLevel").toString())) {%>
                <li><a class="nav-link" href="check.do">Perform Check</a></li>
                <%}%>
                <% if (Authorisation.checkAuthority("resetPassword",session.getAttribute("userLevel").toString())) {%>
                <li><a class="nav-link" href="createUser.do">Manage Users</a></li>
                <% } else { %>
                <li><a class="nav-link" href="createUser.do">Create User</a></li> <% } %>
                <%if (Authorisation.checkAuthority("createDrug",session.getAttribute("userLevel").toString())) {%>
                <li><a class="nav-link" href="addDrug.do">Create New Drug</a></li>
                <%}%>
                <%if (Authorisation.checkAuthority("allocateDrug",session.getAttribute("userLevel").toString())) {%>
                <li><a class="nav-link" href="allocateDrug.do">Allocate Drug to Area</a></li>
                <%}%>
                <%if (Authorisation.checkAuthority("createWard",session.getAttribute("userLevel").toString())) {%>
                <li><a class="nav-link" href="addWard.do">Add Area</a></li>
                <%}%>
                <%if (session.getAttribute("userLevel").toString().equals("Admin")) {%>
                <li><a class="nav-link" href="viewAuthorisations.do">Authorisations</a></li>
                <%}%>
                <li><a class="nav-link" href="changePassword.do">Change Password</a></li>
                <li><a class="nav-link" href="logout.do">Logout</a></li>
            </ul>
        </div>
    </nav>
<%
    if (!drugList.isEmpty()) {
%>
<form class="drugList" method="get" action="viewDrug.do">
    <select name="drugId" id="drugSelect">
        <%
            for (Drug drug : drugList) {
                if (selectedId != -1 && drug.getId() == selectedId) { %>
        <option value="<%out.print(drug.getId());%>" selected><%out.print(drug.toString());%></option>
        <%
        } else {
        %>
        <option value="<%out.print(drug.getId());%>"><%out.print(drug.toString());%></option>
        <%
                }
            }
        %>
    </select>
    <input type="hidden" name="wardId" value="<%out.print(wardId);%>">
    <input type="submit" value="View Drug" style="display: inline">
</form>
<%
    }
%>