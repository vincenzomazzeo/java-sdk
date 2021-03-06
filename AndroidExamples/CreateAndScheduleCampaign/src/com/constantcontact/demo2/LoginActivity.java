package com.constantcontact.demo2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity {

	WebView loginWebView;
	boolean isFirstRedirect = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("Login");
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		loginWebView = (WebView)findViewById(R.id.loginWebView);
		loginWebView.getSettings().setJavaScriptEnabled(true);
		loginWebView.setWebViewClient(new CustomWebViewClient());
		String strUrl = String.format("%s%s?response_type=token&client_id=%s&redirect_uri=%s", getString(R.string.BASE_URL), getString(R.string.AUTHORIZATION_ENDPOINT), getString(R.string.API_KEY), getString(R.string.REDIRECT_URL));
		loginWebView.loadUrl(strUrl);
	}
	private class CustomWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	loginWebView.loadUrl(url);
	        return true;
	    }
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	super.onPageFinished(view, url);
	    	if(url.contains("access_token") && isFirstRedirect)
	    	{
	    		//handle only the first redirect
	    		isFirstRedirect = false;
	    		
	    		//Parse the URL and extract the accessToken
	    		GlobalConfig.getInstance().setAccessToken(url.substring(url.indexOf("access_token") + 13, url.indexOf("&")));
	    		
	    		Intent intent = new Intent(LoginActivity.this, EmailCampaignActivity.class);
	            startActivity(intent);
	            
	            finish();
	    		
	    	}
	    }
	}
}
