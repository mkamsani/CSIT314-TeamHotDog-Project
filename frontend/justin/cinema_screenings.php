<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');

$cinemaRoomIdsCh = curl_init();
curl_setopt($cinemaRoomIdsCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allCinemaRoomIds");
curl_setopt($cinemaRoomIdsCh, CURLOPT_RETURNTRANSFER, 1);
$cinemaRoomIds = curl_exec($cinemaRoomIdsCh);
$cinemaRoomIds = trim($cinemaRoomIds, '[');
$cinemaRoomIds = trim($cinemaRoomIds, ']');
$cinemaRoomIds = explode(", ",$cinemaRoomIds);
curl_close($cinemaRoomIdsCh);
print_r($cinemaRoomIds);


$activeCinemaRoomIdsCh = curl_init();
curl_setopt($activeCinemaRoomIdsCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allActiveCinemaRoomIds");
curl_setopt($activeCinemaRoomIdsCh, CURLOPT_RETURNTRANSFER, 1);
$cinemaRoomDetails = curl_exec($activeCinemaRoomIdsCh);
curl_close($activeCinemaRoomIdsCh);
//$cinemaRoomDetails = json_decode($cinemaRoomDetails, true);
print_r($cinemaRoomDetails);


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
                <a class="nav-link bg-danger active" href="cinema_screenings.php">Cinemas</a>
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
    <h1>Cinema Screenings</h1>
<!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->

    <table>
        <thead><tr>

            <th> Cinema Room </th>
            <th> Room Active </th>


        </thead><tbody><tr>

            <td><?php foreach($cinemaRoomIds as $CinemaRoomKey) {
                    echo ''.$CinemaRoomKey.'<br/>';
                }  ?></td>

<!--            <td>-->
<!--                --><?php
//                $cinemaRoom = array_column($cinemaRoomDetails, 'id');
//                $checkActive = array_column($cinemaRoomDetails, 'isActive');
//                foreach($cinemaRoom as $cinemaRoomDetailKey) {
//                 foreach($checkActive as $activeRoom)
//                     if($CinemaRoomKey == $cinemaRoomDetailKey && $activeRoom == 1) {
//                            echo ''.$cinemaRoomDetailKey.'<br/>';
//                    }
//                }
//                ?>



    </table>
</div>

<br>
<?php include('footer.php') ?>

</html>