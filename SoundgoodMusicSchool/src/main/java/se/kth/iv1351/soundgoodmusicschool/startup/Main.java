
package se.kth.iv1351.soundgoodmusicschool.startup;


import java.sql.SQLException;
import se.kth.iv1351.soundgoodmusicschool.controller.Controller;

import se.kth.iv1351.soundgoodmusicschool.integration.InstrumentDBException;
import se.kth.iv1351.soundgoodmusicschool.view.BlockingInterpreter;

public class Main {
   
    public static void main(String[] args) throws InstrumentDBException, SQLException {
           try {
        new BlockingInterpreter(new Controller()).handleCmds();
        } catch(InstrumentDBException excepionDB) {
            System.out.println("Could not connect to instrument db.");
            excepionDB.printStackTrace();
        }
        
    }
}
