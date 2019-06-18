<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/30/16
 * Time: 2:13 PM
 */
global $log;

?>
<html>
<head>
    <title>Educontest</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" 
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" 
          crossorigin="anonymous">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css" 
          rel="stylesheet" integrity="sha384-hQpvDQiCJaD2H465dQfA717v7lu5qHWtDbWNPvaTJ0ID5xnPUlVXnKzq7b8YUkbN"
          crossorigin="anonymous">
    <script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular-animate.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet">
<!--    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootcards/1.0.0/css/bootcards-desktop.min.css">-->
    <script src="/public/app.js"></script>
    <script src="/public/js/scripts.js"></script>
    <link type="text/css" rel="stylesheet" href="/public/css/syles.css">
    <script src="/dirPagination.js"></script>
    <!-- CONTROLLERS   -->
    <script src="/public/controllers/SchoolController.js"></script>
    <script src="/public/controllers/LeagueController.js"></script>

</head>
<body >
<nav class="navbar navbar-inverse">
		<div class="container admin-container">
			<ul id="adminbar" class="nav navbar-nav  navbar-right">
		
		                <?php
		                if(!$log->isAdmin()){
		                    if($log->isUserLoggedIn()) {
		                        echo ' <li><a href="/logout">Logout</a></li>';
		                        echo '<li><a href="/users/account/' . $_SESSION['school_id'] . '">My Account</a></li>';
		                        echo '<li><a href="/users/dashboard/' . $_SESSION['school_id'] . '">My Dashboard</a></li>';
		                        ?>
		
		                        </ul><?php
		
		                    }else{
		                        echo ' <li><a href="/returning">Register</a></li>';
		                        echo '<li><a href="/users/sign-in">Sign In</a></li>';
		                    }
		                }else{
		                    echo ' <li><a href="/logout">Logout</a></li>';
		                }
		                ?>
		                <li>
		                <?php
		                //ADMIN PAGES
		                if($log->isAdmin()) { ?>
		                    <a href="/admin">Admin</a>
		                    <?php }?>
		                </li>
		   </ul>
		</div>
	<div class="main menu">
	    <div class="container" style="padding: 0px;">
	        <div class="navbar-header">
	            <a href="/">
	                <img class="logo" src="/public/assets/img/educontestlogo.png">
	            </a>
	        </div>
	        <div id="navbar" class="navbar-collapse collapse">
	            <ul class="nav navbar-nav">
	                <li><a href="/">Home</a></li>
	                <li><a href="/about">About Us</a></li>
	                <li><a href="/samples">Samples</a></li>
	                <li><a href="/results">Test Results</a></li>
	                <li><a class="last-item" href="/contact">Contact</a></li> 
	            </ul>
	            
	        </div>
	    </div>
    </div>
</nav>

