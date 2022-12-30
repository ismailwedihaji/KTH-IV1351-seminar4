
package se.kth.iv1351.soundgoodmusicschool.controller;


import java.util.ArrayList;
import java.util.List;
import se.kth.iv1351.soundgoodmusicschool.integration.InstrumentDAO;
import se.kth.iv1351.soundgoodmusicschool.integration.InstrumentDBException;
import se.kth.iv1351.soundgoodmusicschool.model.InstrumentDTO;
import se.kth.iv1351.soundgoodmusicschool.model.InstrumentException;


/**
 * This is the application's only controller, all calls to the model pass here.
 * The controller is also responsible for calling the DAO. Typically, the
 * controller first calls the DAO to retrieve data (if needed), then operates on
 * the data, and finally tells the DAO to store the updated data (if any).
 */
public class Controller {
   
    private final InstrumentDAO instrumentDB;

    /**
     * Creates a new instance, and retrieves a connection to the database.
     * 
     * @throws InstrumentDBException If unable to connect to the database.
     * 
     */
    public Controller() throws InstrumentDBException {
        
        instrumentDB = new InstrumentDAO();
    }

     // this method shows all available instruments searching by their types 
    public void showAllInstruments() throws InstrumentException{
        try {
            instrumentDB.findAllAvailableInstruments();
        } catch (InstrumentDBException e) {
               
            throw new InstrumentException("Unable to show all available instruments ", e);
        }
    }

        // this method shows all available instruments searching by their types 
    public List<? extends InstrumentDTO> showAllinstrumentbyType(String instrument_type) throws InstrumentException  {
        if (instrument_type == null) {
            return new ArrayList<>();
        }
    
            try {
                return instrumentDB.listInstrumentsByType(instrument_type);
            } catch (InstrumentDBException e) {
              
                throw new InstrumentException("Could not search for instrument by this type", e);
            }        
        
    }
    public void showInstrumentPK(int instrument_PK) throws InstrumentException{
        if (instrument_PK < 0) {
            throw new InstrumentException("input is null");
        }

            try {
                instrumentDB.getInstrument_PK(instrument_PK);
            } catch (InstrumentDBException e) {
                
                throw new InstrumentException("Unable to find the --> " + instrument_PK +" primary key", e);
            }  
    }
        // this method used to rent the instrument through calling from instrumentDAO 
    public void rentInstrumentToStudent(int instrument_id, int studentRented_ID) throws InstrumentException, InstrumentDBException{
        
        if(instrumentDB.getTotalRented(studentRented_ID) < 2){
            
            try {     
                instrumentDB.rentInsrument(instrument_id, studentRented_ID);
            } catch (InstrumentDBException e) {
               
                throw new InstrumentException("Unable to rent the -->" + studentRented_ID + " instrument", e);
            }  
        }
        else 
            System.out.print("student can not rent more than 2 instruments at the same time");

    }

    // this method terminates the instrument through calling from instrumentDAO which was rented from student
    public void terminateInstrument(int instrumentId) throws InstrumentException{
        try {
            instrumentDB.terminateInsrument(instrumentId);
        } catch (InstrumentDBException e) {
               
            throw new InstrumentException("Unable to terminate the -->" + instrumentId + " instrument", e);
        } 
    }

    /**
     * @return A list containing all instruments available to rent. The list is empty if there are no
     *         instruments.
     * @throws InstrumentException If unable to retrieve instruments.
     */
    
    public List<? extends InstrumentDTO> getAllInstruments() throws InstrumentException  {
        
            try {
                return instrumentDB.findAllAvailableInstruments();
            } catch (InstrumentDBException e) {
               
                throw new InstrumentException("Unable to list instruments", e);
            }   
    }

}
