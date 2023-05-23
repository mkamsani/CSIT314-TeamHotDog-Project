<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');

$username = "";
$privilege = "";

if (isset($_SESSION["username"]))
{
    $username = $_SESSION["username"];
    // Use the $username variable as needed
}

if (isset($_SESSION["privilege"]))
{
    $privilege = $_SESSION["privilege"];
    // Use the $username variable as needed
}

//session_unset();
//$_SESSION["username"] = $username;
//$_SESSION["privilege"] = $privilege;

//connecting and setting up readmovie controller
$moviesDetailsCh = curl_init();
curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/active");
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

?>

<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 style="width:25px; margin-bottom: 5px"> HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="Customer.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="CustomerMovies.php">Movies</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="Booking.php">Book a Movie</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="OrderHistory.php">My Bookings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CustAccountDetails.php">My Account</a>
            </li>
            &emsp;<li class="nav-item">
                <a class="nav-link" href="CustomerReview.php">Customer Review</a>
            </li>
            <form method = "get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Book A Movie</h1>
    <p>Username: <?php echo $username; ?></p>
</div>

<div class="container mt-4" style="width: 40%; margin-left: 20%">
    <main>
        <form action="Booking_Screening.php" method="post">
            <fieldset>
                <legend class="text-white">Choose a movie</legend>

                <label for="movie" class ="text-white">Select a movie:</label>
                <select name="movie" id="movie" class="form-control" required>
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
            <div class="mt-3">
                <input type="submit" name="submit" class="btn btn-primary" value="Next">
            </div>
        </form>
    </main>
</div>
</body>

<style>
    .navbar .nav-link
    {
        color: white;
    }

    .navbar .nav-link:hover
    {
        transform: scale(1.1);
    }

    .navbar-brand
    {
        font-family: Cinzel, Arial, sans-serif;
        font-size: 36px;
        color: #e50914;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    }

    #display .movie-image {
        width: 200px; /* Adjust the width as desired */
        height: auto; /* Maintain the aspect ratio */
        display: block; /* Ensure the image is displayed as a block element */
        margin-top: 20px;
    }

    #date {
        margin-top: 20px;
    }

    p
    {
        color: white;
    }
</style>
<?php include('footer.php') ?>

</html>