/**
 * @Author: Ajay Pratap Kerketta
 * @Description: UserBean class represents a user entity in the system.
 * It stores details such as first name, last name, login credentials, date of birth,
 * mobile number, role, and gender. This class extends BaseBean to inherit common 
 * attributes like id, createdBy, modifiedBy, and timestamps.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * UserBean is a JavaBean that encapsulates data for a system user.
 */
public class UserBean extends BaseBean {

    /** First name of the user */
    private String firstName;

    /** Last name of the user */
    private String lastName;

    /** Login ID of the user */
    private String login;

    /** Password of the user */
    private String password;

    /** Confirm password for validation */
    private String confirmPassword;

    /** Date of birth of the user */
    private Date dob;

    /** Mobile number of the user */
    private String mobileNo;

    /** Role ID assigned to the user */
    private long roleId;

    /** Gender of the user */
    private String gender;

    /**
     * Gets the first name of the user.
     * 
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * 
     * @param firstName first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     * 
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * 
     * @param lastName last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the login ID of the user.
     * 
     * @return login ID
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login ID of the user.
     * 
     * @param login login ID to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets the password of the user.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * 
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the confirm password of the user.
     * 
     * @return confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password of the user.
     * 
     * @param confirmPassword confirm password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the date of birth of the user.
     * 
     * @return date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the user.
     * 
     * @param dob date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Gets the mobile number of the user.
     * 
     * @return mobile number
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the mobile number of the user.
     * 
     * @param mobileNo mobile number to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Gets the role ID assigned to the user.
     * 
     * @return role ID
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID assigned to the user.
     * 
     * @param roleId role ID to set
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * Gets the gender of the user.
     * 
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     * 
     * @param gender gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the key for dropdown lists (used in UI selections).
     * 
     * @return user ID as key
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the value for dropdown lists (used in UI selections).
     * 
     * @return full name (firstName + lastName) as value
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }

}
