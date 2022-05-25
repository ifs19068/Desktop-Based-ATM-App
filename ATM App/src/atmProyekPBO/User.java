package atmProyekPBO;

import java.util.List;
import java.util.Map;

public class User {
    
    private String username;
    private String fullName;
    private String password;
    private String accountNo;
    private Integer balance;
    private static DbConnectivity db  = DbConnectivity.getInstance();

    public User(String username,String fullname,String accountNo,String password,Integer balance) {
        this.username = username;
        this.fullName= fullname;
        this.password = password;
        this.accountNo = accountNo;
        this.balance = balance;
    }

    public boolean transfer(String acccountNoTo,Integer amount) {
            if(acccountNoTo.equals(accountNo)) {
                throw new RuntimeException("cannot transfer to your own account");
            }
            if(!isBalanceReductionValid(amount))
                return false;
            boolean isSuccess = db.execTransfer(amount,accountNo,acccountNoTo);
            if(isSuccess == true) {
                System.out.println("success");
            }
            return isSuccess;
        }

        public boolean withdraw(Integer amount) {
            if(!isBalanceReductionValid(amount))
                return false;
            boolean isSuccess = db.execWithdrawal(amount,accountNo);
            return isSuccess;
        }

        public boolean deposit(Integer amount) {
            boolean isSuccess = db.execDepo(amount,accountNo);
            return isSuccess;
        }

        public boolean isBalanceReductionValid(Integer amount) {
            boolean valid = balance >= 0 && amount >= 0 && (balance - amount) >= 0;
            if(!valid) {
                System.out.println("canceling transfer. reason : amount is "+ amount+" while balance left " + balance);
            }
            return valid;
        }

    public static User getSingle(String username) {
        User u = DbConnectivity.getInstance().getSingleUserByUsername(username);
        return u;
    }
    
    public static List<Map<String,String>> getUsers() {
        List<Map<String,String>> users = DbConnectivity.getInstance().getUsers();
        return users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", balance=" + balance +
                '}';
    }
    
}
