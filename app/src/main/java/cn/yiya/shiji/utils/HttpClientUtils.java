package cn.yiya.shiji.utils;

public class HttpClientUtils {
//	private static final String TAG = "HTTP RESPONSE";
//	public static final int TIMEOUT_IN_MILLIONS = 10000;
//	public static final int SO_TIME_OUT = 10000;
//	public static final int ContentTypeForm = 0;
//	public static final int ContentTypeJson = 1;
//
//	// public static final int HTTP_GET = 0;
//	// public static final int HTTP_POST = 1;
//
//	private static HttpClientUtils mInstance;
//
//	private HttpClientUtils() {
//	}
//
//	public static HttpClientUtils getInstance() {
//		if (mInstance == null) {
//			mInstance = new HttpClientUtils();
//		}
//		return mInstance;
//	}
//
//	public interface CallBack {
//		void onRequestComplete(String result);
//	}
//
//	/**
//	 * 异步的Get请求
//	 *
//	 * @param urlStr
//	 * @param callBack
//	 */
//	public static void doGetAsyn(final String urlStr, final CallBack callBack) {
//		new Thread() {
//			public void run() {
//				try {
//					String result = doGet(urlStr);
//					Log.d(TAG, result);
//					if (callBack != null) {
//						callBack.onRequestComplete(result);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					if (callBack != null) {
//						callBack.onRequestComplete("");
//					}
//				}
//			};
//		}.start();
//	}
//
//	/**
//	 * 异步的Post请求
//	 *
//	 * @param urlStr
//	 * @param params
//	 * @param callBack
//	 * @throws Exception
//	 */
//	public static void doPostAsyn(final String urlStr, final String params,
//								  final CallBack callBack) {
//		new Thread() {
//			public void run() {
//				try {
//					String result = doPost(urlStr, params);
//					Log.d(TAG, result);
//					if (callBack != null) {
//						callBack.onRequestComplete(result);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					if (callBack != null) {
//						callBack.onRequestComplete("");
//					}
//				}
//			};
//		}.start();
//
//	}
//
//	/**
//	 * Get请求，获得返回数据
//	 *
//	 * @param urlStr
//	 * @return
//	 * @throws Exception
//	 */
//	public static String doGet(String urlStr) {
//		Log.e("GETZYL", urlStr);
//		URL url = null;
//		HttpURLConnection conn = null;
//		InputStream is = null;
//		ByteArrayOutputStream baos = null;
//		String result = null;
//		try {
//			url = new URL(urlStr);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
//
//			//set cookie
//			CookieManager cm = new CookieManager();
//			String cookieString = BaseApplication.Cookie;
//			if(cookieString != "")
//			{
//				cm.setStore(new Gson().fromJson(cookieString
//						, new ConcurrentHashMap<String, Map<String, Map<String, String>>>().getClass()));
//			}
//
//            // 将设备信息上传到头消息
//            String deviceInfo = BaseApplication.getDeviceInfo();
//
//			cm.setCookies(conn);
//			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("device", deviceInfo);
//			conn.setRequestProperty("connection", "Keep-Alive");
//			if (conn.getResponseCode() == 200) {
//				is = conn.getInputStream();
//				baos = new ByteArrayOutputStream();
//				int len = -1;
//				byte[] buf = new byte[128];
//
//				while ((len = is.read(buf)) != -1) {
//					baos.write(buf, 0, len);
//				}
//
//				String session_value= conn.getHeaderField("Set-Cookie");
//				Log.d(TAG, "set-cookie : " + session_value);
//				BaseApplication.setSession(session_value);
//
//				cm.storeCookies(conn);
//				Map<String, Map<String, Map<String, String>>> store = cm.getStore();
//				BaseApplication.setCookie(new Gson().toJson(store));
//
//				baos.flush();
//				result = baos.toString();
//				Log.d(TAG, "result: "+result);
//				//SimpleUtils.writeLogSyc(urlStr, result);
//
//			} else {
//				throw new RuntimeException(" responseCode is not 200 ... ");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (baos != null)
//					baos.close();
//				if (is != null)
//					is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			conn.disconnect();
//		}
//
//		return result;
//
//	}
//
//
//	public static String doPost(String url, String param){
//		return doPost(url,param,ContentTypeForm);
//	}
//	/**
//	 * 向指定 URL 发送POST方法的请求
//	 *
//	 * @param url
//	 *            发送请求的 URL
//	 * @param param
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//	 * @return 所代表远程资源的响应结果
//	 * @throws Exception
//	 */
//	public static String doPost(String url, String param, int contentType) {
//		Log.e("POSTZYL", url);
//		Log.e("HttpPost param", param);
//		PrintWriter out = null;
////		OutputStream os =null;
//		BufferedReader in = null;
//		String result = "";
//		HttpURLConnection conn = null;
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			conn = (HttpURLConnection) realUrl.openConnection();
//
//			//set cookie
//			CookieManager cm = new CookieManager();
//			String cookieString = BaseApplication.Cookie;
//			if(cookieString != "")
//			{
//				cm.setStore(new Gson().fromJson(cookieString
//						, new ConcurrentHashMap<String, Map<String, Map<String, String>>>().getClass()));
//			}
//
//			cm.setCookies(conn);
//
//            // 将设备信息上传到头消息
//            String deviceInfo = BaseApplication.getDeviceInfo();
//
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("device", deviceInfo);
//			conn.setRequestMethod("POST");
//			//conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("charset", "utf-8");
//			if(contentType==ContentTypeForm) {
//				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			}else if(contentType == ContentTypeJson){
//				conn.setRequestProperty("Content-Type", "application/json");
//			}
//			conn.setUseCaches(false);
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
//			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
//
//			if (param != null && !param.trim().equals("")) {
//				// 获取URLConnection对象对应的输出流
//				out = new PrintWriter(conn.getOutputStream());
//				// 发送请求参数
//				out.print(param);
//				// flush输出流的缓冲
//				out.flush();
//				Log.e("HttpPost param","Do Post");
////				os = conn.getOutputStream();
////				os.write(param.getBytes());
////				Log.e("HttpPost param","Do Post");
//			}
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//
//			String session_value= conn.getHeaderField("Set-Cookie");
//			Log.d(TAG, "set-cookie : "+session_value);
//			BaseApplication.setSession(session_value);
//
//			cm.storeCookies(conn);
//			Map<String, Map<String, Map<String, String>>> store = cm.getStore();
//			BaseApplication.setCookie(new Gson().toJson(store));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输出流、输入流
//		finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
////				if(os!=null){
////					os.close();
////				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//			conn.disconnect();
//		}
//		SimpleUtils.writeLogSyc(url+"\nparams : "+param, result);
//		return result;
//	}
//	/**
//	 *  将参数放在http请求头里面
//	 */
//	public static String doPostParamsInHeader(String url, Map<String,Object> params) {
//		Log.e("HttpPost url", url);
//		//Log.e("HttpPost param", param);
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		HttpURLConnection conn = null;
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			conn = (HttpURLConnection) realUrl.openConnection();
//
//			//set cookie
//			CookieManager cm = new CookieManager();
//			String cookieString = BaseApplication.Cookie;
//			if(cookieString != "")
//			{
//				cm.setStore(new Gson().fromJson(cookieString
//						, new ConcurrentHashMap<String, Map<String, Map<String, String>>>().getClass()));
//			}
//
//			cm.setCookies(conn);
//
//		// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("charset", "utf-8");
//			conn.setUseCaches(false);
//
//
////			if (param != null && !param.trim().equals("")) {
////				// 获取URLConnection对象对应的输出流
////				out = new PrintWriter(conn.getOutputStream());
////				// 发送请求参数
////				out.print(param);
////				// flush输出流的缓冲
////				out.flush();
////			}
////			if (params == null || params.isEmpty()) {
////				return "参数错误";
////			}
//
//			for (String key : params.keySet()) {
//				//sb.append("&" + key + "=" + params.get(key));
//				conn.setRequestProperty(key, params.get(key).toString());
//				Log.e(key, params.get(key).toString());
//			}
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
//			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//			String session_value= conn.getHeaderField("Set-Cookie");
//			Log.d(TAG, "set-cookie : "+session_value);
//			BaseApplication.setSession(session_value);
//
//			cm.storeCookies(conn);
//			Map<String, Map<String, Map<String, String>>> store = cm.getStore();
//			BaseApplication.setCookie(new Gson().toJson(store));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输出流、输入流
//		finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//			conn.disconnect();
//		}
//		return result;
//	}
//
//
//
//
//	public static boolean download(String url, String fileName, File cacheDir) {
//		File file = null;
//		try {
//			URL uc = new URL(url);
//			file = new File(cacheDir, System.currentTimeMillis() + "");
//			OutputStream os = new FileOutputStream(file);
//			InputStream is = uc.openStream();
//			byte[] buff = new byte[1024];
//			int hasRead = 0;
//			while ((hasRead = is.read(buff)) > 0) {
//				os.write(buff, 0, hasRead);
//			}
//			is.close();
//			os.close();
//			file.renameTo(new File(cacheDir, fileName));
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//			if (file != null && file.exists()) {
//				file.delete();
//			}
//			return false;
//		}
//	}
//
//	/**
//	 * post方法重载
//	 *
//	 * @param url
//	 * @param params
//	 *            键值对
//	 * @return
//	 */
//	public String doPost(String url, Map<String, Object> params) {
//		if (params != null) {
//			String param = getParamsString(params);
//			return doPost(url, param);
//		}else{
//			return doGet(url);
//		}
//	}
//	public String doPost(String url, Map<String, Object> params, int contentType) {
//		if (params != null) {
//			String param = getParamsString(params);
//			return doPost(url, param,contentType);
//		}else{
//			return doGet(url);
//		}
//	}
////	public String doPost(String url, String params,int contentType) {
////		if (params != null) {
////			return doPost(url, param,contentType);
////		}else{
////			return doGet(url);
////		}
////	}
//	/**
//	 * get方法重载
//	 *
//	 * @param url
//	 * @param params
//	 *            键值对
//	 * @return
//	 */
//	public String doGet(String url, Map<String, Object> params) {
//		if (params != null) {
//			String param = getParamsString(params);
//
//			return doGet(url + "?" + param);
//		}
//		return doGet(url);
//	}
//
//	/**
//	 * 将map转化为字符串
//	 *
//	 * @param params
//	 * @return
//	 */
//	private String getParamsString(Map<String, Object> params) {
//		if (params == null || params.isEmpty()) {
//			return "";
//		}
//		StringBuilder sb = new StringBuilder();
//		for (String key : params.keySet()) {
//			sb.append("&" + key + "=" + params.get(key));
//		}
//		// String param = sb.substring(1, sb.length());
//		return sb.substring(1, sb.length());
//	}
////	/**
////	 *
////	 * @param conn
////	 * @return
////	 */
////	public static Map<String, String> getCookies(URLConnection conn) {
////		Map<String, String> map = new HashMap<String, String>();
////		String headerName = null;
////		for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
////			if (headerName.equalsIgnoreCase("Set-Cookie")) {
////				StringTokenizer st = new StringTokenizer(
////						conn.getHeaderField(i), ";");
////				// the specification dictates that the first name/value pair
////				// in the string is the cookie name and value, so let's handle
////				// them as a special case:
////				if (st.hasMoreTokens()) {
////					String token = st.nextToken();
////					String name = token.substring(0, token.indexOf('='));
////					String value = token.substring(token.indexOf('=') + 1,
////							token.length());
////					map.put(name, value);
////				}
////			}
////		}
////		return map;
////	}

}
