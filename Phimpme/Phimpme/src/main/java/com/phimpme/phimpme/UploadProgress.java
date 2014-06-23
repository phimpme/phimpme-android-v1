package com.phimpme.phimpme;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.wordpress.android.AddCatagory;
import org.wordpress.android.models.MediaFile;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class UploadProgress extends Activity {
    Context ctx;
    Bundle data;
    LinearLayout lytUploadProgress;

    public static String[] account_id;
    public static String[] account_name;
    static String[] account_service;
    //static String MAINTAG = "MBM";
    //static String DRUPALTAG = "MBM.Drupal";
    static String imagelist;
    static ArrayList<Integer> checked_ids = new ArrayList<Integer>();
    //static ArrayList<ProgressBar> progressbar_array = new ArrayList<ProgressBar>();
    static String[] path;
    //static final String default_tag_separate = ",";
    private static int progressStatus = 0, progressStatus1 = 0;
    private ConnectivityManager mSystemService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ctx = this;
        mSystemService = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        data = getIntent().getExtras();
        account_id = data.getStringArray("id");
        account_service = data.getStringArray("service");
        account_name = data.getStringArray("name");
        imagelist = data.getString("imagelist");
        path = imagelist.split("#");
        new UploadProgressAsyncTask().execute(0);
    }

    @Override
    public void onBackPressed() {
        try {
            checked_ids.clear();
            lytUploadProgress.removeAllViews();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UploadProgressAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        private int pos;
        //long totalSize;
        ResponseHandler<String> res = new BasicResponseHandler();
        /*DrupalItem acc = null;
        String session_id = "";
        String session_name = "";
        String service_url = "";*/

        /* Login */
        /*protected String login(String username, String password) {
            try {
                String url = service_url + "user/login";
                HttpPost post = new HttpPost(url);
                HttpClient client = new DefaultHttpClient();*/
                /* Why does each subroutines only works with its own HttpClient and not share HttpClient
				 * with each other? */

                /*MultipartEntity multi = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multi.addPart("username", new StringBody(username));
                multi.addPart("password", new StringBody(password));

                post.setEntity(multi);

                String httpResponse = client.execute(post, res);

                Log.d(DRUPALTAG, "Login step: ");
                Log.v(DRUPALTAG, httpResponse);

                Log.d(DRUPALTAG, "Create JSONObject");
                JSONObject json = new JSONObject(httpResponse);

                session_id = json.getString("sessid");
                session_name = json.getString("session_name");
                String userId = json.getJSONObject("user").getString("uid");
                Log.d(DRUPALTAG, "OK! Logged in");
                return userId;
            } catch (Exception e) {
                Log.e(DRUPALTAG, "login: " + e.toString());
                e.printStackTrace();
                return "";
            }
        }*/

        /* Retrieve the nodes owned by user */
        /*protected String[] getOwnNodes(String uid) {
            String[] list = null;
            String url = "";
            if (uid != "") {
                url = service_url + "own_nodes.json";
            } else {
                url = service_url + "own_nodes/" + uid + ".json";
            }
            try {
                Log.d(DRUPALTAG, "url: " + url);
                HttpGet get = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();

                //Authorize
                get.setHeader("Cookie", session_name + "=" + session_id);
                Log.d(DRUPALTAG, "Cookie: " + session_name + "=" + session_id);

                String textresponse = client.execute(get, res);
                Log.d(DRUPALTAG, "getOwnNodes: textresponse: " + textresponse);
                JSONArray jsonResult = new JSONArray(textresponse);
                int n = jsonResult.length();
                if (n == 0) {
                    return null;
                }
                Log.d(DRUPALTAG, "n: " + Integer.toString(n));
                list = new String[n];
                for (int i = 0; i < n; i++) {
                    list[i] = jsonResult.getJSONObject(i).getString("nid");
                }
                Log.d(DRUPALTAG, "getOwnNodes: List length: " + Integer.toString(list.length));
                return list;
            } catch (Exception e) {
                Log.e(DRUPALTAG, "getOwnNodes: " + e.toString());
                e.printStackTrace();
                return list;
            }
        }*/

        /* Count the photos in the node */
        /*protected int countMedia(String nodeId) throws Exception {
            String service_url = acc.getSerivceURL();
            int total = 0;

            try {
                String url = service_url + "node/" + nodeId + ".json";
                HttpGet get = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();

                //Authorize
                get.setHeader("Cookie", session_name + "=" + session_id);

                String textresponse = client.execute(get, res);
                JSONObject jsonResult = new JSONObject(textresponse);
                JSONArray medias = jsonResult.getJSONObject("media_gallery_media").getJSONArray("und");
                total = medias.length();
            } catch (Exception e) {
                Log.e(DRUPALTAG, "countMedia: Failed to count the photos.");
                e.printStackTrace();
                throw e;
            }
            return total;
        }*/

        /* Upload photo */
        /*@SuppressWarnings("resource")
        protected String uploadPhoto(String path) {
            String fileId = "";
            try {
                String url = service_url + "file";
                File f = new File(path.split(";")[0]);
                HttpPost post = new HttpPost(url);
                HttpClient client = new DefaultHttpClient();

                Log.d("Hon", path);
                //Authorize
                post.setHeader("Cookie", session_name + "=" + session_id);

                //Get imagedata
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(f);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                        baos.write(buf, 0, readNum);
                    }
                } catch (IOException ex) {

                }
                byte[] b = baos.toByteArray();
                baos.flush();
                baos.close();
                baos = null;
                String img_data_b64 = Base64.encodeToString(b, Base64.DEFAULT);
                CustomMultiPartEntity multi = new CustomMultiPartEntity(new ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                multi.addPart("file", new StringBody(img_data_b64));
                File jpeg = new File(path);
                String filename = jpeg.getName();
                Log.i("Danh-UploadProgress", "Filename : " + filename);
                multi.addPart("filename", new StringBody(filename));
                totalSize = multi.getContentLength();
                Log.d("Size", String.valueOf(totalSize));
                post.setEntity(multi);
                String httpResponse = client.execute(post, res);
                Log.d("drupal", "Upload file step: ");
                Log.d("drupal", "httpResponse : " + httpResponse);
                img_data_b64 = "";
                JSONObject json = new JSONObject(httpResponse);
                Log.d("Hon", json.toString());
                // The file ID, will be used for attaching the file to a node.
                fileId = json.getString("fid");
                return fileId;
            } catch (Exception e) {
                Log.e(DRUPALTAG, "uploadPhoto");
                e.printStackTrace();
                return fileId;
            }
        }*/

        /* Attached the just-uploaded photo to node */
        /*protected Boolean attachPhoto(String fileId, String nodeId, int newIndex) {
            try {
                String url = service_url + "node/" + nodeId + ".json";

                HttpPut put = new HttpPut(url);
                HttpClient client = new DefaultHttpClient();

                //Authorize
                put.setHeader("Cookie", session_name + "=" + session_id);
                Log.d("Hon", session_id.toString());
                // Add your data
                String param = "media_gallery_media[und][" + Integer.toString(newIndex) + "][fid]";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(param, fileId));
                nameValuePairs.add(new BasicNameValuePair("type", "media_gallery"));
                put.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.d(MAINTAG, param + fileId);

                String httpResponse = client.execute(put, res);

                Log.d("drupal", "Update node: ");
                Log.v("drupal", httpResponse);
                return true;
            } catch (Exception e) {
                Log.e(DRUPALTAG, "attachPhoto");
                e.printStackTrace();
                return false;
            }
        }*/

        /* Log out */
        /*protected Boolean logout() {
            try {
                String url = service_url + "user/logout";
                HttpPost post = new HttpPost(url);
                HttpClient client = new DefaultHttpClient();

                //Authorize
                post.setHeader("Cookie", session_name + "=" + session_id);

                String httpResponse = client.execute(post, res);
                Log.d("drupal", "Log out step: ");
                Log.v("drupal", httpResponse);
                return true;
            } catch (Exception e) {
                Log.e(DRUPALTAG, "logout");
                e.printStackTrace();
                return false;
            }
        }*/

        @SuppressWarnings("deprecation")
        @Override
        protected Boolean doInBackground(Integer... params) {
            Boolean result = false;
            pos = params[0];
            String _s = account_service[pos].toLowerCase();

            /*if (DrupalServices.title.toLowerCase().equals(_s)) {
                acc = DrupalItem.getItem(ctx, account_id[pos]);
                String username = acc.getUsername();
                String password = acc.getPassword();
                service_url = acc.getSerivceURL();
				
				// Login
                for (int i = 0; i < (path.length); i++) {
                    String userId = login(username, password);
                    if (userId == "") {
                        return false;
                    }
				
				// Get list of nodes
                    String[] list = getOwnNodes(userId);
                    if (list == null) {
                        return false;
                    }
				// Choose the first list
                    String nodeId = list[0];
                    if (nodeId == "") {
                        return false;
                    }
                    // The index for the new photo is determined by the amount of existing photos
                    int newIndex;
                    try {

                        newIndex = countMedia(nodeId);


                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                    String fileId = uploadPhoto(path[i]);
                    if (fileId == "") {
                        return false;
                    }

                    Boolean attached = attachPhoto(fileId, nodeId, newIndex);
                    if (!attached) {
                        return false;
                    }
                    logout();
                }

                result = true;
            }else if ("wordpressdotcom".equals(_s)) {
                if (mSystemService.getActiveNetworkInfo() == null) {
                    return false;
                } else {

                    for (int i = 0; i < path.length; i++) {
                        progressStatus = 0;
                        String content = "";
                        int featuredImageID = -1;
                        MediaFile mf = new MediaFile();

                        WordpressItem acc = WordpressItem.getItem(ctx, account_id[pos]);
                        String username = acc.getUsername();
                        String password = acc.getPassword();
                        String url = acc.getUrl();
                        //progress bar
                        new Thread(new Runnable() {
                            public void run() {
                                while (progressStatus < 100) {
                                    try {
                                        myHandle.sendMessage(myHandle.obtainMessage());
                                        Thread.sleep(100);

                                    } catch (Throwable t) {
                                    }
                                }
                            }
                        }).start();

                        // check catagory and set phimpme is default catagory
                        AddCatagory add = new AddCatagory();
                        add.getCategories(url, username, password);

                        String[] theCategories = null;
                        theCategories = new String[1];
                        theCategories[0] = "phimpme mobile";

                        XMLRPCClient client = new XMLRPCClient(url, "", "");
                        //create temp file for media upload
                        String tempFileName = "wp-" + System.currentTimeMillis();

                        String imagepath = path[i].split(";")[0];
                        try {
                            ctx.openFileOutput(tempFileName, Context.MODE_PRIVATE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        File tempFile = ctx.getFileStreamPath(tempFileName);

                        //=================get link upload==================================

                        mf.setFilePath(imagepath);
                        Map<String, Object> m = new HashMap<String, Object>();
                        File jpeg = new File(imagepath);
                        String filename = jpeg.getName();
                        Log.d("UploadProgress", "filename : " + filename);
                        m.put("name", filename);
                        m.put("type", "image/jpeg");
                        m.put("bits", mf);
                        m.put("overwrite", true);

                        Object[] params1 = {1, username, password, m};
                        Object result1 = null;

                        try {
                            result1 = (Object) client.callUploadFile("wp.uploadFile", params1, tempFile);
                            Log.d("UploadProgress", "result : " + result1.toString());
                        } catch (XMLRPCException e) {
                            e.printStackTrace();
                        }

                        //=================content to upload=================================
                        HashMap<?, ?> contentHash = new HashMap<Object, Object>();

                        contentHash = (HashMap<?, ?>) result1;

                        String resultURL = contentHash.get("url").toString();


                        try {
                            if (contentHash.get("id") != null) {
                                featuredImageID = Integer.parseInt(contentHash.get("id").toString());
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        String alignmentCSS = "class=\"" + "alignnone" + "\" ";
                        if (resultURL != null) {
                            content = content
                                    + "<a href=\""
                                    + resultURL
                                    + "\"><img title=\""
                                    + mf.getTitle() + "\" "
                                    + alignmentCSS
                                    + "alt=\"image\" src=\""
                                    + resultURL + "\" /></a>";

                        }

                        //========start upload photo to wordpress====================
                        Map<String, Object> contentStruct = new HashMap<String, Object>();
                        contentStruct.put("wp_post_format", "standard");
                        contentStruct.put("post_type", "post");
                        contentStruct.put("title", "");
                        contentStruct.put("wp_password", "");
                        contentStruct.put("description", content);
                        contentStruct.put("mt_keywords", "");
                        contentStruct.put("categories", theCategories);
                        contentStruct.put("mt_excerpt", "");
                        contentStruct.put("post_status", "publish");

                        if (featuredImageID != -1)
                            contentStruct.put("wp_post_thumbnail", featuredImageID);

                        Object[] params2;
                        params2 = new Object[]{1, username, password, contentStruct, false};
                        XMLRPCClient client1 = new XMLRPCClient(url, "", "");

                        try {
                            Object result3 = null;
                            result3 = (Object) client1.call("metaWeblog.newPost", params2);
                            Log.d("UploadProgress", "result upload : " + result3.toString());
                            result = true;

                        } catch (final XMLRPCException e) {
                            e.printStackTrace();
                        }


                    }

                }
            } else*/ if ("wordpress".equals(_s)) {
                if (mSystemService.getActiveNetworkInfo() == null) {
                    return false;
                } else {
                    for (int i = 0; i < path.length; i++) {
                        progressStatus1 = 0;
                        String content = "";
                        int featuredImageID = -1;
                        MediaFile mf = new MediaFile();


                        String username = data.getString("userName");
                        String password = data.getString("passWord");
                        String url = data.getString("userUrl");

                        AddCatagory add = new AddCatagory();
                        add.getCategories(url, username, password);

                        String[] theCategories = null;
                        theCategories = new String[1];
                        theCategories[0] = "phimpme mobile";

                        XMLRPCClient client = new XMLRPCClient(url, "", "");
                        //create temp file for media upload
                        String tempFileName = "wp-" + System.currentTimeMillis();

                        String imagepath = path[i].split(";")[0];
                        try {
                            ctx.openFileOutput(tempFileName, Context.MODE_PRIVATE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        File tempFile = ctx.getFileStreamPath(tempFileName);
                        mf.setFilePath(imagepath);

                        Map<String, Object> m = new HashMap<String, Object>();
                        File jpeg = new File(imagepath);
                        String filename = jpeg.getName();
                        Log.d("UploadProgress", "filename : " + filename);
                        m.put("name", filename);
                        m.put("type", "image/jpeg");
                        m.put("bits", mf);
                        m.put("overwrite", true);

                        Object[] params1 = {1, username, password, m};
                        Object result1 = null;

                        try {
                            result1 = (Object) client.callUploadFile("wp.uploadFile", params1, tempFile);
                            Log.d("UploadProgress", "result : " + result1.toString());
                        } catch (XMLRPCException e) {
                            e.printStackTrace();
                        }
                        HashMap<?, ?> contentHash = new HashMap<Object, Object>();

                        contentHash = (HashMap<?, ?>) result1;

                        String resultURL = contentHash.get("url").toString();
                        System.out.println("aaaaaaaaaa " + resultURL);

                        try {
                            if (contentHash.get("id") != null) {
                                featuredImageID = Integer.parseInt(contentHash.get("id").toString());
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        String alignmentCSS = "class=\"" + "alignnone" + "\" ";
                        if (resultURL != null) {
                            content = content
                                    + "<a href=\""
                                    + resultURL
                                    + "\"><img title=\""
                                    + mf.getTitle() + "\" "
                                    + alignmentCSS
                                    + "alt=\"image\" src=\""
                                    + resultURL + "\" /></a>";

                        }

                        Map<String, Object> contentStruct = new HashMap<String, Object>();
                        contentStruct.put("wp_post_format", "standard");
                        contentStruct.put("post_type", "post");
                        contentStruct.put("title", "");
                        contentStruct.put("wp_password", "");
                        contentStruct.put("description", content);
                        contentStruct.put("mt_keywords", "");
                        contentStruct.put("categories", theCategories);
                        contentStruct.put("mt_excerpt", "");
                        contentStruct.put("post_status", "publish");

                        if (featuredImageID != -1)
                            contentStruct.put("wp_post_thumbnail", featuredImageID);

                        Object[] params2;
                        params2 = new Object[]{1, username, password, contentStruct, false};
                        XMLRPCClient client1 = new XMLRPCClient(url, "", "");

                        try {
                            Object result3 = null;
                            result3 = (Object) client1.call("metaWeblog.newPost", params2);
                            Log.d("UploadProgress", "result upload : " + result3.toString());
                            result = true;

                        } catch (final XMLRPCException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } /*else if ("joomla".equals(_s)) {
                if (mSystemService.getActiveNetworkInfo() == null) {
                    return false;
                } else {
                    JoomlaItem user = JoomlaItem.getItem(ctx, account_id[pos]);
                    for (int i = 0; i < path.length; i++) {
                        JoooidRpc rpcClient = JoooidRpc.getInstance(user.getUrl(), Constants.TASK_WS_URI_J17, user.getUsername(), user.getPassword(), User.JOOMLA_16);

                        try {
                            File mFile = new File(path[i].split(";")[0]);
                            Date mDate = new Date();
                            String name = String.valueOf(mDate.getTime()) + "_" + mFile.getName();
                            if (path[i].split(";").length == 2) {
                                JSONObject js = new JSONObject(path[i].split(";")[1]);
                                //String lat = js.getString("lati");
                                //tring logi = js.getString("logi");
                                name = js.getString("name");
                            }
                            pb.setProgress(0);
                            pb.setProgress(100);
                            name = name.replaceAll("\\s", "");
                            String imgUrl = rpcClient.uploadFile(user.getUsername(), user.getPassword(), name, mFile, "phimpme");
                            String fulltext = "<p><img src=images/phimpme/" + name + " /></p>";
                            Log.e("Image Url", imgUrl);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String currentDate = formatter.format(mDate);
                            String res = rpcClient.newPost(user.getUsername(), user.getPassword(), user.getCatId(), name, name, "Content has create by phimpme app", fulltext, 1, 1, true, currentDate);
                            try {
                                Integer.parseInt(res);
                                result = true;
                            } catch (NumberFormatException e) {
                                result = false;
                            }
                        } catch (com.joooid.android.xmlrpc.XMLRPCException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }*/
            return result;
        }

        /*@Override
        protected void onProgressUpdate(Integer... progress) {

            int _p = (int) progress[0];
            tvPC.setText(_p + "...%");
            pb.setProgress(_p);
        }*/

        /*@Override
        protected void onPostExecute(Boolean b) {
            tvPC.setVisibility(View.GONE);

            if (b) {
                ivOK.setVisibility(View.VISIBLE);
                ivFail.setVisibility(View.GONE);
            } else {
                ivOK.setVisibility(View.GONE);
                ivFail.setVisibility(View.VISIBLE);
            }
        }

        Handler myHandle = new Handler() {
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                progressStatus++;
                if (progressStatus < 100) {
                    tvPC.setText(progressStatus + "...%");
                } else {
                    tvPC.setText("100%");
                }
                pb.setProgress(progressStatus);

            }
        };
        Handler myHandle1 = new Handler() {
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                progressStatus1++;
                if (progressStatus1 < 100) {
                    tvPC.setText(progressStatus1 + "...%");
                } else {
                    tvPC.setText("100%");
                }

                pb.setProgress(progressStatus1);


            }
        };*/

    }
}