/*******************************************************************************
 * Copyright (c) 2010 Stefano Norcia.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.html
 ******************************************************************************/
package com.joooid.android.xmlrpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.joooid.android.model.User;


public class JoooidRpc {
    private XMLRPCClient rpcClient;
    private String httpUser;
    private String httpPass;
    private String url;
    private int version;
    private static JoooidRpc uniqueInstance = null;

    public JoooidRpc(String url, String httpUser, String httpPwd) {
        rpcClient = new XMLRPCClient(url, httpUser, httpPwd);
        this.httpUser = httpUser;
        this.httpPass = httpPwd;
        this.url = url;
    }

    public JoooidRpc getInstance(String uriString, int jVersion) {
        synchronized (JoooidRpc.class) {

            if (uriString == null) {
                switch (jVersion) {
                    case User.JOOMLA_15:
                        this.version = User.JOOMLA_15;
                        uriString = Constants.TASK_WS_URI_J15;
                        break;

                    case User.JOOMLA_16:
                        this.version = User.JOOMLA_16;
                        uriString = Constants.TASK_WS_URI_J17;
                        break;
                }
            }

            uniqueInstance = new JoooidRpc(this.url + uriString, this.httpUser, this.httpPass);
        }
        return uniqueInstance;
    }

    public String newPost(String catid, String title, String alias, String[] introtext, String fulltext, Integer state, Integer access, Boolean front, String date)
            throws XMLRPCException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = formatter.format(new Date());
        String resp = null;
        switch (this.version) {
            case User.JOOMLA_15:
                return (String) rpcClient.call("joooid.newPost", "key", catid, this.httpUser, this.httpPass, title, alias, introtext, fulltext, state, access, front, date, currentDate);

            case User.JOOMLA_16:
                return (String) rpcClient.call("blogger.newPost", "key", catid, this.httpUser, this.httpPass, title, alias, introtext, fulltext, state, access, front, date, currentDate);

            default:
                return resp;
        }
    }

    public String uploadFile(File aFile, String dir)
            throws XMLRPCException {
        String encodedText = "";
        Map<String, Object> app = new HashMap<String, Object>();
        try {
            InputStream is = new FileInputStream(aFile);
            long length = aFile.length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new XMLRPCException("Could not completely read file "
                        + aFile.getName());
            }
            encodedText = new String(Base64Coder.encode(bytes));
            app.put("name", aFile.getName());
            app.put("bits", encodedText);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (version) {
            case User.JOOMLA_15:
                return (String) rpcClient.call(
                        "joooid.uploadFile",
                        this.httpUser,
                        this.httpPass,
                        dir,
                        app);
            case User.JOOMLA_16:
                return (String) rpcClient.call(
                        "metaWeblog.newMediaObject",
                        0,
                        this.httpUser,
                        this.httpPass,
                        dir,
                        app.put("name", aFile.getName()),
                        encodedText);
            default:
                return null;
        }
    }

	/*public String getResponseString(){
		return rpcClient.responseString;
	}
	
	public String getErrorString(){
		return rpcClient.errorString;
	}
	
	public String getErrorMessage(String err, Context context){

		String errorMessage = err;
		
		if(err.contains(Constants.TASK_WS_CODE_ERROR_XML_RPC)){
			errorMessage = context.getString(R.string.xml_rpc_client_error_fault) + " :\n";
			if (err.contains("Log in was not able to be done")) 
				errorMessage += context.getString(R.string.login_error_user_pass);
			else if (err.contains("Login Failed")) 
				errorMessage += context.getString(R.string.login_error_user_pass);
			else if (err.contains("Unknown method")) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_plugin);
			else if (err.contains("Article must have some content")) 
				errorMessage += context.getString(R.string.new_post_publish_error_text);
			else if (err.contains("Please provide a valid, non-blank title")) 
				errorMessage += context.getString(R.string.new_post_publish_error_title);
		    else if(err.contains("Post check failed"))
		    	errorMessage += context.getString(R.string.new_post_publish_error_check);
			else if(err.contains("Post store failed"))
				errorMessage += context.getString(R.string.new_post_publish_error_store);
			else if(err.contains("Another article from this category has the same alias"))
				errorMessage += context.getString(R.string.new_post_publish_error_alias);
			else if ((err.contains("You do not have the authority to do this operation"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_permission);
			else if ((err.contains("is currently being edited by another user"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_other_user);
			else if ((err.contains("Your folder was not able to be create"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_file_folder);
			else if ((err.contains("Your file was not able to be upload"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_file_upload);
			else if ((err.contains("File size is too big"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_file_size);
			else if ((err.contains("It is a file type that has not been permitted"))) 
				errorMessage += context.getString(R.string.xml_rpc_client_error_fault_file_type);
			else errorMessage += err; 

		} else if (err.contains(Constants.TASK_WS_CODE_ERROR_HTTP_CODE)){
			errorMessage = context.getString(R.string.xml_rpc_client_error_http) + " :\n";
			if (err.contains("404")) errorMessage += "404 Not Found\n" + context.getString(R.string.login_error_resource_not_found);
			else if (err.contains("403")) errorMessage += "403 Forbidden\n" + err;
			else if (err.contains("401")) errorMessage += "401 Authorization required\n" + err;
			else if (err.contains("500")) errorMessage += "500 Internal server error\n" + err;
			else if (err.contains("502")) errorMessage += "502 Bad gateway\n" + err;
			else if (err.contains("300")) errorMessage += "300 Multiple Choices\n" + err;
			else if (err.contains("301")) errorMessage += "301 Moved Permanently\n" + err;
			else if (err.contains("302")) errorMessage += "302 Moved Temporarily\n" + err;
			else if (err.contains("303")) errorMessage += "303 See Other\n" + err;
			else errorMessage += err; 
			
		} else if (err.contains(Constants.TASK_WS_CODE_ERROR_XML_PARSER)){
			errorMessage = context.getString(R.string.xml_rpc_client_error_parser) + " :\n";
			if (err.contains("START_TAG")) errorMessage += "error parsing response from server";
			else errorMessage += err; 

		} else if (err.contains(Constants.TASK_WS_CODE_ERROR_RPC_CLIENT)){
			errorMessage = context.getString(R.string.xml_rpc_client_error_connection) + " :\n";
			if (err.contains("Target host must not be null")) errorMessage += context.getString(R.string.login_error_host_unresolved);
			else if (err.contains("Host name may not be null")) errorMessage += context.getString(R.string.login_error_host_unresolved);
			else if (err.contains("Host is unresolved")) errorMessage += context.getString(R.string.login_error_host_unresolved);
			else if (err.contains("Unable to resolve host")) errorMessage += context.getString(R.string.login_error_host_unresolved);

			else errorMessage += err; 
		}

		return errorMessage;
	
	}
	

	
	@SuppressWarnings("unchecked")
	public User userInfo(String joomlaUser,String joomlaPass) throws XMLRPCException {
	    	    
		switch (version) {
		case User.JOOMLA_15:
		
	    		try{
	    			Map<String, Object> app = (Map<String, Object>)rpcClient.call("joooid.getUserInfo","key",joomlaUser,joomlaPass);
	    			User user = new User( url,
	    					uri,
	    					joomlaUser,
	    					joomlaPass,
	    					httpUser,
	    					httpPass,
	    					version,
	    					app.get("firstname").toString(),
	    					app.get("lastname").toString(),
	    					app.get("email").toString(),
	    					Integer.parseInt(app.get("userid").toString()));
	    			try{ 
	    				String version = (String) app.get("version");
	    				if (!version.equals(Constants.JOOOID_VERSION)){
	    					user.setUpdated(false);
	    				}else user.setUpdated(true);
	    			}catch (Exception e) {
	    				user.setUpdated(false);
					}
	    			
	    			return user;
	    		} catch (XMLRPCException e) {
	    			error = e.toString();
	    			throw e;
	    		} 
	    		
		case User.JOOMLA_16:
			
    		try{
    			Map<String, Object> app = (Map<String, Object>)rpcClient.call("blogger.getUserInfo", "", joomlaUser, joomlaPass);
    			User user = new User( url,
    					uri, 
    					joomlaUser,
    					joomlaPass, 
    					httpUser,
    					httpPass,
    					version,
    					app.get("firstname").toString(),
    					app.get("lastname").toString(),
    					app.get("email").toString(),
    					Integer.parseInt(app.get("userid").toString()));
    			try{ 
    				String version = (String) app.get("version");
    				if (!version.equals(Constants.JOOOID_VERSION)){
    					user.setUpdated(false);
    				}else user.setUpdated(true);
    			}catch (Exception e) {
    				user.setUpdated(false);
				}
    			
    			return user;
    		} catch (XMLRPCException e) {
    			error = e.toString();
    			throw e;
    		} 

		default:
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Category> usersBlogs(String user, String pass) throws XMLRPCException{

		ArrayList<Category> m_cat = new ArrayList<Category>();
		switch (version) {
		case User.JOOMLA_15:
		try{
				Object[] appArray = (Object[])rpcClient.call("joooid.getUsersBlogs","key", user, pass);
				int len = appArray.length;
				for(int i=0;i<len;i++){
					  Map<String, Object> app = (Map<String, Object>)appArray[i];
					  m_cat.add(new Category(app.get("id")!=null ? Integer.parseInt((String)app.get("id")) : null,
								app.get("title")!=null ? app.get("title").toString() : null,
								app.get("alias")!=null ? app.get("alias").toString() : null,
								app.get("path")!=null ? app.get("path").toString() : null,
								app.get("date")!=null ? app.get("date").toString() : null,
								app.get("published")!=null ? Integer.parseInt(app.get("published").toString()) : null,
								app.get("access")!=null ? Integer.parseInt(app.get("access").toString()) : null,
								app.get("parent_id")!=null ? Integer.parseInt(app.get("parent_id").toString()) : null,
								app.get("level")!=null ? Integer.parseInt(app.get("level").toString()) : null,
								app.get("description")!=null ? app.get("description").toString() : null));
				}
				return m_cat;
		} catch (XMLRPCException e) {
			error = e.toString();
			throw e;
		}
		case User.JOOMLA_16:
			try{
				Object[] appArray = (Object[])rpcClient.call("blogger.getUsersBlogs","key",user, pass);
				int len = appArray.length;
				for(int i=0;i<len;i++){
					  Map<String, Object> app = (Map<String, Object>)appArray[i];
					  m_cat.add(new Category(app.get("id")!=null ? Integer.parseInt((String)app.get("id")) : null,
							  	app.get("title")!=null ? app.get("title").toString() : null,
								app.get("alias")!=null ? app.get("title").toString() : null,
  								app.get("path")!=null ? app.get("path").toString() : null,
								app.get("date")!=null ? app.get("date").toString() : null,
								app.get("published")!=null ? Integer.parseInt(app.get("published").toString()) : null,
								app.get("access")!=null ? Integer.parseInt(app.get("access").toString()) : null,
								app.get("parent_id")!=null ? Integer.parseInt(app.get("parent_id").toString()) : null,
								app.get("level")!=null ? Integer.parseInt(app.get("level").toString()) : null,
								app.get("description")!=null ? app.get("description").toString() : null));
				}
				return m_cat;
		} catch (XMLRPCException e) {
			error = e.toString();
			throw e;
		}
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Article> recentPosts(String user, String pass, String catId, String numposts)
			throws XMLRPCException {
		
		ArrayList<Article> m_art = new ArrayList<Article>();
		
		switch (version) {
		case User.JOOMLA_15:
		
		try {
			Object[] appArray = (Object[]) rpcClient.call("joooid.getRecentPosts","key", catId, user, pass, Integer.parseInt(numposts));
			int len = appArray.length;
			for (int i = 0; i < len; i++) {
				Map<String, Object> app = (Map<String, Object>) appArray[i];
				m_art.add(new Article(app.get("userid")!=null ? Integer.parseInt(app.get("userid").toString()) : null, 
						app.get("postid")!=null ? Integer.parseInt(app.get("postid").toString()) : null, 
						Integer.parseInt(catId),
						app.get("title")!=null ? app.get("title").toString() : null,
						app.get("alias")!=null ? app.get("alias").toString() : null,
						app.get("description")!=null ? app.get("description").toString() : null,
						app.get("text_more")!=null ? app.get("text_more").toString() : null,
						app.get("date")!=null ? app.get("date").toString() : null,
						app.get("state")!=null ? Integer.parseInt(app.get("state").toString()) : null,
						app.get("access")!=null ? Integer.parseInt(app.get("access").toString()) : null,
						app.get("frontpage")!=null ? Boolean.parseBoolean(app.get("frontpage").toString()) : null));
			}
			return m_art;
		} catch (XMLRPCException e) {
			error = e.toString();
			throw e;
		}
		case User.JOOMLA_16:
			try {
				Object[] appArray = (Object[]) rpcClient.call("blogger.getRecentPosts","key", catId, user, pass, Integer.parseInt(numposts));
				int len = appArray.length;
				for (int i = 0; i < len; i++) {
					Map<String, Object> app = (Map<String, Object>) appArray[i];
					m_art.add(new Article(app.get("userid")!=null ? Integer.parseInt(app.get("userid").toString()) : null, 
							app.get("postid")!=null ? Integer.parseInt(app.get("postid").toString()) : null, 
							Integer.parseInt(catId),
							app.get("title")!=null ? app.get("title").toString() : null,
							app.get("alias")!=null ? app.get("alias").toString() : null,
							app.get("description")!=null ? app.get("description").toString() : null,
							app.get("text_more")!=null ? app.get("text_more").toString() : null,
							app.get("date")!=null ? app.get("date").toString() : null,
							app.get("state")!=null ? Integer.parseInt(app.get("state").toString()) : null,
							app.get("access")!=null ? Integer.parseInt(app.get("access").toString()) : null,
							app.get("frontpage")!=null ? Boolean.parseBoolean(app.get("frontpage").equals("1") ? "true" : "false") : null));
				}
				return m_art;
			} catch (XMLRPCException e) {
    			error = e.toString();
				throw e;
			}
			
		default:
			return null;
		}
	}



	public String uploadImage(String user, String pass, String strFile, String base64, String dir, String blogId) throws XMLRPCException{

		Map<String, Object> app = new HashMap<String, Object>();
		
		String res;

		switch (version) {
			case User.JOOMLA_15:
				app.put("name", strFile);
				app.put("bits", base64);
				res = (String) rpcClient.call("joooid.uploadImage", user, pass,
						"/images/" + dir, app);
				return res;
				
			case User.JOOMLA_16:
				res = (String) rpcClient.call("metaWeblog.newMediaObject",
						blogId, user, pass, dir, strFile, base64);
				return res;

			default: 
				return null;
			}
		
	}


	
	public Boolean editPost (String user, String pass, String postid, String catId, String title, String alias, String introtext, String fulltext, Integer state, Integer access, Boolean front, String date) throws XMLRPCException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = formatter.format(new Date());
		String resp;
		switch (version) {
			case User.JOOMLA_15:
				resp = (String)rpcClient.call("joooid.editPost","key", postid, catId, user, pass, title, alias, introtext, fulltext, state, access, front, date, currentDate);
				if (resp.equals("true")) return true;
				else return false;

			case User.JOOMLA_16:
				resp = (String)rpcClient.call("blogger.editPost","key", postid, catId, user, pass, title, alias, introtext, fulltext, state, access, front, date, currentDate);
				if (resp.equals("true")) return true;
				else return false;

			default:
			 return null;
			
		}
	}
	
public String newCategory (String user, String pass, String title, String alias, String description, Integer parentId, Integer state, Integer access) 
			throws XMLRPCException{
			String resp = null;
			switch (version) {
				case User.JOOMLA_15:
					return resp = (String)rpcClient.call("blogger.newCategory", "key", user, pass, title, alias, description, parentId, state, access, "", "*", 0);
			
			case User.JOOMLA_16:
					return resp = (String)rpcClient.call("blogger.newCategory", "key", user, pass, title, alias, description, parentId, state, access, "", "*", 0);
			default:
				return resp;
			}
		}
	
public Boolean editCategory (String user, String pass, String title, String alias, String description, Integer parentId, Integer state, Integer access, Integer id) throws XMLRPCException{
		
		String resp;
		switch (version) {
			case User.JOOMLA_15:
				resp = (String)rpcClient.call("blogger.newCategory", "key", user, pass, title, alias, description, parentId, state, access, "", "*", id);
				if (resp.equals("true")) return true;
				else return false;

			case User.JOOMLA_16:
				resp = (String)rpcClient.call("blogger.newCategory", "key", user, pass, title, alias, description, parentId, state, access, "", "*", id);
				if (resp.equals("true")) return true;
				else return false;

			default:
			 return null;
			
		}
	}
	
	public Boolean trashPost (String user, String pass, String postId, String state) throws XMLRPCException{
		
		switch (version) {
		case User.JOOMLA_15:
			
		try{
			Boolean resp = (Boolean)rpcClient.call("joooid.trashPost","key", postId, user, pass, true);
			return resp;
		} catch (XMLRPCException e) {
			error = e.toString();
			return false;
		}
		case User.JOOMLA_16:
			try{
				Boolean resp = (Boolean)rpcClient.call("blogger.updatePost","key", postId, user, pass, state);
				return resp;
			} catch (XMLRPCException e) {
    			error = e.toString();
				return false;
			}
			
		default:
			return null;
		}
	}

	public Boolean publishPost (String user, String pass, String postId, String state) throws XMLRPCException{
		
		switch (version) {
		case User.JOOMLA_15:
		
		try{
						
			Boolean resp = (Boolean)rpcClient.call("joooid.publishPost","key", postId, user, pass, true);
			return resp;
		} catch (XMLRPCException e) {
			error = e.toString();
			return false;
		}
		
		case User.JOOMLA_16:
			
			try{
				
				Boolean resp = (Boolean)rpcClient.call("blogger.updatePost","key", postId, user, pass, state);
				return resp;
			} catch (XMLRPCException e) {
    			error = e.toString();
				return false;
			}
			
		default: 
			return null;
			
		}	
	}

	public Boolean unpublishPost (String user, String pass, String postId, String state) throws XMLRPCException{
		
		switch (version) {
		case User.JOOMLA_15:
		
		try{
			Boolean resp = (Boolean)rpcClient.call("joooid.unpublishPost","key", postId, user, pass, true);
			return resp;
		} catch (XMLRPCException e) {
			error = e.toString();
			return false;
		}
		case User.JOOMLA_16:
			try{
				Boolean resp = (Boolean)rpcClient.call("blogger.updatePost","key", postId, user, pass, state);
				return resp;
			} catch (XMLRPCException e) {
    			error = e.toString();
				return false;
			}	
			
		default:
			return null;
		}
	}

	public String getError() {
		return error;
	}*/

}


