var ReloadTimeID;function onLoad(){parent.loading="N",parent.ShowType="OU","N"==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none")}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(e){if(parent.loading_var="Y","up"==e)var a="./"+parent.sel_gtype+"_browse/body_var.php";else a="./body_var.php";var n=document.getElementById("sel_lid").value;""==n&&top.swShowLoveI&&(n="1"),parent.body_var.location=a+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&league_id="+n,onload()}function chg_league(){obj_pg=document.getElementById("pg_txt");var e=document.getElementById("sel_lid");parent.sel_league=e.value,parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&league_id="+e.value+(""==e.value&&top.swShowLoveI?"3":""),""==e.value?(parent.pg=0,onload()):obj_pg.innerHTML=""}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg,onload())}