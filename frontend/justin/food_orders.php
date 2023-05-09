<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');


//$viewFoodOrderCh = curl_init();
//curl_setopt($viewFoodOrderCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allActiveCinemaRoomIds");
//curl_setopt($viewFoodOrderCh, CURLOPT_RETURNTRANSFER, 1);
//$foodOrder = curl_exec($viewFoodOrderCh);
//curl_close($viewFoodOrderCh);
//$cinemaRoomDetails = json_decode($cinemaRoomDetails, true);
//print_r($foodOrder);


//$foodDescription = $_POST['foodDesc'];
//$foodPrice = $_POST['foodPrice'];
//$data = array('foodDesc' => $foodDescription, 'foodPrice' => $foodPrice);
//$data_json = json_encode($data);
//$createFoodOrderCh = curl_init();
//curl_setopt($createFoodOrderCh, CURLOPT_URL, "http://localhost:8000/api/cinemaRoom/read/allActiveCinemaRoomIds");
//curl_setopt($createFoodOrderCh, CURLOPT_POST, "1");
//curl_setopt($createFoodOrderCh, CURLOPT_POSTFIELDS, $data_json);
//curl_setopt($createFoodOrderCh, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($createFoodOrderCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//$foodOrder = curl_exec($createFoodOrderCh);
//curl_close($createFoodOrderCh);
//$cinemaRoomDetails = json_decode($cinemaRoomDetails, true);
//print_r($foodOrder);

//$foodDescription = $_POST['foodDesc'];
//$foodPrice = $_POST['foodPrice'];
//
//$updateFoodCh = curl_init();
//$data = array('foodDesc' => $foodDescription, 'foodPrice'=> $foodPrice);
//$data_json = json_encode($data);
////print_r($data_json);
//curl_setopt($updateFoodCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/update/ticketType");
//curl_setopt($updateFoodCh, CURLOPT_CUSTOMREQUEST, "PUT");
//curl_setopt($updateFoodCh, CURLOPT_POSTFIELDS, $data_json);
//curl_setopt($updateFoodCh, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($updateFoodCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//$updateFoodOrder = curl_exec($updateFoodCh);
//curl_close($updateFoodCh);
//$updateFoodOrder = json_decode($updateFoodOrder, true);


//$foodDescription = $_POST['foodDesc'];
//$foodPrice = $_POST['foodPrice'];
//
//$updateFoodCh = curl_init();
//$data = array('foodDesc' => $foodDescription, 'foodPrice'=> $foodPrice);
//$data_json = json_encode($data);
////print_r($data_json);
//curl_setopt($updateFoodCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/update/ticketType");
//curl_setopt($updateFoodCh, CURLOPT_CUSTOMREQUEST, "PUT");
//curl_setopt($updateFoodCh, CURLOPT_POSTFIELDS, $data_json);
//curl_setopt($updateFoodCh, CURLOPT_RETURNTRANSFER, 1);
//curl_setopt($updateFoodCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//$updateFoodOrder = curl_exec($updateFoodCh);
//curl_close($updateFoodCh);
//$updateFoodOrder = json_decode($updateFoodOrder, true);


//    $foodDesc = $_POST['movies'];
//    $data = array('foodDesc' => $foodDesc);
//    $data_json = json_encode($data);
//    // print_r(  $data_json);
//    $deleteFoodCh = curl_init( "http://localhost:8000/api/movie/delete/movie");
//    curl_setopt($deleteFoodCh, CURLOPT_CUSTOMREQUEST, "DELETE");
//    curl_setopt($deleteFoodCh, CURLOPT_POSTFIELDS, $data_json);
//    curl_setopt($deleteFoodCh, CURLOPT_RETURNTRANSFER, 1);
//    curl_setopt($deleteFoodCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
//
//    $response = curl_exec($deleteFoodCh);
//    curl_close($deleteFoodCh);
//    print_r ($response);


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
                <a class="nav-link bg-danger active" href="food_orders.php">Food Orders</a>
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
    <h1>Food Orders</h1>
<!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->
</div>
<?php include('footer.php') ?>

</html>