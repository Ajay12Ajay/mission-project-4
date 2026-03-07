/**
 * @Author: Ajay Pratap Kerketta
 * @Description: TransportBean class represents transport details in the system.
 * It stores information such as transport ID, vehicle type, driver name,
 * and transport charges.
 * This class extends BaseBean to inherit common attributes like id,
 * createdBy, modifiedBy, and timestamps.
 *
 * @Creation Date: 07-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

/**
 * TransportBean is a JavaBean that encapsulates data for transport details.
 */
public class TransportBean extends BaseBean {

	/** Unique ID for the transport */
	private String transportId;

	/** Type of vehicle used for transport */
	private String vehicleType;

	/** Name of the driver */
	private String driverName;

	/** Charges for the transport */
	private Double charges;

	/**
	 * Gets the transport ID.
	 * 
	 * @return transport ID
	 */
	public String getTransportId() {
		return transportId;
	}

	/**
	 * Sets the transport ID.
	 * 
	 * @param transportId the transport ID to set
	 */
	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}

	/**
	 * Gets the vehicle type.
	 * 
	 * @return vehicle type
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * Sets the vehicle type.
	 * 
	 * @param vehicleType the vehicle type to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * Gets the driver name.
	 * 
	 * @return driver name
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * Sets the driver name.
	 * 
	 * @param driverName the driver name to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * Gets the transport charges.
	 * 
	 * @return charges
	 */
	public Double getCharges() {
		return charges;
	}

	/**
	 * Sets the transport charges.
	 * 
	 * @param charges the charges to set
	 */
	public void setCharges(Double charges) {
		this.charges = charges;
	}

	/**
	 * Returns the key for dropdown lists (used in UI selections).
	 * 
	 * @return transportId as key
	 */
	@Override
	public String getKey() {
		return transportId;
	}

	/**
	 * Returns the value for dropdown lists (used in UI selections).
	 * 
	 * @return driverName as value
	 */
	@Override
	public String getValue() {
		return driverName;
	}

	/**
	 * Returns string representation of TransportBean.
	 * 
	 * @return string representation of transport details
	 */
	@Override
	public String toString() {
		return "TransportBean [transportId=" + transportId + ", vehicleType=" + vehicleType + ", driverName="
				+ driverName + ", charges=" + charges + "]";
	}
}