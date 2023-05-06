<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/hotdog_cinemas.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Hotdog Cinemas
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="movies.php">Home</a>
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
    <h1>Welcome Customer!</h1>
    <p>Customer ID: </p>
</div>

<div class="container mt-6 row g-3 mx-auto" style="width: 20%">
    <h1 class="text-center">Cinema Manager Information</h1>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="name" readonly>
            <label for="name">Name: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="surname" readonly>
            <label for="surname">Last name: </label>
        </div>
    </div>
    <div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" readonly>
            <label for="email">E-Mail: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="phone" readonly>
            <label for="phone">Phone No: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="type" readonly>
            <label for="type">Type: </label>
        </div>
    </div>

    
</div>
<div class="text-center mt-3">
    <a class="btn btn-danger" href="movies.php" role="button" style="margin-right: 2.7%">View Movies</a>
    <a class="btn btn-danger" href="food_orders.php" role="button">View Food Orders</a>
    <a class="btn btn-danger" href="cinema_screenings.php" role="button">View Cinema Screenings</a>
    <a class="btn btn-danger" href="ticket_types.php" role="button">View Ticket Types</a>
</div>

</body>
<?php include('footer.php') ?>

</html>