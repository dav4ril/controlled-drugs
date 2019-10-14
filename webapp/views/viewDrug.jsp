<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="webapp.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  int localDrugId = Integer.parseInt(request.getAttribute("drugId").toString());
    Drug selectedDrug = Drug.getDrug(localDrugId);
    int localWardId = Integer.parseInt(request.getParameter("wardId"));
%>
<html>
<head>
    <a id="details"></a>
    <title>Controlled Drugs Record - View Drug</title>

</head>
<body class="extraTopMargin">
<%@include file="header.jsp"%>
<ul class="nav justify-content-center">
    <li class="nav-item"><a class="nav-link" href="#details">Details</a> </li>
    <li class="nav-item"><a class="nav-link" href="#create">Create Entry</a> </li>
    <li class="nav-item"><a class="nav-link" href="#entries">Entries</a> </li>
    <li class="nav-item"><a class="nav-link" href="#batches">Batches</a> </li>
</ul>
<h4>Details: <%out.print(selectedDrug.toString());%></h4>
<% if (request.getAttribute("alertErrorMessage") != null) { %>
<div class="custom-container">
    <ul class="error">
        ${errorMessage}
    </ul>
</div>
<% } %>
<% if (request.getAttribute("alertSuccessMessage") != null) { %>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<div class="custom-container">
    <table>
        <tr>
            <td>Strength:</td>
            <td><%out.print(selectedDrug.getStrength());%></td>
        </tr>
        <tr>
            <td>Units:</td>
            <td><%out.print(selectedDrug.getUnits());%></td>
        </tr>
        <tr>
            <td>Current Quantity:</td>
            <td><%out.print(Ward.getWardDrugQuantity(localWardId, selectedDrug.getId()));%></td>
        </tr>
        <tr>
            <td>Alert when below:</td>
            <td><%out.print(Ward.getWardDrugAlert(localWardId, selectedDrug.getId()));%></td>
        </tr>
        <%
            if (Authorisation.checkAuthority("setAlert", session.getAttribute("userLevel").toString())) {
        %>
        <tr>
            <td>Edit alert level:</td>
            <td>
                <form method="post" action="updateAlertLevel.do" autocomplete="off">
                    <input type="hidden" name="drugId" value="<%out.print(selectedDrug.getId());%>">
                    <input type="hidden" name="wardId" value="<%out.print(wardId);%>">
                    <input type="text" name="alert" id="autowidth">
                    <input type="submit" value="Update Alert Level">
                </form>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
<a id="create"></a>
<h4>Create New Entry</h4>
<%
    if (request.getAttribute("errorMessage") != null) {
%>
<div class="custom-container">
    <ul class="error">${errorMessage}</ul>
</div>
<% } %>
<%  if (request.getAttribute("successMessage") != null) {
%>
<div class="custom-container">
    <p class="success">${successMessage}</p>
</div>
<% } %>
<%
    ArrayList<Batch> wardBatchList = Batch.getWardDrugBatches(wardId, localDrugId);
    ArrayList<Batch> batchList = Batch.getDrugBatches(localDrugId);
%>
<div class="custom-container">
    <form action="viewDrug.do" method="post" autocomplete="off">
        <h2>Drug: <%out.print(selectedDrug.getName() + " " + selectedDrug.getStrength());%></h2>
        Type of Entry:<br>
        <select id="typeSelector" name="type">
            <option value="patient">Patient</option>
            <option value="restock">Add Stock</option>
            <option value="return">Return to Pharmacy</option>
            <option value="error">Record Error</option>
        </select><br>
        <div id="patientNameInput">
            Patient Name:<br>
            <input type="text" name="patient"><br>
        </div>
        <div id="takenInput">
            Quantity (<%out.print(selectedDrug.getUnits());%>) Taken:<br>
            <input type="text" name="taken"><br>
        </div>
        <div id="addedInput">
            Quantity (<%out.print(selectedDrug.getUnits());%>) Added:<br>
            <input type="text" name="added"><br>
        </div>
        <div id="newQuantityInput">
            New Quantity (<%out.print(selectedDrug.getUnits());%>):<br>
            <input type="text" name="newQuantity"><br>
        </div>
        <div id="addBatchSelectContainer">
            Select Batch
            <select id="addBatchSelect" name="addBatchSelect">
                <option value="newbatch">(New Batch)</option>
                <%
                    for (Batch batch : batchList) {
                        if (batch.getExpiryDate().isBefore(LocalDate.now())) {
                            continue;
                        }
                %>
                <option value="<%out.print(batch.getId());%>"><%out.print(batch.getBatchStr());%></option>
                <%
                    }
                %>
            </select>
        </div>
        <div id="takeBatchSelectContainer">
            Select Batch
            <select id="takeBatchSelect" name="takeBatchSelect">
                <%
                    for (Batch batch : wardBatchList) {
                        if (Ward.getWardBatchQuantity(wardId, batch.getId()) == 0.0) {
                            continue;
                        }
                %>
                <option value="<%out.print(batch.getId());%>"><%out.print(batch.getBatchStr());%></option>
                <%
                    }
                %>
                <option value="notlisted">(Batch Not Listed)</option>
            </select>
        </div>
        <div id="batchStrInput">
            Batch:
            <input type="text" name="batch">
        </div>
        <div id="batchDatesInput">
            Expiry Date:<br>
            <select name="expMonth" class="autowidth">
                <option value="1">January</option>
                <option value="2">February</option>
                <option value="3">March</option>
                <option value="4">April</option>
                <option value="5">May</option>
                <option value="6">June</option>
                <option value="7">July</option>
                <option value="8">August</option>
                <option value="9">September</option>
                <option value="10">October</option>
                <option value="11">November</option>
                <option value="12">December</option>
            </select>
            <input type="text" name="expYear" maxlength="4" class="autowidth" value="YYYY"><br>
        </div>
        <div id="commentsInput">
            Comments:<br>
            <textarea rows="4" cols="50" name="comments"></textarea><br>
        </div>
        Checking User:<br>
        <input name="checkingUser" type="text"><br>
        Checking User Password:<br>
        <input name="checkPass" type="password"><br>
        <input name="drugId" type="hidden" value="<%out.print(localDrugId);%>">
        <input name="wardId" type="hidden" value="<%out.print(localWardId);%>">
        <input type="submit" value="Create entry">
    </form>
</div>
<a id="entries"></a>
<h4>Entries</h4>
<%
    ArrayList<Entry> entriesList = Entry.getEntries(localDrugId, localWardId);
    if (!entriesList.isEmpty()) {
%>

    <table class="table table-hover">
        <tr>
            <th>Date</th>
            <th>Time</th>
            <th>Batch</th>
            <th>Patient</th>
            <th>Given By</th>
            <th>Checked By</th>
            <th>Type</th>
            <th>Quantity Change</th>
            <th>New Quantity</th>
            <th>Comments</th>
        </tr>
        <%
            for (Entry entry : entriesList) {
                int batchId = entry.getBatchId();
                String batchStr;
                if (batchId == -1) {
                    batchStr = "(No batch)";
                } else {
                    Batch batch = Batch.getBatch(batchId);
                    batchStr = batch.getBatchStr();
                }
                String pattern = "00.#";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                String hour = decimalFormat.format(entry.getDateTime().getHour());
                String minute = decimalFormat.format(entry.getDateTime().getMinute());
                String seconds = decimalFormat.format(entry.getDateTime().getSecond());
        %>
        <tr>
            <td><%out.print(entry.getDateTime().getDayOfMonth());%>/
                <%out.print(entry.getDateTime().getMonth());%>/
                <%out.print(entry.getDateTime().getYear());%>
            </td>
            <td><%out.print(hour);%>:
                <%out.print(minute);%>:
                <%out.print(seconds);%>
            </td>
            <td><%out.print(batchStr);%></td>
            <td><%out.print(entry.getPatient());%></td>
            <td><a href="viewUser.do?username=<%out.print(entry.getGivenBy());%>" >
                <%out.print(entry.getGivenBy());%>
                </a>
            </td>
            <td><a href="viewUser.do?username=<%out.print(entry.getCheckedBy());%>" >
                <%out.print(entry.getCheckedBy());%>
                </a>
            </td>
            <td>
                <%out.print(entry.getType().substring(0,1).toUpperCase() + entry.getType().substring(1));%>
            </td>
            <td><%out.print(entry.getQuantityChange());%></td>
            <td><%out.print(entry.getNewQuantity());%></td>
            <td><%out.print(entry.getComments());%></td>
        </tr>
        <%
            }
        %>
    </table>

<%
    } else {
%>
    <div class="custom-container">
        <p class="error">No entries exist for this drug</p>
    </div>
<%
    }
%>
<a id="batches"></a>
<h4>Batches</h4><a id="batches"></a>
<%
    if (!wardBatchList.isEmpty()) {
%>
    <table class="table table-hover">
        <tr>
            <th>Batch</th>
            <th>Expiry Date</th>
            <th>Quantity</th>
        </tr>
        <%
            for (Batch batch : wardBatchList) {
                LocalDate date = batch.getExpiryDate();
                int month = date.getMonthValue();
                int year = date.getYear();
                if (Ward.getWardBatchQuantity(wardId, batch.getId()) == 0.0) {
                    continue;
                }
        %>
        <tr class="centreCell">
            <td><%out.print(batch.getBatchStr());%></td>
            <td><%out.print(month);%>/<%out.print(year);%></td>
            <td><%out.print(Ward.getWardBatchQuantity(wardId, batch.getId()));%></td>
        </tr>
        <%
            }
        %>
    </table>
<%
    } else {
%>
    <div class="custom-container">
        <p class="error">No batches exist for this drug</p>
    </div>
<%
    }
%>

<script type="text/javascript" src="../scripts/entries.js"></script>
<%@include file="footer.jsp"%>
