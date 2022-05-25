package atmProyekPBO;

import atmProyekPBO.view.ATMView;
import atmProyekPBO.view.DashboardView;
import atmProyekPBO.view.LoginView;

public class ATMController {
    
    private static  ATMView view = null;
    private static User model = null;
    private static ATMController instance = null;
    
    public static ATMController getInstance(){
        if(instance == null) {
            instance  = new ATMController(new ATMView(new LoginView(),new DashboardView()),model);
        }
        return instance;
    }
    
    private ATMController(ATMView v,User m){
        view = v;
        model = m;
    }

    public ATMView getView() {
        return view;
    }

    public void setView(ATMView v) {
        view = v;
    }

    public User getModel() {
        return model;
    }

    public void setModel(User m) {
        model = m;
    }
    
    void start(String... args) {
        if(model == null) {
            view.setContentPane(view.getLoginView());
        }else {
            view.getDashboardView().setLoggedInUser(model);
            view.setContentPane(view.getDashboardView());
        }  
    }
    
    public void onUserLogout() {
        model = null;
        this.start();
        
    }
    
    public void onUserLogin(User u) {
        model = u;
        this.start();
    }
    
    public void onTransactionSuccess() {
        model = User.getSingle(model.getUsername());
        this.start();
    }
    
    public boolean doWithdrawal(Integer amount) {
        boolean isSucceed = model.withdraw(amount);
        if(isSucceed) {
            this.onTransactionSuccess();  
        }
        return isSucceed;
    }
    
    public boolean doTransfer(Integer amount,String targetAccountNo) {
        boolean isSucceed = model.transfer(targetAccountNo, amount);
        if(isSucceed) {
            this.onTransactionSuccess();
        }
        return isSucceed;
    }

    public boolean doDeposit(Integer amount) {
        boolean isSucceed = model.deposit(amount);
        if(isSucceed) {
            this.onTransactionSuccess();
            
        }
        return isSucceed;
    }
   
}
