/**
 * @Author: Ajay Pratap Kerketta
 * @Description: DoctorBean class represents a doctor entity in the system.
 * It stores details such as name, date of birth, mobile number, and area of expertise.
 * This class extends BaseBean to inherit common attributes like id, 
 * createdBy, modifiedBy, and timestamps.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * DoctorBean class is a JavaBean that encapsulates the data for a Doctor.
 */
public class DoctorBean extends BaseBean {

    /** The name of the doctor */
    private String name;

    /** The date of birth of the doctor */
    private Date dob;

    /** The mobile number of the doctor */
    private String mobile;

    /** The area of medical expertise */
    private String expertise;

    /**
     * Gets the name of the doctor.
     * 
     * @return name of the doctor
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the doctor.
     * 
     * @param name the doctor's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the date of birth of the doctor.
     * 
     * @return date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the doctor.
     * 
     * @param dob the date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets the mobile number of the doctor.
     * 
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile number of the doctor.
     * 
     * @param mobile the mobile number to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the area of expertise of the doctor.
     * 
     * @return area of expertise
     */
    public String getExpertise() {
        return expertise;
    }

    /**
     * Sets the area of expertise of the doctor.
     * 
     * @param expertise the expertise to set
     */
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    /**
     * Returns the key for the dropdown list.
     * 
     * @return expertise as key
     */
    @Override
    public String getKey() {
        return expertise;
    }

    /**
     * Returns the display value for the dropdown list.
     * 
     * @return expertise as value
     */
    @Override
    public String getValue() {
        return expertise;
    }

}
