/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FeedbackModel handles CRUD operations and search functionality
 * for Feedback entities. It interacts with the database using JDBC.
 *
 * @Creation Date: 28-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.FeedbackBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class FeedbackModel {

	/**
	 * Returns next primary key.
	 */
	public static Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_feedback");
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
	 * Adds a new Feedback record.
	 */
	public void add(FeedbackBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		// Duplicate check (feedbackCode must be unique)
		FeedbackBean existing = findByCode(bean.getFeedbackCode());
		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Feedback Code already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			int pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO st_feedback VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFeedbackCode());
			pstmt.setString(3, bean.getUserName());
			pstmt.setString(4, bean.getComments());
			pstmt.setInt(5, bean.getRating());
			pstmt.setDate(6, new java.sql.Date(bean.getFeedbackDate().getTime()));
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Add rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in adding Feedback");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Updates Feedback record.
	 */
	public void update(FeedbackBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		FeedbackBean existing = findByCode(bean.getFeedbackCode());
		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Feedback Code already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_feedback SET feedback_code=?, user_name=?, comments=?, rating=?, feedback_date=?, "
							+ "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

			pstmt.setString(1, bean.getFeedbackCode());
			pstmt.setString(2, bean.getUserName());
			pstmt.setString(3, bean.getComments());
			pstmt.setInt(4, bean.getRating());
			pstmt.setDate(5, new java.sql.Date(bean.getFeedbackDate().getTime()));
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Update rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Feedback");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Deletes Feedback by ID.
	 */
	public void delete(long id) throws ApplicationException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_feedback WHERE id=?");
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Delete rollback exception: " + ex.getMessage());
			}
			throw new ApplicationException("Exception in deleting Feedback");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find by PK.
	 */
	public FeedbackBean findByPk(long id) throws ApplicationException {

		FeedbackBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_feedback WHERE id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Feedback by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by Feedback Code (Unique).
	 */
	public FeedbackBean findByCode(String code) throws ApplicationException {

		FeedbackBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_feedback WHERE feedback_code=?");
			pstmt.setString(1, code);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Feedback by Code");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * List all records.
	 */
	public List<FeedbackBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Search with pagination (SAFE & FIXED VERSION).
	 */
	public List<FeedbackBean> search(FeedbackBean bean, int pageNo, int pageSize)
	        throws ApplicationException {

	    List<FeedbackBean> list = new ArrayList<>();
	    Connection conn = null;

	    StringBuilder sql = new StringBuilder(
	            "SELECT * FROM st_feedback WHERE 1=1 ");

	    List<Object> params = new ArrayList<>();

	    try {

	        if (bean != null) {

	            if (bean.getFeedbackCode() != null
	                    && bean.getFeedbackCode().length() > 0) {

	                sql.append(" AND feedback_code LIKE ? ");
	                params.add(bean.getFeedbackCode() + "%");
	            }

	            if (bean.getUserName() != null
	                    && bean.getUserName().length() > 0) {

	                sql.append(" AND user_name LIKE ? ");
	                params.add(bean.getUserName() + "%");
	            }

	            if (bean.getRating() != null
	                    && bean.getRating() > 0) {

	                sql.append(" AND rating = ? ");
	                params.add(bean.getRating());
	            }

	            if (bean.getFeedbackDate() != null) {

	                sql.append(" AND feedback_date = ? ");
	                params.add(new java.sql.Date(
	                        bean.getFeedbackDate().getTime()));
	            }
	        }

	        if (pageSize > 0) {
	            sql.append(" LIMIT ?, ? ");
	            params.add((pageNo - 1) * pageSize);
	            params.add(pageSize);
	        }

	        conn = JDBCDataSource.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql.toString());

	        // Set parameters safely
	        for (int i = 0; i < params.size(); i++) {
	            pstmt.setObject(i + 1, params.get(i));
	        }

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            list.add(mapResultSetToBean(rs));
	        }

	        rs.close();
	        pstmt.close();

	    } catch (Exception e) {

	        
	        e.printStackTrace();

	        throw new ApplicationException(
	                "Exception in searching Feedback: "
	                        + e.getMessage());
	    } finally {
	        JDBCDataSource.closeConnection(conn);
	    }

	    return list;
	}

	/**
	 * Maps ResultSet to FeedbackBean.
	 */
	private FeedbackBean mapResultSetToBean(ResultSet rs) throws Exception {

		FeedbackBean bean = new FeedbackBean();

		bean.setId(rs.getLong(1));
		bean.setFeedbackCode(rs.getString(2));
		bean.setUserName(rs.getString(3));
		bean.setComments(rs.getString(4));
		bean.setRating(rs.getInt(5));
		bean.setFeedbackDate(rs.getDate(6));
		bean.setCreatedBy(rs.getString(7));
		bean.setModifiedBy(rs.getString(8));
		bean.setCreatedDatetime(rs.getTimestamp(9));
		bean.setModifiedDatetime(rs.getTimestamp(10));

		return bean;
	}
}