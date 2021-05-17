package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.SmileToast;
import com.gpfei.recruit.utils.ToastUtils;
import com.gpfei.recruit.utils.UploadUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.longsh.optionframelibrary.OptionBottomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private PullToRefreshLayout refreshLayout;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_user_head;
    private RelativeLayout rl_modify_user_head;
    private RelativeLayout rl_id;
    private RelativeLayout rl_modify_user_nick;
    private RelativeLayout rl_modify_user_motto;
    private RelativeLayout rl_to_mydata;
    private TextView tv_username;
    private TextView tv_nick;
    private TextView tv_motto;
    private Uri imageUri;
    private static final int REQUEST_CAPTURE = 2;
    public static final int REQUEST_INFO = 4;
    private static final int REQUEST_PICTURE = 5;
    private static final int RESULT_CROP = 7;
    private static final int GALLERY_ACTIVITY_CODE = 9;
    private Uri localUri = null;

    private static final String TAG = "MyInfoActivity";

    String url = "http://114.117.0.103:8080/recruit/userinfo/getUserInfo";
    String uploadUrl = "http://114.117.0.103:8080/upload/fileoss";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    //初始化
    private void initView() {
        sharedPreferences = getSharedPreferences("allInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_username = findViewById(R.id.tv_username);
        tv_nick = findViewById(R.id.tv_nick);
        tv_motto = findViewById(R.id.tv_motto);
        tv_title.setText("我的资料");
        iv_user_head = findViewById(R.id.iv_user_head);
        iv_back.setOnClickListener(this);
        rl_modify_user_head = findViewById(R.id.rl_modify_user_head);
        rl_modify_user_head.setOnClickListener(this);
        rl_modify_user_nick = findViewById(R.id.rl_modify_user_nick);
        rl_modify_user_nick.setOnClickListener(this);
        rl_modify_user_motto = findViewById(R.id.rl_modify_user_motto);
        rl_modify_user_motto.setOnClickListener(this);
        rl_to_mydata = findViewById(R.id.rl_to_mydata);
        rl_to_mydata.setOnClickListener(this);
        rl_id = findViewById(R.id.rl_id);
        rl_id.setOnClickListener(this);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showUserInfo();
                        //结束刷新
                        refreshLayout.finishRefresh();
                        SmileToast smileToast = new SmileToast();
                        smileToast.smile("加载完成");
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(MyInfoActivity.this, "没有更多内容了哟~");
                        //结束加载更多
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //显示用户信息
        showUserInfo();
    }
    //显示用户资料
    private void showUserInfo() {
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
                            String head = data.getString("url");
                            //图片回显
                            showImage(head);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(MyInfoActivity.this, "获取失败！");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showImage(String head) {
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
                String file = sharedPreferences.getString("file",null);
                String motto = sharedPreferences.getString("motto",null);
                String nick = sharedPreferences.getString("nick",null);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String,Object> map = new HashMap<>();
                editor.putString("head",head);
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
                                SmileToast smileToast = new SmileToast();
                                smileToast.smile("头像修改完成");
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
                            String nick = object.getString("nick");
                            String head = object.getString("head");
                            String motto = object.getString("motto");
                            getInfo(head,username,nick,motto);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(MyInfoActivity.this, "获取失败！");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getInfo(String head, String username, String nick, String motto) {
        //获取头像地址
        if (head != null) {
            //圆形头像
            Glide.with(MyInfoActivity.this).load(head).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_user_head) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyInfoActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_user_head.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        //获取用ID
        if (username != null) {
            tv_username.setText(username);
        }
        //获取用昵称
        if (nick != null) {
            tv_nick.setText(nick);
        }
        //获取用个性签名
        if (motto != null) {
            tv_motto.setText(motto);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_modify_user_head:
                //Android6.0以上要获取动态权限
                //先判断该页面是否已经授予拍照权限
                methodRequiresTwoPermission();//调用权限
                List<String> stringList = new ArrayList<String>();
                stringList.add("拍照");
                stringList.add("从相册选择");
                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(MyInfoActivity.this, stringList);
                optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            //拍照
                            openCamera();
                        }
                        if (position == 1) {
                            //调用相册
                            selectAlbum();
                        }
                        optionBottomDialog.dismiss();
                    }
                });

                break;
            case R.id.rl_id:
                ToastUtils.showTextToast(MyInfoActivity.this, "用户ID不支持修改哟(＾Ｕ＾)");
                break;
            case R.id.rl_modify_user_nick:
                Intent intent = new Intent(MyInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("tag", "nick");
                intent.putExtra("nick",tv_nick.getText().toString());
                startActivityForResult(intent, REQUEST_INFO);
                break;
            case R.id.rl_modify_user_motto:
                Intent intent1 = new Intent(MyInfoActivity.this, ModifyUserInfoActivity.class);
                intent1.putExtra("tag", "motto");
                intent1.putExtra("motto", tv_motto.getText().toString());
                startActivityForResult(intent1, REQUEST_INFO);
                break;
            case R.id.rl_to_mydata:
                startActivity(new Intent(MyInfoActivity.this, MyDataActivity.class));
                break;
        }

    }

    @AfterPermissionGranted(1)//添加注解，是为了首次执行权限申请后，回调该方法
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //已经申请过权限，直接调用相机
            // openCamera();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取权限",
                    1, perms);
        }
    }

    //选择相机
    private void openCamera() {  //调用相机拍照
        Intent intent = new Intent();
        File file = getOutputMediaFile(); //工具类稍后会给出
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(file);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);//启动拍照
    }

    //选择相册
    private void selectAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_ACTIVITY_CODE);
    }

    //裁剪图片
    private void performCrop(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                grantUriPermission("com.android.camera", uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile().toString());
            startActivityForResult(intent, RESULT_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "你的设备不支持裁剪行为！";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //建立保存头像的路径及名称
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        String mImageName ="avatar.png";
        System.out.println("avatar"+mImageName);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    //保存图像
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "requestCode: requestCode");
            switch (requestCode) {
                case REQUEST_INFO:
                    if (resultCode == 200) {
                        //刷新数据
                        showUserInfo();
                    }
                    break;
                case REQUEST_CAPTURE:
                    if (null != imageUri) {
                        localUri = imageUri;
                        performCrop(localUri);
                    }
                    break;
                case REQUEST_PICTURE:
                    localUri = data.getData();
                    performCrop(localUri);
                    break;
                case RESULT_CROP:
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //判断返回值extras是否为空，为空则说明用户截图没有保存就返回了，此时应该用上一张图，
                    //否则就用用户保存的图
                    if (extras == null) {
                        // iv_user_head.setImageBitmap(mBitmap);
                        // storeImage(mBitmap);
                    } else {
                        Log.d(TAG, "RESULT_CROP: 准备");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String picPath = getOutputMediaFile().toString();
                                Log.d(TAG, "run: 准备"+picPath);
                                File file = new File(picPath);
                                try {
                                    ResponseBody responseBody = UploadUtils.uploadFile(uploadUrl, picPath, file.getName());
                                    String res = responseBody.string();
                                    jx(res);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv_user_head.setImageBitmap(selectedBitmap);
                                        storeImage(selectedBitmap);
                                    }
                                });
                            }
                        }).start();
                    }
                    break;
                case GALLERY_ACTIVITY_CODE:
                    localUri = data.getData();
                    //  setBitmap(localUri);
                    performCrop(localUri);
                    break;
            }
        }
    }
}