<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');
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
            &emsp;<li class="nav-item">
                <a class="nav-link" href="Booking.php">Book a Movie</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="OrderHistory.php">My Bookings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CustAccountDetails.php">My Account</a>
            </li>
            &emsp;<li class="nav-item">
                <a class="nav-link active bg-danger" href="CustomerReview.php">Customer Review</a>
            </li>
            <form method = "get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Leave a Rating/Review</h1>
</div>

<?php
if (isset($_POST['rating'])) {
    $ch = curl_init("http://localhost:8000/api/customer/ratings/create");
    $json = json_encode(array(
        "username" => $_SESSION['username'],
        "rating" => $_POST['rating'],
        "review" => $_POST['review']
    ));
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
    $result = curl_exec($ch);
    curl_close($ch);

    if ($result == "Success")
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Thank you for your cinema review! We appreciate your feedback and contribution.</strong> Head over to <a href="" class="alert-link">Book a Movie</a>
                        to book a movie, or go back to <a href="Customer.php" class="alert-link">main page</a>.
                        </div>
                    </div>';
    }
}

?>

<div class="d-flex justify-content-center">
    <form action="CustomerReview.php" method="POST" class="text-light" style="font-size: 1.2rem;">
        <div class="form-group mt-3" style="text-align: center">
            <label for="rating">Rating:</label><br>
            <span>(Worst)</span>
            &nbsp;
            &nbsp;
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="rating" id="rating1" value="1" checked>
                <label class="form-check-label" for="rating1">1</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="rating" id="rating2" value="2">
                <label class="form-check-label" for="rating2">2</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="rating" id="rating3" value="3">
                <label class="form-check-label" for="rating3">3</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="rating" id="rating4" value="4">
                <label class="form-check-label" for="rating4">4</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="rating" id="rating5" value="5">
                <label class="form-check-label" for="rating5">5</label>
            </div>
            <span>(Best)</span>
        </div>

        <div class="form-group mt-3" style="text-align: center">
            <label for="review">Review:</label><br>
            <textarea class="form-control" name="review" rows="4" cols="50" placeholder="Write your review here..." required></textarea>
        </div>

        <div class="form-group mt-3" style="text-align: center">
            <button type="submit" class="btn btn-primary">Submit</button>
        </div>
    </form>
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
</style>
<?php include('footer.php') ?>

</html>