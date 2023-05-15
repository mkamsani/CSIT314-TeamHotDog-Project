<!DOCTYPE html>
<html>
<head>
	<title>Book Movie</title>
	<link rel="stylesheet" href="cBooking.css">
</head>
<body>
	<header>
		<h1>Book Movie</h1>
	</header>
	<nav>
		<ul>
			<li><a href="cHome.php">Home</a></li>
			<li><a href="cBooking.php">Book Movie</a></li>
		</ul>
	</nav>
	<main>
		<form action="loyalty.php" method="post">
			<fieldset>
				<legend>Choose a movie</legend>
				
				<label for="movie">Select a movie:</label>
				<select name="movie" required>
					<option value="">--Select a movie--</option>
					<option value="movie1">Avengers: Endgame</option>
					<option value="movie2">Sausage Party</option>
					<option value="movie3">Scooby Dooby Doo</option>
				</select>
				
				<label for="date">Select a date:</label>
				<input type="date" name="date" required>
				
				<label for="time">Select a time:</label>
				<select name="time" required>
					<option value="">--Select a time--</option>
					<option value="morning">Morning</option>
					<option value="afternoon">Afternoon</option>
					<option value="evening">Evening</option>
				</select>
				
				<label for="ticketType">Type of Ticket:</label>
				<select name="ticketType" required>
					<option value="">--Select a type--</option>
					<option value="adult">Adult</option>
					<option value="child">Child</option>
					<option value="student">Student</option>
					<option value="senior">Senior</option>
				</select>
				
			</fieldset>

			<fieldset>
				<legend>Choose a food item</legend>
				<label for="popcorn">Popcorn:</label>
				<input type="number" name="popcorn" value="0">
				<label for="drink">Drink:</label>
				<input type="number" name="drink" value="0">
			</fieldset>
			<input type="submit" name="submit" value="Book Now">
		</form>
	</main>
	<footer>
		<p>&copy; Hot Dog Cinemas. All rights reserved.</p>
	</footer>
</body>
</html>
