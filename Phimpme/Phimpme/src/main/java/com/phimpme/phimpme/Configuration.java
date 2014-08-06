package com.phimpme.phimpme;

/**
 * This is the configuration file of Phimpme app.
 * The generator can change values of constants here to customize this app.
 */
public class Configuration {
    // Basic functions
    public static final boolean ENABLE_MAP = true;
    public static final boolean ENABLE_GOOGLEMAP = false;
    public static final boolean ENABLE_PHOTO_CAPTURING = true;
    public static final boolean ENABLE_CHOOSE_FROM_LIBRARY = true;
    public static final boolean ENABLE_PHOTO_MANIPULATION = true;
    public static boolean ENABLE_PHOTO_LOCATION = true;
    public static final boolean ENABLE_PHOTO_LOCATION_MODIFICATION = true;
    public static final boolean ENABLE_NFC = true;
    public static final boolean ENABLE_ANDROID_SHARING = true;
    public static final boolean ENABLE_DONATE = true;
    public static final boolean ENABLE_ADVERTISEMENT = true;
    public static final String DONATE_URL = "";

    // Home page configurations
    public static final boolean HOME_SHOW_WEBPAGE = true;
    public static final String HOME_WEBPAGE_URL = "http://www.google.com/";

    // WordPress
    public static final boolean ENABLE_SHARING_TO_WORDPRESS = true;
    public static final String WORDPRESS_ROOT_URL = "http://www.yuzhiqiang.org/xmlrpc.php";
    public static final String WORDPRESS_CATEGORY = "Phimpme for Android";

    // Drupal
    public static final boolean ENABLE_SHARING_TO_DRUPAL = true;
    public static final String DRUPAL_ROOT_URL = "http://www.yuzhiqiang.org/drupal/drupapp";
    public static final String DRUPAL_CATEGORY = "Phimpme"; // No space

    // Joomla
    public static final boolean ENABLE_SHARING_TO_JOOMLA = true;
    public static final String JOOMLA_ROOT_URL = "http://www.yuzhiqiang.org/joomla";
    public static final String JOOMLA_DIR = "Phimpme"; // No space
    public static final String JOOMLA_CATEGORY = "2";
}
