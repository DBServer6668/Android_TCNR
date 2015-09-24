package tcnr14.com.example.m1418;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class DBConnector {
	//宣告類別變數以方便存取，並判斷是否連線成功
	static int httpstate;
	
	public static String executeQuery(String query_string) {
		String result = "";
		httpstate = 0;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://www.oldpa.tw/android/android_connect_db.php");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("query_string", query_string));
			// JSON編碼，避免中文亂碼
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			//使用httpResponse的方式，取得HTTP的狀態碼並設定給httpstate變數	
			httpstate = httpResponse.getStatusLine().getStatusCode();
			// view_account.setText(httpResponse.getStatusLine().toString());
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			// JSON編碼，避免中文亂碼
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(inputStream, "utf-8"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = bufReader.readLine()) != null) {
				builder.append(line + "\n");
			}
			inputStream.close();
			result = builder.toString();
		} catch (Exception e) {
			// Log.e("log_tag", e.toString());
		}
		return result;
	}

}
