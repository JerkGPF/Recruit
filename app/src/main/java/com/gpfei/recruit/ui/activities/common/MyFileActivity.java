package com.gpfei.recruit.ui.activities.common;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.kotlin.upload.Constants;
import com.gpfei.recruit.kotlin.upload.LocalUpdateActivity;
import com.gpfei.recruit.utils.DownloadUtil;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;
import com.gpfei.recruit.utils.UploadUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyFileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title,tv_file,tv_action;
    private Button btn_upload;
    private String fileUrl ="";//文档地址
    private PullToRefreshLayout refresh;

    String url = "http://114.117.0.103:8080/recruit/userinfo/getUserInfo";
    String uploadUrl = "http://114.117.0.103:8080/upload/fileoss";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file);
        initView();
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("allInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("附件管理");
        btn_upload = findViewById(R.id.btn_upload);

        tv_file = findViewById(R.id.tv_file);
        tv_action = findViewById(R.id.tv_action);
        btn_upload.setOnClickListener(this);
        tv_file.setOnClickListener(this);
        tv_action.setOnClickListener(this);
        refresh = findViewById(R.id.refresh);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show();
                        //结束刷新
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh.finishRefresh();
                            }
                        });

                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(MyFileActivity.this, "没有更多内容了哟~");
                        //结束加载更多
                        refresh.finishLoadMore();
                    }
                }, 2000);
            }
        });
        show();
    }

    private void show() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                username = sharedPreferences.getString("username",null);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url+"?username="+username).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    parseJson(responsedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                JSONArray items = data.optJSONArray("data");
                //第三层解析
                JSONObject object = items.getJSONObject(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String file = object.getString("file");
                            fileUrl = file;
                            if (file.isEmpty()){
                                tv_file.setText("请上传简历!");
                            }else {
                                String fileName = file.substring(89);  // or str=str.Substring(i);
                                tv_file.setText(fileName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.UPLOAD_FILE_REQUEST && resultCode == Constants.UPLOAD_FILE_RESULT){
            List<String> list = data.getStringArrayListExtra("pathList");
            String filePath = null;
            for(String path:list){
                Log.d("地址：",path);
                filePath = path;
            }
            uploadFile(filePath);
        }

    }
    private void uploadFile(String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                try {
                    ResponseBody responseBody = UploadUtils.uploadFile(uploadUrl, filePath, file.getName());
                    String res = responseBody.string();
                    jx(res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void jx(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String file = data.getString("url");
                            //图片回显
                            showFile(file,"添加成功！");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showFile(String file,String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String upUrl = "http://114.117.0.103:8080/recruit/userinfo/updateuserinfo";
                String username = sharedPreferences.getString("username",null);
                String phone = sharedPreferences.getString("phone",null);
                String qq = sharedPreferences.getString("qq",null);
                String sex = sharedPreferences.getString("sex",null);
                String name = sharedPreferences.getString("name",null);
                String email = sharedPreferences.getString("email",null);
                String experience = sharedPreferences.getString("experience",null);
                String birthday = sharedPreferences.getString("birthday",null);
                String profile = sharedPreferences.getString("profile",null);
                String motto = sharedPreferences.getString("motto",null);
                String nick = sharedPreferences.getString("nick",null);
                String head = sharedPreferences.getString("head",null);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String,Object> map = new HashMap<>();
                editor.putString("file",file);
                editor.commit();
                map.put("motto",motto);
                map.put("nick",nick);
                map.put("username",username);
                map.put("phone",phone);
                map.put("qq",qq);
                map.put("sex",sex);
                map.put("name",name);
                map.put("email",email);
                map.put("experience",experience);
                map.put("birthday",birthday);
                map.put("profile",profile);
                map.put("head",head);
                map.put("file",file);
                okHttpUtil.getDataFromePostJsonBack(upUrl, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fileUrl = file;
                                if (file.isEmpty()){
                                    tv_file.setText("请上传简历!");
                                }else {
                                    String fileName = file.substring(89);  // or str=str.Substring(i);
                                    tv_file.setText(fileName);
                                }
                                ToastUtils.showTextToast(MyFileActivity.this,msg);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_upload:
                Intent intent=new Intent(MyFileActivity.this, LocalUpdateActivity.class);
                intent.putExtra("maxNum",1);//设置最大选择数
                startActivityForResult(intent, Constants.UPLOAD_FILE_REQUEST);
                break;
            case R.id.tv_file:
                if (fileUrl.isEmpty()){
                    ToastUtils.showTextToast(MyFileActivity.this,"请先上传简历！");
                }else {
                    //预览简历信息
                    startActivity(new Intent(MyFileActivity.this,FileWebDetailsActivity.class).putExtra("fileUrl",fileUrl));
                    System.out.println("地址"+fileUrl);
                }
                //download();
                break;
            case R.id.tv_action:
                if (fileUrl.isEmpty()){
                    ToastUtils.showTextToast(MyFileActivity.this,"请先上传简历！");
                }else {
                    PopupMenu popupMenu=new PopupMenu(MyFileActivity.this,v);//1.实例化PopupMenu
                    getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());//2.加载Menu资源
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.popup_download:
                                    download();
                                    return true;
                                case R.id.popup_delete:
                                    //删除操作
                                    delete();
                                    return true;
                                case R.id.popup_preview:
                                    //预览简历信息
                                    startActivity(new Intent(MyFileActivity.this,FileWebDetailsActivity.class).putExtra("fileUrl",fileUrl));
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();//4.显示弹出菜单
                }
                break;
        }
    }
    //简历下载
    private void download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadUtil.get().download(fileUrl, getDiskCacheDir(MyFileActivity.this).toString(), username+"个人简历.doc", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File filePerson) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyFileActivity.this, "下载成功,保存路径:"+filePerson.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed(Exception e) {

                    }
                });
            }
        }).start();


    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    //简历删除
    private void delete(){
        showFile("","删除成功！");
    }

}