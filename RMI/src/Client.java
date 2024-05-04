import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            RemoteInterface obj = (RemoteInterface) Naming.lookup("//localhost/RemoteObject");
            System.out.println(obj.RemoteMethod("Hello, server!"));
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
