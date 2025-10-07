/**
 * @Author: Ajay Pratap Kerketta
 * @Description: DropdownListBean interface provides methods to represent 
 * key-value pairs for populating dropdown lists in the user interface.
 * Implementing classes should define how to generate the key and value 
 * that will be displayed in dropdown menus.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

/**
 * Interface that defines methods for generating key-value pairs
 * used in dropdown lists within the application.
 */
public interface DropdownListBean {
    
    /**
     * Returns the unique key for the dropdown list item.
     * 
     * @return key as a String
     */
    public String getKey();

    /**
     * Returns the display value for the dropdown list item.
     * 
     * @return value as a String
     */
    public String getValue();

}
