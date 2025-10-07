/**
 * @Author: Ajay Pratap Kerketta
 * @Description: DoctorCtl is a Servlet controller class to manage Doctor operations.
 * It extends BaseCtl to inherit common controller functionality.
 * This controller handles operations such as add, update, preload, validation,
 * and view rendering for Doctor entities in the application.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
import in.co.rays.proj4.controller.ORSView;

/**
 * DoctorCtl servlet handles all CRUD operations and form validations
 * related to Doctor entities.
 */
@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {
	
	Logger log = Logger.getLogger(PatientListCtl.class);

    /**
     * Loads preloaded data into the request scope before the HTML form is displayed.
     * Here, it loads a map of expertise fields for doctors.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> expertiseMap = new HashMap<>();
        expertiseMap.put("Cardiology", "Cardiology");
        expertiseMap.put("Neurology", "Neurology");
        expertiseMap.put("Pulmonology", "Pulmonology");
        expertiseMap.put("Gastroenterology", "Gastroenterology");
        expertiseMap.put("Endocrinology", "Endocrinology");
        expertiseMap.put("Dermatology", "Dermatology");
        expertiseMap.put("Pediatrics", "Pediatrics");
        expertiseMap.put("Oncology", "Oncology");
        expertiseMap.put("Psychiatry", "Psychiatry");
        expertiseMap.put("Orthopedics", "Orthopedics");

        request.setAttribute("experticeMap", expertiseMap);
    }

    /**
     * Validates the form data submitted by the user.
     * 
     * @param request HttpServletRequest object
     * @return true if validation passes; false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	log.info("DoctorCtl validate Method Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dob"))) {
            request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Invalid Mobile No");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("expertise"))) {
            request.setAttribute("expertise", PropertyReader.getValue("error.require", "Expertise"));
            pass = false;
        }
        log.info("DoctorCtl validate Method Ended");
        return pass;
    }

    /**
     * Populates a DoctorBean object with form data from the request.
     * 
     * @param request HttpServletRequest object
     * @return populated DoctorBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.info("DoctorCtl populateBean Method Started");
        DoctorBean bean = new DoctorBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExpertise(DataUtility.getString(request.getParameter("expertise")));

        populateDTO(bean, request);
        log.info("DoctorCtl populateBean Method Ended");
        return bean;
    }

    /**
     * Handles HTTP GET requests. Loads existing doctor details if an ID is provided.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	log.info("DoctorCtl doGet Method Started");
        long id = DataUtility.getLong(req.getParameter("id"));

        DoctorModel model = new DoctorModel();

        if (id > 0) {
            try {
                DoctorBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        log.info("DoctorCtl doGet Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles HTTP POST requests for saving, updating, resetting, or canceling.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	log.info("DoctorCtl doPost Method Started");
    	String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));
        DoctorModel model = new DoctorModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            DoctorBean bean = (DoctorBean) populateBean(req);
            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Doctor added successfully", req);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Doctor already exists", req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            DoctorBean bean = (DoctorBean) populateBean(req);
            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Doctor updated successfully", req);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Doctor already exists", req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
            return;
        }
        log.info("DoctorCtl doPost Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view (JSP page) for the doctor form.
     * 
     * @return JSP page path as String
     */
    @Override
    protected String getView() {
        return ORSView.DOCTOR_VIEW;
    }

}
