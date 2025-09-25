package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class DoctorModel {

	public static Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_doctor");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = (rs.getInt(1));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;

	}

	public void add(DoctorBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk;

		DoctorBean duplicateCourse = findByName(bean.getName());
		if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
			throw new DuplicateRecordException("Doctor Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_doctor values (?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getMobile());
			pstmt.setString(5, bean.getExpertise());

			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			System.out.println("Data Added => " + i);

			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : add rollback exception" + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception in adding Doctor");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void update(DoctorBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		DoctorBean duplicateCourse = findByName(bean.getName());

		if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
			throw new DuplicateRecordException("Doctor Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_doctor set name=?, dob=?, mobile=?, expertise=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setDate(2, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getExpertise());

			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Data Updated => " + i);

			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : update rollback exception" + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception in updating Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_doctor where id=?");
			pstmt.setLong(1, id);
			int i = pstmt.executeUpdate();
			System.out.println("Data Deleted => " + i);

			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : delete rollback exception" + ex.getMessage());
			}
			e.printStackTrace();
			throw new ApplicationException("Exception in Deleting Doctor");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public DoctorBean findByPk(long id) throws ApplicationException {

		DoctorBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_doctor where id = ?");

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDob(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setExpertise(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting Doctor by pk");

		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;
	}

	public DoctorBean findByName(String name) throws ApplicationException {

		DoctorBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_doctor where name = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDob(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setExpertise(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Doctor by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;

	}

	public List<DoctorBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<DoctorBean> search(DoctorBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_doctor where 1=1 ");

		if (bean != null) {

			if (bean.getId() != null && bean.getId() > 0) {
				sql.append("and id =" + bean.getId());
			}

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append("and name like '" + bean.getName() + "%'");
			}

			if (bean.getDob() != null && bean.getDob().getTime() > 0) {
				sql.append(" and dob like '" + new java.sql.Date(bean.getDob().getTime()) + "%'");
			}

			if (bean.getExpertise() != null && bean.getExpertise().length() > 0) {
				sql.append(" and expertise like '" + bean.getExpertise() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("sql => " + sql);

		List<DoctorBean> list = new ArrayList<DoctorBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new DoctorBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDob(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setExpertise(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in searching Doctor");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

}
