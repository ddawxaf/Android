var ReloadTimeID="";function onLoad(){""+parent.BSFU_lname_ary=="undefined"&&(parent.BSFU_lname_ary="ALL"),""+parent.BSFU_lid_ary=="undefined"&&(parent.BSFU_lid_ary="ALL"),""+parent.sel_gtype=="undefined"&&(parent.sel_gtype="BSFU"),""==parent.ShowType&&(parent.ShowType="OU"),"Y"==parent.parent.leg_flag&&(parent.parent.leg_flag="N",parent.pg=0,reload_var()),(parent.loading="N")==parent.loading_var&&(parent.ShowGameList(),obj_layer=document.getElementById("LoadLayer"),obj_layer.style.display="none"),"Y"==parent.retime_flag&&(ReloadTimeID=setInterval("reload_var()",1e3*parent.retime)),futureShowGtypeTable()}function count_down(){if(setTimeout("count_down()",1e3),"Y"==parent.retime_flag){if(parent.retime<=0)return void("N"==parent.loading_var&&reload_var());parent.retime--,obj_cd=document.getElementById("cd"),obj_cd.innerHTML=parent.retime}}function reload_var(e){if(parent.loading_var="Y","up"==e)var t="./BS_future/body_var.php";else t="./body_var.php";parent.body_var.location=t+"?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date+"&league_id="+parent.parent.BSFU_lid_type,document.all.line_window.style.visibility="hidden"}function chg_gdate(){var e=document.getElementById("g_date"),t="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&g_date="+e.value+"&mtype="+parent.ltype+"&league_id="+parent.parent.BSFU_lid_type;parent.pg=0,parent.body_var.location=t}function chg_pg(e){e!=parent.pg&&(parent.pg=e,parent.loading_var="Y",parent.body_var.location="./body_var.php?uid="+parent.uid+"&rtype="+parent.rtype+"&langx="+parent.langx+"&mtype="+parent.ltype+"&page_no="+parent.pg+"&g_date="+parent.g_date)}function show_more(e){document.all.line_window.style.position="absolute",document.all.line_window.style.top=document.body.scrollTop+event.clientY+12,document.all.line_window.style.left=document.body.scrollLeft+5,line_form.gid.value=e,line_form.uid.value=parent.uid,line_form.ltype.value=parent.ltype,line_form.submit()}function show_detail(){show_team=document.getElementById("table_team"),show_pd=document.getElementById("table_pd"),show_t=document.getElementById("table_t"),parent.ShowData_Other(show_team,show_pd,show_t,GameOther,top.odd_f_type),document.all.line_window.style.visibility="visible",document.all.line_window.focus()}function unload(){clearInterval(ReloadTimeID)}window.onunload=unload;