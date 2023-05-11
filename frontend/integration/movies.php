<!DOCTYPE html>
<html lang="en">

<style>
    <?php  include('test.css'); ?>
  </style>

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
</div>


<div class="container mt-3 center movies-container">
    <div class="input-group mb-3" style="width: 40%; margin: auto;">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
    </div>
    <table id="moviesTable" class="table table-hover" style="margin: auto; width: 100%; table-layout: fixed">
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
        foreach ($moviesDetails as $movie) {
            $title = $movie['title'];
            $description = $movie['description'];
            $genre = $movie['genre'];
            $poster = $movie['imageUrl'];
            ?>
            <tr>
                <td class="movie-title"><?php echo $title; ?></td>
                <td class="movie-description"><?php echo $description; ?></td>
                <td class="movie-genre"><?php echo $genre; ?></td>
                <td>
                    <div class="flip-card">
                        <div class="flip-card-inner">
                            <div class="flip-card-front">
                                <img class="movie-poster" src="<?php echo $poster; ?>">
                            </div>
                            <div class="flip-card-back">
                                <h6 class = "movie-poster-title"><?php echo $title; ?></h6>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        <?php } ?>
        </tbody>
    </table>
</div>


<div class="mt-3 fixed-forms-container">
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="updateMovie form-registration">
        <div class="mt-3">
            <label class="form-label" for="moviesDDL">Choose a movie:</label>
            <select class="form-control" name="movies" id="movies">
                <option>Select Movie</option>
                <?php
                foreach ($movies as $movie) {
                    echo '<option>' . $movie . '</option>';
                }
                ?>
            </select>
        </div>

        <div class="mt-3">
            <label class="form-label" for="movieName">Update Movie Name to update:</label>
            <input type="text" class="form-control" name="movieName" id="movieName" placeholder="Enter Movie Name">
        </div>

        <div class="mt-3">
            <input type="submit" class="btn btn-primary" name="update" value="Update">
            <input type="submit" class="btn btn-outline-danger" name="suspend" value="Suspend">
        </div>
    </form>

    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="createMovie form-registration">
        <div class="mt-3">
            <label class="form-label">Insert New Movie:</label>
            <input type="text" class="form-control" name="movieName" id="movieName" placeholder="Enter Movie title">
        </div>

        <div class="mt-3">
            <input type="text" class="form-control" name="movieGenre" id="movieGenre" placeholder="Enter Movie genre">
        </div>

        <div class="mt-3">
            <textarea class="form-control" name="movieDesc" id="movieDesc" placeholder="Enter Movie Description"></textarea>
        </div>

        <div class="mt-3">
            <input type="date" class="form-control" name="movieRD" id="movieRD">
        </div>

        <div class="mt-3">
            <input type="text" class="form-control" name="moviePoster" id="moviePoster" placeholder="Enter image URL">
        </div>

        <div class="mt-3">
            <select class="form-control" name="isActive" id="isActive">
                <option>Select Movie Activity</option>
                <option value="TRUE">Active</option>
                <option value="FALSE">Not Active</option>
            </select>
        </div>

        <div class="mt-3">
            <input type="text" class="form-control" name="contentRating" id="contentRating" placeholder="Enter content Rating">
        </div>

        <div class="mt-3">
            <input type="submit" class="btn btn-primary" name="create" value="Create">
        </div>
    </form>
</div>
    <?php

    if (isset($_POST['update'])) {

    $title = $_POST['movies'];

    $movieName = $_POST['movieName'];
    $updatedTitle = $movieName;
    $data = array('targetTitle' => $title, 'title' => $updatedTitle);
    $data_json = json_encode($data);
   // print_r(  $data_json);
    $updateMoviesCh = curl_init( "http://localhost:8000/api/movie/update/movie/Title");
    curl_setopt($updateMoviesCh, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($updateMoviesCh, CURLOPT_POSTFIELDS, $data_json);
    curl_setopt($updateMoviesCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($updateMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

    $response = curl_exec($updateMoviesCh);
    curl_close($updateMoviesCh);
    print_r ($response);
    }

    if (isset($_POST['create'])) {

        $movieName = $_POST['movieName'];
        $movieGenre = $_POST['movieGenre'];
        $movieDesc = $_POST['movieDesc'];
        $movieDate = $_POST['movieRD'];
        $moviePoster = $_POST['moviePoster'];
        $movieActive = $_POST['isActive'];
        $movieRating = $_POST['contentRating'];
        $data = array('title' => $movieName, 'genre' => $movieGenre, 'description' => $movieDesc, 'releaseDate' => $movieDate,
            'imageUrl' => $moviePoster, 'isActive' => $movieActive, 'contentRating' => $movieRating);
        $data_json = json_encode($data);
        print_r(  $data_json);
        $createMoviesCh = curl_init("http://localhost:8000/api/movie/create/movie");
        curl_setopt($createMoviesCh, CURLOPT_POST, "1");
        curl_setopt($createMoviesCh, CURLOPT_POSTFIELDS, $data_json);
        curl_setopt($createMoviesCh, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($createMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

        $response = curl_exec($createMoviesCh);
        curl_close($createMoviesCh);
        print_r ($response);
    }
    if (isset($_POST['delete'])) {
        $movieName = $_POST['movies'];
       // echo $movieName;
        $deletingTitle = $movieName;
        $data = array('title' => $deletingTitle);
        $data_json = json_encode($data);
       // print_r(  $data_json);
        $deleteMoviesCh = curl_init( "http://localhost:8000/api/movie/delete/movie");
        curl_setopt($deleteMoviesCh, CURLOPT_CUSTOMREQUEST, "DELETE");
        curl_setopt($deleteMoviesCh, CURLOPT_POSTFIELDS, $data_json);
        curl_setopt($deleteMoviesCh, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($deleteMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

        $response = curl_exec($deleteMoviesCh);
        curl_close($deleteMoviesCh);
        print_r ($response);
        }

    ?>

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
        top: 19%;
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

    .movies-container {
        margin-left: 25%;
        width: 75%;
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

</body>
<?php include('footer.php') ?>

</html>