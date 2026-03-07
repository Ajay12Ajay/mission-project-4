/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FeedbackBean class represents feedback submitted by a user.
 * It stores details such as feedback code, user name, comments, rating,
 * and feedback date.
 * This class extends BaseBean to inherit common attributes like id,
 * createdBy, modifiedBy, and timestamps.
 *
 * @Creation Date: 28-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * FeedbackBean is a JavaBean that encapsulates data for user feedback.
 */
public class FeedbackBean extends BaseBean {

	/** Unique code for the feedback */
	private String feedbackCode;

	/** Name of the user who submitted feedback */
	private String userName;

	/** Feedback comments provided by the user */
	private String comments;

	/** Rating given by the user */
	private Integer rating;

	/** Date when feedback was submitted */
	private Date feedbackDate;

	/**
	 * Gets the feedback code.
	 *
	 * @return feedback code
	 */
	public String getFeedbackCode() {
		return feedbackCode;
	}

	/**
	 * Sets the feedback code.
	 *
	 * @param feedbackCode the feedback code to set
	 */
	public void setFeedbackCode(String feedbackCode) {
		this.feedbackCode = feedbackCode;
	}

	/**
	 * Gets the user name.
	 *
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the feedback comments.
	 *
	 * @return comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the feedback comments.
	 *
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the rating.
	 *
	 * @return rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Gets the feedback date.
	 *
	 * @return feedback date
	 */
	public Date getFeedbackDate() {
		return feedbackDate;
	}

	/**
	 * Sets the feedback date.
	 *
	 * @param feedbackDate the feedback date to set
	 */
	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	/**
	 * Returns the key for dropdown lists (used in UI selections).
	 *
	 * @return feedbackCode as key
	 */
	@Override
	public String getKey() {
		return feedbackCode;
	}

	/**
	 * Returns the value for dropdown lists (used in UI selections).
	 *
	 * @return userName as value
	 */
	@Override
	public String getValue() {
		return userName;
	}

	/**
	 * Returns string representation of FeedbackBean.
	 *
	 * @return string representation of feedback
	 */
	@Override
	public String toString() {
		return "FeedbackBean [feedbackCode=" + feedbackCode + ", userName=" + userName + ", comments=" + comments
				+ ", rating=" + rating + ", feedbackDate=" + feedbackDate + "]";
	}
}