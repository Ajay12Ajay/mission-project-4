/**
 * @Author: Ajay Pratap Kerketta
 * @Description: TransportModel handles CRUD operations and search functionality
 * for Transport entities. It interacts with the database using JDBC.
 *
 * @Creation Date: 10-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class TransportModel {

	/**
	 * Returns next primary key.
	 */
	public static Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_transport");

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
	 * Add Transport record.
	 */
	public void add(TransportBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		TransportBean existing = findByTransportId(bean.getTransportId());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Transport ID already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();
			int pk = nextPk();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO st_transport VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getTransportId());
			pstmt.setString(3, bean.getVehicleType());
			pstmt.setString(4, bean.getDriverName());
			pstmt.setDouble(5, bean.getCharges());
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

			throw new ApplicationException("Exception in adding Transport");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Update Transport record.
	 */
	public void update(TransportBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		TransportBean existing = findByTransportId(bean.getTransportId());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Transport ID already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_transport SET transport_id=?, vehicle_type=?, driver_name=?, charges=?, "
							+ "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

			pstmt.setString(1, bean.getTransportId());
			pstmt.setString(2, bean.getVehicleType());
			pstmt.setString(3, bean.getDriverName());
			pstmt.setDouble(4, bean.getCharges());
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

			throw new ApplicationException("Exception in updating Transport");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Delete Transport by ID.
	 */
	public void delete(long id) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_transport WHERE id=?");

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

			throw new ApplicationException("Exception in deleting Transport");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find by PK.
	 */
	public TransportBean findByPk(long id) throws ApplicationException {

		TransportBean bean = null;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_transport WHERE id=?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Transport by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by Transport ID (Unique).
	 */
	public TransportBean findByTransportId(String transportId) throws ApplicationException {

		TransportBean bean = null;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_transport WHERE transport_id=?");

			pstmt.setString(1, transportId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Transport by ID");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * List all records.
	 */
	public List<TransportBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Search with pagination.
	 */
	public List<TransportBean> search(TransportBean bean, int pageNo, int pageSize) throws ApplicationException {

		List<TransportBean> list = new ArrayList<>();
		Connection conn = null;

		StringBuilder sql = new StringBuilder("SELECT * FROM st_transport WHERE 1=1");

		List<Object> params = new ArrayList<>();

		try {

			if (bean != null) {

				if (bean.getTransportId() != null && bean.getTransportId().length() > 0) {

					sql.append(" AND transport_id LIKE ?");
					params.add(bean.getTransportId() + "%");
				}

				if (bean.getVehicleType() != null && bean.getVehicleType().length() > 0) {

					sql.append(" AND vehicle_type LIKE ?");
					params.add(bean.getVehicleType() + "%");
				}

				if (bean.getDriverName() != null && bean.getDriverName().length() > 0) {

					sql.append(" AND driver_name LIKE ?");
					params.add(bean.getDriverName() + "%");
				}

				if (bean.getCharges() != null && bean.getCharges() > 0) {

					sql.append(" AND charges = ?");
					params.add(bean.getCharges());
				}
			}

			if (pageSize > 0) {

				sql.append(" LIMIT ?, ?");

				params.add((pageNo - 1) * pageSize);
				params.add(pageSize);
			}

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

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

			throw new ApplicationException("Exception in searching Transport " + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	/**
	 * Map ResultSet to Bean.
	 */
	private TransportBean mapResultSetToBean(ResultSet rs) throws Exception {

		TransportBean bean = new TransportBean();

		bean.setId(rs.getLong(1));
		bean.setTransportId(rs.getString(2));
		bean.setVehicleType(rs.getString(3));
		bean.setDriverName(rs.getString(4));
		bean.setCharges(rs.getDouble(5));
		bean.setCreatedBy(rs.getString(6));
		bean.setModifiedBy(rs.getString(7));
		bean.setCreatedDatetime(rs.getTimestamp(8));
		bean.setModifiedDatetime(rs.getTimestamp(9));

		return bean;
	}
}