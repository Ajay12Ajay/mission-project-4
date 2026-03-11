/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FreelancerModel handles CRUD operations and search functionality
 * for Freelancer entities. It interacts with the database using JDBC.
 *
 * @Creation Date: 11-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.FreelancerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class FreelancerModel {

	/**
	 * Returns next primary key.
	 */
	public static Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_freelancer");

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
	 * Add Freelancer record.
	 */
	public void add(FreelancerBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		FreelancerBean existing = findByProjectName(bean.getProjectName());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Project Name already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();

			int pk = nextPk();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO st_freelancer VALUES (?,?,?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFreelancerName());
			pstmt.setString(3, bean.getProjectName());
			pstmt.setDate(4, new java.sql.Date(bean.getDeadline().getTime()));
			pstmt.setDouble(5, bean.getPaymentAmount());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			pstmt.executeUpdate();

			conn.commit();

			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Add rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in adding Freelancer");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Update Freelancer record.
	 */
	public void update(FreelancerBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		FreelancerBean existing = findByProjectName(bean.getProjectName());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Project Name already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_freelancer SET freelancer_name=?, project_name=?, deadline=?, payment_amount=?, "
							+ "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

			pstmt.setString(1, bean.getFreelancerName());
			pstmt.setString(2, bean.getProjectName());
			pstmt.setDate(3, new java.sql.Date(bean.getDeadline().getTime()));
			pstmt.setDouble(4, bean.getPaymentAmount());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			pstmt.executeUpdate();

			conn.commit();

			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Update rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in updating Freelancer");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Delete Freelancer.
	 */
	public void delete(long id) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_freelancer WHERE id=?");

			pstmt.setLong(1, id);

			pstmt.executeUpdate();

			conn.commit();

			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Delete rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in deleting Freelancer");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find Freelancer by PK.
	 */
	public FreelancerBean findByPk(long id) throws ApplicationException {

		FreelancerBean bean = null;

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_freelancer WHERE id=?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Freelancer by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by Project Name (Unique).
	 */
	public FreelancerBean findByProjectName(String projectName) throws ApplicationException {

		FreelancerBean bean = null;

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_freelancer WHERE project_name=?");

			pstmt.setString(1, projectName);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Freelancer by Project Name");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * List all records.
	 */
	public List<FreelancerBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Search with pagination.
	 */
	public List<FreelancerBean> search(FreelancerBean bean, int pageNo, int pageSize) throws ApplicationException {

		List<FreelancerBean> list = new ArrayList<>();

		Connection conn = null;

		StringBuffer sql = new StringBuffer("SELECT * FROM st_freelancer WHERE 1=1");

		if (bean != null) {

			if (bean.getFreelancerName() != null && bean.getFreelancerName().length() > 0) {

				sql.append(" AND freelancer_name like '" + bean.getFreelancerName() + "%'");
			}

			if (bean.getProjectName() != null && bean.getProjectName().length() > 0) {

				sql.append(" AND project_name like '" + bean.getProjectName() + "%'");
			}

			if (bean.getPaymentAmount() != null && bean.getPaymentAmount() > 0) {

				sql.append(" AND payment_amount = " + bean.getPaymentAmount());
			}

			if (bean.getDeadline() != null) {

				sql.append(" AND deadline = '" + new java.sql.Date(bean.getDeadline().getTime()) + "'");
			}
		}

		if (pageSize > 0) {

			pageNo = (pageNo - 1) * pageSize;

			sql.append(" LIMIT " + pageNo + "," + pageSize);
		}

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

			e.printStackTrace();

			throw new ApplicationException("Exception in searching Freelancer");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	/**
	 * Map ResultSet to Bean.
	 */
	private FreelancerBean mapResultSetToBean(ResultSet rs) throws Exception {

		FreelancerBean bean = new FreelancerBean();

		bean.setId(rs.getLong(1));
		bean.setFreelancerName(rs.getString(2));
		bean.setProjectName(rs.getString(3));
		bean.setDeadline(rs.getDate(4));
		bean.setPaymentAmount(rs.getDouble(5));
		bean.setCreatedBy(rs.getString(6));
		bean.setModifiedBy(rs.getString(7));
		bean.setCreatedDatetime(rs.getTimestamp(8));
		bean.setModifiedDatetime(rs.getTimestamp(9));

		return bean;
	}

}