<?php
session_start();
?>
<html>
<head>
<title></title>
<style><?php include 'homepage.css'; ?></style> 
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<table width="100%" style="border: 0" 
<tr>
    <td class = "CinemaName"><h1>Silver City Cinema</h1></td>
	<td>
	<form action="redirectPage.php" method="POST" class = "loginForm">
	<label class = "nameLabel">Name:</label> <input type="text" name="name" class ="textBox1"></input>
	<label class = "passwordLabel">Password:</label> 
	 <input type="password" name="password" class ="textBox2"></input><br>
	<label for="user" class = "labelDDL">Select User:</label>
	<select name="user" id="user" class= "dropdown">
		<option value="">--- Select User ---</option>
		<option value="UA">User Admin</option>
		<option value="CO">Cinema Owner</option>
		<option value="CM">Cinema Manager</option>
		<option value="C">Customer</option>
	</select>
	<label class ="alertLabel">
	<?php

	ini_set('display_errors', 0);
	ini_set('display_startup_errors', 0);
	error_reporting(-1);
	$_SESSION['alert'];
	echo $_SESSION['alert'];	
	?>
	</label>
	<br>
	<button type="submit" name="check" value="register" class="register">register</button>
	<button type="submit" name="check" value="login" class="login">login</button>
	</form></td>
</tr>
</table>

<br>
<div class="topnav">
  <a class="active" href="HomePage.php">Home</a>
  <a href="CustomerTest.php">View Movies</a>
  <a href="CinemaOwnerTest.php">Book a movie</a>
  <a href="CinemaManagerTest.php">Contact</a>
</div>

<hr />



</body>
</html>