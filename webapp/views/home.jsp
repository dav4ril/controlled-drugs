<%@ page import="java.time.LocalDate" %>
<%@ page import="static java.time.temporal.ChronoUnit.DAYS" %>
<%@ page import="webapp.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Controlled Drugs Record - Home</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2>Dashboard</h2>
<% if (request.getAttribute("errorMessage") != null) { %>
<p class="error">${errorMessage}</p>
<% } %>
<%
    if (drugList.isEmpty()) {
%>
<p class="error">Your area has no drugs allocated yet. Click "Allocate Drugs to Area" above<br>
    Drugs will then appear in a list in the top right of the screen</p>
<%
    }
%>
<div class="lowStockContainer">
    <h2>Low Stock Alerts</h2>
    <%
        ArrayList<Drug> lowStock = Drug.getLowStockDrugs(wardId);
        if (lowStock.size() == 0) {
    %>
            <p class="success">No drugs below alert levels</p>
    <%
        } else {
    %>
            <table>
                <tr>
                    <th>Drug</th>
                    <th>Current Quantity</th>
                    <th>Alert Level</th>
                    <th>Drug Page</th>
                </tr>
    <%
            for (Drug drug : lowStock) {
                String viewDrugStr = "viewDrug.do?drugId=" + drug.getId() + "&wardId=" + wardId;
    %>
                <tr class="centreCell">
                    <td><%out.print(drug.toString());%></td>
                    <td><%out.print(Ward.getWardDrugQuantity(wardId, drug.getId()));%></td>
                    <td><%out.print(Ward.getWardDrugAlert(wardId, drug.getId()));%></td>
                    <td><a href="<%out.print(viewDrugStr);%>"><button>View Drug Page</button></a></td>
                </tr>
    <%
            }
        }
    %>
            </table>
</div>

<div class="lowStockContainer">
    <h2>Expiring Batches</h2>
    <% ArrayList<Batch> expiringBatchList = Batch.getWardExpiringBatches(wardId); %>
    <% LocalDate now = LocalDate.of(2019, 8, 25); %>
    <% if (expiringBatchList.size() == 0) { %>
            <p class="success">No Expiring Batches</p>
    <% } else { %>
            <table>
                <tr>
                    <th>Drug Description</th>
                    <th>Batch No.</th>
                    <th>Expiring</th>
                    <th>Quantity in Batch</th>
                    <th>Drug Page</th>
                </tr>
    <%      for (Batch batch : expiringBatchList) {
                String viewDrugStr = "viewDrug.do?drugId=" + batch.getDrugId() + "&wardId=" + wardId;
                String daysLeft = Utils.getDaysLeft(batch.getExpiryDate()); %>
                <tr class="centreCell">
                    <td><%out.print(Drug.getDrug(batch.getDrugId()).toString());%></td>
                    <td><%out.print(batch.getBatchStr());%></td>
                    <td><%out.print(batch.getExpiryDate().toString());%>
                        <%out.print(daysLeft);%>
                    </td>
                    <td><%out.print(Ward.getWardBatchQuantity(wardId, batch.getId()));%></td>
                    <td>
                        <a href="<%out.print(viewDrugStr);%>"><button>View Drug</button></a>
                    </td>
                </tr>
    <%      } %>
    <% } %>
            </table>
</div>
<%@include file="footer.jsp"%>
