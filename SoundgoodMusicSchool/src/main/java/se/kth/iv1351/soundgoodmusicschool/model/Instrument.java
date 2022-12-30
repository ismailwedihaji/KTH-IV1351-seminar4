package se.kth.iv1351.soundgoodmusicschool.model;

import java.sql.Date;

public class Instrument implements InstrumentDTO{
    private int id;
    private String instrument_type;
    private boolean is_available;
    private String brand;
    
    private Date start_rented_time;
    private Date  end_rented_time;
    private int student_id;
    private int rentPrice;
    
    public Instrument(int id, String instrument_type, boolean is_available, String brand, String start_rented_time, String end_rented_time, int student_id, int rentPrice){
        this.id = id;
        this.instrument_type = instrument_type;
        this.is_available = is_available;
        this.brand = brand;
        this.student_id = student_id;
        this.start_rented_time = Date.valueOf(start_rented_time);
        this.end_rented_time = Date.valueOf(end_rented_time);
        this.rentPrice = rentPrice;
        // this.start_rented_time = Date.valueOf("2021-12-03");

    }
    public Instrument(){

    }

    @Override
    public int getId() {
        
        return this.id;
    }

    @Override
    public String getInstrumentType() {
       return this.instrument_type;
    }

    @Override
    public boolean getIs_Available() {
        return this.is_available;
    }

    @Override
    public Date getStartRentedTime() {
        return this.start_rented_time;
        
    }

    @Override
    public Date getEndRentedTime() {
        return this.end_rented_time;
    }

    @Override
    public int getStudent_id() {
        return this.student_id;
    }

    @Override
    public String getBrand() {
        return this.brand;
}

    @Override
    public int getRentPrice() {
        return this.rentPrice;
    }
   
}
