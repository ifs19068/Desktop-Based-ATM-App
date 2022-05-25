package atmProyekPBO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbConnectivity {

    private static abstract class Query {
        private static final String selectUserByUsername = "SELECT username,fullname,accountNo,passwd,balance from atm_user where username=?";
        private static final String reduceBalanceByAccountNo = "UPDATE atm_user SET balance=balance-? where accountNo=?";
        private static final String addBalanceByAccountNo= "UPDATE atm_user SET balance=balance+? where accountNo=?";
        private static final String getUsers = "SELECT accountNo,fullname from atm_user";
    }
    
    // Constructor
    private DbConnectivity() {
        this.connect();
    }
    
    public static DbConnectivity getInstance() {
        if(connInstance == null) {
            connInstance =  new DbConnectivity();
        }
        return connInstance;
    }
    
    private static DbConnectivity connInstance = null;
    private Connection connection;
    private final static String userTable = "atm_user";
    private PreparedStatement pst = null;
    private Statement stm = null;
    private ResultSet rs = null;
    
    private void connect() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            try {
                connection = DriverManager.getConnection("jdbc:mysql:"
                + "//localhost/db_atm?"+ "user=alfrendo&password=silalahi");
            } 
            catch(SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        } 
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void clearStatementAndResultSet() {
        try {
            if(pst != null)pst.close();
            if(rs != null)rs.close();
            if(stm != null)stm.close();
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());  
        }
    }

    public void disconnect() {
        try {
            if(connection!= null)connection.close();
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public boolean execTransfer(Integer amount, String fromAccountNo, String toAccountNo) {
        try {
            int res;
            pst = connection.prepareStatement(Query.reduceBalanceByAccountNo);
            pst.setInt(1,amount);
            pst.setString(2,fromAccountNo);
            res = pst.executeUpdate();
            if(res < 1) {
                return false;
            }
            pst = connection.prepareStatement(Query.addBalanceByAccountNo);
            pst.setInt(1,amount);
            pst.setString(2,toAccountNo);
            pst.executeUpdate();
            System.out.println(res);
            return res == 1;
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        finally {
            clearStatementAndResultSet();
        }
    }
    
    public List<Map<String,String>> getUsers(){
        List<Map<String,String>> users = new ArrayList<>();
        try{
            stm = connection.createStatement();
            rs = stm.executeQuery(Query.getUsers);
            while(rs.next()) {
                Map<String,String> user = new HashMap();
                user.put("accountNo",rs.getString(1));
                user.put("fullname",rs.getString(2));
                users.add(user);
            }
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            clearStatementAndResultSet();
        }
        return users;
    }

    public boolean execWithdrawal(Integer amount, String accountNo) {
        try {
            pst = connection.prepareStatement(Query.reduceBalanceByAccountNo);
            pst.setInt(1,amount);
            pst.setString(2,accountNo);
            int rs = pst.executeUpdate();
            if(rs < 1)
                return false;
            return true;
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        finally {
            clearStatementAndResultSet();
        }
    }

    public boolean execDepo(Integer amount, String accountNo) {
        try {
            pst = connection.prepareStatement(Query.addBalanceByAccountNo);
            pst.setInt(1,amount);
            pst.setString(2,accountNo);
            int rs = pst.executeUpdate();
            if(rs < 1)
                return false;
            return true;
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        finally {
            clearStatementAndResultSet();
        }
    }

    public int getBalance(String accountNo) {
        int balance = -1;
        try {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT balance where accountNo=" + accountNo);
            while(res.next() && res.isFirst()) {
                balance = res.getInt(1);
            }
            stmt.close();
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            clearStatementAndResultSet();
        }
        return balance;
    }

    public User getSingleUserByUsername(String username) {
        User user = null;
        try {
            pst = connection.prepareStatement(Query.selectUserByUsername);
            pst.setString(1,username);
            rs = pst.executeQuery();
            while(rs.next() && rs.isFirst()){
                user = new User(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5)
                );
            }
        } 
        catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            clearStatementAndResultSet();
        }
        return user;
    }

}
