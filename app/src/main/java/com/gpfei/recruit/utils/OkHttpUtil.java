package com.gpfei.recruit.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {


    private static final String TAG = OkHttpUtil.class.getSimpleName();


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //线程池单线程请求
// private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private final static int size = 5;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(size);

    private static final int GET = 1;
    private static final int POST = 2;
    private static final int POSTJSON = 3;
    private static final int ASYNC = 5;
    //超时时间
    public static final int TIMEOUT = 1000 * 10;

    public static final int CONN_TIMEOUT = 1000 * 5;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    private RequestThread requestThread;

    private static OkHttpUtil instance;

    private OkHttpUtil() {
    }


    public static OkHttpUtil getInstance() {
        if (instance == null) {
            instance = new OkHttpUtil();
        }
        return instance;
    }


    private void init() {
        //设置超时
        client.newBuilder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }


    private Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        init();
        Response response = client.newCall(request).execute();
        return response;
    }


    private Response postMap(String url, Map<String, Object> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Log.i("参数:", entry.getKey() + ":" + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        init();
        Response response = client.newCall(request).execute();
        return response;
    }


//    public Response postJson(String url, String json) throws IOException {
    public Response postJson(String url, String json) throws IOException {
        Log.i("POSTJSON请求参数---->", json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        init();
        Response response = client.newCall(request).execute();
        return response;
//response.body().string()这一句代码在方法体里面只能用一次(包括打印输出的使用)
//  return response.body().string();

    }


    private void postAsynPostJson(String url, Map<String, Object> map, final OnCallback callback) {
        //Map转JSON数据
//  Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(map));
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败");
                callback.onFailure(e.getMessage());
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                callback.callback(response.body().string());
            }

        });
    }

    /**
     *  * @param url 请求地址
     *  * @param map 请求参数
     *  
     */

    public void getDataFromeGet(final String url, final Map<String, Object> map, final OnCallback callback) {
        requestThread = new RequestThread(GET, url, map, callback);
        scheduledThreadPool.execute(requestThread);
    }

    /**
     *  * @param url 请求地址
     *  * @param map 请求参数
     *  
     */


    public void getDataFromePostMap(final String url, final Map<String, Object> map, final OnCallback callback) {
        requestThread = new RequestThread(POST, url, map, callback);
        scheduledThreadPool.execute(requestThread);

    }

    /**
     *  * 通过map转换成json的post请求
     *  *
     *  * @param url
     *  * @param map
     *  
     */


    public void getDataFromePostJson(final String url, final Map<String, Object> map, OnCallback callback) {
        requestThread = new RequestThread(POSTJSON, url, map, callback);
        scheduledThreadPool.execute(requestThread);
    }


    public void getDataFromePostJsonBack(String url, Map<String, Object> map, OnCallback callback) {
        requestThread = new RequestThread(POSTJSON, url, map, callback);
        scheduledThreadPool.execute(requestThread);
    }


    public void getDataFromPostJsonAsynCallBack(String url, Map<String, Object> map, OnCallback callback) {
        requestThread = new RequestThread(ASYNC, url, map, callback);
        scheduledThreadPool.execute(requestThread);
    }


    class RequestThread implements Runnable {

        private int requestType;

        private String url;

        private int port;

        private Map<String, Object> map;

        private OnCallback callback;

        /**
         *  * @param requestType 请求方式
         *  * @param url   请求地址
         *  * @param map   请求参数
         *  
         */


        public RequestThread(int requestType, String url, Map<String, Object> map, OnCallback callback) {
            this.requestType = requestType;
            this.port = port;
            this.url = url;
            this.map = map;
            this.callback = callback;
        }




        @Override


        public void run() {
            Response response;
            try {
                switch (requestType) {
                    case GET:
                        Log.e(TAG + "requestType", "使用了GET请求");
                        response = get(url);
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            callback.callback(result);
                        } else {
                            callback.onFailure(response.message());
                        }
                        break;
                    case POST:
                        Log.e(TAG + "requestType", "使用了POST请求");
                        response = postMap(url, map);
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            callback.callback(result);
                        } else {
                            callback.onFailure(response.message());
                        }
                        break;
                    case POSTJSON:
                        Log.e(TAG + "requestType", "使用了POSTJSON请求");
                        response = postJson(url, gson.toJson(map));
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            callback.callback(result);
                        } else {
                            callback.onFailure(response.message());
                        }
                        break;
                    case ASYNC:
                        Log.e(TAG + "requestType", "使用了ASYNC请求");
                        postAsynPostJson(url, map, callback);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public interface OnCallback {


        void callback(String result);


        void onFailure(String message);

    }
}

