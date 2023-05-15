<!DOCTYPE html>
<html>
<head>
	<title>Checkout Page</title>
	<link rel="stylesheet" href="checkOut.css">
</head>
<body>
	<div class="container">
		<h1>Checkout</h1>
		<div class="checkout-form">
			<form action="thankYou.php" method="post">
				<div class="form-row">
					<label for="name">Name</label>
					<input type="text" id="name" name="name" required>
				</div>
				<div class="form-row">
					<label for="email">Email</label>
					<input type="email" id="email" name="email" required>
				</div>
				<div class="form-row">
					<label for="address">Address</label>
					<textarea id="address" name="address" required></textarea>
				</div>
				<div class="form-row">
					<label for="card-number">Card Number</label>
					<input type="text" id="card-number" name="card-number" required>
				</div>
				<div class="form-row">
					<label for="expiry-date">Expiry Date</label>
					<input type="text" id="expiry-date" name="expiry-date" placeholder="MM / YY" required>
				</div>
				<div class="form-row">
					<label for="cvv">CVV</label>
					<input type="text" id="cvv" name="cvv" required>
				</div>
					<input type="submit" value="Pay Now">
			</form>
		</div>
	</div>
</body>
</html>
