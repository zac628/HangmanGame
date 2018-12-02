import java.io.Serializable;
import java.sql.*;
import java.util.Random;

public class DatabaseBean implements Serializable {

    private static String url = "jdbc:sqlite:../../Advanced_Hangman/Hangman/HangmanDB.db";
    private static int currentUser;

    public DatabaseBean(){

        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite Database has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void writeUser(Users usr){
        String u = usr.getUname();
        String p = usr.getPass();
        int userid = -1;

        String sql = "INSERT INTO Users(uname,pass) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, u);
                pstmt.setString(2, p);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql2 = "SELECT id FROM Users WHERE uname = ? ";
        try (Connection conn2 = DriverManager.getConnection(url);
             PreparedStatement pstmt2 = conn2.prepareStatement(sql2)) {
            pstmt2.setString(1, u);
            ResultSet rs2 = pstmt2.executeQuery();
            userid = rs2.getInt("id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql3 = "INSERT INTO History(uid,wins,losses) values(?,?,?)";
        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
                pstmt3.setInt(1, userid);
                pstmt3.setInt(2, 0);
                pstmt3.setInt(3,0);
                pstmt3.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean testLogin(String one, String two){
        String sql = "SELECT uname, pass, id FROM Users";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if(rs.getString("uname").equals(one) && rs.getString("pass").equals(two)){
                    currentUser = rs.getInt("id");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        currentUser = -1;
        return false;
    }

    public static History retrieveHistory(){
        String sql = "SELECT wins,losses FROM History WHERE uid = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser);
            ResultSet rs = pstmt.executeQuery();
            int w = rs.getInt("wins");
            int l = rs.getInt("losses");
            History temp = new History(currentUser,w,l);
            return temp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            History temp = new History(-1,-1,-1);
            return temp;
        }
    }

    public static void updateWin(int n){
        String sql = "UPDATE History SET wins = ?" + "WHERE uid = ?";
        try(Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, n+1);
            pstmt.setInt(2, currentUser);
            pstmt.executeUpdate();
        } catch(SQLException e1){
            System.out.println(e1.getMessage());
        }
    }

    public static void updateLoss(int num){
        String sql = "UPDATE History SET losses = ?" + "WHERE uid = ?";
        currentUser = 2;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, num+1);
            pstmt.setInt(2, currentUser);
            pstmt.executeUpdate();
        } catch(SQLException e1){
            System.out.println(e1.getMessage());
        }
    }

    public static String retrieveWord(int diff){
        String sql = "SELECT word,difficulty FROM Dictionary WHERE wid = ?";
        boolean b = true;
        do {
            Random rand = new Random();
            int n = rand.nextInt(30)+1;
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, diff);
                ResultSet rs = pstmt.executeQuery();
                String temp = rs.getString("word");
                int dif = rs.getInt("difficulty");

                if (dif == diff){
                    b = false;
                    return temp;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                b = false;
            }
        }while (b==true);
        return "";
    }

}
