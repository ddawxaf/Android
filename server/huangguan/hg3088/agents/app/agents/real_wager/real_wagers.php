<?php
session_start();
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT"); 
header("Cache-Control: no-cache, must-revalidate");      
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");

include "../../agents/include/address.mem.php";
// echo "<script>if(self == top) parent.location='".BROWSER_IP."'</script>\n";
require_once ("../../agents/include/config.inc.php");
include_once ("../include/redis.php");
checkAdminLogin(); // 同一账号不能同时登陆

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" || !isset($_SESSION['is_admin']) || $_SESSION['is_admin'] != ADMINLOGINFLAG ) {
    echo "<script>alert('您的登录信息已过期,请重新登录!');top.location.href='/';</script>";
    exit;
}
//print_r($_REQUEST);
$uid=$_REQUEST["uid"];
$langx=$_SESSION["langx"];
$loginname=$_SESSION['UserName'];
$gtype=$_REQUEST['gtype'];//项目
$ptype=$_REQUEST['ptype'];//类型
require ("../../agents/include/traditional.$langx.inc.php");

if ($ptype==''){
	$ptype='S';
}
switch ($ptype){
	case "S":
	    $back_ou="#3399FF";
		$width=975;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=40>场次</td>
			<td width=250 nowrap>队伍</td>
			<td width=100>独赢 / 注单</td>
			<td width=160>让球 / 注单</td>
			<td width=160>大小盘 / 注单</td>
			<td width=110>单双 / 注单</td>";
        break;
	case "H":	
	    $back_hou="#3399FF";
		$width=860;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=40>场次</td>
			<td width=250 nowrap>队伍</td>
			<td width=100>独赢 / 注单</td>
			<td width=160>让球 / 注单</td>
			<td width=160>大小盘 / 注单</td>";
        break;
	case "RB":			
	    $back_re="#3399FF";
		$width=975;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=40>场次</td>
			<td width=250 nowrap>队伍</td>
			<td width=70>独赢</td>
			<td width=100>全场让球 / 注单</td>
			<td width=100>全场大小 / 注单</td>
			<td width=70>上半独赢</td>
			<td width=100>上半让球 / 注单</td>
			<td width=100>上半大小 / 注单</td>";
        break;
	case "PD":			
	    $back_pd="#3399FF";
		$width=520;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=250>主客队伍</td>
			<td width=120>波胆</td>";
		break;
	case "HPD":			
	    $back_hpd="#3399FF";
		$width=520;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=250>主客队伍</td>
			<td width=120>波胆</td>";
		break;
	case "T":			
	    $back_eo="#3399FF";
		$width=720;
        $table="<td width=40>时间</td>
		    <td width=105 nowrap>联盟</td>
			<td width=250>主客队伍</td>
			<td width=80>0~1</td>
			<td width=80>2~3</td>
			<td width=80>4~6</td>
			<td width=80>7up</td>";
		break;
	case "F":			
	    $back_f="#3399FF";
		$width=520;
        $table="<td width=40>时间</td>
			<td width=105 nowrap>联盟</td>
			<td width=250>主客队伍</td>
			<td width=120>半全场</td>";
		break;
	case "P":			
	    $back_par="#3399FF";
		$width=450;
        $table="<td width=40>时间</td>
			<td width=50 nowrap>联盟</td>
			<td width=40>场次</td>
			<td width=200>队伍</td>
			<td width=120>过关</td>
			<td width=120>综合过关</td>";
		break;	
	case "PL":			
	    $back_p="#3399FF";
		$width=975;
        $table="<td nowrap width=40>时间</td>
			<td nowrap width=105>联盟</td>
			<td nowrap width=40>场次</td>
			<td nowrap width=250>队伍</td>
			<td nowrap width=190>让球</td>
			<td nowrap width=100>大小盘</td>
			<td nowrap width=100>滚球</td>
			<td nowrap width=100>滚球大小</td>
			<td nowrap width=40>功能</td>";
		break;		
}
?>
<html>
<head>
<title>main</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="../../../style/agents/control_main.css?v=<?php echo AUTOVER; ?>" type="text/css">

