package atmProyekPBO;

public class Driver {
    
    public static void main(String[] args) {
        ATMController controller = ATMController.getInstance();
        controller.start(args);
    }
    
}
