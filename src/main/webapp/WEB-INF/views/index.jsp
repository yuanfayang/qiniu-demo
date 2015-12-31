<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/taglib.jsp"%>
<html>
<head>
  <title>富文本编辑器例子</title>
  <link rel="stylesheet" href="${ctx}/node_modules/kindeditor/themes/default/default.css">
  <link rel="stylesheet" href="${ctx}/node_modules/kindeditor/plugins/code/prettify.css">
</head>
<body>
<form name="example" method="post">
    <textarea name="content1" cols="100" rows="8"
              style="width:700px;height:300px;visibility:hidden;"></textarea>
  <br/>
  <input type="submit" name="button" value="提交内容"/> (提交快捷键: Ctrl + Enter)
</form>
<p>
  token;${token}
</p>

<script src="${ctx}/node_modules/kindeditor/kindeditor-all.js"></script>
<script src="${ctx}/node_modules/kindeditor/lang/zh-CN.js"></script>
<script src="${ctx}/node_modules/kindeditor/plugins/code/prettify.js"></script>
<script type="text/javascript">
  KindEditor.ready(function(K) {
    var options = {
      uploadJson:"http://upload.qiniu.com",
      allowFileManager: true,
      extraFileUploadParams:{
        token:'${token}',
        dir:"file"
      },
      afterCreate: function () {
        var self = this;
        K.ctrl(document, 13, function () {
          self.sync();
          document.forms['example'].submit();
        });
        K.ctrl(self.edit.doc, 13, function () {
          self.sync();
          document.forms['example'].submit();
        });
      },
      afterUpload : function(url) {
        alert(url);
      },
      filePostName:"file"
    };

    var editor1 = K.create('textarea[name="content1"]',options);

    prettyPrint();
  });
</script>
</body>
</html>

