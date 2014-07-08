package com.joooid.android.xmlrpc;

public class Constants {

    public static final String JOOOID_VERSION = "2.0";

    public static final int STATE_NEW_ARTICLE = 0;
    public static final int STATE_OLD_ARTICLE = 1;
    public static final int STATE_DRAFT_ARTICLE = 2;
    
    public static final int STATE_NEW_CATEGORY = 0;
    public static final int STATE_OLD_CATEGORY = 1;
    
    public static final String UPLOAD_DIR = "joooid";
    
    public static final int REQUEST_LOAD_IMAGE = 0;
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_LOAD_ARTICLE = 2;
    public static final int REQUEST_QUICK_IMAGE = 3;
    public static final int REQUEST_QUICK_VIDEO = 4;

    public static final String KEY_EXTRA_ARTICLE = "article";
    public static final String KEY_EXTRA_CATEGORY = "category";
    public static final String KEY_EXTRA_STATE = "currentState";
    public static final String KEY_EXTRA_IMAGE_URI = "imageUri";
    public static final String KEY_EXTRA_VIDEO_URI = "videoUri";
    public static final String KEY_EXTRA_TEXT = "videoUri";
    
    public static final String KEY_EXTRA_YOUTUBE_URL = "youtubeUrl";
    
    public static final String PREFERENCE_LOGIN = "login";
    public static final String PREFERENCE_LAYOUT = "mmm";

    public static final int TASK_WS_CODE = 0;
    public static final int TASK_WS_CODE_OK = 1;
    public static final int TASK_WS_CODE_KO = 2;
    public static final int TASK_WS_CODE_ERROR = 3;
    
    public static final String TASK_WS_CODE_ERROR_XML_RPC = "XML-RPC ERROR";
    public static final String TASK_WS_CODE_ERROR_HTTP_CODE = "HTTP CODE ERROR";
    public static final String TASK_WS_CODE_ERROR_XML_PARSER = "XML-PARSER ERROR";
    public static final String TASK_WS_CODE_ERROR_RPC_CLIENT = "RPC-CLIENT ERROR";
    
    public static final String TASK_WS_URI_J15 = "/plugins/xmlrpc/xmlrpc_joooid.php";
    public static final String TASK_WS_URI_J17 = "/index.php?option=com_joooid";
    
    public static final String MAP_ROADMAP = "MapTypeId.ROADMAP";
    public static final String MAP_SATELLITE = "MapTypeId.SATELLITE";
    public static final String MAP_HYBRID = "MapTypeId.HYBRID";
    public static final String MAP_TERRAIN = "MapTypeId.TERRAIN";
    
    public static final String[] MAP_CONSTANTS = {"roadmap","satellite","hybrid","terrain"};
    public static final String[] EMBED_THEME_CONSTANTS = {"dark","light"};

    public static final String REGEX_YOUTUBE = "#(?<=v=)[a-zA-Z0-9-]+(?=&)|(?<=v\\/)[^&\\n]+(?=\\?)|(?<=v=)[^&\n]+|(?<=youtu.be/)[^&\n]+#";

}
