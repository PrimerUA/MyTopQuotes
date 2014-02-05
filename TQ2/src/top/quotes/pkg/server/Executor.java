package top.quotes.pkg.server;	

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import top.quotes.pkg.entity.User;
import top.quotes.pkg.entity.UserQuote;

public class Executor {

	private Gson gson = new Gson();
	private final static String url = "http://topquotes-test.esy.es/";

	public int register(User user) {
		try {
			HttpPost httpPost = new HttpPost(url + "register.php");
			StringEntity buff = new StringEntity(gson.toJson(user).toString(),
					HTTP.UTF_8);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(buff);

			HttpResponse response = new AsyncRequest().execute(httpPost).get();
			if (response != null)
				return getUserId(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean sendQuote(UserQuote quote) {
		try {
			HttpPost httpPost = new HttpPost(url + "post.php");
			StringEntity buff = new StringEntity(gson.toJson(quote).toString(),
					HTTP.UTF_8);
			
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(buff);

			HttpResponse response = new AsyncRequest().execute(httpPost).get();
			if (response != null)
				return getResPostQoute(response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<UserQuote> list(int from, int length, int language) {
		try {

			Reader reader = getReader(getStream(url + "list.php?from=" + from
					+ "&length=" + length + "&lang=" + language));

			if (reader != null) {
				return parseLikesNumber(reader);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected InputStream getStream(String url) {
		InputStream source = null;
		try {
			byte[] res = new Streamer().execute(url).get();
			if (res != null) {
				source = new ByteArrayInputStream(res);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return source;
	}

	protected Reader getReader(InputStream source) {
		Reader reader = null;
		if (source != null) {
			reader = new InputStreamReader(source);
		} else {
			Log.e("reader", "source in reader is null");
		}
		return reader;
	}

	@SuppressWarnings("resource")
	private int getUserId(HttpResponse response) throws IllegalStateException, IOException {
		int res = -1;
		InputStream in = response.getEntity().getContent();
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		reader.setLenient(true);

		reader.beginObject();

		if (reader.hasNext()) {
			String name = reader.nextName();

			if ("id".equals(name)) {
				res = reader.nextInt();
			}
		}
		return res;
	}

	@SuppressWarnings("resource")
	private boolean getResPostQoute(HttpResponse response) throws IllegalStateException, IOException {
		InputStream in = response.getEntity().getContent();
		JsonReader reader = new JsonReader(new InputStreamReader(in));
		reader.setLenient(true);

		reader.beginObject();

		if (reader.hasNext()) {
			String name = reader.nextName();

			if ("res".equals(name) && ("good".equals(reader.nextString()))) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<UserQuote> parseLikesNumber(Reader reader) throws IOException {
		List<UserQuote> result = null;

		Type listType = new TypeToken<List<UserQuote>>() {
		}.getType();
		Object buff = gson.fromJson(reader, listType);
		result = (List<UserQuote>) buff;

		reader.close();
		return result;
	}
}
