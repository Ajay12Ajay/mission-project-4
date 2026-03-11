/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FreelancerBean class represents freelancer project details in the system.
 * It stores information such as freelancer name, project name,
 * project deadline, and payment amount.
 * This class extends BaseBean to inherit common attributes like id,
 * createdBy, modifiedBy, and timestamps.
 *
 * @Creation Date: 11-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * FreelancerBean is a JavaBean that encapsulates data for freelancer details.
 */
public class FreelancerBean extends BaseBean {

	/** Name of the freelancer */
	private String freelancerName;

	/** Name of the project */
	private String projectName;

	/** Project deadline */
	private Date deadline;

	/** Payment amount for the project */
	private Double paymentAmount;

	/**
	 * Gets the freelancer name.
	 * 
	 * @return freelancer name
	 */
	public String getFreelancerName() {
		return freelancerName;
	}

	/**
	 * Sets the freelancer name.
	 * 
	 * @param freelancerName the freelancer name to set
	 */
	public void setFreelancerName(String freelancerName) {
		this.freelancerName = freelancerName;
	}

	/**
	 * Gets the project name.
	 * 
	 * @return project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets the project name.
	 * 
	 * @param projectName the project name to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets the project deadline.
	 * 
	 * @return deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * Sets the project deadline.
	 * 
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * Gets the payment amount.
	 * 
	 * @return payment amount
	 */
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets the payment amount.
	 * 
	 * @param paymentAmount the payment amount to set
	 */
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Returns the key for dropdown lists (used in UI selections).
	 * 
	 * @return freelancerName as key
	 */
	@Override
	public String getKey() {
		return freelancerName;
	}

	/**
	 * Returns the value for dropdown lists (used in UI selections).
	 * 
	 * @return projectName as value
	 */
	@Override
	public String getValue() {
		return projectName;
	}

	/**
	 * Returns string representation of FreelancerBean.
	 * 
	 * @return string representation of freelancer details
	 */
	@Override
	public String toString() {
		return "FreelancerBean [freelancerName=" + freelancerName + ", projectName=" + projectName + ", deadline="
				+ deadline + ", paymentAmount=" + paymentAmount + "]";
	}
}