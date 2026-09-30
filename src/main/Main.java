
package main;

import gui.LoginFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author admin
 */
public class Main {
    public static void main(String[] Args){
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); 
        });
    
    }
    
}
