package top.quotes.pkg.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

class AsyncRequest extends AsyncTask<HttpPost, Integer, HttpResponse> {

	@Override
	protected HttpResponse doInBackground(HttpPost... arg0) {
		try {
			return new DefaultHttpClient().execute(arg0[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
