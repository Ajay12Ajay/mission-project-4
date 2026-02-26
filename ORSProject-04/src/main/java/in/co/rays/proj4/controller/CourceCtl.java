/**
 * @Author: Ajay Pratap Kerketta
 * @Description: CourceCtl is a Servlet controller responsible for handling
 * operations related to Cource management such as adding, updating, and validating
 * course data. It extends BaseCtl to inherit common controller functionalities
 * like validation and bean population.
 * 
 * @Creation Date: 26-Feb-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CourceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CourceModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CourceCtl", urlPatterns = { "/ctl/CourceCtl" })
public class CourceCtl extends BaseCtl {

    Logger log = Logger.getLogger(CourceCtl.class);

    // ===================== VALIDATE =====================
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.info("CourceCtl validate Method Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("courseCode"))) {
            request.setAttribute("courseCode",
                    PropertyReader.getValue("error.require", "Course Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("courseName"))) {
            request.setAttribute("courseName",
                    PropertyReader.getValue("error.require", "Course Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("courseName"))) {
            request.setAttribute("courseName", "Invalid Course Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("duration"))) {
            request.setAttribute("duration",
                    PropertyReader.getValue("error.require", "Duration"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("courseFee"))) {
            request.setAttribute("courseFee",
                    PropertyReader.getValue("error.require", "Course Fee"));
            pass = false;
        }

        log.info("CourceCtl validate Method Ended");
        return pass;
    }

    // ===================== POPULATE BEAN =====================
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.info("CourceCtl populateBean Method Started");

        CourceBean bean = new CourceBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setCourseCode(DataUtility.getString(request.getParameter("courseCode")));
        bean.setCourseName(DataUtility.getString(request.getParameter("courseName")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        bean.setCourseFee(DataUtility.getString(request.getParameter("courseFee")));

        populateDTO(bean, request);

        log.info("CourceCtl populateBean Method Ended");
        return bean;
    }

    // ===================== DO GET =====================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("CourceCtl doGet Method Started");

        long id = DataUtility.getLong(req.getParameter("id"));
        CourceModel model = new CourceModel();

        if (id > 0) {
            try {
                CourceBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }

        log.info("CourceCtl doGet Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    // ===================== DO POST =====================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("CourceCtl doPost Method Started");

        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));
        CourceModel model = new CourceModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {

            CourceBean bean = (CourceBean) populateBean(req);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Course added successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Course Code already exists", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            CourceBean bean = (CourceBean) populateBean(req);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Course updated successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Course Code already exists", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.COURCE_CTL, req, resp);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.COURCE_LIST_CTL, req, resp);
            return;
        }

        log.info("CourceCtl doPost Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    // ===================== VIEW =====================
    @Override
    protected String getView() {
        return ORSView.COURCE_VIEW;
    }
}