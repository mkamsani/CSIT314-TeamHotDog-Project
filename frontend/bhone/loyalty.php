<?php
	session_start();

	$sessionVariables = ['movie', 'date', 'time', 'ticketType'];

	$moviesDetailsCh = curl_init();
	curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/all");
	curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
	$moviesDetails = curl_exec($moviesDetailsCh);

	curl_close($moviesDetailsCh);
	$moviesDetails = json_decode($moviesDetails, true);

	// $columnTitle = array_column($moviesDetails, 'title');
	// $columnImg_Src = array_column($moviesDetails, 'imageUrl');
	// $combineArray = array_combine($columnTitle, $columnImg_Src);

	foreach ($sessionVariables as $variable) {
		if (isset($_POST[$variable])) {
			$_SESSION[$variable] = $_POST[$variable];
		} elseif (!isset($_SESSION[$variable])) {
			$_SESSION[$variable] = null;
		}
	}

	$moviePrice = 9.00;

	switch ($_SESSION['ticketType']) {
		case 'adult':
			$ticketPrice = $moviePrice;
			break;
		case 'student':
			$ticketPrice = $moviePrice * 0.8;
			break;
		case 'senior':
			$ticketPrice = $moviePrice * 0.7;
			break;
		case 'child':
			$ticketPrice = $moviePrice * 0.6;
			break;
		default:
			$ticketPrice = 0;
	}

	$totalCost = $ticketPrice;
	$totalLoyaltyPoint = 10; //Integrate this

	// for ($moviesDetails as $movies)
	// {
	// 	echo $movies['imageUrl'];
	// }
?>

<!DOCTYPE html>
<html>
<head>
    <title>Confirm Order</title>
    <link rel="stylesheet" href="loyalty.css">
    <style>
        /* Add your CSS styles here */
    </style>
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
            <td rowspan="3"><img src="<?php echo $moviesDetails['imageUrl']; ?>" alt="Error" width="120" height="120"></td>
            <td style="text-align: center"><?php echo $_SESSION['ticketType'] ?> | <?php echo $_SESSION['movie']; ?></td>
        </tr>
        <tr>
            <td style="text-align: center"><?php echo date('d/m/Y', strtotime($_SESSION['date'])) ?></td>
        </tr>
        <tr>
            <td style="text-align: center"><?php echo $_SESSION['time'] ?></td>
        </tr>
    </table>
    <h3 style="text-align: center">Total cost: $<?php echo number_format((float)$totalCost, 2, '.', ''); ?></h3>

    <div>
        <p>You currently have <span id="loyalty-points"><u><?php echo $totalLoyaltyPoint ?></u></span> loyalty points.</p>
        <p>1pt = $1.00 discount.</p>
        <p>Enter the amount of loyalty points you would like to use:</p>
        <form action="loyalty.php" method="post">
            <input type="number" name="loyaltyPoint" id="loyaltyPoint" value="0" min="0"
                   max="<?php echo $loyaltyPoint ?>" style="margin-right: 10px;">
            <input type="submit" name="submitLoyalty" value="Apply" class="button">
        </form>

        <h3 style="text-align: center">
            After Discount: $<?php
            $loyaltyPoint = $_POST['loyaltyPoint'];
            $totalCost = $totalCost - $loyaltyPoint;
            $totalLoyaltyPoint = $totalLoyaltyPoint - $loyaltyPoint;
            echo number_format((float)$totalCost, 2, '.', '');
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