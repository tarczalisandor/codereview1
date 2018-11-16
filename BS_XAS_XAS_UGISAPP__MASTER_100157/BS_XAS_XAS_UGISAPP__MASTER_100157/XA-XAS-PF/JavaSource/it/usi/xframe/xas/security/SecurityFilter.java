/*
 * Created on Nov 07, 2016
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.usi.xframe.xas.security;

import it.usi.xframe.xas.servlet.ServletParameters;
import it.usi.xframe.xas.servlet.Sms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author C305373
 * @version 1.0
 */
public class SecurityFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	private FilterConfig filterConfig = null;
	
	/**
	* @see javax.servlet.Filter#void ()
	*/
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	* @see javax.servlet.Filter#void (javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	*/
	public void doFilter(
		ServletRequest req,
		ServletResponse resp,
		FilterChain chain)
		throws ServletException, IOException {

		if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse)
		{			
			HttpServletRequest httpRequest = (HttpServletRequest) req;
			HttpServletResponse httpResponse = (HttpServletResponse) resp;
			
			HttpSession session = httpRequest.getSession();
		
			String requestURI = httpRequest.getServletPath();
			if(httpRequest.getQueryString()!=null)
				requestURI = requestURI + "?" + httpRequest.getQueryString();
				
			LoggedUser loggedUser = (LoggedUser) session.getAttribute(ServletParameters.LOGGEDUSER);
			if(loggedUser==null || loggedUser.isAuthenticated()==false)
			{
				// USER NOT LOGGED
				String userId = req.getParameter(ServletParameters.JUSERNAME);
				String userPassword = req.getParameter(ServletParameters.JPASSWORD);
	
				if(userId!=null && userId.equals(ServletParameters.ADMINUSERNAME)
				&& userPassword!=null && userPassword.equals(ServletParameters.ADMINUSERPASSWORD))
				{
					// ADD USER TO SESSION
					LoggedUser user = new LoggedUser(userId, userPassword);
					user.setAuthenticated(true);
					session.setAttribute(ServletParameters.LOGGEDUSER, user);
					// redirect
					String newURI = req.getParameter(ServletParameters.JPAGE);
					logger.debug("REDIRECT to " + newURI);
					req.getRequestDispatcher(newURI).forward(req, resp);
				}
				else
				{			
					// PROPOSE LOGIN TO USER
					resp.setContentType("text/html");
					PrintWriter out = resp.getWriter();
					out.println("<html>");
					out.println("<head>");
					out.println("<title>XAS Login</title>");
					out.println("</head>");
					out.println("<body>");
					out.println("<form action=\"\" method=\"post\">");
					out.println("Username: <input type=\"text\" name=\"" + ServletParameters.JUSERNAME + "\" \\>");
					out.println("Password: <input type=\"password\" name=\"" + ServletParameters.JPASSWORD + "\" \\>");
					out.println("Password: <input type=\"hidden\" name=\"" + ServletParameters.JPAGE + "\" value=\"" + requestURI + "\" \\>");
					out.println("<input type=\"submit\" value=\"Login\">");
					if(userId!=null)
						out.println("<p>wrong username/password</p>");
					out.println("</form>");
					out.println("</body>");
					out.println("</html>");
				}
			}
			else
			{
				// USER LOGGED
				String queryStr = httpRequest.getQueryString();
				if(queryStr!=null && queryStr.indexOf(ServletParameters.LOGOUT +"=yes")!=-1)
				{
					// LOGOUT
					session.removeAttribute(ServletParameters.LOGGEDUSER);
					resp.setContentType("text/html");
					PrintWriter out = resp.getWriter();
					out.println("<html>");
					out.println("<head>");
					out.println("<title>XAS Logout</title>");
					out.println("</head>");
					out.println("<body>");
					out.println("<p>Bye Bye!</p>");
					out.println("</form>");
					out.println("</body>");
					out.println("</html>");
				}
				else
				{
					// NAVIGATE
					chain.doFilter(req, resp);
				}
			}
		}
		else
			throw new ServletException("Login failed");
	}

	/**
	* Method init.
	* @param config
	* @throws javax.servlet.ServletException
	*/
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = filterConfig;
	}

	class RequestWrapper extends HttpServletRequestWrapper {
	
		private String originalDestination, newDestinationAgent;
	
		/*
		 * Constructor
		 */
		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}
	
		/*
		 * 
		 * (non-Javadoc)
		 * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURI()
		 */
		public String getRequestURI() {
			String originalURI = super.getRequestURI();
		
			StringBuffer newURI = new StringBuffer();
		
			newURI.append(originalURI.substring(0, originalURI.indexOf(originalDestination)));
			newURI.append(newDestinationAgent);
			newURI.append(originalURI.substring(originalURI.indexOf(originalDestination) + originalDestination.length(), 
												originalURI.length()));
		
			return newURI.toString();
		}
	
		/**
		 * Change the original destination agent/queue manager set in the request by the
		 * HTTP client (or a previous filter) to a new destination agent/queue manager.
		 * 
		 * @param originalDestination
		 * @param newDestination
		 */
		protected void changeDestinationAgent(String originalDestination, String newDestination) {
			this.originalDestination = originalDestination;
			this.newDestinationAgent = newDestination;
		}
	
	}
}
