package com.phimpme.phimpme;

import android.net.Uri;

/**
 * This is the configuration file of Phimpme app.
 * The generator can change values of constants here to customize this app.
 */
public class Configuration {
    // Basic functions
    public static final boolean ENABLE_MAP = true;
    public static final boolean ENABLE_PHOTO_CAPTURING = true;
    public static final boolean ENABLE_CHOOSE_FROM_LIBRARY = true;
    public static final boolean ENABLE_PHOTO_MANIPULATION = true;
    public static final boolean ENABLE_PHOTO_LOCATION_MODIFICATION = true;
    public static final boolean ENABLE_NFC = true;
    public static final boolean ENABLE_BLUETOOTH = true;
    public static final boolean ENABLE_ANDROID_SHARING = true;

    // Home page configurations
    public static final boolean HOME_PAGE_WEBSITE = true;
    public static final String HOME_PAGE_WEBSITE_URL = "http://www.bing.com/";
    public static final Uri HOME_PAGE_IMAGE_PATH = Uri.parse("file:///storage/emulated/0/Pictures/MyCameraApp/IMG_20140726_123245.jpg");

    // Website systems
    public static final boolean ENABLE_SHARING_TO_WORDPRESS = true;
    public static final boolean ENABLE_SHARING_TO_DRUPAL = true;
    public static final boolean ENABLE_SHARING_TO_JOOMLA = true;

    // Website URLs
    public static final String WORDPRESS_ROOT_URL = "http://www.yuzhiqiang.org/xmlrpc.php";
    public static final String DRUPAL_ROOT_URL = "http://www.yuzhiqiang.org/drupal/drupapp";
    public static final String JOOMLA_ROOT_URL = "http://www.yuzhiqiang.org/joomla";

    // Website Strings
    public static final String WORDPRESS_CATEGORY = "Phimpme for Android";
    public static final String DRUPAL_CATEGORY = "Phimpme for Android";
    public static final String JOOMLA_DIR = "Phimpme for Android";
    public static final String JOOMLA_CATEGORY = "2";
}
