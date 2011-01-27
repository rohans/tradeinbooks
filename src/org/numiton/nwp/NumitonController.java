/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: NumitonController.java,v 1.4 2008/10/14 13:29:54 numiton Exp $
 *
 **********************************************************************************/

/**********************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 **********************************************************************************/

/***************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 ***************************************************************************/
package org.numiton.nwp;

import static com.numiton.generic.PhpWeb.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ExitException;
import com.numiton.Misc;
import com.numiton.Options;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.*;
import com.numiton.output.OutputControl;

public abstract class NumitonController implements ContextCarrierInterface, SpringWebPageInterface {
	protected static final Logger	  LOG	          = Logger.getLogger(NumitonController.class.getName());
	public GlobalVars	              gVars;
	public GlobalConsts	              gConsts;

	private Map<String, OutputStream>	__blocks;
	private Map<String, String>	      __blockContents;
	private String	                  prevBlockName;

	public Map<String, String> getOutputBlocks() {
		return __blockContents;
	}

	public String getViewName() {
		return null;
	}
	
	public boolean isBinaryOutput() {
		return false;
	}

	public GlobalVariablesContainer getGlobalVars() {
		return gVars;
	}
	
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		
		__blocks	  = new HashMap<String, OutputStream>();
		__blockContents	= new HashMap<String, String>();
		prevBlockName = null;
		
		StopWatch watch = new StopWatch();
		watch.start();

		ModelAndView modelAndView = null;
		
		try {
			if (!NumitonContextListener.isContextInitialized()) {
				throw new RuntimeException("Context not initialized");
			}

//			javaResponse.setContentType("text/html");

			GlobalVars javaGlobalVariables = (GlobalVars) javaRequest.getAttribute("JAVA_GVARS");

			if (javaGlobalVariables == null) {
				javaGlobalVariables = new GlobalVars();
			}

			GlobalConsts javaGlobalConstants = (GlobalConsts) javaRequest.getAttribute("JAVA_GCONSTS");

			if (javaGlobalConstants == null) {
				javaGlobalConstants = new GlobalConsts();
			}

			javaGlobalVariables.webEnv = new PhpWebEnvironment(javaRequest, javaResponse);

			setContext(javaGlobalVariables, javaGlobalConstants);
			registerResult(gVars, getClass(), DEFAULT_VAL);

			Options.setDefaultTimeLimit(gVars.webEnv);
			ErrorHandling.resetErrorReporting(gVars.webEnv);

			if (StringUtils.isEmpty(getViewName())) {
				OutputControl.setBuffer(gVars.webEnv, javaResponse.getOutputStream());
				gVars.webEnv.setDefaultBuffer(true);
			}

			generateContent(gVars.webEnv);

			modelAndView = doFinalizeBlocks();
		}
		catch (ExitException ex) {
			LOG.debug(ex.getMessage());

			modelAndView = doFinalizeBlocks();
		}
		catch (Throwable th) {
			LOG.error(th, th);

			// if there is a custom error handler defined, use it
			if (ErrorHandling.invokeCustomHandlerAndStop(gVars.webEnv, th.getMessage(), ErrorHandling.E_ERROR)) {
				return null;
			}

			javaResponse.getWriter().write(
			        "<html><body><p>An internal error has occured. Please check the <a href=\"https://sourceforge.net/tracker/?group_id=214436&atid=1029673\">nWP bug tracker</a> or submit a bug there." +
			        "<p>Please also include the stack trace from the <code>nwp_debug.log</code> file from your log directory (e.g. <code>$TOMCAT_HOME/logs/nwp_debug.log</code> if you are running Apache Tomcat)." +
			        "</body></html>");
		}
		finally {
			if (NumitonContextListener.isContextInitialized()) {
				gVars.webEnv.populateRequest();
				ShutdownHandler.executeShutdownHandlers(gVars.webEnv);

				if (OutputControl.hasActiveBuffer(gVars.webEnv)) {
					OutputControl.flush(gVars.webEnv, false/*flush all levels*/, isBinaryOutput());
				}

				Misc.closeResources(gVars.webEnv);
			}

			watch.stop();
			LOG.debug("Request processed in " + watch.getTime() + " ms");
		}

		return modelAndView;
	}

	protected ModelAndView doFinalizeBlocks() {
	    if (!StringUtils.isEmpty(getViewName())) {
	    	finalizeBlocks();

	    	ModelAndView mav = new ModelAndView();
	    	mav.setViewName(getViewName());
	    	mav.addAllObjects(__blockContents);

	    	return mav;
	    }
	    else {
	    	return null;
	    }
    }

	public void finalizeBlocks() {
		if (prevBlockName != null) {
			addPrevBufferContents();
		}
	}

	public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
		gConsts = (GlobalConsts) javaGlobalConstants;
		gVars = (GlobalVars) javaGlobalVariables;
		gVars.gConsts = gConsts;
	}

	public abstract Object generateContent(PhpWebEnvironment phpWebEnv) throws IOException, ServletException;

	public void startBlock(String blockName) {
		if (prevBlockName != null) {
			addPrevBufferContents();
		}

		prevBlockName = blockName;
		getInitializedBlocks().put(blockName, OutputControl.setBuffer(gVars.webEnv, new ByteArrayOutputStream()));
	}

	protected Map<String, OutputStream> getInitializedBlocks() {
		if(__blocks == null) {
			__blocks	  = new HashMap<String, OutputStream>();
		}
		
		return __blocks;
    }

	public void addPrevBufferContents() {
		 String prevBlockContents = OutputControl.ob_get_clean(gVars.webEnv, isBinaryOutput()); 	// Clear
																									// previous
																									// buffer

		if(prevBlockContents.trim().length() == 0) {
			prevBlockContents = "";
		}
		
		getInitializedBlockContents().put(prevBlockName, prevBlockContents);
		OutputStream removedStream = getInitializedBlocks().remove(prevBlockName);
		
		if(removedStream == null) {
			LOG.warn("Cannot remove stream for block " + prevBlockName + " from blocks: " + __blocks);
		}
	}

	protected Map<String, String> getInitializedBlockContents() {
		if(__blockContents == null) {
			__blockContents	= new HashMap<String, String>();
		}
		
		return __blockContents;
    }
}
