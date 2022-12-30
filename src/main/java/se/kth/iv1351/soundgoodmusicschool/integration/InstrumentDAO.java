
package se.kth.iv1351.soundgoodmusicschool.integration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.kth.iv1351.soundgoodmusicschool.model.Instrument;



public class InstrumentDAO {
    private static final String INSTRUMENT_TABLE_NAME = "instrument";
    private static final String INSTRUMENT_PK_COLUMN_NAME = "id";
    private static final String INSTRUMENT_TYPE_COLUMN_NAME = "instrument_type";
    private static final String INSTRUMENT_IS_AVAILABLE_TABLE_NAME = "is_available";
    private static final String INSTRUMENT_BRAND_COLUMN_NAME = "brand";
    private static final String INSTRUMENT_STARTED_RENTED_TIME = "starts_rented_time";
    private static final String INSTRUMENT_ENDS_RENTED_TIME = "ends_rented_time";
    private static final String INSTRUMENT_STUDENT_ID = "student_id";
    private static final String INSTRUMENT_RENT_PRICE = "rent_price";
 

    private Connection connection;
    private PreparedStatement showAll_Instruments;
    private PreparedStatement getStudentRentedID;
    private PreparedStatement listInstruments_usingType;
    private PreparedStatement terminateTheInstrument;
    private PreparedStatement getInstrument_PK;
    private PreparedStatement countStudentRentedInstrument;

    public InstrumentDAO()throws InstrumentDBException{
        try {
            connectToInstrumentDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            throw new InstrumentDBException("Could not connect to datasource.", e);
        }

    }

