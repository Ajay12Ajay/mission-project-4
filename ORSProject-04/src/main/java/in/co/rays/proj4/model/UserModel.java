/**
 * @Author: Ajay Pratap Kerketta
 * @Description: UserModel handles CRUD operations, authentication, password management, 
 * and email notifications for User entities. It interacts with the database using JDBC.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.exception.RecordNotFoundException;
import in.co.rays.proj4.util.EmailBuilder;
import in.co.rays.proj4.util.EmailMessage;
import in.co.rays.proj4.util.EmailUtility;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for managing User entity. Provides methods for 
 * add, update, delete, find by PK, find by Login, authenticate, 
 * search, password change, forget password, and user registration.
 */
public class UserModel {

    private Logger log = Logger.getLogger(UserModel.class);

    /**
     * Returns next primary key for User table.
     * 
     * @return next primary key
     * @throws DatabaseException
     */
    public static Integer nextPk() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_user");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Exception in getting PK in User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk + 1;
    }

    /**
     * Adds a new User record to the database.
     * 
     * @param bean UserBean object
     * @return newly generated primary key
     * @throws ApplicationException
     * @throws DuplicateRecordException if login already exists
     */
    public long add(UserBean bean) throws ApplicationException, DuplicateRecordException {
        log.debug("debug in UserModel add()");

        Connection conn = null;
        int pk = 0;

        // Check for duplicate login
        UserBean existing = findByLogin(bean.getLogin());
        if (existing != null) {
            throw new DuplicateRecordException("Login Id already exists in User");
        }

        try {
            pk = nextPk();
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO st_user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, pk);
            pstmt.setString(2, bean.getFirstName());
            pstmt.setString(3, bean.getLastName());
            pstmt.setString(4, bean.getLogin());
            pstmt.setString(5, bean.getPassword());
            pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(7, bean.getMobileNo());
            pstmt.setLong(8, bean.getRoleId());
            pstmt.setString(9, bean.getGender());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDatetime());
            pstmt.setTimestamp(13, bean.getModifiedDatetime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk;
    }

    /**
     * Updates existing User record in the database.
     * 
     * @param bean UserBean object
     * @throws ApplicationException
     * @throws DuplicateRecordException if login already exists
     */
    public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;

        UserBean existing = findByLogin(bean.getLogin());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Login Id already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_user SET first_name=?, last_name=?, login=?, password=?, dob=?, mobile_no=?, "
                            + "role_id=?, gender=?, created_by=?, modified_by=?, created_datetime=?, modified_datetime=? "
                            + "WHERE id=?");
            pstmt.setString(1, bean.getFirstName());
            pstmt.setString(2, bean.getLastName());
            pstmt.setString(3, bean.getLogin());
            pstmt.setString(4, bean.getPassword());
            pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(6, bean.getMobileNo());
            pstmt.setLong(7, bean.getRoleId());
            pstmt.setString(8, bean.getGender());
            pstmt.setString(9, bean.getCreatedBy());
            pstmt.setString(10, bean.getModifiedBy());
            pstmt.setTimestamp(11, bean.getCreatedDatetime());
            pstmt.setTimestamp(12, bean.getModifiedDatetime());
            pstmt.setLong(13, bean.getId());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in update User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a User record by ID.
     * 
     * @param id User ID
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_user WHERE id=?");
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
            throw new ApplicationException("Exception in delete User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a User by primary key.
     */
    public UserBean findByPk(long id) throws ApplicationException {
        UserBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_user WHERE id=?";

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
            throw new ApplicationException("Exception in getting User by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Finds a User by login.
     */
    public UserBean findByLogin(String login) throws ApplicationException {
        UserBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_user WHERE login=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting User by Login");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Authenticates a user.
     */
    public UserBean authenticate(String login, String password) throws ApplicationException {
        UserBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_user WHERE login=? AND password=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in authenticate User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        return bean;
    }

    /**
     * Returns a list of all users.
     */
    public List<UserBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Searches User records with optional pagination.
     */
    public List<UserBean> search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM st_user WHERE 1=1 ");

        if (bean != null) {
            if (bean.getId() > 0) sql.append(" AND id=").append(bean.getId());
            if (bean.getFirstName() != null && !bean.getFirstName().isEmpty())
                sql.append(" AND first_name LIKE '").append(bean.getFirstName()).append("%'");
            if (bean.getLastName() != null && !bean.getLastName().isEmpty())
                sql.append(" AND last_name LIKE '").append(bean.getLastName()).append("%'");
            if (bean.getLogin() != null && !bean.getLogin().isEmpty())
                sql.append(" AND login LIKE '").append(bean.getLogin()).append("%'");
            if (bean.getPassword() != null)
                sql.append(" AND password LIKE '").append(bean.getPassword()).append("%'");
            if (bean.getDob() != null)
                sql.append(" AND dob LIKE '").append(new java.sql.Date(bean.getDob().getTime())).append("%'");
            if (bean.getMobileNo() != null)
                sql.append(" AND mobile_no LIKE '").append(bean.getMobileNo()).append("%'");
            if (bean.getRoleId() > 0)
                sql.append(" AND role_id=").append(bean.getRoleId());
            if (bean.getGender() != null)
                sql.append(" AND gender LIKE '").append(bean.getGender()).append("%'");
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
        }

        List<UserBean> list = new ArrayList<>();
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
            throw new ApplicationException("Exception in searching User");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }

    /**
     * Changes user's password and sends notification email.
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword)
            throws RecordNotFoundException, ApplicationException {

        UserBean user = findByPk(id);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            throw new RecordNotFoundException("Old Password is Invalid");
        }

        user.setPassword(newPassword);
        try {
            update(user);
        } catch (DuplicateRecordException e) {
            throw new ApplicationException("Login Id already exists");
        }

        // Send email
        HashMap<String, String> map = new HashMap<>();
        map.put("login", user.getLogin());
        map.put("password", user.getPassword());
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());

        String message = EmailBuilder.getChangePasswordMessage(map);
        EmailMessage msg = new EmailMessage();
        msg.setTo(user.getLogin());
        msg.setSubject("ORSProject-04 Password changed successfully");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);
        EmailUtility.sendMail(msg);

        return true;
    }

    /**
     * Handles forget password functionality and sends email with password.
     */
    public boolean forgetPassword(String login) throws RecordNotFoundException, ApplicationException {
        UserBean user = findByLogin(login);
        if (user == null) throw new RecordNotFoundException("Email ID does not exist");

        HashMap<String, String> map = new HashMap<>();
        map.put("login", user.getLogin());
        map.put("password", user.getPassword());
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());

        String message = EmailBuilder.getForgetPasswordMessage(map);
        EmailMessage msg = new EmailMessage();
        msg.setTo(login);
        msg.setSubject("ORSProject-04 Password Reset");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);
        EmailUtility.sendMail(msg);

        return true;
    }

    /**
     * Registers a new user and sends registration email.
     */
    public long registerUser(UserBean bean) throws DuplicateRecordException, ApplicationException {
        long pk = add(bean);

        HashMap<String, String> map = new HashMap<>();
        map.put("login", bean.getLogin());
        map.put("password", bean.getPassword());

        String message = EmailBuilder.getUserRegistrationMessage(map);
        EmailMessage msg = new EmailMessage();
        msg.setTo(bean.getLogin());
        msg.setSubject("Registration successful for ORSProject-04");
        msg.setMessage(message);
        msg.setMessageType(EmailMessage.HTML_MSG);
        EmailUtility.sendMail(msg);

        return pk;
    }

    /**
     * Maps ResultSet row to UserBean.
     */
    private UserBean mapResultSetToBean(ResultSet rs) throws Exception {
        UserBean bean = new UserBean();
        bean.setId(rs.getLong(1));
        bean.setFirstName(rs.getString(2));
        bean.setLastName(rs.getString(3));
        bean.setLogin(rs.getString(4));
        bean.setPassword(rs.getString(5));
        bean.setDob(rs.getDate(6));
        bean.setMobileNo(rs.getString(7));
        bean.setRoleId(rs.getLong(8));
        bean.setGender(rs.getString(9));
        bean.setCreatedBy(rs.getString(10));
        bean.setModifiedBy(rs.getString(11));
        bean.setCreatedDatetime(rs.getTimestamp(12));
        bean.setModifiedDatetime(rs.getTimestamp(13));
        return bean;
    }
}
