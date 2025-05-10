package Launch;

import Network.*;
import Exception.ExceptionClass;

public class Launcher {
    public static void main(String[] args) {
        try {
            System.out.println("Launching client...");
            Client.main(args);
            boolean error = false;
            if(error)
                throw new ExceptionClass("Invalid run");
        } catch (ExceptionClass e) {
            System.out.println("Error while launching the client: " + e.getMessage());
        }
    }
}
