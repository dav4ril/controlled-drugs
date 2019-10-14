<%@ page import="java.text.DecimalFormat" %>
<%@ page import="webapp.Check" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record - Perform Check</title>
</head>
<body>
<%@include file="header.jsp"%>
<h4><%out.print(session.getAttribute("wardName"));%>: Stock Check</h4>
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
<div class="checkContainer">
    <table>
        <tr>
            <th>Drug Description</th>
            <th>Quantity</th>
            <th>Drug Page</th>
        </tr>
        <%
            ArrayList<Drug> drugsList = Drug.getWardDrugs(wardId);
            for (Drug drug : drugsList) {
                String viewDrugStr = "viewDrug.do?drugId=" + drug.getId() + "&wardId=" + wardId;
        %>
        <tr>
            <td>
                <%out.print(drug.toString());%>
            </td>
            <td>
                <%out.print(Ward.getWardDrugQuantity(wardId,drug.getId()));%>
            </td>
            <td class="centreCell"><a href="<%out.print(viewDrugStr);%>"><button>View Drug Page</button></a></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td>
                <p class="emphasise">By checking this box you are confirming that <br>the quantity for each drug shown
                    in the list above is correct:</p>
            </td>
            <form method="post" action="check.do" class="inline_form">
            <td>
                    <input type="checkbox" name="checkedandcorrect">
            </td>
        </tr>
    </table>
    Checking User:<input type="text" name="checkingUser">
    Checking User Password:<input type="password" name="checkPass">
        <input type="hidden" name="wardId" value="<%out.print(wardId);%>">
    <input type="submit" value="Submit Check">
    </form>
</div>
<h2>Previous Checks for <%out.print(session.getAttribute("wardName"));%></h2>
    <%
        ArrayList<Check> checkEntries = Check.getChecks(wardId);
        if (checkEntries.isEmpty()) {
    %>
    <div class="custom-container">
        <p class="error">No Checks for current area yet.</p>
    </div>
    <%
        } else {
    %>
<table class="table table-hover">
    <tr>
        <th>Date</th>
        <th>Time</th>
        <th>1st Checker</th>
        <th>2nd Checker</th>
    </tr>
    <%
            for (Check check : checkEntries) {
    %>
    <tr>
        <td><%out.print(check.getDateTime().getDayOfMonth() + "/" + check.getDateTime().getMonthValue() + "/" +
                check.getDateTime().getYear());%></td>
        <td><%out.print(check.getDateTime().getHour() + ":" + check.getDateTime().getMinute() + ":" +
                check.getDateTime().getSecond());%></td>
        <td>
            <a href="viewUser.do?username=<%out.print(check.getCurrentUser());%>">
                <%out.print(check.getCurrentUser());%>
            </a>
        </td>
        <td>
            <a href="viewUser.do?username=<%out.print(check.getCheckingUser());%>">
                <%out.print(check.getCheckingUser());%>
            </a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
<%@include file="footer.jsp"%>
