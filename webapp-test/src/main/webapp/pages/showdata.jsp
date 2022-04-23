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
    List<EarthquakeDTO> earthquakesAll = (List<EarthquakeDTO>) request.getAttribute("earthquakesAll");
%>
<body>
<h1>hi</h1>

<table>

        <% for(EarthquakeDTO dto:list) {%>
            <tr>
                <td><%=dto.getMagnitude()%></td>
                <td><%=dto.getLatitude()%></td>
                <td><%=dto.getLongitude()%></td>
                <td><%=dto.getDepth()%></td>
                <td><%=dto.getDate()%></td>

            </tr>
        <% }%>

<%--        <% if (list !=null){ %>--%>
<%--            <%= list.size()%>--%>
<%--        <%}else{%>--%>
<%--            <%= request.getAttribute("isnull")%>--%>
<%--        <%}%>--%>

<%--        <% if (earthquakesAll !=null){ %>--%>
<%--             <%= earthquakesAll.size()%>--%>
<%--        <%}else{%>--%>
<%--            <%= request.getAttribute("isnull")%>--%>
<%--        <%}%>--%>
</table>

<table>
    <h2>Data From All Servers</h2>

    <% for(EarthquakeDTO dto:earthquakesAll) {%>
    <tr>
        <td><%=dto.getMagnitude()%></td>
        <td><%=dto.getLatitude()%></td>
        <td><%=dto.getLongitude()%></td>
        <td><%=dto.getDepth()%></td>
        <td><%=dto.getDate()%></td>

    </tr>
    <% }%>
</table>
</body>
</html>
