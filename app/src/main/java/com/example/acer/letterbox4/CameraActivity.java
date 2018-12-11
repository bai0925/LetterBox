package com.example.acer.letterbox4;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.tools.ImageFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.tools.BirmapByte.Bitmap2Bytes;
import static com.tools.BirmapByte.Bytes2Bimap;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mPreview;//实现圆角SurfaceView时将SurfaceView改为SurFaceV
    private int cameraId=1;//1:打开前置摄像头，0：打开后置摄像头
    private int orientation=90;//相机旋转角度，使相机预览时正的
    private SurfaceTexture surfaceTexture;//解决onPreviewFrame未调用添加的
    private byte[]mPreBuffer;

    //待删除
    //private boolean zhayan=false;
    //private String denglu="123";

    private boolean faceExit=false;
    private static final int MAX_FACE_NUM = 5;//最大可以检测出的人脸数量
    private int realFaceNum = 0;//实际检测出的人脸数量
    private Bitmap bitmap;//获取预览帧进行人脸检测
    private long lastModirTime;
    private final int FACE_DETECT_SUCCESS=20;
    private final int FACE_DETECT_FAILED=21;
    private boolean runThread=false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    //初始化预览界面
        mPreview=findViewById(R.id.preview);
        mHolder=mPreview.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceTexture=new SurfaceTexture(10);//解决onPreviewFrame未调用添加的
        //点击预览界面聚焦
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
        requestPermission();

