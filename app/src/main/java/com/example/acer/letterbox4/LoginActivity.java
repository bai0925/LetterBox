package com.example.acer.letterbox4;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tools.BaseActivity;
import com.tools.Httphelper;
import com.tools.ImageFactory;
import com.tools.Md5Encoding;
import com.tools.SetImageUri;
import com.tools.TimeCountUtil;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {
    //用键值对的形式保存账号密码实现记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static Uri imageUri; //相机拍摄照片的输出地址
    private static final String domain = "47.101.173.82:8080";
    private boolean usingFaceLogin = false; //标识刷脸登陆
    private boolean isRemenberPsw = false; //标识记住密码

    String phoneNumber;
    String password;
    String codeInput;

    //登陆布局
    RelativeLayout signInGroup;
    EditText signInUserName;
    EditText signInPassword;
    CheckBox cbUseFaceSignIn; //刷脸登陆
    CheckBox cbRememberPassword; //记住密码
    Button btnForSignIn;
    TextView tvInSignIn;
    TextView tv2InSignIn;

    //注册布局
    RelativeLayout signUpGroup;
    EditText signUpUserName;
    EditText signUpPassword;
    EditText codeForSignUp; //输入验证码
    Button btnGetCodeForSignUp; //获取验证码
    Button btnForSignUp;
    TextView tvInSignUp;

    //重置密码布局，手机号码，密码，验证码
    RelativeLayout rePasswordGroup;
    EditText rePasswordUserName;
    EditText rePassword;
    EditText codeForRePassword; //输入验证码
    Button btnGetCodeForRePassword; //获取验证码
    TextView tvInRePassword;
    Button btnForRePassword;

    //标识用户操作
    public static final int USER_SIGN_UP_ACTION = 200;
    public static final int USER_SIGN_IN_ACTION = 201;
    public static final int USER_REPASSWORD_ACTION=202;

    //接收服务器反馈信息
    public static final int USER_EXIST = 20;
    public static final int USER_NOT_EXIST = 400;
    public static final int PASSWORD_NOT_MATCH = 401;
    public static final int FACE_INFO_NOT_MATCH = 402;

    private TimeCountUtil timeCountUtilFSP;//注册页中获取验证码倒计时
    private TimeCountUtil timeCountUtilFRP;//忘记密码页中获取验证码倒计时
    public String codeGet; //获取验证码md5值

    private  static final int SIGNIN_SUCCESS_ACTION=181;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initID();

        //登陆按钮
        btnForSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignIn();
            }
        });
        //注册按钮
        btnForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignUp();
            }
        });
        //重置按钮
        btnForRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRePassword();
            }
        });

        //立即注册文本框
        tvInSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUp(v);
            }
        });
        //注册页中返回登陆文本框
        tvInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignIn(v);
            }
        });
        //忘记密码页中返回登陆
        tvInRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignIn2(v);
            }
        });
        tv2InSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRePassword(v);
            }
        });

        //复选框监听事件,选择刷脸登陆则隐藏密码输入框，反之则显示
        cbUseFaceSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    signInPassword.setVisibility(View.GONE);
                } else{
                    signInPassword.setVisibility(View.VISIBLE);
                }
                usingFaceLogin = isChecked;
            }
        });
        //选择记住密码则将变量isRemenberPsw置为true，之后保存登陆信息中做判断会用到
        cbRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isRemenberPsw=isChecked;
            }
        });


        //忘记密码中获取短信验证码按钮
        btnGetCodeForRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(handlerCodeForResetPsw,rePasswordUserName);
            }
        });
        //注册页中获取短信验证码按钮
        btnGetCodeForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(handlerCodeForSignUp,signUpUserName);
            }
        });

        //判断本地是否有保存的登陆信息，存在则读取手机号码和密码并存放在输入框中，反之不做操作
        String userName="",password="";
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        isRemenberPsw=pref.getBoolean("remenber_password",false);
        if(isRemenberPsw){
            userName=pref.getString("username","");
            password=pref.getString("password","");
            cbRememberPassword.setChecked(isRemenberPsw);
            signInUserName.setText(userName);//userName来自上次记住的，或者是初始化的空值
            signInUserName.setSelection(signInUserName.getText().length());//让光标在最后面
            signInPassword.setText(password);
        }

        //验证码倒计时用
        timeCountUtilFSP=new TimeCountUtil(btnGetCodeForSignUp,60000,1000);
        timeCountUtilFRP=new TimeCountUtil(btnGetCodeForRePassword,60000,1000);


    }

    public void initID(){
        //登陆页
        signUpGroup= findViewById(R.id.SignUpGroup); //登陆布局
        signInUserName= findViewById(R.id.signInUserName); //手机号码输入框
        signInPassword= findViewById(R.id.signInPassword); //密码输入框
        cbUseFaceSignIn= findViewById(R.id.cbUseFaceSignIn); //刷脸登陆复选框
        cbRememberPassword= findViewById(R.id.cbRememberPassword); //记住密码复选框
        btnForSignIn= findViewById(R.id.signInBtn);//登陆按钮
        tvInSignIn= findViewById(R.id.signInTv);//立即注册文本框
        tv2InSignIn= findViewById(R.id.signInTv2); //忘记密码文本框

        //注册页
        signInGroup= findViewById(R.id.SignInGrpup); //注册布局
        signUpUserName= findViewById(R.id.signUpUserName); //手机号码输入框
        signUpPassword= findViewById(R.id.signUpPassword); //密码输入框
        codeForSignUp= findViewById(R.id.codeForSignUp); //验证码输入框
        btnGetCodeForSignUp= findViewById(R.id.btn_getCodeForSignUp); //获取验证码按钮
        btnForSignUp= findViewById(R.id.signUpBtn);//注册按钮
        tvInSignUp= findViewById(R.id.signUpTv);

        //忘记密码页
        rePasswordGroup= findViewById(R.id.RePasswordGroup); //忘记密码布局
        rePasswordUserName= findViewById(R.id.rePasswordUserName); //手机号码输入框
        rePassword= findViewById(R.id.rePassword); //密码输入框
        codeForRePassword= findViewById(R.id.codeForRePassword); //验证码输入
        btnGetCodeForRePassword= findViewById(R.id.btn_getCodeForRePassword); //获取验证码按钮
        tvInRePassword= findViewById(R.id.rePasswordTv); //返回登录文本框
        btnForRePassword= findViewById(R.id.rePasswordBtn); //重置按钮

        //设置状态栏和标题栏同色（Android5.0以上才有用）
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出登陆时，再次返回不回到之前页面，而是退出应用
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //显示注册布局
    public void showSignUp(View view){
        signInGroup.setVisibility(View.GONE);
        signUpGroup.setVisibility(View.VISIBLE);
    }
    //注册页中返回登录时显示登陆页
    public void showSignIn(View view) {
        signUpGroup.setVisibility(View.GONE);
        signInGroup.setVisibility(View.VISIBLE);
    }
    //忘记密码页中返回登录时显示登陆页
    public void showSignIn2(View view) {
        rePasswordGroup.setVisibility(View.GONE);
        signInGroup.setVisibility(View.VISIBLE);
    }
    // 显示重置密码布局
    public void showRePassword(View view) {
        signInGroup.setVisibility(View.GONE);
        rePasswordGroup.setVisibility(View.VISIBLE);
    }

    /**
     * 判断手机号码是否符合规范
     * @param phoneNumber 手机号码
     * @return 规范则返回true，反之false
     */
    private boolean judPhone(String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(LoginActivity.this,"请输入您的手机号码",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(phoneNumber.length()!=11) {
            Toast.makeText(LoginActivity.this,"您的手机号码位数不正确",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            /*1表示是以1开始的字符串，然后是 3或者是5这二个数字，
            接着\d是表示任意的数字。{9} 表示这任意的数字长度为9个，但现在的手机号码有18开头的
            所以呢，[35]就加上[358]*/
            String num="[1][357869]\\d{9}";
            if(phoneNumber.matches(num))//matches() 方法用于检测字符串是否匹配给定的正则表达式。
                return true;
            else {
                Toast.makeText(LoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    /**
     * 判断密码是否符合规范
     * @param s 密码内容
     * @return 规范则返回true，反之false
     */
    public boolean judPassword(String s){
        if(s.length()<6){
            makeToast("密码不能小于六位,请重新填写");
            return false;
        }
        boolean Password = false;
        for(int i = 0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))||Character.isLetter(s.charAt(i)))
                Password = true;
            else{
                Password = false;
                makeToast("密码不能仅能包含数字或字母，请重新填写");
                break;
            }
        }
        return Password;
    }

    /**
     * 注册页和忘记密码页中获取短信验证码
     * 单击发送验证码按钮后向后端发送获取短信验证码post请求，之后延时60秒
     * url 后端接口地址
     * phonenum 需要发送验证码的手机号
     */
    public void getCode(Handler handle,EditText edtName){
        phoneNumber=edtName.getText().toString().trim();
        if(judPhone(phoneNumber)){
            //启用倒计时60秒
            timeCountUtilFSP.start();

            //向后端发送获取短信验证码post请求
            postSendForCodeGet(handle,phoneNumber);
        }else{
            timeCountUtilFSP.start();
        }
    }

    /**
     * 发送获取验证码post请求
     * @param handle 处理回调的handle名称
     * @param phoneNumber 需要发送短信验证码的手机号码
     */
    public void postSendForCodeGet(Handler handle,String phoneNumber){
        String url = "http://" + domain + "/user/codeget";
        Map<String, String> params = new HashMap<>();
        params.put("phonenum", phoneNumber);
        Httphelper.sendRequestWithOkHttp(handle,url,params);
        makeToast("验证码已发送，请注意查收");
    }

    /**
     * 密码登陆post请求
     * @param phoneNumber 登陆的手机号码
     * @param password 登陆的密码
     */
    public void postSendForPswSignIn(String phoneNumber,String password){
        String url = "http://" + domain + "/user/signin";
        Map<String, String> params = new HashMap<>();
        params.put("name", phoneNumber);
        params.put("password", password);
        Httphelper.sendRequestWithOkHttp(handlerForSignIn,url,params);
    }

    /**
     * 人脸登录post请求
     * @param imageBase Base64编码的图像数据
     */
    private void postSendForFaceSignIn(String phoneNumber,String imageBase) {
        String url = "http://" + domain + "/user/signin";
        Map<String, String> params = new HashMap<>();
        params.put("name", phoneNumber);
        params.put("image", imageBase);
        Httphelper.sendRequestWithOkHttp(handlerForSignIn,url,params);
    }

    /**
     * 人脸注册post请求
     * @param imageBase Base64 编码的图像数据
     * **/
    private void postSendForSignUp(String phoneNumber,String password,String imageBase) {
        String url = "http://" + domain + "/user/signup";
        Map<String, String> params = new HashMap<>();
        params.put("name", phoneNumber);
        params.put("password", password);
        params.put("image", imageBase);
        Httphelper.sendRequestWithOkHttp(handlerForSignUp,url,params);
    }

    /**
     * 暂未此功能
     * 忘记密码页中获取验证码的请求回调Handle
     * 使用Handler来分发Message对象到主线程中
     */
    public Handler handlerCodeForResetPsw=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return true;
        }
    });

    /**
     * 注册页中获取验证码的请求回调Handle
     * 保存后端返回的md5验证码值来验证填写的验证码是否正确
     */
    public Handler handlerCodeForSignUp=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            codeGet=Httphelper.codeGet;
            return true;
        }
    });

    /**
     * 登陆页中发送密码登陆的请求回调Handle
     * 判断是否登陆成功，失败则根据后端返回值做相应提示，成功则跳转到主页并将本次登陆登陆的手机号码传到其中
     */
    private Handler handlerForSignIn =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(Httphelper.result==false){
                switch (message.what){
                    case USER_NOT_EXIST:
                        makeToast("用户未注册,请重试");break;
                    case PASSWORD_NOT_MATCH:
                        makeToast("密码不正确,请重试");break;
                    case FACE_INFO_NOT_MATCH:
                        makeToast("人脸识别失败,请重试");break;
                    default:makeToast("服务器错误,请稍后重试");break;
                }
            }else {
                //此处实现主活动的滑动菜单的登录成功后的用户名显示
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                //mainusername是主界面用户图像下面的用户名
                finish();
                startActivityForResult(intent,SIGNIN_SUCCESS_ACTION);
            }
            return false;
        }
    });

    /**
     * 注册页中发送登陆请求回调Handle
     * 判断是否注册成功，失败则根据后端返回值做相应提示，成功则跳转到主页并将本次登陆登陆的手机号码传到其中
     */
    private Handler handlerForSignUp=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (Httphelper.result == false) {
                switch (message.what) {
                    case USER_EXIST:
                        makeToast("注册失败,用户已存在,请重试");
                        break;

                    default:
                        makeToast("服务器错误,请稍后重试");
                        break;
                }
            } else {
                makeToast("注册成功");
                //此处实现主活动的滑动菜单的登录成功后的用户名显示
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                //mainusername是主界面用户图像下面的用户名
                intent.putExtra("mainusername",phoneNumber);
                setResult(RESULT_OK, intent);
                finish();
                startActivity(intent);
            }
            return false;
        }
    });

    /**
     * 点击登陆页中登陆按钮
     * 选择刷脸登陆后判断是否选择记住密码，记住密码则取出登陆信息的手机号码与此次登陆的手机号码做比较，不一致则更新手机号码并将密码置为空，不记住则清空登陆信息
     * 选择密码登陆后首先判断密码是否规范，之后判断是否选择记住密码，记住密码则保存登陆信息，否则清空登ß陆信息
     */
    public void doSignIn(){
        phoneNumber=signInUserName.getText().toString().trim();
        if (judPhone(phoneNumber)) {
            //选择刷脸登陆
            if (usingFaceLogin) {
                //选择记住密码
                if (isRemenberPsw) {
                    //取出登陆信息的手机号码与此次登陆的手机号码做比较，不一致则更新手机号码并将密码置为空
                    pref=PreferenceManager.getDefaultSharedPreferences(this);
                    String phoneNUmberLastTime=pref.getString("username","");
                    if(phoneNumber!=phoneNUmberLastTime){
                        //保存手机号码和密码，人脸登陆则将密码保存为空
                        editor = pref.edit();
                        editor.putBoolean("remenber_password", true);
                        editor.putString("username", phoneNumber);
                        editor.putString("password", "");
                    }
                } else {
                    editor.clear();
                }
                editor.apply();
                imageUri=SetImageUri.setImageUri(LoginActivity.this);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//启动相机程序
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定图片输出地址
                startActivityForResult(intent,USER_SIGN_IN_ACTION);//拍完后结果返回到onActivityResult

            } else {
                //可加入密码规范性验证代码！！！
                //选择密码登录
                password=signInPassword.getText().toString().trim();
                if(judPassword(password)){
                    //选择记住密码
                    editor = pref.edit();
                    if (isRemenberPsw) {
                        //保存手机号码和密码
                        editor.putBoolean("remenber_password", true);
                        editor.putString("username", phoneNumber);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    postSendForPswSignIn(phoneNumber, password);
                }
            }
        }
    }

    /**
     * 点击注册页中注册按钮
     * 判断验证码是否正确，正确则调用相机进行面部注册，失败则提示验证码错误
     */
    public void doSignUp(){
        phoneNumber= signUpUserName.getText().toString();
        password= signUpPassword.getText().toString();
        codeInput=codeForSignUp.getText().toString();

        if(judPhone(phoneNumber)) {
            if(judPassword(password)) {
                String md5Code = Md5Encoding.md5(phoneNumber + codeInput);
                if (!md5Code.equals(codeGet)) {
                    makeToast("您填写的验证码错误");
                } else {
                    imageUri = SetImageUri.setImageUri(LoginActivity.this);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定图片输出地址
                    startActivityForResult(intent, USER_SIGN_UP_ACTION);//拍完后结果返回到onActivityResult
                }
            }
        }
    }

    /**
     * 暂未此功能
     * 点击忘记密码中重置按钮
     */
    public void doRePassword(){
        makeToast("暂未此功能");
    }


    /**
     * 封装Toast函数，方便之后调用
     * @param message 需要弹出Toast的信息内容
     */
    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //用于将照片数据处理返回上一个活动
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imageBase="";
        try{
            //将拍摄的照片解析为bmp
            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            Bitmap bmp=new ImageFactory().ratio(bitmap,200,300);
            //bmp转为byte数组
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            //Intent传输的bytes（里面存放的图片）不能超过40k，超过后活动就不能正常传递数据
            //bmp.compress(Bitmap.CompressFormat.PNG,100,baos);//这种方式几乎不失真，但是图片太大
            bmp.compress(Bitmap.CompressFormat.JPEG,20,baos);
            //此为压缩80%，保留图片20%的质量
            byte[]picbyte=baos.toByteArray();
            //这里使用系统自带的Base64的包
            imageBase= Base64.encodeToString(picbyte,Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }

        switch (requestCode) {
            //如果是登录行为，将照片数据传给postSendForFaceSignIn函数
            case USER_SIGN_IN_ACTION:
                phoneNumber = signInUserName.getText().toString().trim();
                postSendForFaceSignIn(phoneNumber, imageBase);
                break;

            //如果是注册行为，将照片传给postSendForSignUp函数
            case USER_SIGN_UP_ACTION:
                phoneNumber = signUpUserName.getText().toString().trim();
                password=signUpPassword.getText().toString().trim();
                postSendForSignUp(phoneNumber,password,imageBase);
                break;

            //其他情况
            default:
                makeToast("未知错误，请稍后重试");
                break;
        }
    }
}
