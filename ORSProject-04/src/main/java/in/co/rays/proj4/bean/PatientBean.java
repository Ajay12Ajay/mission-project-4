package in.co.rays.proj4.bean;

import java.util.Date;

public class PatientBean extends BaseBean{

	private String name;
	private Date dateOfVisit;
	private String mobile;
	private String decease;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(Date dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDecease() {
		return decease;
	}

	public void setDecease(String decease) {
		this.decease = decease;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id + "";
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return decease;
	}

	@Override
	public String toString() {
		return "PatientBean [name=" + name + ", dateOfVisit=" + dateOfVisit + ", mobile=" + mobile + ", decease="
				+ decease + "]";
	}
	
	

}
