/**
 * @Author: Ajay Pratap Kerketta
 * @Description: VehicleModel handles CRUD operations and search functionality
 * for Vehicle entities. It interacts with the database using JDBC.
 *
 * @Creation Date: 09-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.VehicleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class VehicleModel {

	/**
	 * Returns next primary key.
	 */
	public static Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_vehicle");

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
	 * Add Vehicle record.
	 */
	public void add(VehicleBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		VehicleBean existing = findByVehicleNumber(bean.getVehicleNumber());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Vehicle Number already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();

			int pk = nextPk();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO st_vehicle VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getVehicleNumber());
			pstmt.setString(3, bean.getOwnerName());
			pstmt.setDate(4, new java.sql.Date(bean.getServiceDate().getTime()));
			pstmt.setString(5, bean.getServiceType());
			pstmt.setDouble(6, bean.getCost());
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
				throw new ApplicationException("Add rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in adding Vehicle");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Update Vehicle record.
	 */
	public void update(VehicleBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		VehicleBean existing = findByVehicleNumber(bean.getVehicleNumber());

		if (existing != null && existing.getId() != bean.getId()) {
			throw new DuplicateRecordException("Vehicle Number already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE st_vehicle SET vehicle_number=?, owner_name=?, service_date=?, service_type=?, cost=?, "
							+ "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

			pstmt.setString(1, bean.getVehicleNumber());
			pstmt.setString(2, bean.getOwnerName());
			pstmt.setDate(3, new java.sql.Date(bean.getServiceDate().getTime()));
			pstmt.setString(4, bean.getServiceType());
			pstmt.setDouble(5, bean.getCost());
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
				throw new ApplicationException("Update rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception in updating Vehicle");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Delete Vehicle by ID.
	 */
	public void delete(long id) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_vehicle WHERE id=?");

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

			throw new ApplicationException("Exception in deleting Vehicle");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	/**
	 * Find by PK.
	 */
	public VehicleBean findByPk(long id) throws ApplicationException {

		VehicleBean bean = null;

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM st_vehicle WHERE id=?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Vehicle by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * Find by Vehicle Number (Unique).
	 */
	public VehicleBean findByVehicleNumber(String vehicleNumber) throws ApplicationException {

		VehicleBean bean = null;

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn
					.prepareStatement("SELECT * FROM st_vehicle WHERE vehicle_number=?");

			pstmt.setString(1, vehicleNumber);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				bean = mapResultSetToBean(rs);
			}

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Vehicle by Number");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;
	}

	/**
	 * List all records.
	 */
	public List<VehicleBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	/**
	 * Search with pagination.
	 */
	public List<VehicleBean> search(VehicleBean bean, int pageNo, int pageSize)
			throws ApplicationException {

		List<VehicleBean> list = new ArrayList<>();

		Connection conn = null;

		StringBuilder sql = new StringBuilder("SELECT * FROM st_vehicle WHERE 1=1");

		List<Object> params = new ArrayList<>();

		try {

			if (bean != null) {

				if (bean.getVehicleNumber() != null && bean.getVehicleNumber().length() > 0) {
					sql.append(" AND vehicle_number LIKE ?");
					params.add(bean.getVehicleNumber() + "%");
				}

				if (bean.getOwnerName() != null && bean.getOwnerName().length() > 0) {
					sql.append(" AND owner_name LIKE ?");
					params.add(bean.getOwnerName() + "%");
				}

				if (bean.getServiceType() != null && bean.getServiceType().length() > 0) {
					sql.append(" AND service_type LIKE ?");
					params.add(bean.getServiceType() + "%");
				}

				if (bean.getCost() != null && bean.getCost() > 0) {
					sql.append(" AND cost = ?");
					params.add(bean.getCost());
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

			throw new ApplicationException("Exception in searching Vehicle " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}

	/**
	 * Map ResultSet to Bean.
	 */
	private VehicleBean mapResultSetToBean(ResultSet rs) throws Exception {

		VehicleBean bean = new VehicleBean();

		bean.setId(rs.getLong(1));
		bean.setVehicleNumber(rs.getString(2));
		bean.setOwnerName(rs.getString(3));
		bean.setServiceDate(rs.getDate(4));
		bean.setServiceType(rs.getString(5));
		bean.setCost(rs.getDouble(6));
		bean.setCreatedBy(rs.getString(7));
		bean.setModifiedBy(rs.getString(8));
		bean.setCreatedDatetime(rs.getTimestamp(9));
		bean.setModifiedDatetime(rs.getTimestamp(10));

		return bean;
	}
}