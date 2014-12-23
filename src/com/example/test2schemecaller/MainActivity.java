package com.example.test2schemecaller;

import com.example.test2schemecaller.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DebugUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String myURL = "http://www.naver.com";
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView webView, final String url) {

				Intent intent = null;
				if(url.indexOf("tel://") > -1){
					intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));		// 전화번호 콜일
				}else{
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));		// 전화번호 외일
				}
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);					

				try{
					// 원하는 스키마, 호스트 못찾으면 에러 발생함.
					startActivity(intent);
				}catch(Exception e){
					//TODO
					Log.d("TEST",e.getMessage());
				
				}
		        return true;
			}
		});
		 
//		webView.loadUrl(myURL);
		webView.loadUrl("file:///android_asset/testscheme.html");
		
		((Button)findViewById(R.id.testListBtn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(Intent.ACTION_MAIN);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse("myscheme1://myListHost?r=received&ci=1234"));
				try{
					startActivity(intent);
				}catch(Exception e){
					//TODO
					Log.d("TEST",e.getMessage());
				
				}
			}
		});
		
		((Button)findViewById(R.id.testRegBtn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse("myscheme1://myRegHost?r=received&ci=5678"));
				try{
					startActivity(intent);
				}catch(Exception e){
					//TODO
					Log.d("TEST",e.getMessage());				
				}
				
			}
		});
		
		((Button)findViewById(R.id.testPackageBtn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String CHECK_PACKAGE_NAME = "com.example.test2schemereceiver";
		        // Package 설치여부 확인
				PackageManager pm = getPackageManager();
		        try {
		            //pm.getApplicationInfo(CHECK_PACKAGE_NAME.toLowerCase(), PackageManager.GET_META_DATA);
		             
		            //패키지가 있을경우 실행할 내용
		            Toast.makeText(getBaseContext(), "PACKAGE 명 = " + CHECK_PACKAGE_NAME.toLowerCase(), Toast.LENGTH_SHORT).show();
		            Log.d("TEST","PACKAGE 명 = " + CHECK_PACKAGE_NAME.toLowerCase());
		          
		            Intent intent = pm.getLaunchIntentForPackage(CHECK_PACKAGE_NAME);
		            intent.addCategory(Intent.CATEGORY_LAUNCHER);
		            startActivity(intent);
		             
		        }
/*		        catch (NameNotFoundException e1)
		        {
		            //패키지가 없을경우 실행할 내용
		            Toast.makeText(getBaseContext(), "PACKAGE 가 설치 되지 않았습니다.", Toast.LENGTH_SHORT).show();
					Log.d("TEST",e1.getMessage());	
		        }*/
		        catch(Exception e){
					Log.d("TEST",e.getMessage());		        	
		        }
				
			}
		});
		
		((Button)findViewById(R.id.testMyhomeBtn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String CHECK_PACKAGE_NAME = "com.example.test2schemereceiver";
		        // Package 설치여부 확인
				PackageManager pm = getPackageManager();

				IntentFilter filter = new IntentFilter();
				filter.addAction("android.intent.action.MAIN");
				filter.addCategory("android.intent.category.HOME");
				filter.addCategory("android.intent.category.DEFAULT");

				Context context = getApplicationContext();
				ComponentName component = new ComponentName(context.getPackageName(), MainActivity.class.getName());

				ComponentName[] components = new ComponentName[]
						{
						   new ComponentName("com.intuitiveui.android", "com.intuitiveui.android.Friday"),
						   new ComponentName("com.android.launcher2","com.android.launcher2.Launcher")
						};
				pm.clearPackagePreferredActivities("com.android.launcher");
				pm.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_EMPTY, components, component);
			}
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
