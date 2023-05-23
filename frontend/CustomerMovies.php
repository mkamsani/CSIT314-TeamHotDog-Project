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
                <a class="nav-link active bg-danger" href="CustomerMovies.php">Movies</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="Booking.php">Book a Movie</a>
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
    <h1>Movies</h1>
    <p>Username: <?php echo $username; ?></p>
</div>

<div class="container mt-3 center movies-container">
    <div class="input-group mb-3" style="width: 40%; margin: auto;">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
    </div>
    <table id="moviesTable" class="table text-white" style="margin: auto; width: 100%; table-layout: fixed">
        <thead>
        <tr>
            <th>Movie Title</th>
            <th>Movie Description</th>
            <th>Genre</th>
            <th>Poster</th>
        </tr>
        </thead>
        <tbody>
        <?php
        $moviesDetailsCh = curl_init();
        curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/active");
        curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
        $moviesDetails = curl_exec($moviesDetailsCh);
        $moviesDetails = json_decode($moviesDetails, true);
        curl_close($moviesDetailsCh);

        foreach ($moviesDetails as $movie)
        {
            $title = $movie['title'];
            $description = $movie['description'];
            $genre = $movie['genre'];
            $poster = $movie['imageUrl'];
            $CR = $movie['contentRating'];
            ?>
            <tr>
                <td class="movie-title text-white"><?php echo $title ." (" .$CR .")"; ?></td>
                <td class="movie-description text-white"><?php echo $description; ?></td>
                <td class="movie-genre text-white"><?php echo $genre; ?></td>
                <td>

                    <img class="movie-poster" src="<?php echo $poster; ?>">
                </td>
            </tr>
        <?php } ?>



        </tbody>
    </table>
</div>
</body>

<style>
    .movie-title {
        font-size: 20px;
        font-weight: bold;
    }

    .movie-description {
        font-size: 18px;
        font-family: "Arial", sans-serif;
        color: #555555;
    }

    .movie-genre {
        font-size: 16px;
        font-family: "Arial", sans-serif;
        color: #888888;
        text-transform: uppercase;
    }

    .movie-poster {
        width: 200px;
        height: 300px;
    }

    .movie-poster-title {
        font-weight: bold;
    }

    .fixed-forms-container {
        position: fixed;
        top: 16%;
        left: 0;
        width: 20%;
        height: 100vh; /* Use 100vh for full screen height */
        display: flex;
        flex-direction: column;
        justify-content: flex-start; /* Align forms to the top */
        align-items: center;
        padding: 20px;
        background-color: rgba(255, 255, 255, 0);
        z-index: 999;
    }

    .movies-container
    {
        margin-left: 25%;
        width: 75%;
    }

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
</style>
<script>
    function tableSearch()
    {
        // Declare variables
        var input, filter, table, tr, td, i;
        input = document.getElementById("searchBox");
        filter = input.value.toUpperCase();
        table = document.getElementById("moviesTable");
        tr = table.getElementsByTagName("tr"),
            th = table.getElementsByTagName("th");

        // Loop through all table rows, and hide those who don't match the search query
        for (i = 1; i < tr.length; i++)
        {
            tr[i].style.display = "none";
            for (var j = 0; j < th.length; j++)
            {
                td = tr[i].getElementsByTagName("td")[j];
                if (td)
                {
                    if (td.innerHTML.toUpperCase().indexOf(filter.toUpperCase()) > -1)
                    {
                        tr[i].style.display = "";
                        break;
                    }
                }
            }
        }
    }
</script>
<?php include('footer.php') ?>

</html>