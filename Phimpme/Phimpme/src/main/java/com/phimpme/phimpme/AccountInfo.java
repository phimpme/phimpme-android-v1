package com.phimpme.phimpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.Serializable;

public class AccountInfo implements Serializable {
    private String accountCategory;
    private String userName;
    private String passWord;

    public AccountInfo(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public static AccountInfo getSavedAccountInfo(Context context, String accountCategory) {
        SharedPreferences accountInfo = context.getSharedPreferences(accountCategory, context.MODE_PRIVATE);
        AccountInfo savedAccount = new AccountInfo(accountInfo.getString(accountCategory, null));
        savedAccount.setUserName(accountInfo.getString("userName", null));
        savedAccount.setPassWord(accountInfo.getString("passWord", null));
        return savedAccount;
    }

    public static void createAndSaveAccountInfo(Context context, String accountCategory) {
        // TODO: Encryption
        // TODO: Do not put this here, move to UploadActivity
        Intent toAccoutEditor = new Intent(context, AccountEditor.class);
        toAccoutEditor.putExtra("accountCategory", accountCategory);
        context.startActivity(toAccoutEditor);
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
}