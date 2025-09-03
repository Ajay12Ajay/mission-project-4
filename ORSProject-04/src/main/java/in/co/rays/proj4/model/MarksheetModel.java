package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.bean.StudentBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class MarksheetModel {

	public static Integer nextPk() throws DatabaseException {
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_marksheet");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = (rs.getInt(1));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new DatabaseException("Exception in Marksheet in getting Pk");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;

	}

	public void add(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		int pk = 0;

		StudentModel studentModel = new StudentModel();
		StudentBean studentBean = studentModel.findByPk(bean.getStudentId());
		bean.setName(studentBean.getFirstName() + " " + studentBean.getLastName());

		MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());

		if (duplicateMarksheet != null) {
			throw new DuplicateRecordException("Roll No already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_marksheet values (?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getRollNo());
			pstmt.setLong(3, bean.getStudentId());
			pstmt.setString(4, bean.getName());
			pstmt.setInt(5, bean.getPhysics());
			pstmt.setInt(6, bean.getChemistry());
			pstmt.setInt(7, bean.getMaths());

			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
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
			throw new ApplicationException("Exception in add marksheet");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		StudentModel studentModel = new StudentModel();
		StudentBean studentBean = studentModel.findByPk(bean.getStudentId());
		bean.setName(studentBean.getFirstName() + " " + studentBean.getLastName());

		MarksheetBean duplicateMarksheet = findByRollNo(bean.getRollNo());

		if (duplicateMarksheet != null && duplicateMarksheet.getId() != bean.getId()) {
			throw new DuplicateRecordException("Roll No already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_marksheet set  roll_no=?, student_id=?,name=?,physics=?,chemistry=?,maths=?, created_by=?, modified_by=?,created_datetime=?, modified_datetime=? where id = ?");
			pstmt.setString(1, bean.getRollNo());
			pstmt.setLong(2, bean.getStudentId());
			pstmt.setString(3, bean.getName());
			pstmt.setInt(4, bean.getPhysics());
			pstmt.setInt(5, bean.getChemistry());
			pstmt.setInt(6, bean.getMaths());

			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			pstmt.setLong(11, bean.getId());
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
			throw new ApplicationException("Exception in updating Marksheet");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(long id) throws ApplicationException {
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id=?");
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
			throw new ApplicationException("Exception in deleting Marksheet");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public MarksheetBean findByPk(long id) throws ApplicationException {

		MarksheetBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_marksheet where id = ?");

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getInt(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));

				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting marksheet by Pk");

		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;
	}

	public MarksheetBean findByRollNo(String rollNo) throws ApplicationException {

		MarksheetBean bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_marksheet where roll_no = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rollNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				bean = new MarksheetBean();
				bean.setId(rs.getInt(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));

				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in getting Marksheet by RollNo");
		} finally {
			JDBCDataSource.closeConnection(conn);

		}

		return bean;

	}

	public List<MarksheetBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public List<MarksheetBean> search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_marksheet where 1=1 ");

		if (bean != null) {

			if (bean.getId() != null && bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getRollNo() != null && bean.getRollNo().length() > 0) {
				sql.append(" and roll_no like '" + bean.getRollNo() + "%'");
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getPhysics() != null && bean.getPhysics() > 0) {
				sql.append(" and physics = " + bean.getPhysics());
			}
			if (bean.getChemistry() != null && bean.getChemistry() > 0) {
				sql.append(" and chemistry = " + bean.getChemistry());
			}
			if (bean.getMaths() != null && bean.getMaths() > 0) {
				sql.append(" and maths = '" + bean.getMaths());
			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println("sql => " + sql);

		List<MarksheetBean> list = new ArrayList<MarksheetBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));

				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);

			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception in search Marksheet " + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}

}
