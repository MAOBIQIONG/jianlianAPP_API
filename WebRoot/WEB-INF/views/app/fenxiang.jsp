<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>上海建筑联盟</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<style type="text/css">
	.toutitle{
		text-align: left; 
	}
	.conetent{
		clear: both;
	}
	.conetent p img:first-child{
    	display: block;
    }
    
    .conetent img{
    	max-width: 100%;
    	max-height: 256px;
    }
	</style>
	
  </head>
  
  <body>
	<div class="content">
		<div style="clear: both;margin-bottom:0px;">
			<h3 class="toutitle">${fenData.TITLE}</h3>
		</div>
		<div style="padding-left: 5px;float: left;margin-top:-10px;margin-bottom:20px;padding:0px;">
			<span style="color: black;font-size: 12px;">${fenData.NAME}</span>&nbsp;&nbsp;<span style="font-size: 12px;">${fenData.CREATE_DATE}</sapn>
		</div>
		<div class="conetent">
			${fenData.CONETENT}
		</div>
		<div class="mui-col-sm-12" style="width:90%;margin:15px;padding:0px;float: left;">
       		<ul style="list-style: none;padding-left: 10px;margin:0px;padding:0px;">
	        	<li style="display: inline;">
	        		<span style="font-size: small;" id="num">收藏:</span>
	        		<span style="font-size: small;" id="num">${fenData.COLLECT_COUNT}</span>
				</li>
				<li style="display: inline;padding-left: 10px;">
					<span style="font-size: small;" id="num">转发:</span>
					<span style="font-size: small;" id="num">${fenData.SHARE_COUNT}</span>
				</li>
				<li style="display: inline;padding-left: 10px;">
         			    	<span style="font-size: small;" id="num">评论:</span>
					<span style="font-size: small;" id="num">${fenData.COMMENT_COUNT}</span>
				</li>
			</ul>
		</div>
	</div>
  </body>
</html>
