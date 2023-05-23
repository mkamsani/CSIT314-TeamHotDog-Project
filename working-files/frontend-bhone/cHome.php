<!DOCTYPE html>
<html>
<head>
	<title>Hot Dog Cinemas</title>
	<link rel="stylesheet" href="cHome.css">
</head>
<body>
	<header>
		<h1>Hot Dog Cinemas</h1>
	</header>
	<nav>
		<ul>
			<li><a href="cHome.php">Home</a></li>
			<li><a href="cBooking.php">Book Movie</a></li>
		</ul>
	</nav>
	<main>
		<div class="container">
      		<form action="update.php" method="POST">
				<div class="form-group">
					<label for="name">Name:</label>
					<input type="text" id="name" name="name" value="John Doe">
				</div>
				<div class="form-group">
					<label for="email">Email:</label>
					<input type="email" id="email" name="email" value="johndoe@example.com">
				</div>
				<button type="submit">Update</button>
      		</form>
    	</div>
	</main>
	<footer>
		<p>&copy; Hot Dog Cinemas. All rights reserved.</p>
	</footer>
</body>
</html>
