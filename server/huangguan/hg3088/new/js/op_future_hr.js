var ReloadTimeID="";function onLoad(){parent.loading="N",parent.ShowType="OU","Y"==parent.parent.leg_flag&&(parent.parent.leg_flag="N",parent.pg=0,reload_var()),"N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none"),"Y"==parent.retime_flag&&(ReloadTimeID=setInterval("reload_var()",1e3*parent.retime)),futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(e){if(parent.loading_var="Y","up"==e)var a="./OP_future/body_var.php";else a="./body_var.php";parent.body_var.location=a+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&league_id="+parent.parent.OM_lid_type+"&g_date="+parent.g_date}function chg_gdate(){var e=document.getElementById("g_date"),a="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+e.value+"&mtype="+parent.ltype+"&league_id="+parent.parent.OM_lid_type;parent.pg=0,parent.body_var.location=a}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&page="+parent.g_date)}function unload(){clearInterval(ReloadTimeID)}window.onunload=unload;