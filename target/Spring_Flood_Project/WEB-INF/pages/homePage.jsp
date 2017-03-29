<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/1/11
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="resources/fullPage-2.9.2/jquery.fullPage.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <script src="resources/fullPage-2.9.2/jquery.easings.min.js"></script>
  <script src="resources/fullPage-2.9.2/jquery.fullPage.js"></script>
  <script src="resources/fullPage-2.9.2/scrolloverflow.min.js"></script>
  <script src="resources/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
  <script>
    $(function(){
      $('#fullpage').fullpage(
              {
                slidesColor: ['#1bbc9b', '#4BBFC3', '#7BAABE', '#f90'],
                anchors: ['page1', 'page2', 'page3', 'page4']
              }
      );
    });
  </script>

  <title>全生命周期洪涝事件探测系统</title>
</head>
<body>
<div id="fullpage">
  <div class="fp-section">事件基本信息
    <div class="container">
      <div class="row clearfix">
        <div class="col-md-6 column">
          <canvas id="floodPart" style="width:100%;height: 20%;padding-top:0px;margin-top:0px;background-color: khaki"></canvas>
        </div>
      </div>
    </div>

  </div>
  <div class="fp-section">洪涝事件图</div>
  <div class="fp-section">
    <div class="fp-slide">第三屏幕第一个</div>
    <div class="fp-slide">第三屏幕第二个</div>
    <div class="fp-slide">第三屏幕第三个</div>
  </div>
  <div class="fp-section">
    <div class="container">
      <div class="row clearfix">
        <div class="col-md-9 column">
          <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">Brand</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul class="nav navbar-nav">
                <li class="active">
                  <a href="#">Link</a>
                </li>
                <li>
                  <a href="#">Link</a>
                </li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown<strong class="caret"></strong></a>
                  <ul class="dropdown-menu">
                    <li>
                      <a href="#">Action</a>
                    </li>
                    <li>
                      <a href="#">Another action</a>
                    </li>
                    <li>
                      <a href="#">Something else here</a>
                    </li>
                    <li class="divider">
                    </li>
                    <li>
                      <a href="#">Separated link</a>
                    </li>
                    <li class="divider">
                    </li>
                    <li>
                      <a href="#">One more separated link</a>
                    </li>
                  </ul>
                </li>
              </ul>
              <form class="navbar-form navbar-left" role="search">
                <div class="form-group">
                  <input type="text" class="form-control" />
                </div> <button type="submit" class="btn btn-default">Submit</button>
              </form>
              <ul class="nav navbar-nav navbar-right">
                <li>
                  <a href="#">Link</a>
                </li>
                <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown<strong class="caret"></strong></a>
                  <ul class="dropdown-menu">
                    <li>
                      <a href="#">Action</a>
                    </li>
                    <li>
                      <a href="#">Another action</a>
                    </li>
                    <li>
                      <a href="#">Something else here</a>
                    </li>
                    <li class="divider">
                    </li>
                    <li>
                      <a href="#">Separated link</a>
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </nav>
        </div>
        <div class="col-md-3 column">
          <div class="btn-group">
            <button class="btn btn-default">Action</button> <button data-toggle="dropdown" class="btn btn-default dropdown-toggle"><span class="caret"></span></button>
            <ul class="dropdown-menu">
              <li>
                <a href="#">操作</a>
              </li>
              <li class="disabled">
                <a href="#">另一操作</a>
              </li>
              <li class="divider">
              </li>
              <li>
                <a href="#">其它</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row clearfix">
        <div class="col-md-4 column">
          <div class="panel-group" id="panel-287945">
            <div class="panel panel-default">
              <div class="panel-heading">
                <a class="panel-title collapsed" data-toggle="collapse" data-parent="#panel-287945" href="#panel-element-475628">Collapsible Group Item #1</a>
              </div>
              <div id="panel-element-475628" class="panel-collapse collapse">
                <div class="panel-body">
                  Anim pariatur cliche...
                </div>
              </div>
            </div>
            <div class="panel panel-default">
              <div class="panel-heading">
                <a class="panel-title" data-toggle="collapse" data-parent="#panel-287945" href="#panel-element-351858">Collapsible Group Item #2</a>
              </div>
              <div id="panel-element-351858" class="panel-collapse in">
                <div class="panel-body">
                  Anim pariatur cliche...
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-8 column">
          <div class="jumbotron">
            <h1>
              Hello, world!
            </h1>
            <p>
              This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.
            </p>
            <p>
              <a class="btn btn-primary btn-large" href="#">Learn more</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
