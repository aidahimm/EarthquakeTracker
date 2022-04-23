package it.unipi.dstm;

import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DataServersRemoteEJB implements DataServersInterface {
    List<EarthquakeDTO> list1=new ArrayList<>();
    @Override
    public List<EarthquakeDTO> collectServersData() {
        try {
            list1 = dataCollection("localhost", "regional1");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            list1.addAll(dataCollection("localhost", "regional2"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            list1.addAll(dataCollection("localhost", "regional3"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list1;
    }

    public List<EarthquakeDTO> dataCollection (String IP, String dbName) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://"+IP+":3306/"+dbName+"?autoReconnect=true&useSSL=false";
        String user = "root";
        String password = "annamarcia";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        ResultSet rs = null;
        List<EarthquakeDTO> earthquakeDTOS=new ArrayList<>();

        PreparedStatement pstm = null;

        try{
            earthquakeDTOS.add(new EarthquakeDTO());
            StringBuilder sqlStringBuilder=new StringBuilder();
            sqlStringBuilder.append("select " );
            sqlStringBuilder.append("  e.magnitude,  ");
            sqlStringBuilder.append("  e.latitude,  ");
            sqlStringBuilder.append("  e.longitude,  ");
            sqlStringBuilder.append("  e.depth,  ");
            sqlStringBuilder.append("  e.date  ");
            sqlStringBuilder.append(" from earthquakedata e ");
            pstm = conn.prepareStatement(sqlStringBuilder.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                EarthquakeDTO earthquakeDTO=new EarthquakeDTO();
                earthquakeDTO.setMagnitude(rs.getDouble(1));
                earthquakeDTO.setLatitude(rs.getDouble(2));
                earthquakeDTO.setLongitude(rs.getDouble(3));
                earthquakeDTO.setDepth(rs.getDouble(4));
//                earthquakeDTO.setD(rs.getDouble(4));
                earthquakeDTO.setDate(new Date(rs.getTimestamp(5).getTime()));
                earthquakeDTOS.add(earthquakeDTO);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return earthquakeDTOS;
    }
}

