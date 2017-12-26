<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="favicon.ico"> <link href="<%=basePath%>HPLUS/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="<%=basePath%>HPLUS/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="<%=basePath%>HPLUS/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="<%=basePath%>HPLUS/css/animate.min.css" rel="stylesheet">
    <link href="<%=basePath%>HPLUS/css/style.min862f.css?v=4.1.0" rel="stylesheet">
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <!-- Panel Other -->
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5>公众账号管理</h5>
            </div>
            <div class="ibox-content">
                <div class="row row-lg">

                    <div class="col-sm-12">
                        <!-- Example Toolbar -->
                        <div class="example-wrap">
                            <div class="example">
                                <div class="btn-group hidden-xs" id="exampleToolbar" role="group">
                                    <a href="wxaccount/add" class="btn btn-outline btn-default" >
                                        <i class="glyphicon glyphicon-plus" aria-hidden="true"></i>
                                    </a>
                                </div>
                                <table id="exampleTableToolbar" data-mobile-responsive="true">
                                    <thead>
                                        <tr>
                                            <th data-field="WX_NAME">名称</th>
                                            <th data-field="WX_TOKEN">TOKEN</th>
                                            <th data-field="WX_ACCOUNT">微信号</th>
                                            <th data-field="WX_TYPE">公众号类型</th>
                                            <th data-field="WX_EMAIL">E-MAIL</th>
                                            <th data-field="WX_DES">描述</th>
                                            <th data-field="WX_APPID">APPID</th>
                                            <th data-field="WX_APPSECRET">APPSECRET</th>
                                            <th data-field="WX_SHID">商户ID</th>
                                            <th data-field="WX_SHMY">商户秘钥</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                        <!-- End Example Toolbar -->
                    </div>

                </div>
            </div>
        </div>
        <!-- End Panel Other -->
    </div>
    <script src="<%=basePath%>HPLUS/js/jquery.min.js?v=2.1.4"></script>
    <script src="<%=basePath%>HPLUS/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="<%=basePath%>HPLUS/js/content.min.js?v=1.0.0"></script>
    <script src="<%=basePath%>HPLUS/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="<%=basePath%>HPLUS/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
    <script src="<%=basePath%>HPLUS/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="<%=basePath%>HPLUS/js/demo/bootstrap-table-demo.min.js"></script>
</body>
<!-- Mirrored from www.zi-han.net/theme/hplus/table_bootstrap.html by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:20:06 GMT -->
</html>
