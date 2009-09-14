/*
 * Copyright (C) 2009 JavaRosa
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.javarosa.core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.javarosa.core.api.Constants;

/**
 * Context is a data storage object which is intended to contain variables
 * pertaining to the state of the currently executing assembly. Ideally,
 * the context maintains enough information to encompass the entire state
 * of the current execution.
 * 
 * The context may be extended to provide hooks for valuable module-based
 * variables. Extended contexts can absorb root concepts using the provided
 * copy constructor, and can absorb context values and report which have 
 * changed using the provided merge function.
 * 
 * @author Clayton Sims
 *
 */
public class Context {

	protected Hashtable contextObject;
	
	/**
	 * Constructs a new, empty context
	 */
	public Context() {
		init();
	}
	
	/**
	 * Copy constructor, creates a duplicate context from
	 * the one given.
	 * 
	 * @param context The context to be copied
	 */
	public Context(Context context) {
		init();
		this.contextObject = context.clone().contextObject;
	}
	
	private void init() {
		contextObject = new Hashtable();
	}
	
	/**
	 * Gets the context value for the current user
	 * 
	 * @return The current user name
	 */
	public String getCurrentUser() {
		return (String)getElement(Constants.USER_KEY);
	}
	
	/**
	 * Sets the context value for the current user
	 * 
	 * @param name The current user name
	 */
	public void setCurrentUser(String name) {
		setElement(Constants.USER_KEY, name);
	}
	
	/**
	 * Sets the context value for the return code of the last
	 * running activity, if this context is being used for
	 * an activity resume.
	 * 
	 * @param key The return code of the last running activity.
	 */
	public void setReturnKey(String key) { 
		setElement(Constants.RETURN_ARG_KEY, key);
	}
	
	/**
	 * Gets the context value for the return code of the last
	 * running activity, if this context is being used for
	 * an activity resume.
	 * 
	 * @return The return code of the last running activity.
	 */
	public String getReturnKey() { 
		return (String)getElement(Constants.RETURN_ARG_KEY);
	}
	
	
	
	/**
	 * Gets the context value for the current user
	 * 
	 * @return The current user id
	 */
	public Integer getCurrentUserID() {
		return (Integer)getElement(Constants.USER_ID_KEY);
	}
	
	/**
	 * Sets the context value for the current user
	 * 
	 * @param id The current user id
	 */
	public void setCurrentUserID(Integer id) {
		setElement(Constants.USER_ID_KEY, id);
	}
	
	/**
	 * Gets the element associated with a key.
	 * 
	 * @param key The value to be retrieved
	 * @return The value, if any, associated with the given key
	 */
	public Object getElement(String key) {
		return contextObject.get(key);
	}
	
	/**
	 * Sets a context value for the given name
	 * 
	 * @param key The name of the context value
	 * @param value The value to be stored
	 */
	public void setElement(String key, Object value) {
		if(value != null) {
			contextObject.put(key, value);
		}
		else {
			//#if debug.output==verbose
			System.out.println("Attempt to add a null context value to key " + key);
			//#endif
		}
	}
	
	/**
	 * Removes a given element from the Context
	 * 
	 * @param key The name of the value
	 */
	public void removeElement(String key) {
		contextObject.remove(key);
	}
	
	/**
	 * Clones the current context and returns a copy of it.
	 * 
	 * @return A new context containing the same values as the current context
	 */
	public Context clone() {
		Context newContext = new Context();
		Enumeration en = this.contextObject.keys();
		while(en.hasMoreElements()) {
			Object key = en.nextElement();
			newContext.contextObject.put(key, contextObject.get(key));
		}
		return newContext;
	}
	
	/**
	 * Places all values that exist in the given context into this context,
	 * overwriting existing values when necessary, and returns a list of key/value
	 * pairs that didn't already exist in the current context. 
	 *      
	 * @param context the context whose values will be written to this context
	 * @return A Vector v, where every key in v exists in context, but either 
	 * doesn't exist in this, or exists in this with a different value
	 */
	public Vector mergeInContext(Context context) {
		Vector differentValues = new Vector();
		Enumeration en = context.contextObject.keys();
		
		while(en.hasMoreElements()) {
			String key = (String)en.nextElement();
			Object value = context.getElement(key);
			
			if(getElement(key) != value) {
				setElement(key,value);
				differentValues.addElement(key);
			}
		}
		
		return differentValues;
	}
	
	/**
	 * Places all values in the given hashtable into the Context
	 *      
	 * @param context the context whose values will be written to this context
	 */
	public void addAllValues(Hashtable values) {
		Enumeration en = values.keys();
		
		while(en.hasMoreElements()) {
			String key = (String)en.nextElement();
			Object value = values.get(key);
			
			if(getElement(key) != value) {
				setElement(key,value);
			}
		}
	}
}