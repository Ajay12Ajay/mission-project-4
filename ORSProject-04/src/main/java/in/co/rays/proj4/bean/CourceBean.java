/**
 * @Author: Ajay Pratap Kerketta
 * @Description: CourceBean class represents a course entity in the system.
 * It stores details such as course code, course name, duration, and course fee.
 * This class extends BaseBean to inherit common attributes like id, createdBy,
 * modifiedBy, and timestamps.
 * 
 * @Creation Date: 26-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

/**
 * CourceBean is a JavaBean that encapsulates data for a course.
 */
public class CourceBean extends BaseBean {

	/** Unique course code */
	private String courseCode;

	/** Name of the course */
	private String courseName;

	/** Duration of the course */
	private String duration;

	/** Fee of the course */
	private String courseFee;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCourseFee() {
		return courseFee;
	}

	public void setCourseFee(String courseFee) {
		this.courseFee = courseFee;
	}

	@Override
	public String getKey() {
		return courseCode;
	}

	@Override
	public String getValue() {
		return courseName;
	}

	@Override
	public String toString() {
		return "CourceBean [id=" + getId() + ", courseCode=" + courseCode + ", courseName=" + courseName + ", duration="
				+ duration + ", courseFee=" + courseFee + "]";
	}
}