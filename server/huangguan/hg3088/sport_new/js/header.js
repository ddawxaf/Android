var cgTypebtn="re_class";var head_FU="FT";var head_btn="today";var liveid="";function onloadSet(b,c,a){document.getElementById(a).height=c}function close_bet(){$(".add-bet-container").show();document.getElementById("bet_order_frame").height=0;bet_order_frame.document.close();try{bet_order_frame.clearAllTimer()}catch(a){}}function onloaded(){var b=document.getElementById(cgTypebtn+"");b.className="type_on";try{if((navigator.appVersion).indexOf("MSIE 6")==-1){document.getElementById("download").style.visibility="visible"}}catch(a){}try{document.getElementById("today_btn").className="early"}catch(a){}try{document.getElementById("early_btn").className="early"}catch(a){}try{document.getElementById("rb_btn").className="rb"}catch(a){}try{document.getElementById(head_btn+"_btn").className=head_btn+"_on"}catch(a){}}function changeLangx(o){var m="re_class";var i=o;var x="FT";var h="FT";var j="today";var d=new Array();var v=new Array();var u=new Array();d["FT_lid_ary"]=FT_lid_ary="ALL";d["FT_lid_type"]=FT_lid_type="";d["FT_lname_ary"]=FT_lname_ary="ALL";d["FT_lid_ary_RE"]=FT_lid_ary_RE="ALL";d["FT_lname_ary_RE"]=FT_lname_ary_RE="ALL";v["FU_lid_ary"]=FU_lid_ary="ALL";v["FU_lid_type"]=FU_lid_type="";v["FU_lname_ary"]=FU_lname_ary="ALL";u["FSFT_lid_ary"]=FSFT_lid_ary="ALL";u["FSFT_lname_ary"]=FSFT_lname_ary="ALL";var c=new Array();var f=new Array();var s=new Array();c["BK_lid_ary"]=BK_lid_ary="ALL";c["BK_lid_type"]=BK_lid_type="";c["BK_lname_ary"]=BK_lname_ary="ALL";c["BK_lid_ary_RE"]=BK_lid_ary_RE="ALL";c["BK_lname_ary_RE"]=BK_lname_ary_RE="ALL";f["BU_lid_ary"]=BU_lid_ary="ALL";f["BU_lid_type"]=BU_lid_type="";f["BU_lname_ary"]=BU_lname_ary="ALL";s["FSBK_lid_ary"]=FSBK_lid_ary="ALL";s["FSBK_lname_ary"]=FSBK_lname_ary="ALL";var b=new Array();var k=new Array();var q=new Array();b["BS_lid_ary"]=BS_lid_ary="ALL";b["BS_lid_type"]=BS_lid_type="";b["BS_lname_ary"]=BS_lname_ary="ALL";b["BS_lid_ary_RE"]=BS_lid_ary_RE="ALL";b["BS_lname_ary_RE"]=BS_lname_ary_RE="ALL";k["BSFU_lid_ary"]=BSFU_lid_ary="ALL";k["BSFU_lid_type"]=BSFU_lid_type="";k["BSFU_lname_ary"]=BSFU_lname_ary="ALL";q["FSBS_lid_ary"]=FSBS_lid_ary="ALL";q["FSBS_lname_ary"]=FSBS_lname_ary="ALL";var t=new Array();var a=new Array();var w=new Array();t["TN_lid_ary"]=TN_lid_ary="ALL";t["TN_lid_type"]=TN_lid_type="";t["TN_lname_ary"]=TN_lname_ary="ALL";t["TN_lid_ary_RE"]=TN_lid_ary_RE="ALL";t["TN_lname_ary_RE"]=TN_lname_ary_RE="ALL";a["TU_lid_ary"]=TU_lid_ary="ALL";a["TU_lid_type"]=TU_lid_type="";a["TU_lname_ary"]=TU_lname_ary="ALL";w["FSTN_lid_ary"]=FSTN_lid_ary="ALL";w["FSTN_lname_ary"]=FSTN_lname_ary="ALL";var p=new Array();var e=new Array();var r=new Array();p["VB_lid_ary"]=VB_lid_ary="ALL";p["VB_lid_type"]=VB_lid_type="";p["VB_lname_ary"]=VB_lname_ary="ALL";p["VB_lid_ary_RE"]=VB_lid_ary_RE="ALL";p["VB_lname_ary_RE"]=VB_lname_ary_RE="ALL";e["VU_lid_ary"]=VU_lid_ary="ALL";e["VU_lid_type"]=VU_lid_type="";e["VU_lname_ary"]=VU_lname_ary="ALL";r["FSVB_lid_ary"]=FSVB_lid_ary="ALL";r["FSVB_lname_ary"]=FSVB_lname_ary="ALL";var g=new Array();var n=new Array();var l=new Array();g["OP_lid_ary"]=OP_lid_ary="ALL";g["OP_lid_type"]=OP_lid_type="";g["OP_lname_ary"]=OP_lname_ary="ALL";g["OP_lid_ary_RE"]=OP_lid_ary_RE="ALL";g["OP_lname_ary_RE"]=OP_lname_ary_RE="ALL";n["OM_lid_ary"]=OM_lid_ary="ALL";n["OM_lid_type"]=OM_lid_type="";n["OM_lname_ary"]=OM_lname_ary="ALL";l["FSOP_lid_ary"]=FSOP_lid_ary="ALL";l["FSOP_lname_ary"]=FSOP_lname_ary="ALL";var j="today";parent.location.href=((""+parent.location).replace("zh-tw",o).replace("zh-cn",o).replace("en-us",o))}function reloadRB(b,a){reloadPHP.location.href="./getrecRB.php?gtype="+b+"&uid="+a;chkMemOnline()}function chkMemOnline(){}function SetRB(b,a){reloadRB(b,a);setInterval("reloadRB('"+b+"','"+a+"')",60*1000)}function OpenLive(){if(liveid==undefined){parent.self.location="";return}window.open("./live/live.php?langx="+langx+"&uid="+uid+"&liveid="+liveid,"Live","width=780,height=580,top=0,left=0,status=no,toolbar=no,scrollbars=yes,resizable=no,personalbar=no")}function chkDelAllShowLoveI(getGtype){ShowLoveIarray[getGtype]=new Array();ShowLoveIOKarray[getGtype]="";if(swShowLoveI){swShowLoveI=false;eval("parent."+parent.body.sel_gtype+"_lid_type="+parent.body.sel_gtype+"_lid['"+parent.body.sel_gtype+"_lid_type']");parent.body.pg=0;parent.body.body_browse.reload_var("up")}else{parent.body.ShowGameList()}showTable();parent.body.body_browse.futureShowGtypeTable()}function mouseEnter_pointer(a){try{var b=a.split("_")[1];var d=ShowLoveIarray[b].length;if(d!=0){document.getElementById(a).style.display="block"}}catch(c){}}function mouseOut_pointer(a){try{document.getElementById(a).style.display="none"}catch(b){}}try{showGtype=gtypeShowLoveI;var xx=showGtype.length}catch(E){initDate();showGtype=gtypeShowLoveI}function initDate(){gtypeShowLoveI=new Array("FTRE","FT","FU","BKRE","BK","BU","BSRE","BS","BSFU","TNRE","TN","TU","VBRE","VB","VU","OPRE","OP","OM");ShowLoveIarray=new Array();ShowLoveIOKarray=new Array();for(var a=0;a<gtypeShowLoveI.length;a++){ShowLoveIarray[gtypeShowLoveI[a]]=new Array();
    ShowLoveIOKarray[gtypeShowLoveI[a]]=new Array()}};
