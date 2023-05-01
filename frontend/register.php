<html>
<head>
<title></title>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<style><?php include 'register.css'; ?></style>
</head>
<body>
<table width="100%" style="border: 0">
<tr>
    <td><h1>Registration</h1></td>
  
</tr>
</table>
<hr />
<?php

	/* class Mysql extends Dbconfig{
		public $connectionString;
		public $dataSet;
		private $sqlQuery;
		
		protected $databaseName;
		protected $hostName;
		protected $username;
		protected $password;

		function Mysql() {
			$this -> connectionString = NULL;
			$this -> sqlQuery = NULL;
			$this -> dataSet = NULL;

			$dbPara = new Dbconfig();
			$this -> databaseName = $dbPara -> dbName;
			$this -> hostName = $dbPara -> servername;
			$this -> username = $dbPara -> username;
			$this -> password = $dbPara ->password;
			$dbPara = NULL;
    }
  
    function dbConnect()    {
	 
        $this -> connectionString = mysqli_connect($this -> servername,$this -> username,$this -> password);
        mysql_select_db($this -> dbName,$this -> connectionString);
        return $this -> connectionString;
    }
		
		

	} */
	
	
	
	session_start();
	$errors = 0;
	$errorMsg = "";
	$msg = "";
	$testmsg = "";
	$jsondata = "";

	$ch = curl_init();



	
	
	if (empty($_POST['fname'])) {
		++$errors;
		$errorMsg .= "<p>You need to enter a first name.</p>\n";
	
	}
	
	else{
		
		$fname = stripslashes($_POST['fname']);
	}
	
	if (empty($_POST['lname'])) {
		++$errors;
		$errorMsg .= "<p>You need to enter a last name.</p>\n";
	
	}
	
	else{
		
		$lname = stripslashes($_POST['lname']);
	}
	
	if (	empty($_POST['password']) || empty($_POST['password2']) ){
		++$errors;
		$errorMsg .= "<p>You need to enter password.</p>\n";
		$password = "";
		$password2 = "";
	}
	
	else
	{
		$password = stripslashes($_POST['password']);
		$password2 = stripslashes($_POST['password2']);
	}
	
	if(	!empty($_POST['password']) || !empty($_POST['password2'])	){
		
		if(	strlen($password  < 6) || strlen($password2 < 6)	)
		{
			++$errors;
			$errorMsg .= "<p>Passwords is too short.</p>\n";
		}
		
		if($password <>  $password2 )
		{
			++$errors;
			$errorMsg .= "<p>Passwords do not match.</p>\n";
		}
		
	}

			
	if (empty($_POST['email'])) {
		++$errors;
		$errorMsg .= "<p>You need to enter an e-mail address.</p>\n";
	}

	else {
		$email = stripslashes($_POST['email']);
		if (preg_match("/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)*(\.[a-z]{2,3})$/i", $email) == 0) {
			++$errors;
			$errorMsg .= "<p>You need to enter a valid " . "e-mail address.</p>\n";
			$email = "";
		}
	}

	if (empty($_POST['address'])) {
		++$errors;
		$errorMsg .= "<p>You need to enter an address.</p>\n";
	}
	else
	{
		$address = stripslashes($_POST['address']);
	}


if(	isset($_POST['register']) && $_POST['userR'] == 'UA' ){
	if($errors == 0){
	/* try {
			$conn = mysqli_connect ("localhost", "root", "");
			$dbName = "registerdb";
			mysqli_select_db($conn, $dbName);
			//$msg .= "<p>connected to DB</p>\n";
			
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to connect to the database </p>\n";
			++$errors;
		} 

		try {
			

			$tableName = "Students";
			$sql = "INSERT INTO $tableName (Name, Password, Phone, Email) VALUES( '$name', '$password', '$phone', '$email' )";
			mysqli_query($conn, $sql);
			$StudentID = mysqli_insert_id($conn);
			$msg .= "<p>registration successful, welcome" ." $name". "</p>\n";
			mysqli_close($conn);
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to insert record</p>";
			++$errors;
		} */
	
		$privilege = "admin";
	
		$arr = array("username" => $fname, "password" => $password, "privilege" => $privilege);
			
		$jsondata = json_encode($arr);
	
		curl_setopt($ch, CURLOPT_URL, "http://www.example.com/");
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch,CURLOPT_POSTFIELDS, $jsondata);
		$result = curl_exec($ch);
			
		$testmsg = "UA Successfully registered"; 
	  
	}

}

