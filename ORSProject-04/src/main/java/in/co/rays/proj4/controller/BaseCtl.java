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

	/*
	 * Variables
	 * 
	 * UseCase :
	 * 
	 * 1. We have created constants for all the commonly used buttons
	 * 
	 * Advantage :
	 * 
	 * 1. this ways removes the chances of Spelling mistakes. 2. Make changes on one
	 * place effect everywhere.
	 */

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

	public static final String MSG_SUCCESS = "success";

	public static final String MSG_ERROR = "error";

	/*
	 * validate Methods
	 * 
	 * UseCase :
	 * 
	 * 1. its use to validate input data entered by users. 2. Child class override
	 * only when form is submitted. 3. HttpServletRequest request is used as
	 * parameter because we get data from request also set data in the request. 4.
	 * Return true When nothing to validate Or Data is validated else false.
	 * 
	 * Advantage :
	 * 
	 * 1. Also take care of the format of data entered by users. 2. Reduce
	 * hard-coding of code
	 * 
	 */

	protected boolean validate(HttpServletRequest request) {
		return true;
	}

	/*
	 * preload method
	 * 
	 * UseCase :
	 * 
	 * 1. Loads the Pre-loaded data at the time of html form loading. 2.
	 * HttpServletRequest request is used as parameter because we set data in the
	 * request and needs nothing in return so used void as return type.
	 * 
	 * Advantage :
	 * 
	 * 1. its loads the data before page is loaded. 2. it give us ablity to get the
	 * data dynamically(From the Database) as well as statically( inside the jsp
	 * page ) 3. it use HTMLUtility String getList(String name, String selectedVal,
	 * List list) method to set the data dynamically 4. it use HTMLUtility String
	 * getList(String name, String selectedVal, HashMap<String, String> map) method
	 * to set the data statically( inside the jsp page )
	 * 
	 */

	protected void preload(HttpServletRequest request) {
	}

	/*
	 * populateBean
	 * 
	 * UseCase :
	 * 
	 * 1. Get the data from request and set in BaseBean and return the BaseBean
	 * object 2. When no data is available return null
	 * 
	 * Advantage :
	 * 
	 * 1.
	 */

	protected BaseBean populateBean(HttpServletRequest request) {
		return null;
	}

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

	/*
	 * service method
	 * 
	 * UseCase :
	 * 
	 * 1. to perform some task everytime 2.
	 * 
	 * 
	 * Advantage :
	 * 
	 * 1. Runs everytime when any Servlet called
	 * 
	 */

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		preload(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		if (DataValidator.isNotNull(op) && !OP_CANCEL.equalsIgnoreCase(op) && !OP_VIEW.equalsIgnoreCase(op)
				&& !OP_DELETE.equalsIgnoreCase(op) && !OP_RESET.equalsIgnoreCase(op)) {

			if (!validate(request)) {
				BaseBean bean = (BaseBean) populateBean(request);
				ServletUtility.setBean(bean, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		}
		super.service(request, response);
	}

	/*
	 * getView
	 * 
	 * UseCase :
	 * 
	 * 1. All Child class must override this method
	 * 
	 * 
	 * Advantage :
	 * 
	 * 1. return name of CurrentView
	 * 
	 */

	protected abstract String getView();

}
