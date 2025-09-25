package in.co.rays.proj4.bean;

import java.util.Date;

public class DoctorBean extends BaseBean {

	private String name;
	private Date dob;
	private String mobile;
	private String expertise;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return expertise;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return expertise;
	}

}
