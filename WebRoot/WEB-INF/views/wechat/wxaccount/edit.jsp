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
		<title></title>
		<link rel="shortcut icon" href="favicon.ico">
		<link href="<%=basePath%>HPLUS/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
		<link href="<%=basePath%>HPLUS/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
		<link href="<%=basePath%>HPLUS/css/animate.min.css" rel="stylesheet">
		<link href="<%=basePath%>HPLUS/css/style.min862f.css?v=4.1.0" rel="stylesheet">
	</head>

	<body class="gray-bg">
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-sm-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>公众账号新增</h5>
							<div class="ibox-tools">
								<a href="javascript:history.go(-1);" class="close-link">
									<i class="fa fa-times"></i>
								</a>
							</div>
						</div>
						<div class="ibox-content">
							<form class="form-horizontal m-t" id="signupForm">
								<div class="form-group">
									<label class="col-sm-3 control-label">名称 <span style="color: red;">*</span>：</label>
									<div class="col-sm-8">
										<input id="WX_NAME" name="WX_NAME" class="form-control" required type="text">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">TOKEN：</label>
									<div class="col-sm-8">
										<input id="WX_TOKEN" name="WX_TOKEN" class="form-control" type="text" class="valid">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">微信号：</label>
									<div class="col-sm-8">
										<input id="WX_ACCOUNT" name="WX_ACCOUNT" class="form-control" type="text" class="error">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">公众号类型 <span style="color: red;">*</span>：</label>
									<div class="col-sm-8">
										<input id="公众号类型" name="公众号类型" class="form-control" type="text" required>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">E-MAIL：</label>
									<div class="col-sm-8">
										<input id="WX_EMAIL" name="WX_EMAIL" class="form-control" type="text" email="true">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">描述：</label>
									<div class="col-sm-8">
										<input id="WX_DES" name="WX_DES" class="form-control" type="email">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">APPID <span style="color: red;">*</span>：</label>
									<div class="col-sm-8">
										<input id="WX_APPID" name="WX_APPID" class="form-control" required type="email">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">APPSECRET <span style="color: red;">*</span>：</label>
									<div class="col-sm-8">
										<input id="WX_APPSECRET" name="WX_APPSECRET" class="form-control" required type="email">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">商户ID：</label>
									<div class="col-sm-8">
										<input id="WX_SHID" name="WX_SHID" class="form-control" type="email">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">商户秘钥：</label>
									<div class="col-sm-8">
										<input id="WX_SHMY" name="WX_SHMY" class="form-control" type="email">
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-8 col-sm-offset-3">
										<button class="btn btn-primary" type="submit">提交</button>
										<a href="javascript:history.go(-1);" class="btn btn-default">取消</a>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script src="<%=basePath%>HPLUS/js/jquery.min.js?v=2.1.4"></script>
		<script src="<%=basePath%>HPLUS/js/bootstrap.min.js?v=3.3.6"></script>
		<script src="<%=basePath%>HPLUS/js/content.min.js?v=1.0.0"></script>
		<script src="<%=basePath%>HPLUS/js/plugins/validate/jquery.validate.min.js"></script>
		<script src="<%=basePath%>HPLUS/js/plugins/validate/messages_zh.min.js"></script>
		<script src="<%=basePath%>HPLUS/js/demo/form-validate-demo.min.js"></script>
	</body>

</html>