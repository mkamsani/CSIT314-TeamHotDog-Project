<!DOCTYPE html>
<html lang="en">

  <?php
  session_start();
  include('header.php');


  $moviesDetailsCh = curl_init();
  curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/manager/movie/read/all");
  curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
  $moviesDetails = curl_exec($moviesDetailsCh);
  curl_close($moviesDetailsCh);
  $moviesDetails = json_decode($moviesDetails, true);

  ?>
<head>
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
</head>




<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 style="width:25px; margin-bottom: 5px">HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="CinemaManager.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="movies.php">Movies</a>
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

<?php

if (isset($_POST['update'])) {

    $updatedMovieName = str_replace(' ', '%20', $_POST['movies']);
    // echo $updatedMovieName;
    $movieName = $_POST['movieName'];
    $updatedMovieGenre = $_POST['movieGenre'];
    $updatedMovieDesc = $_POST['movieDesc'];
    $updatedMovieDate = $_POST['movieRD'];
    $updatedMoviePoster = $_POST['moviePoster'];
    $updatedMovieLandScapePoster = $_POST['landScapePoster'];
    $updatedMovieRating = $_POST['moviesCR'];
    $data = array('title' => $movieName, 'genre' => $updatedMovieGenre, 'description' => $updatedMovieDesc, 'releaseDate' => $updatedMovieDate,
        'imageUrl' => $updatedMoviePoster, 'landscapeImageUrl' =>$updatedMovieLandScapePoster , 'contentRating' => $updatedMovieRating);

    $data_json = json_encode($data);
    //print_r(  $data_json);
    $updateMoviesCh = curl_init( 'http://localhost:8000/api/manager/movie/update/'. $updatedMovieName);
    curl_setopt($updateMoviesCh, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($updateMoviesCh, CURLOPT_POSTFIELDS, $data_json);
    curl_setopt($updateMoviesCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($updateMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

    $updateResponse = curl_exec($updateMoviesCh);
    curl_close($updateMoviesCh);

    if (strpos($updateResponse, 'Success') !== false) //Show success message
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Movie has been updated</a>.
                        </div>
                    </div>';
    }

    else
    {
        // Error message
        echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $updateResponse . '
                </div>
            </div>';
    }

}

if (isset($_POST['create'])) {

    $movieName = $_POST['movieName'];
    $movieGenre = $_POST['movieGenre'];
    $movieDesc = $_POST['movieDesc'];
    $movieDate = $_POST['movieRD'];
    $moviePoster = $_POST['moviePoster'];
    $movieLandScapePoster = $_POST['landScapePoster'];
    $movieActive = $_POST['isActive'];
    $movieRating = $_POST['moviesCR'];
    $data = array('title' => $movieName, 'genre' => $movieGenre, 'description' => $movieDesc, 'releaseDate' => $movieDate,
        'imageUrl' => $moviePoster, 'landscapeImageUrl' =>$movieLandScapePoster , 'isActive' => $movieActive, 'contentRating' => $movieRating);
    $data_json = json_encode($data);
    //print_r(  $data_json);
    $createMoviesCh = curl_init('http://localhost:8000/api/manager/movie/create/movie');
    curl_setopt($createMoviesCh, CURLOPT_POST, "1");
    curl_setopt($createMoviesCh, CURLOPT_POSTFIELDS, $data_json);
    curl_setopt($createMoviesCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($createMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

    $createResponse = curl_exec($createMoviesCh);
    curl_close($createMoviesCh);
    //print_r ($createResponse);
    if (strpos($createResponse, 'Success') !== false) //Show success message
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Movie has been created</a>.
                        </div>
                    </div>';
    }

    else
    {
        // Error message
        echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $createResponse . '
                </div>
            </div>';
    }
}
if (isset($_POST['suspend'])) {
    $suspendMovieName = str_replace(' ', '%20', $_POST['movies']);
    //echo $suspendMovieName;
    $suspendMoviesCh = curl_init( 'http://localhost:8000/api/manager/movie/suspend/'.$suspendMovieName);
    // print_r(  $data_json);
    curl_setopt($suspendMoviesCh, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($suspendMoviesCh, CURLOPT_POSTFIELDS, $suspendMovieName);
    curl_setopt($suspendMoviesCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($suspendMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
    $suspendResponse = curl_exec($suspendMoviesCh);
    curl_close($suspendMoviesCh);

    if (strpos($suspendResponse, 'Success') !== false) //Show success message
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Movie has been suspended</a>.
                        </div>
                    </div>';
    }

    else
    {
        // Error message
        echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $suspendResponse . '
                </div>
            </div>';
    }
}


if (isset($_POST['delete']) ) {
    $deleteMovieName = str_replace(' ', '%20', $_POST['movies']);
    $deleteMoviesCh = curl_init( 'http://localhost:8000/api/manager/movie/delete/'.$deleteMovieName);

    curl_setopt($deleteMoviesCh, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($deleteMoviesCh, CURLOPT_POSTFIELDS, $deleteMovieName);
    curl_setopt($deleteMoviesCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($deleteMoviesCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
    $deleteResponse = curl_exec($deleteMoviesCh);
    curl_close($deleteMoviesCh);
    //print_r($deleteResponse);
//    echo "<meta http-equiv='refresh' content='0'>";
    if (strpos($deleteResponse, 'Success') !== false) //Show success message
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Movie has been deleted </a>.
                        </div>
                    </div>';
    }

    else
    {
        // Error message
        echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $deleteResponse . '
                </div>
            </div>';
    }
}


?>
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
            <th>Activity</th>
        </tr>
        </thead>
        <tbody>
        <?php
        foreach ($moviesDetails as $movie) {
            $title = $movie['title'];
            $description = $movie['description'];
            $genre = $movie['genre'];
            $poster = $movie['imageUrl'];
            $CR = $movie['contentRating'];
            $isActive = $movie['isActive'];
            ?>
            <tr>
                <td class="movie-title text-white"><?php echo $title ." (" .$CR .")"; ?></td>
                <td class="movie-description text-white"><?php echo $description; ?></td>
                <td class="movie-genre text-white"><?php echo $genre; ?></td>
                <td>

                    <img class="movie-poster" src="<?php echo $poster; ?>">

                </td>
                <td class="movie-isActive"><?php echo $isActive; ?></td>
            </tr>
        <?php } ?>
        </tbody>
    </table>
</div>


<div class="mt-3 fixed-forms-container">
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="updateMovie form-registration">
        <div class="mt-3">
            <label class="form-label text-white" for="moviesDDL" >Choose a movie:</label>
            <select class="form-control" name="movies" id="movies">
                <option>Select Movie</option>
                <?php
                foreach ($moviesDetails as $movie) {
                    $title = $movie['title'];
                    echo '<option>' . $title . '</option>';
                }
                ?>
            </select>

            <div class="mt-3">
                <label class="form-label text-white">Insert New Movie:</label>
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
                <input type="text" class="form-control" name="landScapePoster" id="landScapePoster" placeholder="Enter landscape image URL">
            </div>


            <select class="form-control" name="moviesCR" id="moviesCR">
                <option>Select Content Rating</option>
                <?php
                $data = array( "g", "pg", "pg13", "nc16", "m18", "r21");
                foreach ($data as $CR){
                    echo '<option>' . $CR . '</option>';
                }

                ?>
            </select>
        </div>

        <div class="mt-3">
            <input type="submit" class="btn btn-primary" name="update" value="Update">
            <input type="submit" class="btn btn-primary" name="create" value="Create">
            <input type="submit" class="btn btn-outline-danger" name="suspend" value="Suspend">
            <input type="submit" class="btn btn-outline-danger" name="delete" value="Delete">


        </div>
    </form>


</div>


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