//待删除
//        Intent intent=getIntent();
//        denglu=intent.getStringExtra("denglu");
//        Log.d(TAG, "onCreate: "+denglu);
//        zhayan=false;
    }

    //定义拍照方法
    public void btnCapture(View view){

        //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //摄像头聚焦
        Log.e(TAG,"拍照按钮被点击");
//        mCamera.autoFocus(new Camera.AutoFocusCallback(){
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if(success){mCamera.takePicture(null,null, mpictureCallback);}
//            }
//        });   if
//        if("DENGLU".equals(denglu)){//if语句所有代码待删除，只留下else中的所有代码
//            Log.d(TAG, "btnCapture: "+zhayan);
//            if(zhayan){
//                if (null == mCamera) {
//                    // return if instance is null
//                    android.util.Log.w(TAG, "Camera is not initialized!");
//                    return;
//                }
//                Log.i(TAG, "Attempt to take a picture");
//                try {
//                    Log.d(TAG, "btnCapture: 人脸是否存在"+faceExit);
//                    if(faceExit){
//                        //mCamera.takePicture(null,null, mpictureCallback);
//
//                        //为了避免在拍照的时候，最后一张图片也要进行检测，耗时太长，所以将直接
//                        //用预览帧中的图片，所以没用takePicure
//                        Bitmap bmp= new ImageFactory().ratio(bitmap,150,200);
//                        byte[] picbyte =Bitmap2Bytes(bmp);
//                        String imageBase="";
//                        imageBase= Base64.encodeToString(picbyte,Base64.DEFAULT);
//                        Intent intent=getIntent();
//                        intent.putExtra("image_base",imageBase);
//                        setResult(RESULT_OK, intent);
//                        releaseCamera();
//                        finish();//关闭现有界面
//                    }
//
//                    else
//                        Toast.makeText(CameraActivity.this, "请拍摄人脸", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    // print error stack by default
//                    android.util.Log.e(TAG, "Error occured while taking a picture: " + e);
//                }
//            }else {
//                Toast.makeText(CameraActivity.this, "请眨眼，完成验证", Toast.LENGTH_SHORT).show();
//                zhayan=true;
//            }
//        }
//        else {
            if (null == mCamera) {
                // return if instance is null
                Log.w(TAG, "Camera is not initialized!");
                return;
            }
            Log.i(TAG, "Attempt to take a picture");
            try {
                Log.d(TAG, "btnCapture: 人脸是否存在"+faceExit);
                if(faceExit){
                    //mCamera.takePicture(null,null, mpictureCallback);

                    //为了避免在拍照的时候，最后一张图片也要进行检测，耗时太长，所以将直接
                    //用预览帧中的图片，所以没用takePicure
                    Bitmap bmp= new ImageFactory().ratio(bitmap,150,200);
                    byte[] picbyte =Bitmap2Bytes(bmp);
                    String imageBase="";
                    imageBase= Base64.encodeToString(picbyte,Base64.DEFAULT);
                    Intent intent=getIntent();
                    intent.putExtra("image_base",imageBase);
                    setResult(RESULT_OK, intent);
                    releaseCamera();
                    finish();//关闭现有界面
                }

                else
                    Toast.makeText(CameraActivity.this, "请拍摄人脸", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // print error stack by default
                Log.e(TAG, "Error occured while taking a picture: " + e);
            }
//        }
    }

    //拍照后，在这里
    private Camera.PictureCallback mpictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Bitmap bmp=Bytes2Bimap(bytes);//该函数在BirmpaByte工具类中

            bmp= new ImageFactory().ratio(bmp,150,200);
            byte[] picbyte =Bitmap2Bytes(bmp);
            String imageBase="";
            imageBase= Base64.encodeToString(picbyte,Base64.DEFAULT);
            Intent intent=getIntent();
            intent.putExtra("image_base",imageBase);
            setResult(RESULT_OK, intent);
            releaseCamera();
            finish();//关闭现有界面

        }
    };

    //activity生命周期在onResume是界面应是显示状态
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera==null){//如果此时摄像头值仍为空
            mCamera=getCamera();//则通过getCamera()方法开启摄像头
            if(mHolder!=null){
                setStartPreview(mCamera,mHolder);//开启预览界面
            }
        }
    }
    //activity暂停的时候释放摄像头
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
    //onResume()中提到的开启摄像头的方法


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera!=null)mCamera.release();
    }

    private Camera getCamera(){
        Camera camera;//声明局部变量camera
        try{
            camera=Camera.open(cameraId);}//根据cameraId的设置打开前置摄像头
        catch (Exception e){
            camera=null;
            e.printStackTrace(); }
        return camera;
    }
    //开启预览界面
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try{
            camera.setPreviewTexture(surfaceTexture);//解决onPreviewFrame未调用添加的
            camera.setPreviewDisplay(holder);//上面那句话代替了这句
            orientation=90;//在人脸检测中用到
            camera.setDisplayOrientation(orientation);//如果没有这行你看到的预览界面就会是水平的
            camera.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    //定义释放摄像头的方法
    private void releaseCamera(){
        if(mCamera!=null){//如果摄像头还未释放，则执行下面代码
            mCamera.stopPreview();//1.首先停止预览
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.setPreviewCallback(null);//2.预览返回值为null
            mCamera.release(); //3.释放摄像头
            mCamera=null;//4.摄像头对象值为null
        }
    }

    //定义新建预览界面的方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        int size = previewSize.width * previewSize.height *ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8;
        mPreBuffer=new byte[size];
        mCamera.addCallbackBuffer(mPreBuffer);
        mCamera.setPreviewCallbackWithBuffer(this);

        mCamera.setPreviewCallback(this);
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //设置相机参数
        Camera.Parameters parameters=mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);//设置照片格式
        //获取屏幕宽高
        WindowManager wm1 = this.getWindowManager();
        int screenWidht = wm1.getDefaultDisplay().getWidth();
        int screenHeight = wm1.getDefaultDisplay().getHeight();
        Log.e(TAG,"屏幕宽高为："+screenWidht+","+screenHeight);
        //获取手机支持的预览宽高信息
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : previewSizeList) {
            Log.i(TAG, "previewSizeList size.width=" + size.width + "  size.height=" + size.height);
        }
        //选取最合适的预览参数来显示在surfaceview上，这里的surfaceview尺寸是全屏
        Camera.Size preSize = getCloselyPreSize(true, screenWidht, screenHeight, parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(preSize.width,preSize.height);
        Log.e(TAG,"预览宽高："+preSize.width+","+preSize.height);
        mCamera.setParameters(parameters);//这一行起关键作用，如果没有这一行，新设置的参数将无效
        mCamera.stopPreview();//如果预览界面改变，则首先停止预览界面
        setStartPreview(mCamera,mHolder);//调整再重新打开预览界面
        Log.e(TAG,"预览参数宽高："+width+","+height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();//预览界面销毁则释放相机
    }


    /**
     * 通过对比得到与宽高比最接近的预览尺寸（如果有相同尺寸，优先选择）
     *
     * @param isPortrait 是否竖屏
     * @param surfaceWidth 需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList 需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public static  Camera.Size getCloselyPreSize(boolean isPortrait, int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {
        int reqTmpWidth;
        int reqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (isPortrait) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == reqTmpWidth) && (size.height == reqTmpHeight)){
                Log.e(TAG,"找到合适的尺寸");
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) reqTmpWidth) / reqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                Log.e(TAG,"找到相近的尺寸");
                retSize = size;
            }
        }
        return retSize;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, final Camera camera) {
        //每200毫秒执行一次
        if(System.currentTimeMillis()-lastModirTime<=500){
            return;
        }
        lastModirTime=System.currentTimeMillis();
        Log.d(TAG, "onPreviewFrame: 开始获取预览帧");
        //旋转bitmap，为了将图片旋转至能检测的方向
        Matrix matrix=new Matrix();
        matrix.postRotate(orientation*-1);
        matrix.postScale(-1,1);

        //bitmap = Bytes2Bimap(bytes);//老是返回空，不是代码问题，解决办法如下
        ///////////////////////bitmap为空解决办法///////////////////////
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        YuvImage yuvimage=new YuvImage(bytes, ImageFormat.NV21, previewSize.width, previewSize.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);  //这里 80 是图片质量，取值范围 0-100，100为品质最高
        byte[] jdata = baos.toByteArray();//这时候 bmp 就不为 null 了
        bitmap = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
        ///////////////////////////解决完毕/////////////////////////////
        bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
        //下面代码必须要，人脸检测需要将bmp转为RGB_565

        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);

        //人脸检测比骄耗时，放在线程中执行，最好把上面的都写在子线程中，只是我没成功
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: 子线程开始执行");

                Message message=new Message();
//////////////////////////////android自带人脸检测//////////////////////////////
                FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_NUM);
                FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_NUM];
                realFaceNum = faceDetector.findFaces(bitmap, faces);
                //faces[0].eyesDistance();

                if(realFaceNum > 0){
                    faceExit=true;
                    Log.d(TAG,"子线程检测到人脸");
                    message.what=FACE_DETECT_SUCCESS;
                }else {
                    faceExit=false;
                    Log.d(TAG,"子线程未检测到人脸");
                    message.what=FACE_DETECT_FAILED;
                }
/////////////////////////////////dlib人脸检测////////////////////////////////////////
//                FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());
//                List<VisionDetRet> results = faceDet.detect(bitmap);
//                Log.d(TAG, "run: 即将进入检测人脸是否存在");
//                if(results.size()!=0){
//                    faceExit=true;
//                    Log.d(TAG,"子线程检测到人脸");
//                    message.what=FACE_DETECT_SUCCESS;
//                }else {
//                    faceExit=false;
//                    Log.d(TAG,"子线程未检测到人脸");
//                    message.what=FACE_DETECT_FAILED;
//                }
//                for (final VisionDetRet ret : results) {
//                    String label = ret.getLabel();
//                    int rectLeft = ret.getLeft();
//                    int rectTop = ret.getTop();
//                    int rectRight = ret.getRight();
//                    int rectBottom = ret.getBottom();
//                    // Get 68 landmark points
//                    ArrayList<Point> landmarks = ret.getFaceLandmarks();
//                    for (Point point : landmarks) {
//                        int pointX = point.x;
//                        int pointY = point.y;
//                        Log.d(TAG, "run: 特征点："+pointX+","+pointY);
//                    }
//                }
                handler.sendMessage(message);
            }
        }).start();
        Log.d(TAG, "onPreviewFrame: 主线程执行");

        Log.d(TAG, "onPreviewFrame: 获取预览帧");

        mCamera.addCallbackBuffer(mPreBuffer);
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case FACE_DETECT_SUCCESS:
                    Log.d(TAG, "handleMessage: 检测到人脸");
                    Toast.makeText(CameraActivity.this, "检测到人脸", Toast.LENGTH_SHORT).show();
                    break;
                case FACE_DETECT_FAILED:
                    Log.d(TAG, "handleMessage: 未检测到人脸");
                    Toast.makeText(CameraActivity.this, "未检测到人脸", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(CameraActivity.this, "检测失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

//动态申请相机权限，似乎没毛用
    private final int CAMERA_REQUEST_CODE = 1;
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions(CameraActivity.this,
                                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        } else {
           // tvPermissionStatus.setTextColor(Color.GREEN);
           // tvPermissionStatus.setText("相机权限已申请");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //tvPermissionStatus.setTextColor(Color.GREEN);
                //tvPermissionStatus.setText("相机权限已申请");
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

