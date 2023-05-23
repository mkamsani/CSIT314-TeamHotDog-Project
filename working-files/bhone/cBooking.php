<!DOCTYPE html>

<?php
	//connecting and setting up readmovie controller
	$moviesDetailsCh = curl_init();
	curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/all");
	curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
	$moviesDetails = curl_exec($moviesDetailsCh);
	
	curl_close($moviesDetailsCh);
	$moviesDetails = json_decode($moviesDetails, true);

	//connecting and setting up readscreening controller
	$screeningDetailsCh = curl_init();
	curl_setopt($screeningDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/screening/read/all");
	curl_setopt($screeningDetailsCh, CURLOPT_RETURNTRANSFER, 1);
	$screeningDetails = curl_exec($screeningDetailsCh);

	curl_close($screeningDetailsCh);
	$screeningDetails = json_decode($screeningDetails, true);

	//connecting and setting up readscreening controller
	$ticketTypeDetailsCh = curl_init();
	curl_setopt($ticketTypeDetailsCh, CURLOPT_URL, "http://localhost:8000/api/manager/ticketType/read/all");
	curl_setopt($ticketTypeDetailsCh, CURLOPT_RETURNTRANSFER, 1);
	$ticketTypeDetails = curl_exec($ticketTypeDetailsCh);

	curl_close($ticketTypeDetailsCh);
	$ticketTypeDetails = json_decode($ticketTypeDetails, true);

	// foreach ($screeningDetails as $screening)
	// {
	// 	echo $screening['showDate'];
	// }

	// foreach ($moviesDetails as $movies)
	// {
	// 	echo $movies['title'];
	// }

	// foreach ($ticketTypeDetails as $ticketType)
	// {
	// 	echo $ticketType['typename'];
	// }

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
				<option style="text-align:center;" value="">-------- Select a movie --------</option>
				<?php
				$selectedMovies = []; // Array to store selected movies

				foreach ($screeningDetails as $screening) {
					foreach ($moviesDetails as $movie) {
						if ($movie['title'] == $screening['movie'] && !in_array($movie['title'], $selectedMovies)) {
							$selectedMovies[] = $movie['title']; // Add selected movie to the array
							echo '<option style="text-align:center;" value="' . $movie['title'] . '" data-img-src="' . $movie['imageUrl'] . '" data-description="' . htmlspecialchars($movie['description'], ENT_QUOTES) . '">' . $movie['title'] . '</option>';
							break; // Move to the next screening
						}
					}
				}
				?>
				</select>

				<div id="display">
				<!-- Images and description will display here -->
				</div>

				<div>
				<label for="date">Date:</label>
				<select name="date" id="date" required disabled>
				<option style="text-align: center;" value="">--Select a date--</option>
				</select>
				</div>

				<div>
				<label for="time">Time:</label>
				<select name="time" id="time" required disabled>
				<option value="">--Select a time--</option>
				</select>
				</div>

				<div>
				<label for="ticketType">Ticket:</label>
				<select name="ticketType" id="ticketType" required disabled>
				<option value="">--Select a type--</option>
				<?php
					foreach ($ticketTypeDetails as $ticketType) {
						if ($ticketType['typename'] != "redemption") {
							echo '<option style="text-align:center;" value="' . $ticketType['typename'] . '">' . $ticketType['typename'] . '</option>';
						}
					}
				?>
				</select>
				</div>

				<script>
					var movieDropdown = document.getElementById("movie");
					var dateDropdown = document.getElementById("date");
					var timeDropdown = document.getElementById("time");
					var displayDiv = document.getElementById("display");

					movieDropdown.addEventListener("change", function() {
						var selectedOption = this.options[this.selectedIndex];
						var movieTitle = selectedOption.value;
						var movieDescription = selectedOption.getAttribute("data-description");

						displayDiv.innerHTML = "";

						if (selectedOption.value !== "") {
							var movieImage = document.createElement("img");
							movieImage.src = selectedOption.getAttribute("data-img-src");
							movieImage.classList.add("movie-image");

							var movieOption = document.createElement("div");
							movieOption.classList.add("movie-option");
							movieOption.appendChild(movieImage);

							var movieDescriptionParagraph = document.createElement("p");
							movieDescriptionParagraph.textContent = movieDescription;
							movieOption.appendChild(movieDescriptionParagraph);

							displayDiv.appendChild(movieOption);

							// Clear existing date options
							dateDropdown.innerHTML = '<option style="text-align: center;" value="">--Select a date--</option>';

							// Loop through the screeningDetails and add date options for the selected movie
							var dates = [];
							<?php foreach ($screeningDetails as $screening) { ?>
								<?php foreach ($moviesDetails as $movie) { ?>
									<?php if ($movie['title'] == $screening['movie']) { ?>
										if (movieTitle === '<?php echo $movie['title']; ?>') {
											var date = '<?php echo $screening['showDate']; ?>';
											var formattedDate = formatDate(date);
											if (!dates.includes(formattedDate)) {
												dates.push(formattedDate);

												var dateOption = document.createElement("option");
												dateOption.style.textAlign = "center";
												dateOption.value = date;
												dateOption.textContent = formattedDate;
												dateDropdown.appendChild(dateOption);
											}
										}
									<?php } ?>
								<?php } ?>
							<?php } ?>

							// Enable the date dropdown
							dateDropdown.disabled = false;
						} else {
							// Clear the display and disable the date dropdown
							displayDiv.innerHTML = "";
							dateDropdown.innerHTML = '<option style="text-align: center;" value="">--Select a date--</option>';
							dateDropdown.disabled = true;
						}
					});

					dateDropdown.addEventListener("change", function(){
						var selectedDate = this.value;

						//Clear existing time options
						timeDropdown.innerHTML = '<option style="text-align: center;" value="">--Select a time--</option>';

						// Create an array to store unique time values
						var uniqueTimes = [];

						// Loop through the screeningDetails and add unique time options for the selected date
						<?php foreach ($screeningDetails as $screening) { ?>
						if ('<?php echo $screening['showDate']; ?>' === selectedDate) {
							var time = '<?php echo $screening['showTime']; ?>';

							// Check if the time is already added to the uniqueTimes array
							if (!uniqueTimes.includes(time)) {
								uniqueTimes.push(time);

								var timeOption = document.createElement("option");
								timeOption.style.textAlign = "center";
								timeOption.value = time;
								timeOption.textContent = time;
								timeDropdown.appendChild(timeOption);
							}
						}
						<?php } ?>

						// Enable the time dropdown
						timeDropdown.disabled = false;
					});

					function formatDate(dateString) {
						var date = new Date(dateString);
						var day = date.getDate();
						var month = date.getMonth() + 1;
						var year = date.getFullYear();

						// Pad day and month with leading zeros if necessary
						day = day < 10 ? "0" + day : day;
						month = month < 10 ? "0" + month : month;

						return day + "/" + month + "/" + year;
					}

					var movieInput = document.getElementById("movie");
					var dateInput = document.getElementById("date");
					var timeSelect = document.getElementsByName("time")[0];
					var ticketTypeSelect = document.getElementsByName("ticketType")[0];

					movieInput.addEventListener("change", function() {
						dateInput.disabled = false;
					});
					dateInput.addEventListener("change", function() {
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
