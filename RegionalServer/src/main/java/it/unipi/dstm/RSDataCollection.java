package it.unipi.dstm;

import com.ericsson.otp.erlang.*;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RSDataCollection {


    private String nodeId;
    private String cookie;
    private String mBox;
    public RSDataCollection(String nodeId,String cookie, String mBox)
    {
        this.nodeId=nodeId;
        this.cookie=cookie;
        this.mBox=mBox;

    }

    public void receiveData() throws IOException, OtpErlangDecodeException, OtpErlangExit {

        OtpNode otpNode = new OtpNode(nodeId, cookie);

        OtpMbox otpMbox = otpNode.createMbox(mBox);
        System.out.println("The server " + nodeId + " is running.");
        System.out.println("cookie: " + cookie);
        System.out.println("TmBox: " + mBox);
        for(int i=0; i<5;i++){
        //while (true) {
            try {

                OtpErlangObject message = otpMbox.receive(40000);
                System.out.println("message " + message);
                if (message instanceof OtpErlangTuple){
                    OtpErlangTuple erlangTuple = (OtpErlangTuple) message;
                    OtpErlangPid senderPID = (OtpErlangPid) erlangTuple.elementAt(1);
                    OtpErlangTuple earthquakeTuple = (OtpErlangTuple)erlangTuple.elementAt(0);

                    OtpErlangDouble magnitudeEr= (OtpErlangDouble) earthquakeTuple.elementAt(0);
                    OtpErlangDouble latitudeEr= (OtpErlangDouble) earthquakeTuple.elementAt(1);
                    OtpErlangDouble longitudeEr= (OtpErlangDouble) earthquakeTuple.elementAt(2);
                    OtpErlangDouble depthEr= (OtpErlangDouble) earthquakeTuple.elementAt(3);

                    double magnitude=magnitudeEr.doubleValue();
                    double latitude=latitudeEr.doubleValue();
                    double longitude=longitudeEr.doubleValue();
                    double depth=depthEr.doubleValue();
                    OtpErlangLong year=(OtpErlangLong) earthquakeTuple.elementAt(4);
                    OtpErlangLong month=(OtpErlangLong) earthquakeTuple.elementAt(5);
                    OtpErlangLong day=(OtpErlangLong) earthquakeTuple.elementAt(6);
                    OtpErlangLong hour=(OtpErlangLong) earthquakeTuple.elementAt(7);
                    OtpErlangLong minute=(OtpErlangLong) earthquakeTuple.elementAt(8);
                    OtpErlangLong second=(OtpErlangLong) earthquakeTuple.elementAt(9);


                    DateTime date = new DateTime(year.intValue(), month.intValue(), day.intValue(),hour.intValue(),minute.intValue(),second.intValue());

                    insertData(magnitude,latitude,longitude,depth,date);

                }
            } catch (Exception e)
            {
                System.out.println("" + e);
                otpNode.closeMbox(otpMbox);
                otpNode.close();
            }
        }
        otpNode.closeMbox(otpMbox);
        otpNode.close();



    }
    public void insertData (double magnitude,double latitude, double longitude,double depth,DateTime date) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/regional1?autoReconnect=true&useSSL=false";
        String user = "root";
        String password = "admin";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        String sql = "INSERT INTO earthquakedata (magnitude, latitude, longitude,depth,date) values (?, ?, ?, ?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, magnitude);
        statement.setDouble(2, latitude);
        statement.setDouble(3, longitude);
        statement.setDouble(4, depth);


        statement.setTimestamp(5,new java.sql.Timestamp( date.toDate().getTime()));
        int row=statement.executeUpdate();
        if (row > 0) {
            System.out.println("data inserted successfully");
        }




    }
}
