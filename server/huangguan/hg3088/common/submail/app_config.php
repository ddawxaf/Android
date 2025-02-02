<?php
    
    /*
     |--------------------------------------------------------------------------
     | Mail configs start
     |--------------------------------------------------------------------------
     | $mail_configs['appid']
     | $mail_configs['appkey']
     | $mail_configs['sign_type']
     |--------------------------------------------------------------------------
     */

    /*
     |--------------------------------------------------------------------------
     |Default API Domain 默认 API 服务域名 
     |--------------------------------------------------------------------------
     */
    $server='http://api.mysubmail.com/';

    /*
     |--------------------------------------------------------------------------
     |Reserve API domain  备用域名
     |--------------------------------------------------------------------------
     */

    // $server='https://api.submail.cn/';

    /*
     |--------------------------------------------------------------------------
     |US Silicon Valley Server 海外线路-美国硅谷
     |--------------------------------------------------------------------------
     */

    //$server='https://us-api.mysubmail.com/';

    
    /*
     |--------------------------------------------------------------------------
     | Mail 应用ID
     |--------------------------------------------------------------------------
     */
    
    
    $mail_configs['appid']='your_mail_appid';
    
    
    /*
     |--------------------------------------------------------------------------
     | Mail 应用密匙
     |--------------------------------------------------------------------------
     */
    
    
    $mail_configs['appkey']='your_mail_appkey';
    
    
    /*
     |--------------------------------------------------------------------------
     | Mail  验证模式
     |--------------------------------------------------------------------------
     | md5=md5 签名验证模式（推荐）
     | sha1=sha1 签名验证模式（推荐）
     | normal=密匙明文验证
     |--------------------------------------------------------------------------
     */
    
    
    $mail_configs['sign_type']='normal';
    
    
     /*
     |--------------------------------------------------------------------------
     | API 服务器节点配置
     |--------------------------------------------------------------------------
     */

    $mail_configs['server']=$server;

    /*
     |--------------------------------------------------------------------------
     | Mail configs end
     |--------------------------------------------------------------------------
     */
    
    
    
    
    
    
    /*
     |--------------------------------------------------------------------------
     | SMS configs start
     |--------------------------------------------------------------------------
     | $message_configs['appid']
     | $message_configs['appkey']
     | $message_configs['sign_type']
     |--------------------------------------------------------------------------
     */
    
    /*
     |--------------------------------------------------------------------------
     | SMS 应用ID
     |--------------------------------------------------------------------------
     */
    
    
    //$message_configs['appid']='your_message_appid';
    $message_configs['appid']='46692';
    
    /*
     |--------------------------------------------------------------------------
     | SMS 应用密匙
     |--------------------------------------------------------------------------
     */
    
    
    
    $message_configs['appkey']='87ca0250eddeb346f9fcdae698a66664';
    
    
    
    /*
     |--------------------------------------------------------------------------
     | SMS  验证模式
     |--------------------------------------------------------------------------
     | md5=md5 签名验证模式（推荐）
     | sha1=sha1 签名验证模式（推荐）
     | normal=密匙明文验证
     |--------------------------------------------------------------------------
     */


    $message_configs['sign_type']='md5';
    
    /*
     |--------------------------------------------------------------------------
     | API 服务器节点配置
     |--------------------------------------------------------------------------
     */

    $message_configs['server']=$server;
    
    /*
     |--------------------------------------------------------------------------
     | SMS configs end
     |--------------------------------------------------------------------------
     */
    
    /*
     |--------------------------------------------------------------------------
     | 国际短信 international SMS 应用ID
     |--------------------------------------------------------------------------
     */
    
    
    $intersms_configs['appid']='your_internationalsms_appid';
    
    /*
     |--------------------------------------------------------------------------
     |  国际短信  Appid 密匙
     |--------------------------------------------------------------------------
     */
    
    
    
    $intersms_configs['appkey']='your_internationalsms_appkey';
    
    
    
    /*
     |--------------------------------------------------------------------------
     | 国际短信  验证模式
     |--------------------------------------------------------------------------
     | md5=md5 签名验证模式（推荐）
     | sha1=sha1 签名验证模式（推荐）
     | normal=密匙明文验证
     |--------------------------------------------------------------------------
     */

    $intersms_configs['sign_type']='normal';

    /*
     |--------------------------------------------------------------------------
     | API 服务器节点配置
     |--------------------------------------------------------------------------
     */

    $intersms_configs['server']=$server;
    
    
    /*
     |--------------------------------------------------------------------------
     | 国际短信 international SMS configs end
     |--------------------------------------------------------------------------
     */


    /*
     |--------------------------------------------------------------------------
     | mobiedata configs start
     |--------------------------------------------------------------------------
     | $mobiedata_configs['appid']
     | $mobiedata_configs['appkey']
     | $mobiedata_configs['sign_type']
     |--------------------------------------------------------------------------
     */
    
    /*
     |--------------------------------------------------------------------------
     | 手机流量 应用ID
     |--------------------------------------------------------------------------
     */
    
    
    $mobiledata_configs['appid']='your_mobiedata_appid';
    
    /*
     |--------------------------------------------------------------------------
     | 手机流量 应用密匙
     |--------------------------------------------------------------------------
     */
    
    
    
    $mobiledata_configs['appkey']='your_mobiedata_appkey';
    
    
    
    /*
     |--------------------------------------------------------------------------
     | 手机流量  验证模式
     |--------------------------------------------------------------------------
     | md5=md5 签名验证模式（推荐）
     | sha1=sha1 签名验证模式（推荐）
     | normal=密匙明文验证
     |--------------------------------------------------------------------------
     */
    $mobiledata_configs['sign_type']='normal';
    
    

    /*
     |--------------------------------------------------------------------------
     | API 服务器节点配置
     |--------------------------------------------------------------------------
     */

    $mobiledata_configs['server']=$server;


    /*
     |--------------------------------------------------------------------------
     | mobiedata configs end
     |--------------------------------------------------------------------------
     */
    

    /*
     |--------------------------------------------------------------------------
     | voice configs start
     |--------------------------------------------------------------------------
     | $voice_configs['appid']
     | $voice_configs['appkey']
     | $voice_configs['sign_type']
     |--------------------------------------------------------------------------
     */
    
    /*
     |--------------------------------------------------------------------------
     | voice 应用ID
     |--------------------------------------------------------------------------
     */
    
    
    $voice_configs['appid']='your_voice_appid';
    
    /*
     |--------------------------------------------------------------------------
     | voice 应用密匙
     |--------------------------------------------------------------------------
     */
    
    
    
    $voice_configs['appkey']='your_voice_appkey';
    
    
    
    /*
     |--------------------------------------------------------------------------
     | voice  验证模式
     |--------------------------------------------------------------------------
     | md5=md5 签名验证模式（推荐）
     | sha1=sha1 签名验证模式（推荐）
     | normal=密匙明文验证
     |--------------------------------------------------------------------------
     */
    $voice_configs['sign_type']='normal';
    
     /*
     |--------------------------------------------------------------------------
     | API 服务器节点配置
     |--------------------------------------------------------------------------
     */

    $voice_configs['server']=$server;

    /*
     |--------------------------------------------------------------------------
     | voice configs end
     |--------------------------------------------------------------------------
     */
