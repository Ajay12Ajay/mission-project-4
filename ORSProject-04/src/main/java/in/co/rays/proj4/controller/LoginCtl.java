/**
 * @Author: Ajay Pratap Kerketta
 * @Description: LoginCtl is a Servlet controller responsible for handling
 * user authentication, login, logout, and redirecting users based on their roles.
 * It extends BaseCtl to inherit common controller functionalities like validation
 * and bean population.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
import in.co.rays.proj4.controller.ORSView;

/**
 * Servlet implementation class LoginCtl.
 * Handles login, logout, and redirects users based on authentication status.
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl {

    public static final String OP_SIGN_IN = "Sign In";
    public static final String OP_SIGN_UP = "Sign Up";
    public static final String OP_LOG_OUT = "Logout";

    /**
     * Validates user input during login.
     * 
     * @param request HttpServletRequest object containing form data
     * @return true if inputs are valid; false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        boolean pass = true;
        String op = request.getParameter("operation");

        if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
            return pass;
        }

        if (DataValidator.isNull(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
            pass = false;
        } else if (!DataValidator.isEmail(request.getParameter("login"))) {
            request.setAttribute("login", PropertyReader.getValue("error.email", "Login"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("password"))) {
            request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
            pass = false;
        }

        return pass;
    }

    /**
     * Populates a UserBean from the login form request.
     * 
     * @param request HttpServletRequest object containing form data
     * @return populated UserBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        UserBean bean = new UserBean();
        bean.setLogin(DataUtility.getString(request.getParameter("login")));
        bean.setPassword(DataUtility.getString(request.getParameter("password")));
        return bean;
    }

    /**
     * Handles HTTP GET requests for login and logout operations.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String op = DataUtility.getString(req.getParameter("operation"));

        if (OP_LOG_OUT.equals(op)) {
            session.invalidate();
            ServletUtility.setSuccessMessage("Logout Successful!", req);
            ServletUtility.forward(getView(), req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles HTTP POST requests for signing in or redirecting to registration.
     * Authenticates users and sets session attributes.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String op = DataUtility.getString(req.getParameter("operation"));
        HttpSession session = req.getSession();

        UserModel model = new UserModel();
        RoleModel role = new RoleModel();

        if (OP_SIGN_IN.equalsIgnoreCase(op)) {
            UserBean bean = (UserBean) populateBean(req);

            try {
                bean = model.authenticate(bean.getLogin(), bean.getPassword());

                if (bean != null) {
                    session.setAttribute("user", bean);

                    RoleBean rolebean = role.findByPk(bean.getRoleId());
                    if (rolebean != null) {
                        session.setAttribute("role", rolebean.getName());
                    }

                    String uri = (String) req.getParameter("uri");

                    if (uri == null || "null".equalsIgnoreCase(uri)) {
                        ServletUtility.redirect(ORSView.WELCOME_CTL, req, resp);
                        return;
                    } else {
                        ServletUtility.redirect(uri, req, resp);
                        return;
                    }

                } else {
                    bean = (UserBean) populateBean(req);
                    ServletUtility.setBean(bean, req);
                    ServletUtility.setErrorMessage("Invalid login Id and Password", req);
                }

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_SIGN_UP.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, req, resp);
            return;
        }

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the login view page path.
     * 
     * @return JSP page path as String
     */
    @Override
    protected String getView() {
        return ORSView.LOGIN_VIEW;
    }

}
