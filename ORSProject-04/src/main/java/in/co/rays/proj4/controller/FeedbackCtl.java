/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FeedbackCtl is a Servlet controller responsible for handling
 * operations related to Feedback management such as adding, updating,
 * and validating feedback data. It extends BaseCtl to inherit common
 * controller functionalities like validation and bean population.
 *
 * @Creation Date: 28-Feb-2026
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
import in.co.rays.proj4.bean.FeedbackBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.FeedbackModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "FeedbackCtl", urlPatterns = { "/ctl/FeedbackCtl" })
public class FeedbackCtl extends BaseCtl {

    Logger log = Logger.getLogger(FeedbackCtl.class);

    /**
     * Preload method (optional if needed).
     */
    @Override
    protected void preload(HttpServletRequest request) {
        // If you want rating dropdown:
        // Map<Integer, String> ratingMap = new HashMap<>();
        // ratingMap.put(1, "1");
        // ratingMap.put(2, "2");
        // ratingMap.put(3, "3");
        // ratingMap.put(4, "4");
        // ratingMap.put(5, "5");
        // request.setAttribute("ratingMap", ratingMap);
    }

    /**
     * Validate Feedback form fields.
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.info("FeedbackCtl validate method started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("feedbackCode"))) {
            request.setAttribute("feedbackCode",
                    PropertyReader.getValue("error.require", "Feedback Code"));
            pass = false;
        } else if (!DataValidator.isModuleCode(request.getParameter("feedbackCode"))) {
            request.setAttribute("feedbackCode", "Code must be in format UPPERCASELETTERS-DIGITS (e.g., FC-001)");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("userName"))) {
            request.setAttribute("userName",
                    PropertyReader.getValue("error.require", "User Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("userName"))) {
            request.setAttribute("userName", "Invalid User Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("comments"))) {
            request.setAttribute("comments",
                    PropertyReader.getValue("error.require", "Comments"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("rating"))) {
            request.setAttribute("rating",
                    PropertyReader.getValue("error.require", "Rating"));
            pass = false;
        } else {
            int rating = DataUtility.getInt(request.getParameter("rating"));
            if (rating < 1 || rating > 5) {
                request.setAttribute("rating", "Rating must be between 1 to 5");
                pass = false;
            }
        }

        if (DataValidator.isNull(request.getParameter("feedbackDate"))) {
            request.setAttribute("feedbackDate",
                    PropertyReader.getValue("error.require", "Feedback Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("feedbackDate"))) {
            request.setAttribute("feedbackDate",
                    PropertyReader.getValue("error.date", "Feedback Date"));
            pass = false;
        }

        log.info("FeedbackCtl validate method ended");
        return pass;
    }

    /**
     * Populate FeedbackBean from request.
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.info("FeedbackCtl populateBean method started");

        FeedbackBean bean = new FeedbackBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFeedbackCode(DataUtility.getString(request.getParameter("feedbackCode")));
        bean.setUserName(DataUtility.getString(request.getParameter("userName")));
        bean.setComments(DataUtility.getString(request.getParameter("comments")));
        bean.setRating(DataUtility.getInt(request.getParameter("rating")));
        bean.setFeedbackDate(DataUtility.getDate(request.getParameter("feedbackDate")));

        populateDTO(bean, request);

        log.info("FeedbackCtl populateBean method ended");

        return bean;
    }

    /**
     * Handles GET request (Edit case).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("FeedbackCtl doGet started");

        long id = DataUtility.getLong(req.getParameter("id"));
        FeedbackModel model = new FeedbackModel();

        if (id > 0) {
            try {
                FeedbackBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }

        log.info("FeedbackCtl doGet ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("FeedbackCtl doPost started");

        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));
        FeedbackModel model = new FeedbackModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {

            FeedbackBean bean = (FeedbackBean) populateBean(req);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Feedback added successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Feedback Code already exists", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            FeedbackBean bean = (FeedbackBean) populateBean(req);

            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Feedback updated successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Feedback Code already exists", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.FEEDBACK_CTL, req, resp);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.FEEDBACK_LIST_CTL, req, resp);
            return;
        }

        log.info("FeedbackCtl doPost ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns Feedback view page.
     */
    @Override
    protected String getView() {
        return ORSView.FEEDBACK_VIEW;
    }
}