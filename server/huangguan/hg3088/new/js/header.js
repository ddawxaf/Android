var today_count=0;var early_count=0;if(""+top.cgTypebtn=="undefined"){top.cgTypebtn="re_class"}if(""+top.head_gtype=="undefined"){top.head_gtype="FT"}if(""+top.head_FU=="undefined"){top.head_FU="FT"}if(""+top.head_btn=="undefined"){top.head_btn="today"}function getIframeId(a){return window.parent.document.getElementById(a)}function getMyselfId(a){return document.getElementById(a)}function onloaded(){if(top.casino!="SI2"){try{getMyselfId("live").style.display="none";getMyselfId("QA_row").style.display="none"}catch(a){}}var b=getMyselfId(top.cgTypebtn+"");b.className="type_on";try{if((navigator.appVersion).indexOf("MSIE 6")==-1){getMyselfId("download").style.visibility="visible"}}catch(a){}try{getMyselfId("today_btn").className="early"}catch(a){}try{getMyselfId("early_btn").className="early"}catch(a){}try{getMyselfId("rb_btn").className="rb"}catch(a){}try{getMyselfId(top.head_btn+"_btn").className=top.head_btn+"_on"}catch(a){}}function chg_head(f,e,g){top.RB_id="";top.hot_game="";if(top.swShowLoveI){e=3}if(top.showtype=="hgft"){e=3}var d="";d="&hot_game="+top.hot_game;parent.body.location=f+"&league_id="+e+d}function chg_type(f,e,i,h){if(top.swShowLoveI){e=3}if(top.showtype=="hgft"){e=3}parent.body.location=f+"&league_id="+e;if(h=="rb"){try{getMyselfId("title_FT").className="ord_sportFT_off noFloat";getMyselfId("wager_FT").className="hide_game_list";getMyselfId("title_BK").className="ord_sportBK_off noFloat";getMyselfId("wager_BK").className="hide_game_list"}catch(g){}}}function chg_index(g,f,i,h,e){top.swShowLoveI=false;top.cgTypebtn="re_class";parent.body.location.href=f;self.location.href=g}function chg_type_class(a){var d=getMyselfId(a+"");var c=getMyselfId(top.cgTypebtn+"");if(a!=top.cgTypebtn){d.className="type_on";try{c.className="type_out"}catch(b){}top.cgTypebtn=a}}function chg_button_bg(c,a){top.head_gtype=c;if(a=="early"||a=="today"||a=="rb"){chg_type_class("re_class")}sessionStorage.setItem("m_type",a);sessionStorage.setItem("g_type",top.head_gtype);if(a!="rb"){if(a=="early"){top.head_FU="FU"}else{top.head_FU="FT"}}try{getMyselfId(top.head_btn+"_btn").className=top.head_btn}catch(b){}top.head_btn=a;try{getMyselfId(a+"_btn").className=a+"_on"}catch(b){}}function changeLangx(a){top.cgTypebtn="re_class";top.langx=a;top.head_gtype="FT";top.head_FU="FT";top.head_btn="today";top.FT_lid=new Array();top.FU_lid=new Array();top.FSFT_lid=new Array();top.FT_lid["FT_lid_ary"]=FT_lid_ary="ALL";top.FT_lid["FT_lid_type"]=FT_lid_type="";top.FT_lid["FT_lname_ary"]=FT_lname_ary="ALL";top.FT_lid["FT_lid_ary_RE"]=FT_lid_ary_RE="ALL";top.FT_lid["FT_lname_ary_RE"]=FT_lname_ary_RE="ALL";top.FU_lid["FU_lid_ary"]=FU_lid_ary="ALL";top.FU_lid["FU_lid_type"]=FU_lid_type="";top.FU_lid["FU_lname_ary"]=FU_lname_ary="ALL";top.FSFT_lid["FSFT_lid_ary"]=FSFT_lid_ary="ALL";top.FSFT_lid["FSFT_lname_ary"]=FSFT_lname_ary="ALL";top.BK_lid=new Array();top.BU_lid=new Array();top.FSBK_lid=new Array();top.BK_lid["BK_lid_ary"]=BK_lid_ary="ALL";top.BK_lid["BK_lid_type"]=BK_lid_type="";top.BK_lid["BK_lname_ary"]=BK_lname_ary="ALL";top.BK_lid["BK_lid_ary_RE"]=BK_lid_ary_RE="ALL";top.BK_lid["BK_lname_ary_RE"]=BK_lname_ary_RE="ALL";top.BU_lid["BU_lid_ary"]=BU_lid_ary="ALL";top.BU_lid["BU_lid_type"]=BU_lid_type="";top.BU_lid["BU_lname_ary"]=BU_lname_ary="ALL";top.FSBK_lid["FSBK_lid_ary"]=FSBK_lid_ary="ALL";top.FSBK_lid["FSBK_lname_ary"]=FSBK_lname_ary="ALL";top.BS_lid=new Array();top.BSFU_lid=new Array();top.FSBS_lid=new Array();top.BS_lid["BS_lid_ary"]=BS_lid_ary="ALL";top.BS_lid["BS_lid_type"]=BS_lid_type="";top.BS_lid["BS_lname_ary"]=BS_lname_ary="ALL";top.BS_lid["BS_lid_ary_RE"]=BS_lid_ary_RE="ALL";top.BS_lid["BS_lname_ary_RE"]=BS_lname_ary_RE="ALL";top.BSFU_lid["BSFU_lid_ary"]=BSFU_lid_ary="ALL";top.BSFU_lid["BSFU_lid_type"]=BSFU_lid_type="";top.BSFU_lid["BSFU_lname_ary"]=BSFU_lname_ary="ALL";top.FSBS_lid["FSBS_lid_ary"]=FSBS_lid_ary="ALL";top.FSBS_lid["FSBS_lname_ary"]=FSBS_lname_ary="ALL";top.TN_lid=new Array();top.TU_lid=new Array();top.FSTN_lid=new Array();top.TN_lid["TN_lid_ary"]=TN_lid_ary="ALL";top.TN_lid["TN_lid_type"]=TN_lid_type="";top.TN_lid["TN_lname_ary"]=TN_lname_ary="ALL";top.TN_lid["TN_lid_ary_RE"]=TN_lid_ary_RE="ALL";top.TN_lid["TN_lname_ary_RE"]=TN_lname_ary_RE="ALL";top.TU_lid["TU_lid_ary"]=TU_lid_ary="ALL";top.TU_lid["TU_lid_type"]=TU_lid_type="";top.TU_lid["TU_lname_ary"]=TU_lname_ary="ALL";top.FSTN_lid["FSTN_lid_ary"]=FSTN_lid_ary="ALL";top.FSTN_lid["FSTN_lname_ary"]=FSTN_lname_ary="ALL";top.VB_lid=new Array();top.VU_lid=new Array();top.FSVB_lid=new Array();top.VB_lid["VB_lid_ary"]=VB_lid_ary="ALL";top.VB_lid["VB_lid_type"]=VB_lid_type="";top.VB_lid["VB_lname_ary"]=VB_lname_ary="ALL";top.VB_lid["VB_lid_ary_RE"]=VB_lid_ary_RE="ALL";top.VB_lid["VB_lname_ary_RE"]=VB_lname_ary_RE="ALL";top.VU_lid["VU_lid_ary"]=VU_lid_ary="ALL";top.VU_lid["VU_lid_type"]=VU_lid_type="";top.VU_lid["VU_lname_ary"]=VU_lname_ary="ALL";top.FSVB_lid["FSVB_lid_ary"]=FSVB_lid_ary="ALL";top.FSVB_lid["FSVB_lname_ary"]=FSVB_lname_ary="ALL";top.OP_lid=new Array();
    top.OM_lid=new Array();top.FSOP_lid=new Array();top.OP_lid["OP_lid_ary"]=OP_lid_ary="ALL";top.OP_lid["OP_lid_type"]=OP_lid_type="";top.OP_lid["OP_lname_ary"]=OP_lname_ary="ALL";top.OP_lid["OP_lid_ary_RE"]=OP_lid_ary_RE="ALL";top.OP_lid["OP_lname_ary_RE"]=OP_lname_ary_RE="ALL";top.OM_lid["OM_lid_ary"]=OM_lid_ary="ALL";top.OM_lid["OM_lid_type"]=OM_lid_type="";top.OM_lid["OM_lname_ary"]=OM_lname_ary="ALL";top.FSOP_lid["FSOP_lid_ary"]=FSOP_lid_ary="ALL";top.FSOP_lid["FSOP_lname_ary"]=FSOP_lname_ary="ALL";top.head_btn="today";parent.location.href=((""+parent.location).replace("zh-tw",a).replace("zh-cn",a).replace("en-us",a))}var record_RB=0;function reloadRB(b,a){reloadPHP.location.href="./getrecRB.php?gtype="+b+"&uid="+top.uid}function showLayer(a){getMyselfId("RB_games").innerHTML=a;getMyselfId("FT_games").innerHTML=0;getMyselfId("BK_games").innerHTML=0;getMyselfId("TN_games").innerHTML=0;getMyselfId("BS_games").innerHTML=0;getMyselfId("VB_games").innerHTML=0;getMyselfId("OP_games").innerHTML=0}var nowTimer=0;var stimer=0;function autoZero(a){if(a<10){return"0"+a}return a}function headerShowTimer(c){var i=new Date(new Date().getTime()-43200000),h=i.getFullYear(),b=i.getMonth()+1,g=i.getDate(),f=i.getHours(),e=i.getMinutes(),a=i.getSeconds();b=b<10?"0"+b:b;g=g<10?"0"+g:g;f=f<10?"0"+f:f;e=e<10?"0"+e:e;a=a<10?"0"+a:a;var d=h+"年"+b+"月"+g+"日"+" "+f+":"+e+":"+a;$(c).text(d)}function SetRB(b,a){reloadRB(b,top.uid);setInterval("reloadRB('"+b+"','"+top.uid+"')",60*1000)}function openPublicWindow(a){window.open(a,"win","width=980,height=650,top=0,left=0,status=no,toolbar=no,scrollbars=yes,resizable=no,personalbar=no")}function OnMouseOverEvent(){}function OnMouseOutEvent(){}function setHeaderInit(a){getIframeId("head_cre").innerHTML=a;showMyaccount()}function Go_Chg_pass(){var a="../../../app/member/account/chg_passwd.php?uid="+top.uid+"&langx="+top.langx;Real_Win=openPublicWindow(a)}function OpenLive(){if(top.liveid==undefined){parent.self.location="";return}var a="./live/live.php?langx="+top.langx+"&uid="+top.uid+"&liveid="+top.liveid;openPublicWindow(a)}function openPublicAction(b){var a="../../../app/member/account/all_account_page.php?wintype="+b+"&langx="+top.langx+"&uid="+top.uid;openPublicWindow(a)}function showMyaccount(){var a=getIframeId("sel_div_acc");var b=getIframeId("sel_div_langx");var c=getIframeId("sel_div_help");if(a){a.onclick=function(d){getIframeId("div_acc").style.display="block"}}if(b){b.onclick=function(d){getIframeId("div_langx").style.display="block"}}if(c){c.onclick=function(d){getIframeId("div_help").style.display="block"}}}function hideDiv(a){var b=getMyselfId(a);if(b!=null){b.style.display="none"}}function hideMoney(a){if(a){getMyselfId("head_cre").className="head_hideCre";getMyselfId("credit").style.display="none";getMyselfId("show_balance").style.display="block";getMyselfId("hide_balance").style.display="none";getMyselfId("div_acc").style.display="none"}else{getMyselfId("head_cre").className="";getMyselfId("credit").style.display="inline-block";getMyselfId("show_balance").style.display="none";getMyselfId("hide_balance").style.display="block";getMyselfId("div_acc").style.display="none"}}function chkDelAllShowLoveI(getGtype){top.ShowLoveIarray[getGtype]=new Array();top.ShowLoveIOKarray[getGtype]="";if(top.swShowLoveI){top.swShowLoveI=false;eval("parent."+parent.body.sel_gtype+"_lid_type=top."+parent.body.sel_gtype+"_lid['"+parent.body.sel_gtype+"_lid_type']");parent.body.pg=0;parent.body.body_browse.reload_var("up")}else{parent.body.ShowGameList()}showTable();parent.body.body_browse.futureShowGtypeTable()}function mouseEnter_pointer(a){try{var b=a.split("_")[1];var d=top.ShowLoveIarray[b].length;if(d!=0){getMyselfId(a).style.display="block"}}catch(c){}}function mouseOut_pointer(a){try{getMyselfId(a).style.display="none"}catch(b){}}try{showGtype=top.gtypeShowLoveI;var xx=showGtype.length}catch(E){initDate();showGtype=top.gtypeShowLoveI}function initDate(){top.gtypeShowLoveI=new Array("FTRE","FT","FU","BKRE","BK","BU","BSRE","BS","BSFU","TNRE","TN","TU","VBRE","VB","VU","OPRE","OP","OM");top.ShowLoveIarray=new Array();top.ShowLoveIOKarray=new Array();for(var a=0;a<top.gtypeShowLoveI.length;a++){top.ShowLoveIarray[top.gtypeShowLoveI[a]]=new Array();top.ShowLoveIOKarray[top.gtypeShowLoveI[a]]=new Array()}}function reloadCrditFunction(){window.reloadPHP1.location.href="reloadCredit.php?uid="+top.uid+"&langx="+top.langx}function reloadCredit(a){var b=a.split(" ");top.mcurrency=b[0];getMyselfId("credit").innerHTML=a}function OpenLive(a,b){if(top.liveid==undefined){parent.self.location="";return}window.open("/app/member/live/live.php?langx="+top.langx+"&uid="+top.uid+"&liveid="+top.liveid+"&eventid="+a+"&gtype="+b,"Live","width=780,height=585,top=0,left=0,status=no,toolbar=no,scrollbars=no,resizable=no,personalbar=no")}function goToOldVersion(){var e=document.getElementsByClassName("goto_old_version")[0];var f=getCookieAction("username");var b=getCookieAction("password");var a=window.location.host.split(".");
    var c;var d;if(a.length<3){c=a[0];d=a[1]}else{c=a[1];d=a[2]}};