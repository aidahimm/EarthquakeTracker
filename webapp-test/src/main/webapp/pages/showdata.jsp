<%@ page import="it.unipi.dstm.EarthquakeDTO" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Bashar
  Date: 4/19/2022
  Time: 12:32 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hi ...</title>
    <script type = text/javascript>
        var ws;
        var host = document.location.host;
        var pathname = document.location.pathname;
        const url = "ws://localhost:8080/webapp-test/MessageSocket1";

        ws = new WebSocket(url);

        ws.onmessage = function(event) {
            console.log(event.data);
        };
    </script>
</head>
<%
    List<EarthquakeDTO> list = (List<EarthquakeDTO>) request.getAttribute("earthquakesList");

%>
<body>
<h1>Earthquakes Tracker System</h1>


<br>
<form action="<%= request.getContextPath()%>/earthInfo">
    <p>Please select your region to retrieve the last updates on earthquakes data: </p>
    <label>
        <input type="radio" name="region" value="current" checked>
    </label>Current Region</input>
    <label>
        <input type="radio" name="region" value="all">
    </label>All Regions</input>

    <br>
    <label for="start">From date:</label>

    <input type="date" id="start" name="startDate"
           min="2022-01-01" max="2050-12-31">


    <label for="end">To date:</label>

    <input type="date" id="end" name="endDate"
           min="2022-01-01" max="2050-12-31">



    <input type="submit" value="Filter"/>
</form>
<br>

<table style="width:100%">
    <tr>
        <th>Magnitude</th>
        <th>Latitude</th>
        <th>Longitude</th>
        <th>Depth</th>
        <th>Date</th>
    </tr>
    <% if(list!=null){%>
    <% for(EarthquakeDTO dto:list) {%>
    <tr>
        <td><%=dto.getMagnitude()%></td>
        <td><%=dto.getLatitude()%></td>
        <td><%=dto.getLongitude()%></td>
        <td><%=dto.getDepth()%></td>
        <td><%=dto.getDate()%></td>

    </tr>
    <% }%>
    <% }%>
</table>



</body>
</html>
