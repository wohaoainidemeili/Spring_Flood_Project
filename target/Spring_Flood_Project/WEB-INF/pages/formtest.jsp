<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/2/7
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/main.css">
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/normalize.css">
  <link rel="stylesheet" href="resources/jquery.steps-1.1.0/css/jquery.steps.css">
  <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-form-3.51/jquery.form.min.js"></script>
  <script src="resources/jquery.steps-1.1.0/modernizr-2.6.2.min.js"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/jquery.steps-1.1.0/jquery.cookie-1.3.1.js"></script>
  <script src="resources/jquery.steps-1.1.0/jquery.steps.js"></script>
  <script src="resources/jquery-2.1.1/jquery.validate.min.js"></script>
    <title></title>
</head>
<body>
<script>
  $(function(){
    $("#form1").steps({
      bodyTag: "fieldset",
      onStepChanging: function (event, currentIndex, newIndex)
      {
        // Always allow going backward even if the current step contains invalid fields!
        if (currentIndex > newIndex)
        {
          return true;
        }

        // Forbid suppressing "Warning" step if the user is to young
        if (newIndex === 3 && Number($("#age").val()) < 18)
        {
          return false;
        }

        var form = $(this);

        // Clean up if user went backward before
        if (currentIndex < newIndex)
        {
          // To remove error styles
          $(".body:eq(" + newIndex + ") label.error", form).remove();
          $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
        }

        // Disable validation on fields that are disabled or hidden.
        form.validate().settings.ignore = ":disabled,:hidden";

        // Start validation; Prevent going forward if false
        return form.valid();
      },
      onStepChanged: function (event, currentIndex, priorIndex)
      {
        // Suppress (skip) "Warning" step if the user is old enough and wants to the previous step.
        if (currentIndex === 2 && priorIndex === 3)
        {
          $(this).steps("previous");
          return;
        }

        // Suppress (skip) "Warning" step if the user is old enough.
        if (currentIndex === 2 && Number($("#age").val()) >= 18)
        {
          $(this).steps("next");
        }
      },
      onFinishing: function (event, currentIndex)
      {
        var form = $(this);

        // Disable validation on fields that are disabled.
        // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
        form.validate().settings.ignore = ":disabled";

        // Start validation; Prevent form submission if false
        return form.valid();
      },
      onFinished: function (event, currentIndex)
      {
        var form = $(this);

        // Submit form input
        form.submit();
      }
    }).validate({
      errorPlacement: function (error, element)
      {
        element.before(error);
      },
      rules: {
        confirm: {
          equalTo: "#password"
        }
      }
    });
  });
</script>
<form id="form1" action="#">
  <h1>Account</h1>
  <fieldset>
    <legend>Account Information</legend>

    <label for="userName">User name *</label>
    <input id="userName" name="userName" type="text" class="required">
    <label for="password">Password *</label>
    <input id="password" name="password" type="text" class="required">
    <label for="confirm">Confirm Password *</label>
    <input id="confirm" name="confirm" type="text" class="required">
    <p>(*) Mandatory</p>
  </fieldset>

  <h1>Profile</h1>
  <fieldset>
    <legend>Profile Information</legend>

    <label for="name">First name *</label>
    <input id="name" name="name" type="text" class="required">
    <label for="surname">Last name *</label>
    <input id="surname" name="surname" type="text" class="required">
    <label for="email">Email *</label>
    <input id="email" name="email" type="text" class="required email">
    <label for="address">Address</label>
    <input id="address" name="address" type="text">
    <label for="age">Age (The warning step will show up if age is less than 18) *</label>
    <input id="age" name="age" type="text" class="required number">
    <p>(*) Mandatory</p>
  </fieldset>

  <h1>Warning</h1>
  <fieldset>
    <legend>You are to young</legend>

    <p>Please go away ;-)</p>
  </fieldset>

  <h1>Finish</h1>
  <fieldset>
    <legend>Terms and Conditions</legend>

    <input id="acceptTerms" name="acceptTerms" type="checkbox" class="required"> <label for="acceptTerms">I agree with the Terms and Conditions.</label>
  </fieldset>
</form>
<h2>创建模态框（Modal）</h2>
<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  开始演示模态框
</button>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
          ×
        </button>
        <h4 class="modal-title" id="myModalLabel">
          模态框（Modal）标题
        </h4>
      </div>
      <div class="modal-body">
        在这里添加一些文本
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭
        </button>
        <button type="button" class="btn btn-primary">
          提交更改
        </button>
      </div>
    </div>
  </div>
</div>
</body>
</html>