if(	isset($_POST['register']) && $_POST['userR'] == 'CO' ){
	if($errors == 0){
	/* try {
			$conn = mysqli_connect ("localhost", "root", "");
			$dbName = "registerdb";
			mysqli_select_db($conn, $dbName);
			//$msg .= "<p>connected to DB</p>\n";
			
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to connect to the database </p>\n";
			++$errors;
		} 

		try {
			$tableName = "Administrator";
			$sql = "INSERT INTO $tableName (Name, Password, Phone, Email) VALUES( '$name', '$password', '$phone', '$email' )";
			mysqli_query($conn, $sql);
			$StaffID = mysqli_insert_id($conn);
			$_SESSION['StaffID'] = $StaffID;
			$msg .= "<p>registration successful, welcome" ." $name". "</p>\n";
			mysqli_close($conn);
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to insert record</p>";
			++$errors;
		} */
		
		$arr = array($fname, $lname, $password, $password2, $email, $address);
		$jsondata = json_encode($arr);
		$testmsg = "CO Successfully registered"; 
	}

}

if(	isset($_POST['register']) && $_POST['userR'] == 'CM' ){
	if($errors == 0){
	/* try {
			$conn = mysqli_connect ("localhost", "root", "");
			$dbName = "registerdb";
			mysqli_select_db($conn, $dbName);
			//$msg .= "<p>connected to DB</p>\n";
			
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to connect to the database </p>\n";
			++$errors;
		} 

		try {
			$tableName = "Educator";
			$sql = "INSERT INTO $tableName (Name, Password, Phone, Email) VALUES( '$name', '$password', '$phone', '$email' )";
			mysqli_query($conn, $sql);
			$StaffID = mysqli_insert_id($conn);
			$_SESSION['StaffID'] = $StaffID;
			$msg .= "<p>registration successful, welcome" ." $name". "</p>\n";
			mysqli_close($conn);
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to insert record</p>";
			++$errors;
		} */
		$arr = array($fname, $lname, $password, $password2, $email, $address);
		$jsondata = json_encode($arr);
		$testmsg = "CM Successfully registered"; 
	}

}	

if(	isset($_POST['register']) && $_POST['userR'] == 'C' ){
	if($errors == 0){
	/* try {
			$conn = mysqli_connect ("localhost", "root", "");
			$dbName = "registerdb";
			mysqli_select_db($conn, $dbName);
			//$msg .= "<p>connected to DB</p>\n";
			
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to connect to the database </p>\n";
			++$errors;
		} 

		try {
			$tableName = "Educator";
			$sql = "INSERT INTO $tableName (Name, Password, Phone, Email) VALUES( '$name', '$password', '$phone', '$email' )";
			mysqli_query($conn, $sql);
			$StaffID = mysqli_insert_id($conn);
			$_SESSION['StaffID'] = $StaffID;
			$msg .= "<p>registration successful, welcome" ." $name". "</p>\n";
			mysqli_close($conn);
		}
		catch (mysqli_sql_exception $e) {
			$errorMsg .= "<p>Unable to insert record</p>";
			++$errors;
		} */
		$arr = array($fname, $lname, $password, $password2, $email, $address);
		$jsondata = json_encode($arr);
		$testmsg = "C Successfully registered"; 
	}

}	



	
?>
<form action="<?php echo $_SERVER['PHP_SELF'];?>" method="POST">

Enter First Name: <input type="text" name="fname" class = "fname"></input><br>

Enter Last Name: <input type="text" name="lname" class = "lname"></input><br>

Enter Password: <input type="password" name="password"  class = "password"></input><br>

confrim Password: <input type="password" name="password2"  class = "password2"></input><br>

Enter Email: <input type="email" name="email"  class = "email"></input><br>

Enter address: <input type="text" name="address" class = "address"></input><br>

Select User:
<select name="userR" id="userR" class = "userR">
		<option value="">--- Select User ---</option>
		<option value="UA">User Admin</option>
		<option value="CO">Cinema Owner</option>
		<option value="CM">Cinema Manager</option>
		<option value="C">Customer</option>
</select>
<br>
<br>
<br>
<input type="button" name="back" value="Go back to main menu" onClick="window.location = 'Homepage.php'" class = "back"></button>
<button type="submit" name="register" value="register" class = "register">confrim registration</button>


</form>
<br>
<?php echo $errorMsg;?>
<?php echo $msg;?>
<?php echo $testmsg;?><br>
<?php echo $jsondata;?>

</body>
</html>