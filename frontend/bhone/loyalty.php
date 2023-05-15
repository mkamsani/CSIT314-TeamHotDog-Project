<?php
	session_start();

	if (isset($_POST['movie']))
		$_SESSION['movie'] = $_POST['movie'];
	
	if (!isset($_SESSION['movie']))
		$_SESSION['movie'] = null;

	if (isset($_POST['date']))
		$_SESSION['date'] = $_POST['date'];
	
	if (!isset($_SESSION['date']))
		$_SESSION['date'] = null;

	if (isset($_POST['time']))
		$_SESSION['time'] = $_POST['time'];
	
	if (!isset($_SESSION['time']))
		$_SESSION['time'] = null;

	if (isset($_POST['ticketType']))
		$_SESSION['ticketType'] = $_POST['ticketType'];
	
	if (!isset($_SESSION['ticketType']))
		$_SESSION['ticketType'] = null;

	if (isset($_POST['popcorn']))
		$_SESSION['popcorn'] = $_POST['popcorn'];
	
	if (!isset($_SESSION['popcorn']))
		$_SESSION['popcorn'] = null;

	if (isset($_POST['drink']))
		$_SESSION['drink'] = $_POST['drink'];
	
	if (!isset($_SESSION['drink']))
		$_SESSION['drink'] = null;

	$moviePrice=9.00;
	$popcornPrice=3.50;
	$drinkPrice=2.00;

	if ($_SESSION['ticketType'] == "adult")
	{
		$ticketPrice=$moviePrice;
	}
	elseif ($_SESSION['ticketType'] == "student") 
	{
		$ticketPrice=$moviePrice*0.8;
	}
	elseif ($_SESSION['ticketType'] == "senior")
	{
		$ticketPrice=$moviePrice*0.7;
	}
	elseif ($_SESSION['ticketType'] == "child")
	{
		$ticketPrice=$moviePrice*0.6;
	}

	$totalCost=$ticketPrice+$popcornPrice*$_SESSION['popcorn']+$drinkPrice*$_SESSION['drink'];


	$totalLoyaltyPoint=10;
?>

<!DOCTYPE html>
<html>
<head>
	<title>Confirm Order</title>
	<link rel="stylesheet" href="loyalty.css">
</head>
<body>
	<header>
		<h1>Confirm Order</h1>
	</header>
	<nav>
		<ul>
			<li><a href="cHome.php">Home</a></li>
			<li><a href="cBooking.php">Book Movie</a></li>
		</ul>
	</nav>
	<main>
	<h2 style="text-align: center">Booking Summary</h2>
		<table align="center" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td rowspan="3"><img src="img/movie.png" alt="Error" width="120" height="120"></td>
				<td style="text-align: center"><?php echo $_SESSION['ticketType']?> | <?php echo $_SESSION['movie'];?></td></tr>
				<tr><td style="text-align: center"><?php echo date('d/m/Y',strtotime($_SESSION['date']))?></td></tr>
				<tr><td style="text-align: center"><?php echo $_SESSION['time']?></td></tr>
			<tr>
				<td><img src="img/popcorn.png" alt="Error" width="120" height="120"></td>
				<td style="text-align: center;font-size:40px;">x<?php echo $_SESSION['popcorn']?></td>
			</tr>
			<tr>
				<td><img src="img/drink.png" alt="Error" width="120" height="120"></td>
				<td style="text-align: center;font-size:40px;">x<?php echo $_SESSION['drink']?></td>
			</tr>
		</table>
		<h3 style="text-align: center">Total cost: $<?php echo number_format((float)$totalCost,2,'.','');?></h3>
		
		<div>
			<p>You currently have <span id="loyalty-points"><u><?php echo $totalLoyaltyPoint?></u></span> loyalty points.</p>
			<p>1pt = $1.00 discount.</p>
			<p>Enter the amount of loyalty points you would like to use:</p>
			<form action="loyalty.php" method="post">
				<input type="number" name="loyaltyPoint" id="loyaltyPoint" value="0" min="0" max="<?php echo $loyaltyPoint ?>" style="margin-right: 10px;">
				<input type="submit" name="submitLoyalty" value="Apply" class="button">
			</form>

			<h3 style="text-align: center">
			After Discount: $<?php
				$loyaltyPoint=$_POST['loyaltyPoint'];
				$totalCost = $totalCost - $loyaltyPoint;
				$totalLoyaltyPoint = $totalLoyaltyPoint - $loyaltyPoint;
				echo number_format((float)$totalCost,2,'.','');
			?>
			</h3>

			<form method="POST" action="checkOut.php">
				<input type="hidden" name="confirm" value="confirm">
				<button type="submit">Confirm</button>
			</form>

		</div>




	</main>
	<footer>
		<p>&copy; Hot Dog Cinemas. All rights reserved.</p>
	</footer>
</body>
</html>