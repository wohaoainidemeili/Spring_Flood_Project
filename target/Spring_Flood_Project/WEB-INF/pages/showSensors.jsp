<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/1/12
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <script src=" http://api.tianditu.com/api?v=4.0" type="text/javascript"></script>
  <script>
    var map;
    var zoom = 12;
    function onLoad() {
      map = new T.Map('mapDiv');
      map.centerAndZoom(new T.LngLat(116.40769, 39.89945), zoom);
    }
  </script>

  <title>洪涝事件管理</title>
</head>
<body onLoad="onLoad()">
<div class="container">
  <div class="row clearfix">
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
              </div> <button type="submit" class="btn btn-default">搜索</button>
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
  </div>
  <div class="row clearfix">
    <div class="box col-md-12 column" id="mapDiv">
      <div class="box-inner">

      </div>
    </div>
  </div>
</div>
</body>
</html>
