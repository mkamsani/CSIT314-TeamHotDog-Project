<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');

$moviesCh = curl_init();

curl_setopt($moviesCh, CURLOPT_URL, "http://localhost:8000/api/movie/read/allMovieTitles");
curl_setopt($moviesCh, CURLOPT_RETURNTRANSFER, 1);
$movies = curl_exec($moviesCh);
$movies = trim($movies, '[');
$movies = trim($movies, ']');
$movies = explode(", ",$movies);
curl_close($moviesCh);

$moviesDetailsCh = curl_init();

curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/movie/read/allMoviesDetails");
curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
$moviesDetails = curl_exec($moviesDetailsCh);
curl_close($moviesDetailsCh);
$moviesDetails = json_decode($moviesDetails, true);
print_r($moviesDetails);
?>

<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/hotdog_cinemas.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Hotdog Cinemas
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="CinemaManager.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="food_orders.php">Food Orders</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="cinema_screenings.php">Cinemas</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="ticket_types.php">Ticket Types</a>
            </li>
            &emsp;
            <form class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Movies</h1>
<!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->


    <table>
        <thead><tr>
            <th>Movie Title</th>
            <th>Movie Description</th>
            <th>Genre</th>
            <th>Poster</th>
        </thead><tbody><tr>
            <td><?php foreach($movies as $key) {
                    echo ''.$key.'<br/>';
                }  ?></td>
            <td><?php
                $description = array_column($moviesDetails, 'description');
                foreach($description as $key) {
                    echo ''.$key.'<br/>';
                }
                ?></td>
            <td><?php
                $genre = array_column($moviesDetails, 'genre');
                foreach($genre as $key) {
                    echo ''.$key.'<br/>';
                }
                ?></td>
            <td><?php
                $poster = array_column($moviesDetails, 'imageUrl');
                foreach($poster as $key) {?>
                    <img src="<?php echo $key ?>" width="500" height="100" > <br/>

                <?php

                }
                ?>

               </td>


    </table>
</div>
<?php include('footer.php') ?>

</html>