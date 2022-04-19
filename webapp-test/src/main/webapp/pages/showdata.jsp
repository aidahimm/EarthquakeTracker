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
</head>
<%
    List<EarthquakeDTO> list= (List<EarthquakeDTO>) request.getAttribute("earthquakesList");
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

    </tr>
        <% }%>


</table>
</body>
</html>
