/**
 * @Author: Ajay Pratap Kerketta
 * @Description: LibraryBookIssueModel handles CRUD operations and search
 * functionality for LibraryBookIssue entities using JDBC.
 *
 * @Creation Date: 27-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.bean.LibraryBookIssueBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class LibraryBookIssueModel {

	/**
	 * Get next primary key
	 */
	public static Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_library_book_issue");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

	/**
	 * Add record
	 */
	public void add(LibraryBookIssueBean bean)
	        throws ApplicationException, DuplicateRecordException {

	    Connection conn = null;
	    int pk;

	    // Duplicate check
	    LibraryBookIssueBean exist =
	            findByBookAndMember(bean.getBookId(), bean.getMemberId());

	    if (exist != null) {
	        throw new DuplicateRecordException(
	                "Book already issued to this member");
	    }

	    try {
	        conn = JDBCDataSource.getConnection();
	        pk = nextPk();
	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement(
	            "INSERT INTO st_library_book_issue "
	            + "(id, book_id, member_id, issue_date, return_date, "
	            + "fine_amount, issued_by, status, created_by, modified_by, "
	            + "created_datetime, modified_datetime) "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	        pstmt.setLong(1, pk);
	        pstmt.setLong(2, bean.getBookId());
	        pstmt.setLong(3, bean.getMemberId());

	        // Issue Date (Mandatory)
	        pstmt.setDate(4,
	                new java.sql.Date(bean.getIssueDate().getTime()));

	        // Return Date (Nullable)
	        if (bean.getReturnDate() != null) {
	            pstmt.setDate(5,
	                    new java.sql.Date(bean.getReturnDate().getTime()));
	        } else {
	            pstmt.setNull(5, java.sql.Types.DATE);
	        }

	        // Fine (default 0 if null)
	        if (bean.getFineAmount() != null) {
	            pstmt.setLong(6, bean.getFineAmount());
	        } else {
	            pstmt.setLong(6, 0);
	        }

	        pstmt.setString(7, bean.getIssuedBy());
	        pstmt.setString(8, bean.getStatus());
	        pstmt.setString(9, bean.getCreatedBy());
	        pstmt.setString(10, bean.getModifiedBy());

	        pstmt.setTimestamp(11, bean.getCreatedDatetime());
	        pstmt.setTimestamp(12, bean.getModifiedDatetime());

	        pstmt.executeUpdate();
	        conn.commit();
	        pstmt.close();

	    } catch (Exception e) {
	        e.printStackTrace();   // IMPORTANT for debugging
	        try {
	            conn.rollback();
	        } catch (Exception ex) {
	            throw new ApplicationException(
	                    "Add rollback exception");
	        }
	        throw new ApplicationException(
	                "Exception in adding Book Issue");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	}

	/**
	 * Update record
	 */
	public void update(LibraryBookIssueBean bean)
	        throws ApplicationException, DuplicateRecordException {

	    Connection conn = null;

	    //  Duplicate Check (same logic as add but ignore same ID)
	    LibraryBookIssueBean exist =
	            findByBookAndMember(bean.getBookId(), bean.getMemberId());

	    if (exist != null && exist.getId() != bean.getId()) {
	        throw new DuplicateRecordException(
	                "Book already issued to this member");
	    }

	    try {
	        conn = JDBCDataSource.getConnection();
	        conn.setAutoCommit(false);

	        PreparedStatement pstmt = conn.prepareStatement(
	            "UPDATE st_library_book_issue SET "
	            + "book_id=?, member_id=?, issue_date=?, return_date=?, "
	            + "fine_amount=?, issued_by=?, status=?, "
	            + "modified_by=?, modified_datetime=? "
	            + "WHERE id=?");

	        pstmt.setLong(1, bean.getBookId());
	        pstmt.setLong(2, bean.getMemberId());

	        // Issue Date
	        pstmt.setDate(3,
	                new java.sql.Date(bean.getIssueDate().getTime()));

	        // Return Date (nullable safe)
	        if (bean.getReturnDate() != null) {
	            pstmt.setDate(4,
	                    new java.sql.Date(bean.getReturnDate().getTime()));
	        } else {
	            pstmt.setNull(4, java.sql.Types.DATE);
	        }

	        // Fine (default 0 if null)
	        if (bean.getFineAmount() != null) {
	            pstmt.setLong(5, bean.getFineAmount());
	        } else {
	            pstmt.setLong(5, 0);
	        }

	        pstmt.setString(6, bean.getIssuedBy());
	        pstmt.setString(7, bean.getStatus());
	        pstmt.setString(8, bean.getModifiedBy());
	        pstmt.setTimestamp(9, bean.getModifiedDatetime());
	        pstmt.setLong(10, bean.getId());

	        pstmt.executeUpdate();
	        conn.commit();
	        pstmt.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            conn.rollback();
	        } catch (Exception ex) {
	            throw new ApplicationException(
	                    "Update rollback exception");
	        }
	        throw new ApplicationException(
	                "Exception in updating Book Issue");
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }
	}

	/**
	 * Delete record
	 */
	public void delete(long id) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_library_book_issue WHERE id=?");

			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in deleting Book Issue");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find by PK
	 */
	public LibraryBookIssueBean findByPk(long id) throws ApplicationException {

		LibraryBookIssueBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_library_book_issue WHERE id=?");

			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in finding by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by BookId and MemberId
	 */
	public LibraryBookIssueBean findByBookAndMember(Long bookId, Long memberId) throws ApplicationException {

		LibraryBookIssueBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("SELECT * FROM st_library_book_issue WHERE book_id=? AND member_id=?");

			pstmt.setLong(1, bookId);
			pstmt.setLong(2, memberId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in finding by Book and Member");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}
	public List<LibraryBookIssueBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

	/**
	 * Search with pagination
	 */
	public List<LibraryBookIssueBean> search(LibraryBookIssueBean bean, int pageNo, int pageSize)
			throws ApplicationException {

		List<LibraryBookIssueBean> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM st_library_book_issue WHERE 1=1 ");

		if (bean != null) {

			if (bean.getBookId() != null && bean.getBookId() > 0) {
				sql.append(" AND book_id=").append(bean.getBookId());
			}

			if (bean.getMemberId() != null && bean.getMemberId() > 0) {
				sql.append(" AND member_id=").append(bean.getMemberId());
			}

			if (bean.getStatus() != null && !bean.getStatus().isEmpty()) {
				sql.append(" AND status LIKE '").append(bean.getStatus()).append("%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(mapResultSetToBean(rs));
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in searching Book Issue");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	/**
	 * Map ResultSet to Bean
	 */
	private LibraryBookIssueBean mapResultSetToBean(ResultSet rs) throws Exception {

		LibraryBookIssueBean bean = new LibraryBookIssueBean();

		bean.setId(rs.getLong(1));
		bean.setBookId(rs.getLong(2));
		bean.setMemberId(rs.getLong(3));
		bean.setIssueDate(rs.getDate(4));
		bean.setReturnDate(rs.getDate(5));
		bean.setFineAmount(rs.getLong(6));
		bean.setIssuedBy(rs.getString(7));
		bean.setStatus(rs.getString(8));
		bean.setCreatedBy(rs.getString(9));
		bean.setModifiedBy(rs.getString(10));
		bean.setCreatedDatetime(rs.getTimestamp(11));

		return bean;
	}
}