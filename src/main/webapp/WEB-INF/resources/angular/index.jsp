<%@ page import="java.net.InetAddress" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<!DOCTYPE html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <title>playground&#0153;</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <!-- Place favicon.ico and apple-touch-icon.png in the root directory-->
    <!-- build:css({app,.tmp}) styles/all.css-->
    <!-- bower:css-->
    <!-- endbower-->
    <link rel="icon" href="images/icon-60px.png">
    <link rel="stylesheet" href="styles/main.css">
    <link rel="stylesheet" href="styles/admin-print.css" media="print">
    <!-- endbuild-->
</head>
<body class="admin" ng-app="playground-admin">
<!--[if gt IE 10]><p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href='http://browsehappy.com/'>upgrade your browser</a> to improve your experience. </p> <![endif]-->
<div data-offcanvas="" ng-class="{'off-canvas-wrap move-right': !registrationFlow}">
    <div class="inner-wrap">
        <div left-side-menu></div>
        <div ng-view></div>
    </div>
</div>
<%
    String hostAddress = InetAddress.getLocalHost().getHostAddress();
    int indexOfLastDot = StringUtils.lastIndexOf(hostAddress, ".") + 1;
    String temp = StringUtils.substring(hostAddress, indexOfLastDot, hostAddress.length());
%>
<div class="hide">
Ops Suffix: <%=temp%>
Build: @buildLabel@ @buildId@
</div>
<script src="scripts/vendor/angular_file_upload/angular-file-upload-html5-shim.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-route.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-resource.min.js"></script>
<script src="scripts/vendor/angular_file_upload/angular-file-upload.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.7.0/underscore-min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/angular-spinner/0.5.1/angular-spinner.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/spin.js/2.0.1/spin.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-sanitize.js"></script>
<script src="scripts/vendor/ng-csv/ng-csv.min.js"></script>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/foundation/5.3.1/js/foundation.min.js"></script>

<script src="scripts/admin_angular.js"></script>
</body>
</html>
