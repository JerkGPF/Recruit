package com.gpfei.recruit.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.xuexiang.xupdate.proxy.IUpdateHttpService;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * 使用okhttp
 *
 * @author xuexiang
 * @since 2018/7/10 下午4:04
 */
public class OKHttpUpdateHttpService implements IUpdateHttpService {

    private boolean mIsPostJson;

    public OKHttpUpdateHttpService() {
        this(false);
    }

    public OKHttpUpdateHttpService(boolean isPostJson) {
        mIsPostJson = isPostJson;
    }


    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        OkHttpUtils.get()
                .url(url)
                .params(transform(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        //这里默认post的是Form格式，使用json格式的请修改 post -> postString
        RequestCall requestCall;
        if (mIsPostJson) {
            requestCall = OkHttpUtils.postString()
                    .url(url)
                    .content(UpdateUtils.toJson(params))
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build();
        } else {
            requestCall = OkHttpUtils.post()
                    .url(url)
                    .params(transform(params))
                    .build();
        }
        requestCall
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject jsonObject1 = new JSONObject(data);

                            JSONObject newJson = new JSONObject();
                            int result = jsonObject1.getInt("updateStatus");
                            if (result == 1) {
                                newJson.put("ApkMd5", jsonObject1.getString("apkMd5"));
                                newJson.put("VersionId", jsonObject1.getString("versionId"));
                                newJson.put("UpdateStatus", jsonObject1.getInt("updateStatus"));
                                newJson.put("DownloadUrl", "http://114.117.0.103:1111/mock/update/apk/" + jsonObject1.getString("downloadUrl"));
                                newJson.put("UploadTime", jsonObject1.getString("uploadTime"));
                                newJson.put("ModifyContent", jsonObject1.getString("modifyContent"));
                                newJson.put("ApkSize", jsonObject1.getString("apkSize"));
                                newJson.put("Code", jsonObject.getString("code"));
                                newJson.put("Msg", jsonObject.getString("msg"));
                                newJson.put("VersionCode", jsonObject1.getString("versionCode"));
                                newJson.put("VersionName", jsonObject1.getString("versionName"));
                                callBack.onSuccess(newJson.toString());
                            }else {
                                newJson.put("UpdateStatus",result);
                                callBack.onSuccess(newJson.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, final @NonNull DownloadCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .tag(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onStart();
                    }
                });
    }

    @Override
    public void cancelDownload(@NonNull String url) {
        OkHttpUtils.getInstance().cancelTag(url);
    }

    private Map<String, String> transform(Map<String, Object> params) {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }

}
