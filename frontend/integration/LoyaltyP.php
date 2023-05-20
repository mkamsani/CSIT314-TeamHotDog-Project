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

$sessionVariables = ['movie', 'date', 'time', 'ticketType'];

$moviesDetailsCh = curl_init();
curl_setopt($moviesDetailsCh, CURLOPT_URL, "http://localhost:8000/api/customer/movie/read/active");
curl_setopt($moviesDetailsCh, CURLOPT_RETURNTRANSFER, 1);
$moviesDetails = curl_exec($moviesDetailsCh);

curl_close($moviesDetailsCh);
$moviesDetails = json_decode($moviesDetails, true);

// $columnTitle = array_column($moviesDetails, 'title');
// $columnImg_Src = array_column($moviesDetails, 'imageUrl');
// $combineArray = array_combine($columnTitle, $columnImg_Src);

foreach ($sessionVariables as $variable)
{
    if (isset($_POST[$variable])) {
        $_SESSION[$variable] = $_POST[$variable];
    } elseif (!isset($_SESSION[$variable])) {
        $_SESSION[$variable] = null;
    }
}

$moviePrice = 9.00;

switch ($_SESSION['ticketType']) {
    case 'adult':
        $ticketPrice = $moviePrice;
        break;
    case 'student':
        $ticketPrice = $moviePrice * 0.8;
        break;
    case 'senior':
        $ticketPrice = $moviePrice * 0.7;
        break;
    case 'child':
        $ticketPrice = $moviePrice * 0.6;
        break;
    default:
        $ticketPrice = 0;
}

$totalCost = $ticketPrice;


$totalLoyaltyPoint = 10; //Integrate this

// for ($moviesDetails as $movies)
// {
// 	echo $movies['imageUrl'];
// }

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
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="Booking.php">Book a Movie</a>
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
    <h1>Confirm Booking</h1>
</div>

<?php

    if ($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        if (isset($_POST['action']) &&  $_POST['action'] == 'Confirm')
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Booking Succesful!</strong>. Head over to the <a href="OrderHistory.php" class="alert-link">My Bookings</a>
                        to view your Booking.
                        </div>
                    </div>';
        }
    }

?>

<div class="container mt-4" style="width: 50%">
    <main>
        <h1 style="text-align: center; color:white;">Booking Summary</h1>
        <table align="center" border="0" cellpadding="5" cellspacing="0">
            <tr>
                <td rowspan="3"><img src="<?php echo $moviesDetails['imageUrl']; ?>" alt="Error" width="120" height="120"></td>
                <td style="text-align: center" class = "text-white"><?php echo $_SESSION['ticketType'] ?> | <?php echo $_SESSION['movie']; ?></td>
            </tr>
            <tr>
                <td style="text-align: center" class = "text-white"><?php echo date('d/m/Y', strtotime($_SESSION['date'])) ?></td>
            </tr>
            <tr>
                <td style="text-align: center" class = "text-white"><?php echo $_SESSION['time'] ?></td>
            </tr>
        </table>
        <h3 style="text-align: center; color: white">Total cost: $<?php echo number_format((float)$totalCost, 2, '.', ''); ?></h3>

        <div class = "mt-5" style="text-align: center;">
            <h4>You currently have <span id="loyalty-points"><u><?php echo $totalLoyaltyPoint ?></u></span> loyalty points.</h4>
            <h4>1pt = $1.00 discount.</h4>
        </div>

        <div class = "mt-5" style="text-align: center;">
            <h4>Enter the amount of loyalty points you would like to use:</h4>
            <form action="LoyaltyP.php" method="POST" style="display: flex; align-items: center; justify-content: center;">
                <input type="number" name="loyaltyPoint" id="loyaltyPoint" style="font-size: 20px; width: 20%; text-align: center; margin-right: 10px;" value="0" min="0"
                       max="<?php echo $totalLoyaltyPoint ?>">
                <input type="submit" name="submitLoyalty" class="btn btn-primary" value="Apply" class="button">
            </form>


        </div>
        <div class = "mt-3" style="text-align: center;">
            <h3 style="text-align: center; color: white;">
                After Discount: $<?php
                $loyaltyPoint = isset($_POST['loyaltyPoint']) ? $_POST['loyaltyPoint'] : 0;
                $totalCost = $totalCost - $loyaltyPoint;
                $totalLoyaltyPoint = $totalLoyaltyPoint - $loyaltyPoint;
                echo number_format((float)$totalCost, 2, '.', '');
                ?>
            </h3>
        </div>

        <div class = "mt-3" style="text-align: center;">
            <form method="POST">
                <input type="hidden" name="confirm" value="confirm">
                <input class="btn btn-primary" type="submit" name="action" style="width: 20%;" value="Confirm">
            </form>
        </div>
    </main>
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

    #display .movie-image {
        width: 200px; /* Adjust the width as desired */
        height: auto; /* Maintain the aspect ratio */
        display: block; /* Ensure the image is displayed as a block element */
        margin-top: 20px;
    }

    #date {
        margin-top: 20px;
    }

    p
    {
        color: white;
    }

    h4
    {
        color: white;
    }
</style>
<?php include('footer.php') ?>

</html>