package com.hgapp.a0086.login.fastlogin;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hgapp.a0086.HGApplication;
import com.hgapp.a0086.Injections;
import com.hgapp.a0086.R;
import com.hgapp.a0086.base.HGBaseFragment;
import com.hgapp.a0086.base.IPresenter;
import com.hgapp.a0086.common.http.Client;
import com.hgapp.a0086.common.util.ACache;
import com.hgapp.a0086.common.util.HGConstant;
import com.hgapp.a0086.common.widgets.HGControlVideo;
import com.hgapp.a0086.data.LoginResult;
import com.hgapp.a0086.data.SportsPlayMethodRBResult;
import com.hgapp.a0086.homepage.handicap.BottombarViewManager;
import com.hgapp.a0086.homepage.online.OnlineFragment;
import com.hgapp.a0086.login.fastregister.RegisterFragment;
import com.hgapp.a0086.login.forgetpwd.ForgetPwdFragment;
import com.hgapp.a0086.login.resetpwd.ResetPwdDialog;
import com.hgapp.a0086.login.resetpwd.ResetPwdEvent;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;

public class LoginFragment extends HGBaseFragment implements LoginContract.View {

    LoginContract.Presenter presenter;
    /*@BindView(R.id.inputCodeLayout)
    InputCodeLayout inputCodeLayout;

    @BindView(R.id.verificationCodeView)
    VerificationCodeView verificationCodeView;*/
//    @BindView(R.id.upVideo)
//    UpVideoView upVideo;
    @BindView(R.id.hgControlVideoPlayer)
    HGControlVideo hgControlVideoPlayer;
    /*@BindView(R.id.sScrollView)
    LinearLayout sScrollView;*/
    @BindView(R.id.fgtLogin)
    LinearLayout fgtLogin;
    @BindView(R.id.fgtResgiter)
    FrameLayout fgtResgiter;
    @BindView(R.id.ivLoginType)
    ImageView ivLoginType;
    @BindView(R.id.etLoginType)
    EditText etLoginType;
    @BindView(R.id.tvLoginPwd)
    ImageView tvLoginPwd;
    @BindView(R.id.etLoginPwd)
    EditText etLoginPwd;
    @BindView(R.id.etLoginEyes)
    ImageView etLoginEyes;
    @BindView(R.id.loginRemeberPwd)
    CheckBox loginRemeberPwd;
    @BindView(R.id.btnLoginSubmit)
    Button btnLoginSubmit;
    @BindView(R.id.tvLoginForgetPwd)
    TextView tvLoginForgetPwd;
    @BindView(R.id.btnLoginUser)
    TextView btnLoginUser;
    @BindView(R.id.btnLoginRegister)
    TextView btnLoginRegister;

    @BindView(R.id.etRegisterIntro)
    EditText etRegisterIntro;
    @BindView(R.id.etRegisterUserName)
    EditText etRegisterUserName;
    @BindView(R.id.etRegisterPwd)
    EditText etRegisterPwd;
    @BindView(R.id.etRegisterPwdVerify)
    EditText etRegisterPwdVerify;
    @BindView(R.id.etRegisterPwdEyes)
    ImageView etRegisterPwdEyes;
    @BindView(R.id.etRegisterPwdVerifyEyes)
    ImageView etRegisterPwdVerifyEyes;
    @BindView(R.id.etRegisterWithDrawName)
    EditText etRegisterWithDrawName;
    @BindView(R.id.etRegisterWithDrawPwd)
    EditText etRegisterWithDrawPwd;
    @BindView(R.id.etRegisterBrithday)
    EditText etRegisterBrithday;
    @BindView(R.id.etRegisterAccountPhone)
    EditText etRegisterAccountPhone;
    @BindView(R.id.etRegisterWeChat)
    EditText etRegisterWeChat;
    @BindView(R.id.etRegisterResource)
    EditText etRegisterResource;
    @BindView(R.id.btnLoginDemo)
    Button btnLoginDemo;
    @BindView(R.id.btnLoginLayDemo)
    LinearLayout btnLoginLayDemo;
    @BindView(R.id.btnLoginBaodu)
    Button btnLoginBaodu;

    @BindView(R.id.layLineTel)
    LinearLayout layLineTel;
    @BindView(R.id.layLineWeChat)
    LinearLayout layLineWeChat;
    @BindView(R.id.layLineQQ)
    LinearLayout layLineQQ;

