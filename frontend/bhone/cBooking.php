<!DOCTYPE html>

<?php

	$moviesDetailsCh = curl_init();
	curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/all");
	curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
	$moviesDetails = curl_exec($moviesDetailsCh);
	
	curl_close($moviesDetailsCh);
	$moviesDetails = json_decode($moviesDetails, true);

	$columnTitle = array_column($moviesDetails, 'title');
	$columnImg_Src = array_column($moviesDetails, 'imageUrl');
	$combineArray = array_combine($columnTitle, $columnImg_Src);

?>
<html>
<head>
	<title>Book Movie</title>
	<link rel="stylesheet" href="cBooking.css">

	<style>
	#display .movie-image {
		width: 200px; /* Adjust the width as desired */
		height: auto; /* Maintain the aspect ratio */
		display: block; /* Ensure the image is displayed as a block element */
		margin-top: 20px;
	}

	#date {
		margin-top: 20px;
	}
	</style>
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
				<select name="movie" id="movie" required>
				<option value="">--Select a movie--</option>
				<?php foreach ($moviesDetails as $movie) { ?>
					<option value="<?php echo $movie['title']; ?>" data-img-src="<?php echo $movie['imageUrl']; ?>">
					<?php echo $movie['title']; ?>
					</option>
				<?php } ?>
				</select>

				<div id="display">
				<!-- Images will display here -->
				</div>

				<script>
				var movieDropdown = document.getElementById("movie");
				var displayDiv = document.getElementById("display");

				movieDropdown.addEventListener("change", function() {
					var selectedOption = this.options[this.selectedIndex];

					displayDiv.innerHTML = "";

					if (selectedOption.value !== "") {
					var movieImage = document.createElement("img");
					movieImage.src = selectedOption.getAttribute("data-img-src");
					movieImage.classList.add("movie-image");

					// var movieName = document.createElement("span");
					// movieName.textContent = selectedOption.text;

					var movieOption = document.createElement("div");
					movieOption.classList.add("movie-option");
					movieOption.appendChild(movieImage);
					//movieOption.appendChild(movieName);

					displayDiv.appendChild(movieOption);
					}
				});
				</script>
				
				<label for="date">Select a date:</label>
				<input type="date" id="date" name="date" min="<?php echo date('Y-m-d'); ?>" max="<?php echo date('Y-m-d', strtotime('+1 month')); ?>" required>

				<label for="time">Select a time:</label>
				<select name="time" required disabled>
					<option value="">--Select a time--</option>
					<option value="morning">Morning</option>
					<option value="afternoon">Afternoon</option>
					<option value="evening">Evening</option>
				</select>

				<label for="ticketType">Type of Ticket:</label>
				<select name="ticketType" required disabled>
					<option value="">--Select a type--</option>
					<option value="adult">Adult</option>
					<option value="child">Child</option>
					<option value="student">Student</option>
					<option value="senior">Senior</option>
				</select>

				<script>
					var dateInput = document.getElementById("date");
					var timeSelect = document.getElementsByName("time")[0];
					var ticketTypeSelect = document.getElementsByName("ticketType")[0];

					dateInput.addEventListener("change", function() {
						// Enable the time and ticket type selects when a date is selected
						timeSelect.disabled = false;
						ticketTypeSelect.disabled = false;
					});
				</script>
			</fieldset>
			<input type="submit" name="submit" value="Book Now">
		</form>
	</main>
	<footer>
		<p>&copy; Hot Dog Cinemas. All rights reserved.</p>
	</footer>
</body>
</html>
