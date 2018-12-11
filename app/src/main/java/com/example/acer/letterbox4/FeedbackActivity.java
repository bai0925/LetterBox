package com.example.acer.letterbox4;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class FeedbackActivity extends AppCompatActivity {

    int limit = 200;

    private EditText et_input_area;
    private TextView tv_limit_area;
    private Button submit_suggestion;

    //还不懂这个是怎么实现返回功能的。。。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://点击设置活动中的返回
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        et_input_area=(EditText)findViewById(R.id.et_input_area);
        tv_limit_area=(TextView)findViewById(R.id.tv_limit_area);
        submit_suggestion = (Button) findViewById(R.id.submit_suggestion);

        Toolbar toolbar=(Toolbar)findViewById(R.id.setting_toolbar) ;
        //标题栏相关设置
        setSupportActionBar(toolbar);
        //设置不现实自带的title文字
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_back);

        try {//设置状态栏和标题栏同色（Android5.0以上才有用）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置文本框监听事件
        et_input_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_input_area.length() > limit) {
                    et_input_area.setText(s.subSequence(0, s.length() - 1));
                    et_input_area.setSelection(et_input_area.length());
                } else {
                    tv_limit_area.setText(et_input_area.length() + "/" + limit);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void submit(View v) {
        switch (v.getId()) {
            case R.id.submit_suggestion:
                String content = et_input_area.getText().toString() + "";
                if ("".equals(content.trim())) {
                    makeToast( "反馈的信息不能为空哦^_^！");
                } else {
                    submitSuggestion(content);
                }
                break;
        }
    }

    //暂时先放在这里，提交到服务器之后实现
    public void submitSuggestion(String content) {
        makeToast("您的意见已经提交到管理员，感谢您的支持！");
    }

    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
