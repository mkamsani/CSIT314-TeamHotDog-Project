<!DOCTYPE html>

<?php

$moviesDetailsCh = curl_init();
  curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/active");
  curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
  $moviesDetails = curl_exec($moviesDetailsCh);
  

  curl_close($moviesDetailsCh);
  $moviesDetails = json_decode($moviesDetails, true);
  //$movieName = array_column($moviesDetails, "title");


?>
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
		<?php
			foreach ($moviesDetails as $movie) {
				$title = $movie["title"];
				$img_src = $movie['imageUrl'];
			}
		?>

		<form action="loyalty.php" method="post">
			<fieldset>
				<legend>Choose a movie</legend>
				
				<label for="movie">Select a movie:</label>
				<select name="movie" id="movie" required>
					<option value="">--Select a movie--</option>
					<?php
						foreach ($moviesDetails as $movie) {
							$title = $movie["title"];
							$img_src = $movie['imageUrl'];

							?>
							<option value="<?php echo $title?>"><?php echo $title?></option>
						<?php 
						} 
						?>
					

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
							<?php 
								$columnTitle = array_column($moviesDetails, 'title');
								$columnImg_Src = array_column($moviesDetails, 'image_url');
							
								$combineArray = array_combine($columnTitle, $columnImg_Src);
								foreach($combineArray as $columnTitle => $columnImg_Src) {
									echo $columnTitle;
								}
								
							?>
							movieImage.src = ".com.com.com";
							movieImage.classList.add("movie-image");

							var movieName = document.createElement("span");
							movieName.textContent = selectedOption.text;

							var movieOption = document.createElement("div");
							movieOption.classList.add("movie-option");
							movieOption.appendChild(movieImage);
							movieOption.appendChild(movieName);

							displayDiv.appendChild(movieOption);
						}
					});
				</script>
				
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

			<input type="submit" name="submit" value="Book Now">
		</form>
	</main>
	<footer>
		<p>&copy; Hot Dog Cinemas. All rights reserved.</p>
	</footer>
</body>
</html>
