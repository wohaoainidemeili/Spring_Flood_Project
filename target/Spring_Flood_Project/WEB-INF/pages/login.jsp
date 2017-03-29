<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<!-- 新 Bootstrap 核心 CSS 文件 -->
	<!--<link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

	<!-- 可选的Bootstrap主题文件（一般不使用） -->
	<!--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap-theme.min.css"></script>-->

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<!--<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>-->

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<!--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>-->
	<!--<link href="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
	<script src="${pageContext.request.contextPath}/resources/jquery-2.1.1/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>-->
	<link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
	<script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
	<script src="resources/jquery-2.1.1/jquery.min.js"></script>
	<script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
	<script src="resources/jquery-form-3.51/jquery.form.min.js"></script>

	<!--提交表单，submit the userForm using ajaxSubmit-->
	<script>
		function formsubmit(){
			var options={
				type:"post",
				url:"check",
				dataType:"json",
				beforeSubmit:validate,
				success:checkuser
			};
			$('#userForm').ajaxSubmit(options);
		};
		function validate(formData,jqForm,options){
			for(var i=0;i<formData.length;i++){
				if(!formData[i].value){
					alert(formData[i].name+"用户名不能为空！");
					return false;
				}
			}
		};
		function checkuser(responseText, statusText, xhr, $form){
			if(Boolean(responseText)){
				window.location.href="/homePage";
			}else{
				alert(responseText);
				error( "用户名或密码错误！")
			}
		};

	</script>
	<title>全生命周期洪涝事件监测系统</title>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="col-md-12">
				<h3>
					洪涝事件探测系统
				</h3>
			</div>
		</div>
		<div class="row-fluid">
			<div class="col-md-8">
				<div class="carousel slide" id="carousel-932711">
					<ol class="carousel-indicators">
						<li data-slide-to="0" data-target="#carousel-932711">
						</li>
						<li data-slide-to="1" data-target="#carousel-932711" class="active">
						</li>
						<li data-slide-to="2" data-target="#carousel-932711">
						</li>
					</ol>
					<div class="carousel-inner">
						<div class="item">
							<img alt="" src="resources/image/1.jpg" />
							<div class="carousel-caption">
								<h4>
									棒球
								</h4>
								<p>
									棒球运动是一种以棒打球为主要特点，集体性、对抗性很强的球类运动项目，在美国、日本尤为盛行。
								</p>
							</div>
						</div>
						<div class="item active">
							<img alt="" src="resources/image/2.jpg" />
							<div class="carousel-caption">
								<h4>
									洪涝
								</h4>
								<p>
									洪涝， 指因大雨、暴雨或持续降雨使低洼地区淹没、渍水的现象。雨涝主要危害农作物生长，造成作物减产或绝收，破坏农业生产以及其他产业的正常发展。其影响是综合的，还会危及人的生命财产安全，影响国家的长治久安等。
								</p>
							</div>
						</div>
						<div class="item">
							<img alt="" src="resources/image/3.jpg" />
							<div class="carousel-caption">
								<h4>
									自行车
								</h4>
								<p>
									以自行车为工具比赛骑行速度的体育运动。1896年第一届奥林匹克运动会上被列为正式比赛项目。环法赛为最著名的世界自行车锦标赛。
								</p>
							</div>
						</div>
					</div> <a data-slide="prev" href="#carousel-932711" class="left carousel-control">‹</a> <a data-slide="next" href="#carousel-932711" class="right carousel-control">›</a>
				</div>
			</div>
			<div class="col-md-4">
				<div class="well center login-box">
					<div class="alert alert-info">
						<!--Please login with your LoginName and Password.-->
						请输入用户名和密码
					</div>
					<form class="form-horizontal" id="userForm" >
						<fieldset>
							<div class="input-group input-group-lg">
								<span class="input-group-addon"><i
										class="glyphicon glyphicon-user red"></i></span> <input type="text"
																								class="form-control" placeholder="用户名" name="userID"
																								id="userID">
							</div>
							<div class="clearfix"></div>
							<br>

							<div class="input-group input-group-lg">
								<span class="input-group-addon"><i
										class="glyphicon glyphicon-lock red"></i></span> <input
									type="password" class="form-control" placeholder="密码"
									name="passWord" id="passWord">
							</div>
							<div class="clearfix"></div>

							<!--                    <div class="input-prepend">
                        <label class="remember" for="remember"><input type="checkbox" id="remember"> 记住我</label>
                    </div>-->
							<div class="clearfix"></div>
							<br>

								<p class="center">
								<input type="button" class="btn btn-block" style="background-color: lightskyblue" onclick="formsubmit()" value="登录"/>
								</p>
							<br>
							<a href="#" >忘记密码？</a>&nbsp<a href="#">新用户注册</a>

						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>