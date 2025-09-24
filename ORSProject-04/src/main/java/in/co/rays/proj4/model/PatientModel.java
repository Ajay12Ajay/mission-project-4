package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class PatientModel {

	public static Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_patient");
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

	public void add(PatientBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk;

		PatientBean duplicateCourse = findByName(bean.getName());
		if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
			throw new DuplicateRecordException("Course Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_patient values (?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setDate(3, new java.sql.Date(bean.getDateOfVisit().getTime()));
			pstmt.setString(4, bean.getMobile());
			pstmt.setString(5, bean.getDecease());

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
			throw new ApplicationException("Exception in adding Course");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void update(PatientBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		PatientBean duplicateCourse = findByName(bean.getName());

		if (duplicateCourse != null && duplicateCourse.getId() != bean.getId()) {
			throw new DuplicateRecordException("Patient Name already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_patient set name=?, date_of_visit=?, mobile=?, decease=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? where id = ?");
			pstmt.setString(1, bean.getName());
			pstmt.setDate(2, new java.sql.Date(bean.getDateOfVisit().getTime()));
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getDecease());

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
			throw new ApplicationException("Exception in updating Course");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_patient where id=?");
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
			throw new ApplicationException("Exception in Deleting Patient");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public PatientBean findByPk(long id) throws ApplicationException {

		PatientBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_patient where id = ?");

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting PAtient by pk");

		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;
	}

	public PatientBean findByName(String name) throws ApplicationException {

		PatientBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_patient where name = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Patient by Name");
		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;

	}

	public List<PatientBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<PatientBean> search(PatientBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_patient where 1=1 ");

		if (bean != null) {

			if (bean.getId() != null && bean.getId() > 0) {
				sql.append("and id =" + bean.getId());
			}

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append("and name like '" + bean.getName() + "%'");
			}

			if (bean.getDateOfVisit() != null && bean.getDateOfVisit().getTime() > 0) {
				sql.append(" and date_of_visit like '" + new java.sql.Date(bean.getDateOfVisit().getTime()) + "%'");
			}

			if (bean.getDecease() != null && bean.getDecease().length() > 0) {
				sql.append(" and decease like '" + bean.getDecease() + "%'");
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("sql => " + sql);

		List<PatientBean> list = new ArrayList<PatientBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setDateOfVisit(rs.getDate(3));
				bean.setMobile(rs.getString(4));
				bean.setDecease(rs.getString(5));

				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in searching Course");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

}
