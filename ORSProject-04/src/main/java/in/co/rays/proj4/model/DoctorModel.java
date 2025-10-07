/**
 * @Author: Ajay Pratap Kerketta
 * @Description: DoctorModel handles CRUD operations and search functionality 
 * for Doctor entities. It interacts with the database using JDBC.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

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

/**
 * Model class for managing Doctor entity. Provides methods for 
 * add, update, delete, find by PK, find by Name, search, and list operations.
 */
public class DoctorModel {

    /**
     * Returns next primary key for Doctor table.
     * 
     * @return next primary key
     * @throws DatabaseException
     */
    public static Integer nextPk() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_doctor");
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
     * Adds a new Doctor record to the database.
     * 
     * @param bean DoctorBean object
     * @throws ApplicationException
     * @throws DuplicateRecordException if doctor with same name exists
     */
    public void add(DoctorBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;
        int pk;

        // Check for duplicate doctor name
        DoctorBean existing = findByName(bean.getName());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Doctor Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO st_doctor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(4, bean.getMobile());
            pstmt.setString(5, bean.getExpertise());
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
            throw new ApplicationException("Exception in adding Doctor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Updates existing Doctor record in the database.
     * 
     * @param bean DoctorBean object
     * @throws ApplicationException
     * @throws DuplicateRecordException if doctor with same name exists
     */
    public void update(DoctorBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;

        DoctorBean existing = findByName(bean.getName());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Doctor Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_doctor SET name=?, dob=?, mobile=?, expertise=?, "
                            + "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");
            pstmt.setString(1, bean.getName());
            pstmt.setDate(2, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(3, bean.getMobile());
            pstmt.setString(4, bean.getExpertise());
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
            throw new ApplicationException("Exception in updating Doctor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Doctor record by ID.
     * 
     * @param id Doctor ID
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_doctor WHERE id=?");
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
            throw new ApplicationException("Exception in deleting Doctor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Doctor by primary key.
     * 
     * @param id Doctor ID
     * @return DoctorBean
     * @throws ApplicationException
     */
    public DoctorBean findByPk(long id) throws ApplicationException {
        DoctorBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_doctor WHERE id=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Doctor by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Finds a Doctor by name.
     * 
     * @param name Doctor name
     * @return DoctorBean
     * @throws ApplicationException
     */
    public DoctorBean findByName(String name) throws ApplicationException {
        DoctorBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_doctor WHERE name=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Doctor by Name");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Returns a list of all Doctor records.
     * 
     * @return List of DoctorBean
     * @throws ApplicationException
     */
    public List<DoctorBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Searches Doctor records with optional pagination.
     * 
     * @param bean     DoctorBean for search criteria
     * @param pageNo   Page number
     * @param pageSize Number of records per page
     * @return List of DoctorBean
     * @throws ApplicationException
     */
    public List<DoctorBean> search(DoctorBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM st_doctor WHERE 1=1 ");

        if (bean != null) {
            if (bean.getId() != null && bean.getId() > 0) {
                sql.append(" AND id=").append(bean.getId());
            }
            if (bean.getName() != null && !bean.getName().isEmpty()) {
                sql.append(" AND name LIKE '").append(bean.getName()).append("%'");
            }
            if (bean.getDob() != null) {
                sql.append(" AND dob LIKE '").append(new java.sql.Date(bean.getDob().getTime())).append("%'");
            }
            if (bean.getExpertise() != null && !bean.getExpertise().isEmpty()) {
                sql.append(" AND expertise LIKE '").append(bean.getExpertise()).append("%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
        }

        List<DoctorBean> list = new ArrayList<>();

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
            throw new ApplicationException("Exception in searching Doctor");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }

    /**
     * Maps ResultSet row to DoctorBean.
     * 
     * @param rs ResultSet
     * @return DoctorBean
     * @throws Exception
     */
    private DoctorBean mapResultSetToBean(ResultSet rs) throws Exception {
        DoctorBean bean = new DoctorBean();
        bean.setId(rs.getLong(1));
        bean.setName(rs.getString(2));
        bean.setDob(rs.getDate(3));
        bean.setMobile(rs.getString(4));
        bean.setExpertise(rs.getString(5));
        bean.setCreatedBy(rs.getString(6));
        bean.setModifiedBy(rs.getString(7));
        bean.setCreatedDatetime(rs.getTimestamp(8));
        bean.setModifiedDatetime(rs.getTimestamp(9));
        return bean;
    }
}
