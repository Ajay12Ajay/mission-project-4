/**
 * @author Ajay
 * 
 * @version 1.0
 * @since 2025
 *
 * <p>
 * The {@code BaseCtl} class is an abstract servlet controller that provides 
 * a base structure for all controller classes in the application. 
 * It defines common constants, methods for validation, data preloading, 
 * bean population, and request servicing.
 * </p>
 *
 * <p>
 * Use Case:
 * <ul>
 *   <li>Provides a common structure for all servlet controllers.</li>
 *   <li>Reduces code duplication by handling common operations.</li>
 *   <li>Enforces implementation of specific methods like {@link #getView()} 
 *       in subclasses.</li>
 * </ul>
 * </p>
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.ServletUtility;

public abstract class BaseCtl extends HttpServlet {

    /** Common operation constants used across controllers. */
    public static final String OP_SAVE = "Save";
    public static final String OP_UPDATE = "Update";
    public static final String OP_CANCEL = "Cancel";
    public static final String OP_DELETE = "Delete";
    public static final String OP_LIST = "List";
    public static final String OP_SEARCH = "Search";
    public static final String OP_VIEW = "View";
    public static final String OP_NEXT = "Next";
    public static final String OP_PREVIOUS = "Previous";
    public static final String OP_NEW = "New";
    public static final String OP_GO = "Go";
    public static final String OP_BACK = "Back";
    public static final String OP_RESET = "Reset";
    public static final String OP_LOG_OUT = "Logout";

    /** Message keys for success and error messages. */
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_ERROR = "error";

    /**
     * Validates input data entered by the user.
     * <p>
     * Child classes override this method to implement specific validation logic.
     * </p>
     *
     * @param request the {@link HttpServletRequest} object containing client input
     * @return {@code true} if validation passes or no validation is required, 
     *         otherwise {@code false}
     */
    protected boolean validate(HttpServletRequest request) {
        return true;
    }

    /**
     * Loads pre-required data before displaying the form.
     * <p>
     * Child classes can override this method to load data (like dropdown lists)
     * dynamically from the database or statically from JSP.
     * </p>
     *
     * @param request the {@link HttpServletRequest} object to set attributes
     */
    protected void preload(HttpServletRequest request) {
    }

    /**
     * Populates the {@link BaseBean} with request parameters.
     * <p>
     * Child classes must override this method to populate specific bean fields.
     * </p>
     *
     * @param request the {@link HttpServletRequest} object containing user input
     * @return populated {@link BaseBean} or {@code null} if no data found
     */
    protected BaseBean populateBean(HttpServletRequest request) {
        return null;
    }

    /**
     * Populates base DTO fields such as createdBy, modifiedBy, 
     * createdDatetime, and modifiedDatetime.
     *
     * @param dto     the {@link BaseBean} object to populate
     * @param request the {@link HttpServletRequest} containing user and audit info
     * @return the populated {@link BaseBean}
     */
    protected BaseBean populateDTO(BaseBean dto, HttpServletRequest request) {

        String createdBy = request.getParameter("createdBy");
        String modifiedBy = null;

        UserBean userbean = (UserBean) request.getSession().getAttribute("user");

        if (userbean == null) {
            createdBy = "root";
            modifiedBy = "root";
        } else {
            modifiedBy = userbean.getLogin();
            if ("null".equalsIgnoreCase(createdBy) || DataValidator.isNull(createdBy)) {
                createdBy = modifiedBy;
            }
        }

        dto.setCreatedBy(createdBy);
        dto.setModifiedBy(modifiedBy);

        long cdt = DataUtility.getLong(request.getParameter("createdDatetime"));

        if (cdt > 0) {
            dto.setCreatedDatetime(DataUtility.getTimestamp(cdt));
        } else {
            dto.setCreatedDatetime(DataUtility.getCurrentTimestamp());
        }

        dto.setModifiedDatetime(DataUtility.getCurrentTimestamp());

        return dto;
    }

    /**
     * Overrides the default {@link HttpServlet#service(HttpServletRequest, HttpServletResponse)} 
     * method to include custom request handling.
     * 
     * This method:
     * <ul>
     *   <li>Calls {@link #preload(HttpServletRequest)} to load necessary data.</li>
     *   <li>Performs validation before forwarding the request.</li>
     *   <li>Delegates processing to child controllers via {@code super.service()}.</li>
     * </ul>
     * 
     *
     * @param request  the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        preload(request);

        String op = DataUtility.getString(request.getParameter("operation"));

        if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op)
                && !OP_VIEW.equalsIgnoreCase(op) && !OP_DELETE.equalsIgnoreCase(op)
                && !OP_RESET.equalsIgnoreCase(op)) {

            if (!validate(request)) {
                BaseBean bean = (BaseBean) populateBean(request);
                ServletUtility.setBean(bean, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }
        }
        super.service(request, response);
    }

    /**
     * Returns the name of the view (JSP page) associated with this controller.
     * <p>
     * Each child controller must implement this method to specify which view to display.
     * </p>
     *
     * @return the view page name as a {@link String}
     */
    protected abstract String getView();

}