</head>
<body onLoad="onLoad('<?php echo $ptype?>')" onUnload="onUnload()"  onSelectStart="self.event.returnValue=false" oncontextmenu="self.event.returnValue=false;window.event.returnValue=false;">
<FORM NAME="REFORM" action="" method="POST">
    <dl class="main-nav"><dt>球赛管理 </dt>
        <dd>
	<table >
		<tr class="m_tline">
						<td nowrap>&nbsp;&nbsp;线上操盘&nbsp;:&nbsp;</td>
						<td>
							<select id="ltype" name="ltype" onChange="chg_ltype()" class="za_select_auto">
								<option value="1">A盘</option>
								<option value="2">B盘</option>
								<option value="3">C盘</option>
								<option value="4">D盘</option>
							</select>
						</td>
						<td nowrap> -- 重新整理&nbsp;:&nbsp;</td>
						<td>
							<select id="retime" name="retime" onChange="chg_retime()" class="za_select_auto">
								<option value="-1" selected>不更新</option>
								<option value="180">180 sec</option>
							</select>
						</td>
						<td id="dt_now" nowrap> -- 美东时间&nbsp;:&nbsp;</td>
						<td nowrap> --
                            &nbsp;<A HREF="#" onClick="chg_page('S');" onMouseOver="window.status='单式'; return true;" onMouseOut="window.status='';return true;" style="background-color:<?php echo $back_ou?>">单式</a>
                            <?php if (in_array($gtype,array('FT','FU','BS','BE','OP','OM'))){?>
							&nbsp;<A HREF="#" onClick="chg_page('H');" onMouseOver="window.status='上半场'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_hou?>">上半场</a>
                            <?php } ?>
                            <?php if (in_array($gtype,array('FT','BK','BS','TN','VB','OM'))){?>
							&nbsp;<A HREF="#" onClick="chg_page('RB');" onMouseOver="window.status='走地'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_re?>">滚球</a>
                            <?php } ?>
                            <?php if (in_array($gtype,array('FT','FU','OP','OM'))){?>
							&nbsp;<A HREF="#" onClick="chg_page('PD');" onMouseOver="window.status='波胆'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_pd?>">波胆</a>
							&nbsp;<A HREF="#" onClick="chg_page('HPD');" onMouseOver="window.status='上半波胆'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_hpd?>">上半波胆</a>
							&nbsp;<A HREF="#" onClick="chg_page('T');" onMouseOver="window.status='总入球'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_eo?>">总入球</a>
							&nbsp;<A HREF="#" onClick="chg_page('F');" onMouseOver="window.status='半全场'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_f?>">半全场</a>
                            <?php } ?>
							&nbsp;<A HREF="#" onClick="chg_page('P');" onMouseOver="window.status='过关'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_par?>">过关</a>
                            <?php if (in_array($gtype,array('FT','BK','BS','TN','VB','OM'))){?>
							&nbsp;<A HREF="#" onClick="chg_page('PL');" onMouseOver="window.status='已开赛'; return true;" onMouseOut="window.status='';return true;"style="background-color:<?php echo $back_p?>">已开赛</a>
                            <?php } ?>
						</td>
		   </tr>
	</table>
  </dd>
 </dl>
<div class="main-ui">
	<table class="m_tab">
		<tr>
			<td nowrap><font color="#000099">&nbsp;&nbsp;单式</font></td>
			<td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;观看方式</td>
			<td>&nbsp;:&nbsp;<select id="set_account" name="set_account" onChange="chg_account(this.value);" class="za_select">
					<option value="0">全部</option>
					<option value="1">自己</option>
				</select>
			</td>
			<td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;选择联盟&nbsp;:&nbsp;</td><td><span id="show_h"></span></td>
			<td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;<span id="pg_txt"></span></td>
		</tr>
	</table>
	<div id="LoadLayer" style="position:absolute; width:1020px; height:500px; z-index:1; background-color: #F3F3F3; layer-background-color: #F3F3F3; border: 1px none #000000; visibility: visible">
		<div align="center" valign="middle">
			loading...............................................................................
		</div>
	</div>
	<table id="glist_table" class="m_tab" >
		<tr class="m_title_<?php echo $gtype?>">
			<?php echo $table?>
		</tr>
	</table>
</div>
</form>
<div class="main-ui">

    <form name="line_form" id=line_form action="real_wagers_var_pl_detail.php" method="post" target=showdata>
        <div class="t_div" id="line_window" style="visibility:hidden;position: absolute;">
            <input type="hidden" name='uid' value='<?php echo $uid?>'>
            <input type="hidden" name='gid' value=''>
            <input type="hidden" name='set_acc' value=''>
            <!--input type="hidden" name='cid' value=''-->
            <table id="gdiv_table"  class="m_tab">
                <tr class="m_title">
                    <td nowrap>独赢</td>
                    <td nowrap>滚球独赢</td>
                    <td nowrap>波胆</td>
                    <td nowrap>半场波胆</td>
                    <td nowrap>单双</td>
                    <td nowrap>总入球</td>
                    <td nowrap>半全场</td>
                    <td nowrap>过关</td>
                    <td nowrap>综合过关</td>
                    <td nowrap>半场滚球让球</td>
                    <td nowrap>半场滚球大小</td>
                    <td nowrap>半场滚球独赢</td>
                    <td nowrap>半场让球</td>
                    <td nowrap>半场大小</td>
                    <td nowrap>半场独赢</td>
                </tr>
            </table>
            <input type='button' class="za_button" onClick="document.all.line_window.style.visibility='hidden';" value='关闭'>
        </div>
    </form>
    <span id="bowling" style="position:absolute; display: none">
        <option value="*LEAGUE_ID*" *SELECT*>*LEAGUE_NAME*</option>
    </span>
    <span id="bodyH" style="position:absolute; display: none">
        <select id="sel_lid" name="sel_lid" onChange="chg_league(this.value);" class="za_select">
            <option value="">全部</option>
            *SHOW_H*
        </select>
    </span>
    <span id="bodyP" style="position:absolute; display: none">页次:&nbsp;*SHOW_P*</span>

</div>


<script type="text/javascript" src="../../../js/agents/FunctionGame.js?v=<?php echo AUTOVER; ?>"></script>

</body>
</html>