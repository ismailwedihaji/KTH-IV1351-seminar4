package se.kth.iv1351.soundgoodmusicschool.model;

import java.sql.Date;

public interface InstrumentDTO {
    public int getId();
    public String getInstrumentType();
    public boolean getIs_Available();
    public String getBrand();
    public Date getStartRentedTime();
    public Date getEndRentedTime();
    public int getStudent_id();
    public int getRentPrice();
}
