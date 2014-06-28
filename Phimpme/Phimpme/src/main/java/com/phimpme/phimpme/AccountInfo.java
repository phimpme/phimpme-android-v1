package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by sensing on 14-6-24.
 */
public class AccountInfo implements Serializable {
    private String accountCategory;
    private String userName;
    private String passWord;
    private String imagePath;
    private String userUrl;

    public AccountInfo(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getAccountCategory() {
        return this.accountCategory;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserUrl() {
        return this.userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static AccountInfo getSavedAccountInfo(Context context, String accountCategory) {
        SharedPreferences accountInfo = context.getSharedPreferences(accountCategory, context.MODE_PRIVATE);
        AccountInfo savedAccount = new AccountInfo(accountInfo.getString(accountCategory, null));
        savedAccount.setUserName(accountInfo.getString("userName", null));
        savedAccount.setPassWord(accountInfo.getString("passWord", null));
        savedAccount.setUserUrl(accountInfo.getString("userUrl", null));
        return savedAccount;
    }

    public static void saveAccountInfo(Context context, String accountCategory) {
        // TODO Encryption
        Intent toAccoutEditor = new Intent();
        toAccoutEditor.putExtra("accountCategory", accountCategory);
        toAccoutEditor.setClass(context, AccountEditor.class);
        context.startActivity(toAccoutEditor);
    }
}
