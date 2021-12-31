<?php
/**
 * 优惠活动类型管理
 * Date: 2019/8/2
 * Time: 14:02
 */
session_start();
header("Expires: Mon, 26 Jul 1970 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");

require_once("../include/config.inc.php");
include_once ("../include/address.mem.php");
include_once ("../include/redis.php");
checkAdminLogin(); // 同一账号不能同时登陆

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" || !isset($_SESSION['is_admin']) || $_SESSION['is_admin'] != ADMINLOGINFLAG ) {
    echo "<script>alert('您的登录信息已过期,请重新登录!');top.location.href='/';</script>";
    exit;
}

$uid = $_REQUEST["uid"];
$langx = $_SESSION["langx"];
$loginname = $_SESSION['UserName'];

$type = $_REQUEST['type'];
$id = $_REQUEST['id'];
$name = $_REQUEST['name'];
$tag = $_REQUEST['tag'];
$sort = $_REQUEST['sort'];
$status = $_REQUEST['status'];
$now = date('Y-m-d H:i:s');

switch ($type){
    case 'del':
        $sqlPromos = "SELECT `id` FROM ". DBPREFIX . "web_promos` WHERE category_id = {$id}";
        $resultPromos = mysqli_query($dbMasterLink, $sql);
        $count = mysqli_num_rows($resultPromos);
        if($count){
            exit(json_encode(['code' => -2, 'msg' => '请先删除此类型下的优惠活动！']));
        }
        $sql = "DELETE FROM `" . DBPREFIX . "web_promos_category` WHERE `id` = {$id}";
        $result = mysqli_query($dbMasterLink, $sql);
        if($result){
            returnPromosType('edit');
            exit(json_encode(['code' => 0, 'msg' => '删除成功！']));
        }else{
            exit(json_encode(['code' => -1, 'msg' => '删除失败！']));
        }
        break;
    case 'edit':
        $sql = "UPDATE `" . DBPREFIX . "web_promos_category` SET `name`='{$name}', `tag`='{$tag}', `sort`='{$sort}', `status`='{$status}', `created_at`='{$now}', `updated_at`='{$now}' WHERE `id` = {$id}";
        $result = mysqli_query($dbMasterLink, $sql);
        if($result){
            returnPromosType('edit');
            exit(json_encode(['code' => 0, 'msg' => '更新成功！']));
        }else{
            exit(json_encode(['code' => -1, 'msg' => '更新失败！']));
        }
        break;
    case 'add':
        $sql = "INSERT INTO `" . DBPREFIX . "web_promos_category` VALUES ('','{$name}','{$tag}','{$status}','{$now}','{$now}')";
        $insertId = mysqli_query($dbMasterLink, $sql);
        if($insertId){
            returnPromosType('edit');
            echo "<script> alert('添加成功！'); </script>";
            echo "<meta http-equiv='Refresh' content='0;URL=promos_category.php?uid=$uid'>";
        }else{
            echo "<script> alert('添加失败！'); </script>";
        }
        break;
    default: break;
}

$sql = "SELECT `id`, `name`, `tag`, `sort`, `status`, `created_at`, `updated_at` FROM " . DBPREFIX . "web_promos_category";
$result = mysqli_query($dbLink, $sql);
$lists = array();
while ($row = mysqli_fetch_assoc($result)){
    $lists[$row['id']] = $row;
}
?>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="../../../style/agents/control_main.css?v=<?php echo AUTOVER; ?>" type="text/css">
    <title>优惠活动管理</title>
    <style>
        .list-tab td{line-height: 20px;}
        .list-tab input{ float: left;}
        input.tag{width: 300px;}
    </style>
</head>
<body>
<dl class="main-nav"><dt>活动类型管理</dt>
    <dd>
        &nbsp;<input type="button" class="za_button" onclick="location.href='promos.php?uid=<?php echo $uid;?>&lv=<?php echo $lv;?>&langx=<?php echo $langx;?>'" value="优惠活动列表" />
    </dd>