    @BindView(R.id.etRegisterAccountPhoneDemo)
    EditText etRegisterAccountPhoneDemo;
    @BindView(R.id.btnRegisterSubmit)
    Button btnRegisterSubmit;
    @BindView(R.id.btnRegisterSubmitDemo)
    Button btnRegisterSubmitDemo;
    OptionsPickerView optionsPickerViewState;
    private int resource = 1;
    List<String> stateList  = new ArrayList<String>();
    String pasth = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/login.mp4";


    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        loginFragment.setArguments(args);
        Injections.inject(loginFragment, null);
        return loginFragment;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_login;
    }


    private void initVideoControl(){
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        String url = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.login;
        //注意，用ijk模式播放raw视频，这个必须打开
        //GSYVideoManager.instance().enableRawPlay(HGApplication.instance().getApplicationContext());

        //系统内核模式
        PlayerFactory.setPlayManager(SystemPlayerManager.class);
        //切换渲染模式
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        ///exo raw 支持
        //String url =  RawResourceDataSource.buildRawResourceUri(R.raw.login).toString();
        hgControlVideoPlayer.setShowPauseCover(true);
        hgControlVideoPlayer.setUp(url, false, null, "");
        hgControlVideoPlayer.setLooping(true);
        hgControlVideoPlayer.startPlayLogic();
    }

    private void onVisiableStatusBar(){
        //Sofia.with(getActivity()).visiableStatusBar(View.VISIBLE).visiableNavigationBar(View.VISIBLE);
    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        //Sofia.with(getActivity()).visiableStatusBar(View.GONE);
//                .statusBarBackground(ContextCompat.getDrawable(getActivity(), R.drawable.status_shape));
        //cpAssertVideoToLocalPath();
//        Sofia.with(getActivity())
////                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        stateList.add(getString(R.string.games_register_where_net_ad));
        stateList.add(getString(R.string.games_register_where_bifen));
        stateList.add(getString(R.string.games_register_where_friends));
        stateList.add(getString(R.string.games_register_where_bass));

        initVideoControl();
        String telOn  = ACache.get(getContext()).getAsString("telOn");
        if(!Check.isEmpty(telOn)&&"true".equals(telOn)){
            layLineTel.setVisibility(View.VISIBLE);
        }else{
            layLineTel.setVisibility(View.GONE);
        }
        String chatOn  = ACache.get(getContext()).getAsString("chatOn");
        if(!Check.isEmpty(chatOn)&&"true".equals(chatOn)){
            layLineWeChat.setVisibility(View.VISIBLE);
        }else{
            layLineWeChat.setVisibility(View.GONE);
        }
        String qqOn  = ACache.get(getContext()).getAsString("qqOn");
        if(!Check.isEmpty(qqOn)&&"true".equals(qqOn)){
            layLineQQ.setVisibility(View.VISIBLE);
        }else{
            layLineQQ.setVisibility(View.GONE);
        }
        GameLog.log("telOn "+telOn+" chatOn "+chatOn+" qqOn "+qqOn);
        EventBus.getDefault().register(this);
        BottombarViewManager.getSingleton().onCloseView();
        String userName = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT);
        String pwd = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_PWD);
        if(!Check.isEmpty(userName)){
            etLoginType.setText(userName);
        }
        if(!Check.isEmpty(pwd)){
            loginRemeberPwd.setChecked(true);
            etLoginPwd.setText(pwd);
        }else{
            loginRemeberPwd.setChecked(false);
        }
        if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
            etLoginType.setText("");
            etLoginPwd.setText("");
        }

        optionsPickerViewState = new OptionsPickerBuilder(getContext(),new OnOptionsSelectListener(){

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                resource = options1;
                etRegisterResource.setText(stateList.get(options1));
            }
        }).build();
        optionsPickerViewState.setPicker(stateList);
        etRegisterResource.setText(getString(R.string.games_register_where_net_ad));
    }

    public void verifyStoragePermissions() {
        try {
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        "android.permission.READ_EXTERNAL_STORAGE","android.permission.READ_PHONE_STATE",
                        "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GameLog.log("获取权限异常:"+ e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        hgControlVideoPlayer.onVideoResume();
        hgControlVideoPlayer.startPlayLogic();
        // 重新开始播放器
        /*upVideo.resume();
        upVideo.start();*/
    }

    @Override
    public void onPause() {
        super.onPause();
        hgControlVideoPlayer.onVideoPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!Check.isNull(hgControlVideoPlayer)){
            hgControlVideoPlayer.release();
            //释放所有
            hgControlVideoPlayer.setVideoAllCallBack(null);
            GSYVideoManager.releaseAllVideos();
        }
        //_mActivity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

    }

    private void cpAssertVideoToLocalPath() {
        verifyStoragePermissions();
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(pasth);
            //myInput = this.getAssets().open("login.mp4");
            myInput = getResources().openRawResource(R.raw.login);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
                GameLog.log("长度 "+length);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            GameLog.log("IOException "+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }

    @Override
    public void postLoginResult(LoginResult loginResult) {
        showMessage(getString(R.string.games_login_success));
        //String userName, String agents, String loginTime, String birthday, String money, String phone, String test_flag, String oid, String alias) {
        EventBus.getDefault().post(new LoginResult(loginResult.getUserName(),loginResult.getAgents(),loginResult.getLoginTime(),loginResult.getBirthday(),loginResult.getMoney(),loginResult.getPhone(),loginResult.getTest_flag(),loginResult.getOid(),loginResult.getAlias(),loginResult.getUserid(),loginResult.getMembermessage().getMem_message()));
        if(loginRemeberPwd.isChecked()){
            ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_PWD, etLoginPwd.getText().toString());
        }else{
            ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_PWD, "");
        }
        popTo(LoginFragment.class,true);
        /*String resetPwf = ACache.get(getContext()).getAsString("ResetPwdEvent");
        GameLog.log("ResetPwdEvent before "+ACache.get(getContext()).getAsString("ResetPwdEvent"));
        if(resetPwf.equals("1")){
            ACache.get(getContext()).put("ResetPwdEvent","0");
            popTo(LoginFragment.class,true);
        }else{
            pop();
        }*/
        GameLog.log("用户登录成功：别名是 "+loginResult.getAlias());
        //正对每一个用户做数据缓存
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_STATUS+loginResult.getUserName(), "1");
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT, loginResult.getUserName());
        ACache.get(getContext()).put(HGConstant.USERNAME_ALIAS, loginResult.getAlias());
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT+loginResult.getUserName()+HGConstant.USERNAME_BIND_CARD, loginResult.getBindCard_Flag());
        ACache.get(getContext()).put(HGConstant.USERNAME_BUY_MIN, loginResult.getBetMinMoney());
        ACache.get(getContext()).put(HGConstant.USERNAME_BUY_MAX, loginResult.getBetMaxMoney());
        ACache.get(getContext()).put(HGConstant.DOWNLOAD_APP_GIFT_GOLD, loginResult.getDOWNLOAD_APP_GIFT_GOLD());
        ACache.get(getContext()).put(HGConstant.DOWNLOAD_APP_GIFT_DEPOSIT, loginResult.getDOWNLOAD_APP_GIFT_DEPOSIT());
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_INFO, JSON.toJSONString(loginResult));
        //试玩的时候有返回
        if(!Check.isNull(loginResult.getChess_demo_url())){
            ACache.get(getContext()).put(HGConstant.KY_DEMO_URL, loginResult.getChess_demo_url().getKy_demo_url());
            ACache.get(getContext()).put(HGConstant.LY_DEMO_URL, loginResult.getChess_demo_url().getLy_demo_url());
            ACache.get(getContext()).put(HGConstant.VG_DEMO_URL, loginResult.getChess_demo_url().getVg_demo_url());
        }
        //ACache.get(getContext()).put(HGConstant.HG_DEMO_URL, loginResult.getChess_demo_url().getHg_demo_url());
        //ACache.get(getContext()).put("HGConstant.uid", loginResult.getOid());
        GameLog.log("登录执行完成 "+loginResult.getAlias());
    }

    @Override
    public void postLoginResultError(String message) {
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT,etLoginType.getText().toString().trim());
        ResetPwdDialog.newInstance(message).show(getFragmentManager());
    }

    @Override
    public void success(SportsPlayMethodRBResult fullPayGameResult) {
        showMessage("SportsPlayMethodRBResult");
    }

    @Override
    public void successGet(LoginResult loginResult) {
        ACache.get(getContext()).put(HGConstant.USERNAME_ALIAS, loginResult.getAlias());
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
    }

    @Subscribe
    public void onEventMain(ResetPwdEvent resetPwdEvent){
        GameLog.log("重置密码成功 需要重新登录");
        etLoginPwd.setText(resetPwdEvent.getPwd());
        if(!Check.isNull(presenter)) {
            presenter.postLogin(HGConstant.PRODUCT_PLATFORM, resetPwdEvent.getName(), resetPwdEvent.getPwd());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        onVisiableStatusBar();
    }

    private void btnLoginSubmit(){
        // sScrollView.scrollTo(0,0);
        String loginType = etLoginType.getText().toString().trim();
        String loginPwd= etLoginPwd.getText().toString().trim();
        if(Check.isEmpty(loginType)){
            showMessage(getString(R.string.games_login_uname_error));
            return;
        }
        if(Check.isEmpty(loginPwd)||loginPwd.length()<6){
            showMessage(getString(R.string.games_login_pwd_error));
            return;
        }
        presenter.postLogin(HGConstant.PRODUCT_PLATFORM, loginType, loginPwd);
    }



    @OnClick({R.id.etLoginEyes,R.id.etRegisterPwdEyes,R.id.etRegisterPwdVerifyEyes,R.id.tvLoginForgetPwd,  R.id.btnLoginSubmit, R.id.btnLoginUser,R.id.btnLoginRegister,R.id.btnLoginDemo,R.id.btnLoginBaodu,R.id.btnRegisterSubmitDemo,R.id.etRegisterResource,R.id.btnRegisterSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etLoginEyes:
                if (etLoginPwd.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    etLoginEyes.setBackgroundResource(R.mipmap.icon_eye);
                    etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    etLoginEyes.setBackgroundResource(R.mipmap.icon_eye_close);
                    etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etLoginPwd.setSelection(etLoginPwd.getText().toString().length());
                break;
            case R.id.etRegisterPwdEyes:
                if (etRegisterPwd.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    etRegisterPwdEyes.setBackgroundResource(R.mipmap.icon_eye);
                    etRegisterPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    etRegisterPwdEyes.setBackgroundResource(R.mipmap.icon_eye_close);
                    etRegisterPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etRegisterPwd.setSelection(etRegisterPwd.getText().toString().length());
                break;
            case R.id.etRegisterPwdVerifyEyes:
                if (etRegisterPwdVerify.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    etRegisterPwdVerifyEyes.setBackgroundResource(R.mipmap.icon_eye);
                    etRegisterPwdVerify.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    etRegisterPwdVerifyEyes.setBackgroundResource(R.mipmap.icon_eye_close);
                    etRegisterPwdVerify.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etRegisterPwdVerify.setSelection(etRegisterPwdVerify.getText().toString().length());
                break;
            case R.id.tvLoginForgetPwd:
                EventBus.getDefault().post(new StartBrotherEvent(ForgetPwdFragment.newInstance(),SupportFragment.SINGLETASK));
                break;
            case R.id.etLoginType:
                // sScrollView.scrollTo(0,400);
                break;
            case R.id.btnLoginSubmit:
                btnLoginSubmit();
                break;
            case R.id.btnLoginUser:
                btnLoginLayDemo.setVisibility(View.GONE);
                fgtLogin.setVisibility(View.VISIBLE);
                fgtResgiter.setVisibility(View.GONE);
                RegisterFragment registerFragment =  RegisterFragment.newInstance();
                FragmentTransaction ft =getFragmentManager().beginTransaction().hide(registerFragment);
                ft.show(registerFragment);
                ft.commit();
                btnLoginUser.setBackground(getResources().getDrawable(R.drawable.btn_normal_top_click));
                btnLoginRegister.setBackground(null);
                break;
            case R.id.btnLoginRegister:
                btnLoginLayDemo.setVisibility(View.GONE);
                fgtLogin.setVisibility(View.GONE);
                fgtResgiter.setVisibility(View.VISIBLE);
                btnLoginUser.setBackground(null);
                btnLoginRegister.setBackground(getResources().getDrawable(R.drawable.btn_normal_top_click));
                //EventBus.getDefault().post(new StartBrotherEvent(RegisterFragment.newInstance(), SupportFragment.SINGLETASK));
                //start(RegisterFragment.newInstance());
                break;
            case R.id.btnLoginDemo:
                String havePhone = ACache.get(getContext()).getAsString("guest_login_must_input_phone");
                if(!Check.isEmpty(havePhone)&&havePhone.equals("true")){
                    btnLoginLayDemo.setVisibility(View.VISIBLE);
                    fgtLogin.setVisibility(View.GONE);
                    fgtResgiter.setVisibility(View.GONE);
                }else{
                    presenter.postLoginDemo(HGConstant.PRODUCT_PLATFORM,"demoguest","demoguest","nicainicainicaicaicaicai");
                }
                //presenter.postLoginDemo(HGConstant.PRODUCT_PLATFORM,"demoguest","nicainicainicaicaicaicai");
                break;
            case R.id.btnLoginBaodu:
                   String baodu =  ACache.get(getContext()).getAsString("android_baodu");
                   GameLog.log("报毒url："+baodu);
                   if(!Check.isEmpty(baodu)){
                       EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance("",baodu)));
                   }else{
                       showMessage(getString(R.string.comm_no_good_net));
                   }

                break;
            case R.id.btnRegisterSubmitDemo:
                String phone = etRegisterAccountPhoneDemo.getText().toString().trim();
                if(Check.isEmpty(phone)||phone.length()<10){
                    showMessage(getString(R.string.games_register_phone_h));
                    return;
                }
                presenter.postLoginDemo(HGConstant.PRODUCT_PLATFORM,phone,"demoguest","nicainicainicaicaicaicai");
                break;
            case R.id.etRegisterResource:
                optionsPickerViewState.show();
                break;
            case R.id.btnRegisterSubmit:
                onCheckRegisterMember();
                break;
        }

    }

    private void onCheckRegisterMember(){
        String introducer = etRegisterIntro.getText().toString().trim();
        String userName = etRegisterUserName.getText().toString().trim();
        String userPwd = etRegisterPwd.getText().toString().trim();
        String userBrithdayQQ = etRegisterBrithday.getText().toString().trim();
        String userPwdVerify = etRegisterPwdVerify.getText().toString().trim();
        String userDrawName = etRegisterWithDrawName.getText().toString().trim();
        String userDrawPwd = etRegisterWithDrawPwd.getText().toString().trim();
        String userPhone = etRegisterAccountPhone.getText().toString().trim();
        String userWechat = etRegisterWeChat.getText().toString().trim();
        if(Check.isEmpty(userName)){
            showMessage(getString(R.string.games_register_uname_error));
            return;
        }

        if(Check.isEmpty(userPwd)||userPwd.length()<6){
            showMessage(getString(R.string.games_register_pwd_error1));
            return;
        }

        if(Check.isEmpty(userPwdVerify)||userPwdVerify.length()<6){
            showMessage(getString(R.string.games_register_pwd_error2));
            return;
        }

        if(!userPwdVerify.equals(userPwd)){
            showMessage(getString(R.string.games_register_pwd_error3));
            return;
        }

        if(Check.isEmpty(userPhone)){
            showMessage(getString(R.string.games_register_phone_error));
            return;
        }

        /*if(Check.isEmpty(userDrawName)){
            showMessage("请输入真实姓名！");
            return;
        }
        if(Check.isEmpty(userDrawPwd)){
            showMessage("请输入提款密码！");
            return;
        }


        if(Check.isEmpty(userWechat)){
            showMessage("请输入微信号码！");
            return;
        }

        if(Check.isEmpty(userBrithday)){
            showMessage("请输入出生日期！");
            return;
        }*/

        /*if(Check.isEmpty(userVerificationCode)){
            showMessage("请输入正确的验证码");
            return;
        }*/
        //String appRefer,String introducer,String keys,String username,String password, String password2,String alias,
        //                                   String paypassword,String phone,String wechat,String birthday,String know_site

        presenter.postRegisterMember("",introducer,"add",userName,userPwd,userPwdVerify,userDrawName,userDrawPwd,userPhone,userWechat,userBrithdayQQ,resource+"",HGApplication.instance().getCommentData());

    }

    @Override
    public void postRegisterMemberResult(LoginResult loginResult) {
        showMessage(getString(R.string.games_register_success));
        //正对每一个用户做数据缓存
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_STATUS+loginResult.getUserName(), "1");
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT, loginResult.getUserName());
        ACache.get(getContext()).put(HGConstant.USERNAME_ALIAS, loginResult.getAlias());
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT+loginResult.getUserName()+HGConstant.USERNAME_BIND_CARD, loginResult.getBindCard_Flag());
        ACache.get(getContext()).put(HGConstant.USERNAME_BUY_MIN, loginResult.getBetMinMoney());
        ACache.get(getContext()).put(HGConstant.USERNAME_BUY_MAX, loginResult.getBetMaxMoney());
        ACache.get(getContext()).put(HGConstant.DOWNLOAD_APP_GIFT_GOLD, loginResult.getDOWNLOAD_APP_GIFT_GOLD());
        ACache.get(getContext()).put(HGConstant.DOWNLOAD_APP_GIFT_DEPOSIT, loginResult.getDOWNLOAD_APP_GIFT_DEPOSIT());
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_INFO, JSON.toJSONString(loginResult));
        popTo(LoginFragment.class,true);
        EventBus.getDefault().post(loginResult);
    }


    /*@Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new LoginAnimator();
    }*/


}

