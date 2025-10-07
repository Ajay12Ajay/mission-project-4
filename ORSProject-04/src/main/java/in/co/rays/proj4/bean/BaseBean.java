/**
 * @author Ajay Pratap Kerketta
 * @version 1.0
 * @since 2025
 *
 * <p>
 * The {@code BaseBean} class serves as the base model (Data Transfer Object - DTO)
 * for all beans in the application. It provides common attributes and methods that
 * are shared among all data entities.
 * </p>
 *
 * <p>
 * <b>Use Case:</b><br>
 * Every entity class in the application (e.g., UserBean, RoleBean, etc.)
 * extends this class to inherit basic properties such as:
 * <ul>
 *   <li>Primary key ID</li>
 *   <li>Audit information like createdBy, modifiedBy</li>
 *   <li>Audit timestamps (createdDatetime, modifiedDatetime)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Advantages:</b>
 * <ul>
 *   <li>Ensures consistency of base fields across all beans.</li>
 *   <li>Promotes code reuse and maintainability.</li>
 *   <li>Facilitates easy data tracking and auditing.</li>
 * </ul>
 * </p>
 */

package in.co.rays.proj4.bean;

import java.sql.Timestamp;

public abstract class BaseBean implements DropdownListBean {

    /** The unique identifier (primary key) for each entity record. */
    protected long id;

    /** The username or identifier of the user who created this record. */
    protected String createdBy;

    /** The username or identifier of the user who last modified this record. */
    protected String modifiedBy;

    /** The timestamp when this record was created. */
    protected Timestamp createdDatetime;

    /** The timestamp when this record was last modified. */
    protected Timestamp modifiedDatetime;

    /**
     * Gets the unique ID of the entity.
     *
     * @return the ID of the entity
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the entity.
     *
     * @param id the ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the username or identifier of the user who created the record.
     *
     * @return the creator's username
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the username or identifier of the user who created the record.
     *
     * @param createdBy the creator's username
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the username or identifier of the user who last modified the record.
     *
     * @return the modifier's username
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the username or identifier of the user who last modified the record.
     *
     * @param modifiedBy the modifier's username
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Gets the timestamp of when the record was created.
     *
     * @return the creation timestamp
     */
    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    /**
     * Sets the timestamp of when the record was created.
     *
     * @param createdDatetime the creation timestamp to set
     */
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    /**
     * Gets the timestamp of when the record was last modified.
     *
     * @return the last modification timestamp
     */
    public Timestamp getModifiedDatetime() {
        return modifiedDatetime;
    }

    /**
     * Sets the timestamp of when the record was last modified.
     *
     * @param modifiedDatetime the modification timestamp to set
     */
    public void setModifiedDatetime(Timestamp modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

}
