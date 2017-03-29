<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/1/12
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"/>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <script src="resources/chart-2.4.0/Chart.js"></script>
  <script>
    $(function() {
      var height=document.documentElement.clientHeight;
      document.getElementById('iframe-page-content').style.height=height+'px';
    });
    var menuClick = function(menuUrl) {
      $("#iframe-page-content").attr('src',menuUrl);
    };
   function test(){
       $.ajax({
         type:"POST",
         url:"getSensorsInfo",
         success:function(data){
           alert("I am in!");

          //alert( data['name'].userID);
           var userarray=data['users'];
           alert(userarray[0].userID);
         }
     })
    }
  </script>
  <title>洪涝事件管理</title>
</head>
<body>
<div class="container">
  <div class="row clearfix">
    <div class="col-md-12 column">
      <nav class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">Flood</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li class="active">
              <a href="#">洪涝事件管理</a>
            </li>
            <li>
              <a href="#">水文传感器查看</a>
            </li>
          </ul>
          <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
              <input type="text" class="form-control" />
            </div> <input type="button" class="btn btn-default" value="搜索"/>
          </form>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <a>尊敬的${userName},您好！</a>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">个人中心<strong class="caret"></strong></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="#">我的事件</a>
                </li>
                <li>
                  <a href="/registerEvent">注册事件</a>
                </li>
                <li>
                  <a href="#">个人信息修改</a>
                </li>
                <li class="divider">
                </li>
                <li>
                  <a href="#">帮助</a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </nav>
    </div>
  </div>
  <div class="row clearfix">
    <div class="col-md-12 column">
      <div id="main-container">
        <div id="sidebar" class="col-md-2 column">
          <!-- 创建菜单树 -->
          <div class="col-md-12">
            <ul id="main-nav" class="nav nav-tabs nav-stacked" style="">
              <li>
                <a href="#systemSetting" class="nav-header collapsed secondmenu" data-toggle="collapse">
                  <i class="glyphicon glyphicon-cog"></i>订阅的事件
                  <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                </a>
                <ul id="systemSetting" class="nav nav-list collapse" style="height: 0px;">
                  <c:if test="${eventIDs!=null}">
                  <c:forEach items="${eventIDs}" varStatus="status">
                    <li><a href="#" onclick="menuClick('showSingleEventInformation/${eventIDs.get(status.index)}')"><i class="glyphicon glyphicon-info-sign"></i>${eventIDs.get(status.index)}</a></li>
                  </c:forEach>
                  </c:if>
                  <c:if test="${eventIDs==null}">
                    没有订阅的洪涝探测事件
                  </c:if>
                  </ul>
              </li>
            </ul>
          </div>
        </div>
        <div class="col-md-10 column">
          <div class="breadcrumbs" id="breadcrumbs">
            <!-- 面包屑导航 -->
            <ul class="breadcrumb">
              <li>
                <a href="#">Home</a>
              </li>
              <li class="active">Event Information</li>
            </ul>
          </div>

          <!-- 内容展示页 -->
          <div>
            <iframe id="iframe-page-content" src="test" width="100%"  frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"></iframe>
          </div>
        </div><!-- /.main-content -->
      </div><!-- /.main-container -->
    </div>
  </div>

</div>
</body>
</html>
