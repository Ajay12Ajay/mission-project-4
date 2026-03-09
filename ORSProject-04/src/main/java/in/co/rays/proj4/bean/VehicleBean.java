/**
 * @Author: Ajay Pratap Kerketta
 * @Description: VehicleBean class represents vehicle details in the system.
 * It stores information such as vehicle number, owner name, service date,
 * service type, and service cost.
 * This class extends BaseBean to inherit common attributes like id,
 * createdBy, modifiedBy, and timestamps.
 *
 * @Creation Date: 09-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * VehicleBean is a JavaBean that encapsulates data for vehicle details.
 */
public class VehicleBean extends BaseBean {

	/** Vehicle number */
	private String vehicleNumber;

	/** Name of the vehicle owner */
	private String ownerName;

	/** Date when the vehicle was serviced */
	private Date serviceDate;

	/** Type of service performed */
	private String serviceType;

	/** Cost of the service */
	private Double cost;

	/**
	 * Gets the vehicle number.
	 * 
	 * @return vehicle number
	 */
	public String getVehicleNumber() {
		return vehicleNumber;
	}

	/**
	 * Sets the vehicle number.
	 * 
	 * @param vehicleNumber the vehicle number to set
	 */
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	/**
	 * Gets the owner name.
	 * 
	 * @return owner name
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * Sets the owner name.
	 * 
	 * @param ownerName the owner name to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * Gets the service date.
	 * 
	 * @return service date
	 */
	public Date getServiceDate() {
		return serviceDate;
	}

	/**
	 * Sets the service date.
	 * 
	 * @param serviceDate the service date to set
	 */
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * Gets the service type.
	 * 
	 * @return service type
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Sets the service type.
	 * 
	 * @param serviceType the service type to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * Gets the service cost.
	 * 
	 * @return cost
	 */
	public Double getCost() {
		return cost;
	}

	/**
	 * Sets the service cost.
	 * 
	 * @param cost the service cost to set
	 */
	public void setCost(Double cost) {
		this.cost = cost;
	}

	/**
	 * Returns the key for dropdown lists (used in UI selections).
	 * 
	 * @return vehicleNumber as key
	 */
	@Override
	public String getKey() {
		return vehicleNumber;
	}

	/**
	 * Returns the value for dropdown lists (used in UI selections).
	 * 
	 * @return ownerName as value
	 */
	@Override
	public String getValue() {
		return ownerName;
	}

	/**
	 * Returns string representation of VehicleBean.
	 * 
	 * @return string representation of vehicle details
	 */
	@Override
	public String toString() {
		return "VehicleBean [vehicleNumber=" + vehicleNumber + ", ownerName=" + ownerName + ", serviceDate="
				+ serviceDate + ", serviceType=" + serviceType + ", cost=" + cost + "]";
	}
}