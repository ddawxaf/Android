var ReloadTimeID;function onLoad(){parent.sel_gtype="VU",parent.loading="N",parent.ShowType="OU","N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none");var e="";for(e='<select id="g_date" name="g_date" onChange="chg_gdate()">',e+='<option value="ALL" selected>'+top.alldata+"</option>",i=0;i<DateAry.length;i++)e+='<option value="'+DateAry[i]+'" >'+DateAry[i]+"</option>";e+="</select>",document.getElementById("show_date_opt").innerHTML=e,futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(e){if(parent.loading_var="Y","up"==e)var t="./VB_future/body_var.php";else t="./body_var.php";var a=document.getElementById("sel_lid");parent.body_var.location=t+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date+"&league_id="+a.value}function chg_gdate(){var e=document.getElementById("g_date"),t=document.getElementById("sel_lid");parent.sel_league=t.value;var a="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+e.value+"&mtype="+parent.ltype+"&league_id="+t.value;parent.pg=0,parent.body_var.location=a}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date)}