</dl>
<div class="main-ui">
    <table  class="m_tab">
        <tr  class="m_title" >
            <td width="5%">ID</td>
            <td width="10%">活动类型</td>
            <td width="30%">活动标识</td>
            <td width="2%">排序</td>
            <td width="5%">启用状态</td>
            <td width="20%">创建时间</td>
            <td width="20%">更新时间</td>
            <td width="10%">操作</td>
        </tr>
        <?php
        foreach ($lists as $k=>$value){?>
            <tr class=m_cen value="<?php echo $value['id']?>" style="text-align: center;">
                <td><?php echo $value['id'];?></td>
                <td><input type="text" name="name" id="name_<?php echo $value['id']?>" value="<?php echo $value['name']?>" /></td>
                <td><input class="tag" type="text" name="tag" id="tag_<?php echo $value['id']?>" value="<?php echo $value['tag']?>"/></td>
                <td><input class="text" type="text" name="sort" id="sort_<?php echo $value['id']?>" value="<?php echo $value['sort']?>"/></td>
                <td><input type="checkbox" name="status" id="status_<?php echo $value['id']?>" value="<?php echo $value['status']?>" <?php echo $value['status'] == 1 ? "checked" : "";?>></td>
                <td><?php echo $value['created_at']?></td>
                <td><?php echo $value['updated_at']?></td>
                <td>
                    <input type="button" class="za_button btn_edit_<?php echo $value['id']?>" onclick="btn_edit(<?php echo $value['id']?>,'<?php echo $uid?>','<?php echo $langx?>','<?php echo $loginname?>')" value="修改" />
                    <input type="button" onclick="btn_del(<?php echo $value['id']?>,'<?php echo $uid?>')" value="删除" />
                </td>
            </tr>
            <?php
        }

        ?>
        <tr class=m_cen >
            <td colspan="13">
                <input type="button" value="取消" class="za_button btn2" onclick="javascript:history.go(-1)" />
                <input type="button" class="za_button" onclick="javascript:$('#adds').show();" value="新增" />
            </td>
        </tr>
    </table>

    <div id="adds" style="display: none;">
        <div class="connects">
            <form id="newsadd" method="post" action="">
                <input type="hidden" name="uid" value="<?php echo $uid?>" />
                <input type="hidden" name="langx" value="<?php echo $langx?>" />
                <input type="hidden" name="type" value="add" />
                <table class="m_tab">
                    <tbody><tr><th>活动类型</th><th>活动标识</th><th>启用状态</th></tr>
                    <tr>
                        <td><input class="inp1" type="text" name="name" value=""></td>
                        <td><input class="inp1" type="text" name="tag" value=""></td>
                        <td><input type="checkbox" name="status" value="1"></td>
                    </tr>
                    <tr class=m_cen >
                        <td colspan="11">
                            <input type="button" value="新增" class="za_button btn2" onclick="javascript:$('#newsadd').submit();">
                            <input type="button" value="取消" class="za_button btn2" onclick="javascript:$('#adds').hide();">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript" src="../../../js/agents/jquery.js"></script>
<script type="text/javascript">
    function btn_del(id,uid) {
        var type = 'del';
        if(confirm("确定删除此类型？请先删除掉此类型下所有优惠活动")){
            $.ajax({
                type: "POST",
                url:"promos_category.php",
                data:{id : id,uid: uid,type : type},
                success:function (response) {
                    response = $.parseJSON(response);
                    alert(response.msg);
                    if (response.code == 0){
                        window.location.href='promos_category.php?uid='+uid;
                    }
                }
            });
        }
    }

    function btn_edit(id, uid, langx, loginname) {
        var type = 'edit';
        var name = $("#name_" + id).val();
        var tag = $("#tag_" + id).val();
        var sort = $("#sort_" + id).val();
        var obj = document.getElementById("status_"+id);
        var status = 0;
        if(obj.checked){
            status = 1;
        }

        // 异步请求更新数据
        $.ajax({
            type:"POST",
            url:"promos_category.php",
            data:{
                id : id,
                uid : uid,
                langx : langx,
                loginname : loginname,
                type : type,
                name : name,
                tag : tag,
                sort : sort,
                status : status
            },
            success:function(response) {
                response = $.parseJSON(response);
                alert(response.msg);
                if (response.code == 0){
                    window.location.href='promos_category.php?uid='+uid;
                }
            }
        })
    }
</script>
</body>
</html>