/**
 * @Author: Ajay Pratap Kerketta
 * @Description: LibraryBookIssueBean class represents the book issue 
 * transaction in the library system.
 * It stores details such as book ID, member ID, issue date, return date, 
 * fine amount, issued by, and status.
 * This class extends BaseBean to inherit common attributes like id,
 * createdBy, modifiedBy, and timestamps.
 * 
 * @Creation Date: 27-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * LibraryBookIssueBean class is a JavaBean that encapsulates the data for
 * issuing books in the library.
 */
public class LibraryBookIssueBean extends BaseBean {

	/** The ID of the issued book */
	private Long bookId;

	/** The ID of the member who borrowed the book */
	private Long memberId;

	/** The date when the book was issued */
	private Date issueDate;

	/** The expected or actual return date of the book */
	private Date returnDate;

	/** Fine amount charged (if any) */
	private Long fineAmount;

	/** Name of the person who issued the book */
	private String issuedBy;

	/** Current status of the book (Issued/Returned/Overdue) */
	private String status;

	/**
	 * Gets the book ID.
	 * 
	 * @return bookId
	 */
	public Long getBookId() {
		return bookId;
	}

	/**
	 * Sets the book ID.
	 * 
	 * @param bookId the book ID to set
	 */
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	/**
	 * Gets the member ID.
	 * 
	 * @return memberId
	 */
	public Long getMemberId() {
		return memberId;
	}

	/**
	 * Sets the member ID.
	 * 
	 * @param memberId the member ID to set
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * Gets the issue date.
	 * 
	 * @return issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * Sets the issue date.
	 * 
	 * @param issueDate the issue date to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * Gets the return date.
	 * 
	 * @return returnDate
	 */
	public Date getReturnDate() {
		return returnDate;
	}

	/**
	 * Sets the return date.
	 * 
	 * @param returnDate the return date to set
	 */
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	/**
	 * Gets the fine amount.
	 * 
	 * @return fineAmount
	 */
	public Long getFineAmount() {
		return fineAmount;
	}

	/**
	 * Sets the fine amount.
	 * 
	 * @param fineAmount the fine amount to set
	 */
	public void setFineAmount(Long fineAmount) {
		this.fineAmount = fineAmount;
	}

	/**
	 * Gets the name of the issuer.
	 * 
	 * @return issuedBy
	 */
	public String getIssuedBy() {
		return issuedBy;
	}

	/**
	 * Sets the issuer name.
	 * 
	 * @param issuedBy the name to set
	 */
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	/**
	 * Gets the current status.
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the current status.
	 * 
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the key for dropdown list.
	 * 
	 * @return status as key
	 */
	@Override
	public String getKey() {
		return status;
	}

	/**
	 * Returns the display value for dropdown list.
	 * 
	 * @return status as value
	 */
	@Override
	public String getValue() {
		return status;
	}
}