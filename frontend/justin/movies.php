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


$cinemaRoomIdsCh = curl_init();
curl_setopt($cinemaRoomIdsCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allCinemaRoomIds");
curl_setopt($cinemaRoomIdsCh, CURLOPT_RETURNTRANSFER, 1);
$cinemaRoomIds = curl_exec($cinemaRoomIdsCh);
$cinemaRoomIds = trim($cinemaRoomIds, '[');
$cinemaRoomIds = trim($cinemaRoomIds, ']');
$cinemaRoomIds = explode(", ",$cinemaRoomIds);
curl_close($cinemaRoomIdsCh);



$cinemaRoomDetailCh = curl_init();
curl_setopt($cinemaRoomDetailCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allCinemaRoomDetails");
curl_setopt($cinemaRoomDetailCh, CURLOPT_RETURNTRANSFER, 1);
$cinemaRoomDetails = curl_exec($cinemaRoomDetailCh);
curl_close($cinemaRoomDetailCh);
$cinemaRoomDetails = json_decode($cinemaRoomDetails, true);
//print_r($cinemaRoomDetails);

//$ticketTypeCh = curl_init();
//curl_setopt($ticketTypeCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/read/allTicketTypes");
//curl_setopt($ticketTypeCh, CURLOPT_RETURNTRANSFER, 1);
//$ticketType = curl_exec($ticketTypeCh);
//curl_close($ticketTypeCh);
//$ticketType = json_decode($ticketType, true);
//print_r($ticketType);


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
            <th>Movie Description </th>
            <th>Genre</th>
            <th>Poster</th>
            <th> Cinema Room </th>
            <th> Room Active </th>


        </thead><tbody><tr>
            <td><?php foreach($movies as $movieKey) {
                    echo ''.$movieKey.'<br/>';
            }  ?></td>

            <td><?php
                $description = array_column($moviesDetails, 'description');
                foreach($description as $descKey) {
                    echo ''.$descKey.'<br/>';
                }
                ?></td>
<           <td><?php
                 $genre = array_column($moviesDetails, 'genre');
                 foreach($genre as $genreKey) {
                      echo ''.$genreKey.'<br/>';
                 }
                 ?></td>
            <td><?php
                $poster = array_column($moviesDetails, 'imageUrl');
                foreach($poster as $imageKey) {
                ?>
                 <img src="<?php echo $imageKey ?>" width="100" height="50"> <br/>
                 <?php
                }
                ?>
             </td>
                <td><?php foreach($cinemaRoomIds as $CinemaRoomKey) {
                    echo ''.$CinemaRoomKey.'<br/>';
                }  ?></td>

            <td>
                <?php
                $cinemaRoom = array_column($cinemaRoomDetails, 'id');
                $checkActive = array_column($cinemaRoomDetails, 'isActive');
                    foreach($cinemaRoom as $cinemaRoomDetailKey) {
                        foreach($checkActive as $activeRoom)
                            if($CinemaRoomKey == $cinemaRoomDetailKey && $activeRoom == 1) {
                                echo ''.$activeRoom.'<br/>';
                            }
                }
                 ?></td>

    </table>
<br>

    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="updateMovie">
        <label for="moviesDDL">Choose a movie:</label>

        <select name="movies" id="movies">
            <option> Select Movie </option>
            <?php
            foreach($movies as $movieKey) {
            ?>
                <option><?php echo ''.$movieKey.'<br/>'; ?></option>
            <?php
            }
            ?>
        </select>
        <input type="submit" name="delete" value="delete">
        <br/>
            <label for="movieName" >Update Movie Name to update:</label>
            <input type="text"  name="movieName" id="movieName" placeholder="Enter Movie Name">
            <input type="submit" name="update" value="update">
    </form>

    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="createMovie">

        <input type="text"  name="movieName" id="movieName" placeholder="Enter Movie title">
        <input type="text"  name="movieGenre" id="movieGenre" placeholder="Enter Movie genre">
        <input type="text"  name="movieDesc" id="movieDesc" placeholder="Enter Movie Description">
        <input type="date"  name="movieRD" id="movieRD">
        <input type="text"  name="moviePoster" id="moviePoster" placeholder="Enter image URL">
        <select name="isActive" id="isActive">
            <option> Select Movie Activity </option>
            <option value = "TRUE"> Active </option>
            <option value = "FALSE"> Not Active </option>
        </select>
        <input type="text"  name="contentRating" id="contentRating" placeholder="Enter content Rating">



        <input type="submit" name="create" value="create">
    </form>
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



</div>
<?php include('footer.php') ?>

</html>