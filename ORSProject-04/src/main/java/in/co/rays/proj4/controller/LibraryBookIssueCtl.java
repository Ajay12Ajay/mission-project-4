/**
 * @Author: Ajay Pratap Kerketta
 * @Description: LibraryBookIssueCtl is a Servlet controller class to manage 
 * Library Book Issue operations. It handles add, update, preload, validation,
 * and view rendering for LibraryBookIssue entities.
 * 
 * @Creation Date: 27-Feb-2026
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
import in.co.rays.proj4.bean.LibraryBookIssueBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.LibraryBookIssueModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * LibraryBookIssueCtl servlet handles CRUD operations and validation
 * for Library Book Issue module.
 */
@WebServlet(name = "LibraryBookIssueCtl", urlPatterns = { "/ctl/LibraryBookIssueCtl" })
public class LibraryBookIssueCtl extends BaseCtl {

    Logger log = Logger.getLogger(LibraryBookIssueCtl.class);

    /**
     * Preload dropdown data (Status)
     */
    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> statusMap = new HashMap<>();

        statusMap.put("Issued", "Issued");
        statusMap.put("Returned", "Returned");
        statusMap.put("Overdue", "Overdue");

        request.setAttribute("statusMap", statusMap);
    }

    /**
     * Validate form input
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.info("LibraryBookIssueCtl validate Method Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("bookId"))) {
            request.setAttribute("bookId",
                    PropertyReader.getValue("error.require", "Book"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("memberId"))) {
            request.setAttribute("memberId",
                    PropertyReader.getValue("error.require", "Member"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("issueDate"))) {
            request.setAttribute("issueDate",
                    PropertyReader.getValue("error.require", "Issue Date"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("issueDate"))) {
            request.setAttribute("issueDate",
                    PropertyReader.getValue("error.date", "Issue Date"));
            pass = false;
        }

        if (!DataValidator.isNull(request.getParameter("returnDate"))
                && !DataValidator.isDate(request.getParameter("returnDate"))) {
            request.setAttribute("returnDate",
                    PropertyReader.getValue("error.date", "Return Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("issuedBy"))) {
            request.setAttribute("issuedBy",
                    PropertyReader.getValue("error.require", "Issued By"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.info("LibraryBookIssueCtl validate Method Ended");
        return pass;
    }

    /**
     * Populate Bean from request
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.info("LibraryBookIssueCtl populateBean Method Started");

        LibraryBookIssueBean bean = new LibraryBookIssueBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setBookId(DataUtility.getLong(request.getParameter("bookId")));
        bean.setMemberId(DataUtility.getLong(request.getParameter("memberId")));
        bean.setIssueDate(DataUtility.getDate(request.getParameter("issueDate")));
        bean.setReturnDate(DataUtility.getDate(request.getParameter("returnDate")));
        bean.setFineAmount(DataUtility.getLong(request.getParameter("fineAmount")));
        bean.setIssuedBy(DataUtility.getString(request.getParameter("issuedBy")));
        bean.setStatus(DataUtility.getString(request.getParameter("status")));

        populateDTO(bean, request);

        log.info("LibraryBookIssueCtl populateBean Method Ended");

        return bean;
    }

    /**
     * Handle GET request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("LibraryBookIssueCtl doGet Method Started");

        long id = DataUtility.getLong(req.getParameter("id"));

        LibraryBookIssueModel model = new LibraryBookIssueModel();

        if (id > 0) {
            try {
                LibraryBookIssueBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }

        log.info("LibraryBookIssueCtl doGet Method Ended");

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handle POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("LibraryBookIssueCtl doPost Method Started");

        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));

        LibraryBookIssueModel model = new LibraryBookIssueModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {

            LibraryBookIssueBean bean =
                    (LibraryBookIssueBean) populateBean(req);

            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage(
                        "Book Issued Successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage(
                        "Book already issued to this member", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_UPDATE.equalsIgnoreCase(op)) {

            LibraryBookIssueBean bean =
                    (LibraryBookIssueBean) populateBean(req);

            try {
                if (id > 0) {
                    model.update(bean);
                }

                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage(
                        "Book Issue Updated Successfully", req);

            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage(
                        "Duplicate Record Found", req);

            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.LIBRARY_BOOK_ISSUE_CTL, req, resp);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(
                    ORSView.LIBRARY_BOOK_ISSUE_LIST_CTL, req, resp);
            return;
        }

        log.info("LibraryBookIssueCtl doPost Method Ended");

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Return View
     */
    @Override
    protected String getView() {
        return ORSView.LIBRARY_BOOK_ISSUE_VIEW;
    }
}