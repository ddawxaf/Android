var ReloadTimeID;function onLoad(){parent.sel_gtype="TU",parent.loading="N",parent.ShowType="OU","N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none"),futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(e){if(parent.loading_var="Y","up"==e)var a="./TN_future/body_var.php";else a="./body_var.php";var t=document.getElementById("sel_lid");parent.body_var.location=a+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date+"&league_id="+t.value}function chg_gdate(){var e=document.getElementById("g_date"),a=document.getElementById("sel_lid");parent.sel_league=a.value;var t="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+e.value+"&mtype="+parent.ltype+"&league_id="+a.value;parent.pg=0,parent.body_var.location=t}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date)}