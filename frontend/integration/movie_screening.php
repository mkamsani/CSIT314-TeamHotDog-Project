<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');

$screeningCh = curl_init();
curl_setopt($screeningCh, CURLOPT_URL, "http://localhost:8000/api/manager/screening/read/all");
curl_setopt($screeningCh, CURLOPT_RETURNTRANSFER, 1);
$screening = curl_exec($screeningCh);
curl_close($screeningCh);
$screening = json_decode($screening, true);

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
            position: absolute;
            left: 2%; /* Remove the 'right' property */
            top: unset; /* Remove the 'top' property */
            right: unset; /* Update the 'left' property to 'right' */
            width: unset; /* Remove the 'width' property */
            height: unset;
            display: flex;
            flex-direction: row;
            justify-content: flex-start; /* Align forms to the top */
            padding: 20px;
            background-color: rgba(255, 255, 255, 0);
            z-index: 999;
            flex-direction: column;
            justify-content: flex-start;
            align-items: flex-start;
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
            <h1 class="text-center">HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="CinemaManager.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="movies.php">Movies</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class=" nav-link bg-danger active" href="movie_screening.php">Screening</a>
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
    <h1>Screening</h1>
    <!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->

</div>

<?php

if (isset($_POST['suspend'])) {

$suspendShowTime = $_POST['suspendShowTime'];
$suspendShowDate = $_POST['suspendShowDate'];
$suspendCinemaRoomID = $_POST['suspendCinemaRoomID'];
$data = array('currentShowTime' => $suspendShowTime, 'currentShowDate' => $suspendShowDate, 'cinemaRoomId' => $suspendCinemaRoomID);
$data_json = json_encode($data);
//print_r(  $data_json);
$suspendScreeningCh = curl_init( 'http://localhost:8000/api/manager/screening/suspend/'.$suspendShowTime.'/'.$suspendShowDate.'/'.$suspendCinemaRoomID);
curl_setopt($suspendScreeningCh, CURLOPT_CUSTOMREQUEST, "PUT");
curl_setopt($suspendScreeningCh, CURLOPT_POSTFIELDS, $data_json);
curl_setopt($suspendScreeningCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($suspendScreeningCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

$suspendResponse = curl_exec($suspendScreeningCh);
curl_close($suspendScreeningCh);
echo $suspendResponse;
if (strpos($suspendResponse, 'Success') !== false) //Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%; margin-left: 150px">
        <strong>Success!</strong> screening has been suspended</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%; margin-left: 150px">
        <strong>Error:</strong> ' . $suspendResponse . '
    </div>
</div>';
}

}



if (isset($_POST['update'])) {

$currentShowTime = $_POST['showTime'];
$currentShowDate = $_POST['showDate'];
$currentCinemaRoomID = $_POST['cinemaRoomID'];
$updatedShowName = $_POST['newMovieName'];
$updatedShowTime = $_POST['newShowTime'];
$updatedShowDate = $_POST['newShowDate'];
$updatedCinemaRoomID =  $_POST['newCinemaRoomID'];
$data = array('targetShowTime' => $currentShowTime, 'targetShowDate' => $currentShowDate, 'targetCinemaRoomId' => $currentCinemaRoomID, 'newMovieTitle' =>  $updatedShowName,
'newShowTime' => $updatedShowTime, 'newShowDate' => $updatedShowDate, 'newCinemaRoomId' => $updatedCinemaRoomID );
$data_json = json_encode($data);
//print_r(  $data_json);
$updateScreeningCh = curl_init( 'http://localhost:8000/api/manager/screening/update/'.$currentShowTime.'/'.$currentShowDate.'/'.$currentCinemaRoomID);
curl_setopt($updateScreeningCh, CURLOPT_CUSTOMREQUEST, "PUT");
curl_setopt($updateScreeningCh, CURLOPT_POSTFIELDS, $data_json);
curl_setopt($updateScreeningCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($updateScreeningCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

$updateResponse = curl_exec($updateScreeningCh);
curl_close($updateScreeningCh);

if (strpos($updateResponse, 'Success') !== false) //Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;margin-left: 150px">
        <strong>Success!</strong>  screening has been updated</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%; margin-left: 150px">
        <strong>Error:</strong> ' . $updateResponse . '
    </div>
</div>';
}

}

if (isset($_POST['create'])) {


$createMovieName = $_POST['createScreeningName'];
$createShowTime= $_POST['createShowTime'];
$createShowDate = $_POST['createShowDate'];
$createCinemaRoomID = $_POST['createCinemaRoomID'];
$data = array('MovieTitle' => $createMovieName, 'ShowTime' => $createShowTime, 'ShowDate' => $createShowDate, 'CinemaRoomId' => $createCinemaRoomID);
$data_json = json_encode($data);
print_r(  $data_json);
$createScreeningCh = curl_init("http://localhost:8000/api/manager/screening/create/screening");
curl_setopt($createScreeningCh, CURLOPT_POST, "1");
curl_setopt($createScreeningCh, CURLOPT_POSTFIELDS, $data_json);
curl_setopt($createScreeningCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($createScreeningCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

$createResponse = curl_exec($createScreeningCh);
curl_close($createScreeningCh);
//print_r ($createResponse);
if (strpos($createResponse, 'Success') !== false) //Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%; margin-left: 150px">
        <strong>Success!</strong> screening has been created</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%; margin-left: 150px">
        <strong>Error:</strong> ' . $createResponse . '
    </div>
</div>';
}
}
//
//if (isset($_POST['cancel']) ) {
//
//$cancelShowTime= $_POST['cancelShowTime'];
//$cancelShowDate = $_POST['cancelShowDate'];
//$cancelCinemaRoomID = $_POST['cancelCinemaRoomID'];
//$data = array('currentShowTime' => $cancelShowTime, 'currentShowDate' => $cancelShowDate, 'cinemaRoomId' => $cancelCinemaRoomID, 'CinemaRoomId' => $cancelCinemaRoomID);
//$data_json = json_encode($data);
////print_r(  $data_json);
//$cancelScreeningCh = curl_init( 'http://localhost:8000/api/manager/screening/cancel/'.$cancelShowTime.'/'.$cancelShowDate.'/'.$cancelCinemaRoomID);
//curl_setopt($cancelScreeningCh, CURLOPT_CUSTOMREQUEST, "PUT");
//curl_setopt($cancelScreeningCh, CURLOPT_POSTFIELDS,  $data_json);
//curl_setopt($cancelScreeningCh, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($cancelScreeningCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//$cancelResponse = curl_exec($cancelScreeningCh);
//curl_close($cancelScreeningCh);
////    echo "<meta http-equiv='refresh' content='0'>";
//if (strpos($cancelResponse, 'Success') !== false) //Show success message
//{
//echo '
//<div class="container mt-5" >
//    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;" margin-left: 150px>
//        <strong>Success!</strong> Screening has been cancelled </a>.
//    </div>
//</div>';
//}
//
//else
//{
//// Error message
//echo '
//<div class="container mt-3">
//    <div class="alert alert-danger" style="width: 75%;" margin-left: 150px>
//        <strong>Error:</strong> ' . $cancelResponse . '
//    </div>
//</div>';
//}
//}
?>
<div class="container mt-4" style="margin-left: 20%; width: 40%">
    <div class="input-group mb-3" style="width: 40%; margin: auto;">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
    </div>

    <div class="mt-3 fixed-forms-container">
        <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="suspendScreening form-registration">
            <div class="mt-2">
                <h2 class="form-label text-white" style = "text-decoration: underline;">Suspend Screening:</h2>
                <select class="form-control" name="suspendShowTime" id="suspendShowTime">
                    <option>Select show Time</option>
                    <?php
                    $data = array("morning", "afternoon", "evening", "midnight");
                    foreach ($data as $showTimeKey) {
                        echo '<option>' . $showTimeKey . '</option>';
                    }
                    ?>
                </select>
            </div>
            <div class="mt-2">
                <input type="date" class="form-control" name="suspendShowDate" id="suspendShowDate">
            </div>
            <div class="mt-2">
                <input type="number" step="1" class="form-control" name="suspendCinemaRoomID" id="suspendCinemaRoomID" placeholder="Enter Cinema Room">
            </div>
            <div class="mt-3">
                <input type="submit" class="btn btn-primary" name="suspend" value="Suspend">
            </div>
        </form>

        <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="updateScreening form-registration">
            <div class="mt-3">
                <h2 class="form-label text-white" style = "text-decoration: underline;">Update Screening:</h2>
                <select class="form-control" name="showTime" id="showTime">
                    <option>Select show Time</option>
                    <?php
                    $data = array("morning", "afternoon", "evening", "midnight");
                    foreach ($data as $showTimeKey) {
                        echo '<option>' . $showTimeKey . '</option>';
                    }
                    ?>
                </select>
            </div>
            <div class="mt-3">
                <input type="date" class="form-control" name="showDate" id="showDate">
            </div>
            <div class="mt-3">
                <input type="number" step="1" class="form-control" name="cinemaRoomID" id="cinemaRoomID" placeholder="Enter Cinema Room">
            </div>
            <div class="mt-3">
                <select class="form-control" name="newMovieName" id="newMovieName">
                    <option>Enter Movie Name</option>
                    <?php
                    foreach ($moviesDetails as $movie) {
                        $movieName = $movie['title'];
                        echo '<option>' . $movieName . '</option>';
                    }
                    ?>
                </select>
            </div>
            <div class="mt-3">
                <select class="form-control" name="newShowTime" id="newShowTime">
                    <option>Select new show Time</option>
                    <?php
                    $data = array("morning", "afternoon", "evening", "midnight");
                    foreach ($data as $showTimeKey) {
                        echo '<option>' . $showTimeKey . '</option>';
                    }
                    ?>
                </select>
            </div>
            <div class="mt-3">
                <input type="date" class="form-control" name="newShowDate" id="newShowDate">
            </div>
            <div class="mt-3">
                <input type="number" step="1" class="form-control" name="newCinemaRoomID" id="newCinemaRoomID" placeholder="Enter new Cinema Room">
            </div>
            <div class="mt-3">
                <input type="submit" class="btn btn-primary" name="update" value="Update">
            </div>
        </form>

        <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="createScreening form-registration">
            <div class="mt-2">
                <h2 class="form-label text-white" style = "text-decoration: underline;">Create Screening:</h2>

                <div class="mt-2">
                    <select class="form-control" name="createScreeningName" id="createScreeningName">
                        <option>Enter Movie Name</option>
                        <?php
                        foreach ($moviesDetails as $movie) {
                            $movieName = $movie['title'];
                            echo '<option>' . $movieName . '</option>';
                        }
                        ?>
                    </select>
                </div>

                <div class="mt-2">
                    <select class="form-control" name="createShowTime" id="createShowTime">
                        <option>Select show Time</option>
                        <?php
                        $data = array("morning", "afternoon", "evening", "midnight");
                        foreach ($data as $showTimeKey) {
                            echo '<option>' . $showTimeKey . '</option>';
                        }
                        ?>
                    </select>
                </div>
            </div>
            <div class="mt-2">
                <input type="date" class="form-control" name="createShowDate" id="createShowDate">
            </div>
            <div class="mt-2">
                <input type="number" step="1" class="form-control" name="createCinemaRoomID" id="createCinemaRoomID" placeholder="Enter Cinema Room">
            </div>
            <div class="mt-3">
                <input type="submit" class="btn btn-primary" name="create" value="Create">
            </div>
        </form>

<!--        <form action="--><?php //echo $_SERVER['PHP_SELF']; ?><!--" method="POST" class="cancelScreening form-registration">-->
<!--            <div class="mt-2">-->
<!--                <h2 class="form-label text-white" style = "text-decoration: underline;">Cancel Screening:</h2>-->
<!--                <select class="form-control" name="cancelShowTime" id="cancelShowTime">-->
<!--                    <option>Select show Time</option>-->
<!--                    --><?php
//                    $data = array("morning", "afternoon", "evening", "midnight");
//                    foreach ($data as $showTimeKey) {
//                        echo '<option>' . $showTimeKey . '</option>';
//                    }
//                    ?>
<!--                </select>-->
<!--            </div>-->
<!--            <div class="mt-2">-->
<!--                <input type="date" class="form-control" name="cancelShowDate" id="cancelShowDate">-->
<!--            </div>-->
<!--            <div class="mt-2">-->
<!--                <input type="number" step="1" class="form-control" name="cancelCinemaRoomID" id="cancelCinemaRoomID">-->
<!--            </div>-->
<!--            <div class="mt-3">-->
<!--                <input type="submit" class="btn btn-outline-danger" name="cancel" value="Cancel">-->
<!--            </div>-->
<!--        </form>-->
    </div>

    <table id="moviesTable" class="table text-white" style="width: 100%; table-layout: fixed; margin: auto auto auto 150px;">
        <thead>
        <tr>
            <th>Movie Title</th>
            <th>Screening</th>
            <th>status</th>
            <th>show Date</th>
            <th>Cinema Room</th>
        </tr>
        </thead>
        <tbody>
        <?php
        foreach ($screening as $screeningDetails) {
            $movieName = $screeningDetails['movie'];
            $showTime = $screeningDetails['showTime'];
            $status = $screeningDetails['status'];
            $showDate = $screeningDetails['showDate'];
            $CR = $screeningDetails['cinemaRoom'];
            ?>
            <tr>
                <a>
                    <td class="movie-title text-white"><?php echo $movieName  ?></td>
                    <td class="show-Time text-white"><?php echo $showTime ?></td>
                    <td class="status text-white"><?php echo $status ?></td>
                    <td class="show-Date text-white"><?php echo $showDate ?></td>
                    <td class="Cinema Room text-white"><?php echo "Cinema Room" . " ". $CR ?></td>
                </a>
            </tr>
        <?php } ?>



        </tbody>
    </table>

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

</div>
</body>


