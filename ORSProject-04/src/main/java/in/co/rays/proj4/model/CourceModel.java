/**
 * @Author: Ajay Pratap Kerketta
 * @Description: CourceModel handles CRUD operations and search functionality 
 * for Cource entities. It interacts with the database using JDBC.
 * 
 * @Creation Date: 26-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.CourceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class CourceModel {

    // ===================== NEXT PK =====================
    public static Integer nextPk() throws DatabaseException {

        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_cource");
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

    // ===================== ADD =====================
    public void add(CourceBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        // Duplicate check (courseCode must be unique)
        CourceBean existing = findByCourseCode(bean.getCourseCode());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Course Code already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            int pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO st_cource VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getCourseCode());
            pstmt.setString(3, bean.getCourseName());
            pstmt.setString(4, bean.getDuration());
            pstmt.setString(5, bean.getCourseFee());
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
                throw new ApplicationException("Add rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in adding Cource");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // ===================== UPDATE =====================
    public void update(CourceBean bean)
            throws ApplicationException, DuplicateRecordException {

        Connection conn = null;

        CourceBean existing = findByCourseCode(bean.getCourseCode());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Course Code already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_cource SET course_code=?, course_name=?, duration=?, course_fee=?, "
                            + "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");

            pstmt.setString(1, bean.getCourseCode());
            pstmt.setString(2, bean.getCourseName());
            pstmt.setString(3, bean.getDuration());
            pstmt.setString(4, bean.getCourseFee());
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
                throw new ApplicationException("Update rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Cource");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // ===================== DELETE =====================
    public void delete(long id) throws ApplicationException {

        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM st_cource WHERE id=?");

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
            throw new ApplicationException("Exception in deleting Cource");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    // ===================== FIND BY PK =====================
    public CourceBean findByPk(long id) throws ApplicationException {

        CourceBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM st_cource WHERE id=?");

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Cource by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    // ===================== FIND BY COURSE CODE (UNIQUE) =====================
    public CourceBean findByCourseCode(String courseCode)
            throws ApplicationException {

        CourceBean bean = null;
        Connection conn = null;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM st_cource WHERE course_code=?");

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Cource by Course Code");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    // ===================== LIST =====================
    public List<CourceBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    // ===================== SEARCH =====================
    public List<CourceBean> search(CourceBean bean, int pageNo, int pageSize)
            throws ApplicationException {

        Connection conn = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM st_cource WHERE 1=1 ");

        if (bean != null) {

            if (bean.getId() != null && bean.getId() > 0) {
                sql.append(" AND id=").append(bean.getId());
            }

            if (bean.getCourseCode() != null && !bean.getCourseCode().isEmpty()) {
                sql.append(" AND course_code LIKE '")
                        .append(bean.getCourseCode()).append("%'");
            }

            if (bean.getCourseName() != null && !bean.getCourseName().isEmpty()) {
                sql.append(" AND course_name LIKE '")
                        .append(bean.getCourseName()).append("%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
        }

        List<CourceBean> list = new ArrayList<>();

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
            throw new ApplicationException("Exception in searching Cource");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }

    // ===================== MAP RESULTSET =====================
    private CourceBean mapResultSetToBean(ResultSet rs) throws Exception {

        CourceBean bean = new CourceBean();

        bean.setId(rs.getLong(1));
        bean.setCourseCode(rs.getString(2));
        bean.setCourseName(rs.getString(3));
        bean.setDuration(rs.getString(4));
        bean.setCourseFee(rs.getString(5));
        bean.setCreatedBy(rs.getString(6));
        bean.setModifiedBy(rs.getString(7));
        bean.setCreatedDatetime(rs.getTimestamp(8));
        bean.setModifiedDatetime(rs.getTimestamp(9));

        return bean;
    }
}