    private void connectToInstrumentDB() throws ClassNotFoundException, SQLException {
         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/task3new",
                                                 "postgres", "Wedihaji123");
        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {
        showAll_Instruments = connection.prepareStatement("SELECT * FROM " + INSTRUMENT_TABLE_NAME + " WHERE " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = 'true' ");             
        
        listInstruments_usingType = connection.prepareStatement("SELECT * FROM " + INSTRUMENT_TABLE_NAME + " WHERE " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = " + "'true'"+ "AND "+ INSTRUMENT_TYPE_COLUMN_NAME + " = ?");
        getInstrument_PK = connection.prepareStatement("SELECT * FROM " + INSTRUMENT_TABLE_NAME + " WHERE " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = " + "'true'"+ "AND "+ INSTRUMENT_PK_COLUMN_NAME + " = ?");
        terminateTheInstrument = connection.prepareStatement("UPDATE " + INSTRUMENT_TABLE_NAME
                  + " SET " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = ?, " + INSTRUMENT_ENDS_RENTED_TIME +" = ?, " + INSTRUMENT_STUDENT_ID + " = null  WHERE " + INSTRUMENT_PK_COLUMN_NAME + " = ? ");
        getStudentRentedID = connection.prepareStatement("UPDATE " + INSTRUMENT_TABLE_NAME
                  + " SET " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = ?, " + INSTRUMENT_STARTED_RENTED_TIME +" = ?, " + INSTRUMENT_STUDENT_ID + " = ?  WHERE " + INSTRUMENT_PK_COLUMN_NAME + " = ? ");
                  
        countStudentRentedInstrument = connection.prepareStatement("SELECT COUNT(*) FROM " + INSTRUMENT_TABLE_NAME + " WHERE " 
        + INSTRUMENT_STUDENT_ID + " = ? AND " + INSTRUMENT_IS_AVAILABLE_TABLE_NAME + " = 'false'");

    }

    public List<Instrument> findAllAvailableInstruments() throws InstrumentDBException{
        String failureMsg = "Could not show list of all Instruments.";
        List<Instrument> instruments = new ArrayList<>();
        try (ResultSet result = showAll_Instruments.executeQuery()) {
            while (result.next()) {
                instruments.add(new Instrument(result.getInt(INSTRUMENT_PK_COLUMN_NAME),
                                         result.getString(INSTRUMENT_TYPE_COLUMN_NAME),
                                         result.getBoolean(INSTRUMENT_IS_AVAILABLE_TABLE_NAME),
                                         result.getString(INSTRUMENT_BRAND_COLUMN_NAME),
                                         result.getString(INSTRUMENT_STARTED_RENTED_TIME),
                                         result.getString(INSTRUMENT_ENDS_RENTED_TIME),
                                         result.getInt(INSTRUMENT_STUDENT_ID), result.getInt(INSTRUMENT_RENT_PRICE)));
                                         
                                        
            }
            connection.commit();
        } catch (SQLException sqle) {
           handleException(failureMsg, sqle);
           
        }
        return instruments;

    }

    public List<Instrument> listInstrumentsByType(String instrument_type) throws InstrumentDBException{
        String failureMsg = "Could not show list of all Instruments with specific type.";
        List<Instrument> instruments = new ArrayList<>();
       
        try  {
            listInstruments_usingType.setString(1, instrument_type);
            ResultSet result = listInstruments_usingType.executeQuery();
         
            while (result.next()) {
                instruments.add(new Instrument(result.getInt(INSTRUMENT_PK_COLUMN_NAME),
                result.getString(INSTRUMENT_TYPE_COLUMN_NAME), result.getBoolean(INSTRUMENT_IS_AVAILABLE_TABLE_NAME),
                result.getString(INSTRUMENT_BRAND_COLUMN_NAME), result.getString(INSTRUMENT_STARTED_RENTED_TIME),
                result.getString(INSTRUMENT_ENDS_RENTED_TIME), result.getInt(INSTRUMENT_STUDENT_ID), result.getInt(INSTRUMENT_RENT_PRICE)));                                 
            }
            connection.commit();
            
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
      
        return instruments;

    }
    public List<Instrument> rentInsrument(int instrument_id, int studentRentedId) throws InstrumentDBException{
        String failureMsg = "Could not rent the Instrument for " + studentRentedId;
        List<Instrument> instruments = new ArrayList<>();
  
        Date date = Date.valueOf(getRentedDateTime());
        String isAvailable= "false";
      
        try  {

            getStudentRentedID.setString(1, isAvailable);
            getStudentRentedID.setDate(2, date);
            getStudentRentedID.setInt(3, studentRentedId);
            getStudentRentedID.setInt(4, instrument_id);
            int updatedRows = getStudentRentedID.executeUpdate();
              if (updatedRows != 1) {
                System.out.print("updaterow is not equal 1");
                handleException(failureMsg, null);
            }
            
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
       
        return instruments;

    }

    public List<Instrument> terminateInsrument(int instrument_id) throws InstrumentDBException{
        String failureMsg = "Could not terminate the Instrument ";
        List<Instrument> instruments = new ArrayList<>();
        String is_available = "true";
      
        Date date = Date.valueOf(getRentedDateTime());
        try  {
            terminateTheInstrument.setString(1, is_available);
            terminateTheInstrument.setDate(2,  date);
            terminateTheInstrument.setInt(3, instrument_id);
           int updatedRows = terminateTheInstrument.executeUpdate();
            if (updatedRows != 1) {
                System.out.print("updaterow is not equal 1");
                handleException(failureMsg, null);
            }

            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
        return instruments;

    }

    private void handleException(String failureMsg, Exception cause) throws InstrumentDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg + 
            ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new InstrumentDBException(failureMsg, cause);
        } else {
            throw new InstrumentDBException(failureMsg);
        }
    }
    
    public void getInstrument_PK(int instrument_PK) throws InstrumentDBException {
        
        try {
            getInstrument_PK.setInt(1, instrument_PK);
            getInstrument_PK.executeQuery();

        } catch (SQLException e) {
            handleException("could not find primary key", e);
        }        
        
    }
   
    // this method counts how many instrument student rented
    public int getTotalRented(int studentId) throws InstrumentDBException {
        int count = 0;
        try { 
            countStudentRentedInstrument.setInt(1, studentId);
            ResultSet result = countStudentRentedInstrument.executeQuery();
               
            if (result.next()) {
                 count = result.getInt(1);
                return count;
            }   
            return count;   
        } catch (SQLException e) {
            handleException("could not find primary key", e);
    }
     System.out.println(count);
    return count;
}

    public String getRentedDateTime(){
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
    
        // Create a DateFormat object for formatting the date as a string
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    
        // Format the date as a string and print it
       return dateFormat.format(date);
        
    }


}
