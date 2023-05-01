<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
</head>
<body>

<?php

	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/api/user-account/readAllUserAccounts");
	curl_setopt($ch, CURLOPT_HEADER, 0);

	// grab URL and pass it to the browser
	$response = curl_exec($ch);
	$response = json_decode($response, true); //because of true, it's in an array
	echo $response;
	
session_start();

if($_SERVER["REQUEST_METHOD"] == "POST") {
	$check = $_POST['check'];
	if($check == "register") {
		header("Location: register.php");
	}

	if($check == "login" && $_POST['user'] == 'UA') {
		if($_POST['name'] == "UserIDTest" && $_POST['password'] == "PasswordTest")
		{
			$_SESSION['alert'] = "";
			header("Location: UserAdminTest.php");
		}
		else{
			$_SESSION['alert'] = "Wrong ID or Password";	
			header("Location: Homepage.php");
		}
	}
	
	if($check == "login" && $_POST['user'] == 'CO') {
//		if($_POST['name'] == "UserIDTest" && $_POST['password'] == "PasswordTest")
		if($_POST['name'] == "marcus")
		{
			$_SESSION['alert'] = "";
			header("Location: CinemaOwnerTest.php");
		}
		else{
			$_SESSION['alert'] = "Wrong ID or Password";	
			header("Location: Homepage.php");
		}
	}
	
	if($check == "login" && $_POST['user'] == 'CM') {
		if($_POST['name'] == "UserIDTest" && $_POST['password'] == "PasswordTest")
		{
			$_SESSION['alert'] = "";	
			header("Location: CinemaManagerTest.php");
		}
		else{
			$_SESSION['alert'] = "Wrong ID or Password";
			header("Location: Homepage.php");
		}
	}
	
	if($check == "login" && $_POST['user'] == 'C') {
		if($_POST['name'] == "UserIDTest" && $_POST['password'] == "PasswordTest")
		{
			$_SESSION['alert'] = "";	
			header("Location: CustomerTest.php");
		}
		else{
			$_SESSION['alert'] = "Wrong ID or Password";	
			header("Location: Homepage.php");
			
		}
	}
		// close cURL resource, and free up system resources
	curl_close($ch);
}


/* 	if($check == "login" && $_POST['user'] == 'S') {
		try {
		$DBName = "registerdb";

		$conn = mysqli_connect("localhost", "root", "",$DBName );
		
		$TableName = "Students";
		$SQLstring = "SELECT Name, StudentID, Password FROM $TableName"  . " WHERE Name='" . stripslashes($_POST['name']) ."' and password='" .stripslashes($_POST['password']) . "'";
		//echo $SQLstring;
		$qRes = mysqli_query($conn, $SQLstring);
		if (mysqli_num_rows($qRes)==0) {
	
		header("Location: homepage.php");

			
		}
		else {
		
			$Row = mysqli_fetch_assoc($qRes);
			$StudentName = $Row['Name'];
			$_SESSION['StudentName'] = $StudentName;
			
			$StudentID = $Row['StudentID'];
			$_SESSION['StudentID'] = $StudentID;
			header("Location: Student.php");

			
		}
	}
	catch(mysqli_sql_exception $e) {
		echo "<p>Error: unable to connect/insert record in the database.</p>";
	}
		
	} */
	
	/* if($check == "login" && $_POST['user'] == 'A') {
		try {
			$DBName = "registerdb";
			$conn = mysqli_connect("localhost", "root", "",$DBName );
		
			$TableName = "Administrator";
			$SQLstring = "SELECT Name, StaffID, Password FROM $TableName"  . " where Name='" . stripslashes($_POST['name']) ."' and password='" .stripslashes($_POST['password']) . "'";
			echo $SQLstring;
			$qRes = mysqli_query($conn, $SQLstring);
			if (mysqli_num_rows($qRes)==0) {
		
			header("Location: homepage.php");

				
			}
			else {
				
				$Row = mysqli_fetch_assoc($qRes);
				$AdminName = $Row['Name'];
				$_SESSION['AdminName'] = $AdminName;
				
				$StaffID = $Row['StaffID'];
				$_SESSION['StaffID'] = $StaffID;
				
				header("Location: Adminstrator.php");
			}
		}
		catch(mysqli_sql_exception $e) {
			echo "<p>Error: unable to connect/insert record in the database.</p>";
		}
				
				
	}

	if($check == "login" && $_POST['user'] == 'E') {
		try {
			$DBName = "registerdb";
			$conn = mysqli_connect("localhost", "root", "",$DBName );
		
			$TableName = "Educator";
			$SQLstring = "SELECT Name, StaffID, Password FROM $TableName"  . " where Name='" . stripslashes($_POST['name']) ."' and password='" .stripslashes($_POST['password']) . "'";
			echo $SQLstring;
			$qRes = mysqli_query($conn, $SQLstring);
			if (mysqli_num_rows($qRes)==0) {
		
			header("Location: homepage.php");

				
			}
			else {
				$Row = mysqli_fetch_assoc($qRes);
				$EducatorName = $Row['Name'];
				$_SESSION['EducatorName'] = $EducatorName;
				
				$StaffID = $Row['StaffID'];
				$_SESSION['StaffID'] = $StaffID;
				header("Location: Educator.php");
			}
		}
		catch(mysqli_sql_exception $e) {
			echo "<p>Error: unable to connect/insert record in the database.</p>";
		}
	}

	if($check == "login" && $_POST['user'] == "") {
		header("Location: homepage.php");
	} */



	

?>

</body>
</html>