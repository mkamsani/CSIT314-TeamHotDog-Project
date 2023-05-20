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

if (isset($_POST["dateScreening"], $_POST["movie"], $_POST["ticketType"])) {
    $dateScreening = $_POST["dateScreening"];
    $dateScreening = explode("/", $dateScreening);
    $_SESSION["date"] = $dateScreening[0]; // "2023-05-21"
    $_SESSION["time"] = $dateScreening[1]; // "evening"
    $_SESSION["movie"] = $_POST['movie'];  // "Black Adam"
    $_SESSION["ticketType"] = $_POST['ticketType'];  // Tickettype
}

if (isset($_SESSION["date"], $_SESSION["time"])) {

    $date = $_SESSION["date"];
    $time = $_SESSION["time"];
    $ticketType = $_SESSION["ticketType"];
    $title = $_SESSION['movie'];
    // Retrieve Movie Details:
    $url = "http://localhost:8000/api/customer/movie/read/" . $title;
    $url = str_replace(' ', '%20', $url);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $moviesDetails = curl_exec($ch);
    curl_close($ch);
    $moviesDetails = json_decode($moviesDetails, true); // {"title":"Black Adam","genre":"action","description":"Starring Dwayne Johnson, Black Adam is a superhero film that chronicles the transformation of a once anti-heroic character into a full-fledged hero, based on the DC Comics character of the same name.","releaseDate":"2022-07-29","imageUrl":"https://www.themoviedb.org/t/p/original/3zXceNTtyj5FLjwQXuPvLYK5YYL.jpg","landscapeImageUrl":"https://www.themoviedb.org/t/p/original/jVsbzy5gj3McD8V6dDr7EMrLSqT.jpg","isActive":"true","contentRating":"pg13"}

    // Retrieve Screenings again, to get the cinema rooms:
    $url = "http://localhost:8000/api/customer/screening/read/" . $title;
    $url = str_replace(' ', '%20', $url);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $screenings = curl_exec($ch);
    curl_close($ch);
    $screenings = json_decode($screenings, true); // {"movie":"Black Adam","showTime":"evening","showDate":"2023-05-21","cinemaRoom":57}

    $cinemaRooms = array();
    foreach ($screenings as $screening) {
        if ($screening["showDate"] == $date && $screening["showTime"] == $time) {
            $cinemaRooms[] = intval($screening["cinemaRoom"]);
        }
    }
    // Pick a random cinema room from the array.
    // Get the size of the array
    $size = count($cinemaRooms);
    // Generate a random number between 0 and $size - 1
    $randomIndex = rand(0, $size - 1);
    $randomCinemaRoom = $cinemaRooms[$randomIndex];

    // Retrieve Seats available:
    $url = "http://localhost:8000/api/customer/screening/seats/read/" . $date . "/" . $time . "/" . $randomCinemaRoom;
    $url = str_replace(' ', '%20', $url);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $seats = curl_exec($ch);
    curl_close($ch);
    $seats = json_decode($seats, true); // {"movie":"Black Adam","showTime":"evening","showDate":"2023-05-21","cinemaRoom":57}
}
else {
    header("location: Booking.php"); // Redirect because customer haven't choose a movie.
}

// $columnTitle = array_column($moviesDetails, 'title');
// $columnImg_Src = array_column($moviesDetails, 'imageUrl');
// $combineArray = array_combine($columnTitle, $columnImg_Src);

//foreach ($sessionVariables as $variable)
//{
//    if (isset($_POST[$variable])) {
//        $_SESSION[$variable] = $_POST[$variable];
//    } elseif (!isset($_SESSION[$variable])) {
//        $_SESSION[$variable] = null;
//    }
//}

$ticketTypesCh = curl_init();
curl_setopt($ticketTypesCh, CURLOPT_URL, 'http://localhost:8000/api/manager/ticketType/read/active');
curl_setopt($ticketTypesCh, CURLOPT_RETURNTRANSFER, 1);
$ticketTypes = curl_exec($ticketTypesCh);
curl_close($ticketTypesCh);
$ticketTypes = json_decode($ticketTypes, true);
$moviePrice = '';
foreach ($ticketTypes as $eachTicketType)
{
    if ($eachTicketType['typename'] == $_SESSION['ticketType']) {
        $_SESSION['ticketPrice'] = $eachTicketType['typeprice'];
        $moviePrice = $eachTicketType['typeprice'];
    }
}
$totalCost = $moviePrice;

//Retrieve Loyalty Points
$loyaltyCh = curl_init();
curl_setopt($loyaltyCh, CURLOPT_URL, "http://localhost:8000/api/customer/loyalty-point/read/" . $username);
curl_setopt($loyaltyCh, CURLOPT_RETURNTRANSFER, 1);
$loyalty = curl_exec($loyaltyCh);
curl_close($loyaltyCh);
$loyalty = json_decode($loyalty, true);
$totalLoyaltyPoints =  $loyalty['pointsBalance']; // Integrate this
$hasRedeemedPoint = false; // becomes true if user has enough points and clicked redeem button.
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
    <p><?php echo $username ?></p>
</div>

<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    if (isset($_POST['action']) &&  $_POST['action'] == 'Confirm')
    {
        if (isset($_POST['seat']))
        {
            $selectedSeat = $_POST['seat'];
        }
        $selectedSeatRow  = substr($selectedSeat, 0, 1);
        $selectedSeatColumn = substr($selectedSeat, 1);
        $ticketArray = array(
            "userName"           => $username,
            "ticketTypeName"     => $_SESSION['ticketType'],
            "showTime"           => $time,
            "showDate"           => $date,
            "cinemaRoomId"       => $randomCinemaRoom,
            "row"                => $selectedSeatRow,
            "column"             => $selectedSeatColumn,
            "isLoyaltyPointUsed" => $hasRedeemedPoint,
        );
        $jsonData = json_encode($ticketArray);
        // Confirm button curl
        $ch = curl_init("http://localhost:8000/api/customer/ticket/create");
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonData);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        $result = curl_exec($ch);
        curl_close($ch);

        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Booking Succesful!</strong>. Head over to the <a href="OrderHistory.php" class="alert-link">My Bookings</a>
                        to view your Booking.
                        </div>
                    </div>';
    }

    if (isset($_POST['action']) &&  $_POST['action'] == 'Redeem')
    {
        $hasRedeemedPoint = true;
        // Redemption is fixed at 10 points.
        $url = 'http://localhost:8000/api/customer/loyalty-point/redeem/' . $username . '/10';
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        $result = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($httpCode === 200) {
            $_SESSION["ticketType"] = 'redemption';
            $ticketType = 'redemption';

            echo '
        <div class="container mt-5">
            <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
            <strong></strong> ' . $result . '
            </div>
        </div>';
            $totalCost = 0;

            //Retrieve Loyalty Points
            $loyaltyCh = curl_init();
            curl_setopt($loyaltyCh, CURLOPT_URL, "http://localhost:8000/api/customer/loyalty-point/read/" . $username);
            curl_setopt($loyaltyCh, CURLOPT_RETURNTRANSFER, 1);
            $loyalty = curl_exec($loyaltyCh);
            curl_close($loyaltyCh);
            $loyalty = json_decode($loyalty, true);
            $totalLoyaltyPoints =  $loyalty['pointsBalance']; // Integrate this
        }
    }
}

?>

<div class="container mt-4" style="width: 50%">
    <form method="POST" action ="<?php echo $_SERVER['PHP_SELF'];?>">
        <main>
            <h1 style="text-align: center; color:white;">Booking Summary</h1>
            <table align="center" border="0" cellpadding="5" cellspacing="0">
                <tr>
                    <td rowspan="3"><img src="<?php echo $moviesDetails[0]['imageUrl'];?>" alt="Error" width="200" height="300"></td>
                    <td style="text-align: center" class = "text-white"><?php echo $ticketType ?> | <?php echo $_SESSION["movie"]; ?></td>
                </tr>
                <tr>
                    <td style="text-align: center" class = "text-white"><?php echo $date ?></td>
                </tr>
                <tr>
                    <td style="text-align: center" class = "text-white"><?php echo $time ?></td>
                </tr>
            </table>

            <div class="mt-3" style = "text-align: center;">
                <h4 for="seat" class="form-label text-white">Choose your Seat: </h4>
                <select name="seat" class="form-select" style="width: 30%; display: inline; text-align:center; font-size: 18px;" id="seat" required>
                    <?php
                    // drop down for seats.
                    foreach ($seats as $seat) // row, column, concat
                    {
                        echo sprintf("<option value=\"%s\">%s</option>", $seat['concat'], $seat['concat']);
                    }
                    ?>
                </select>
            </div>
            <br>
            <br>

            <h3 style="text-align: center; color: white">Total cost: $<?php echo number_format((float)$totalCost, 2, '.', ''); ?></h3>

            <div class = "mt-5" style="text-align: center;">
                <h4>You currently have <span id="loyalty-points"><u><?php echo $loyalty['pointsBalance'] ?></u></span> loyalty points.</h4>
                <h4>Free Ticket = 10 points.</h4>
            </div>

            <div class = "mt-5" style="text-align: center;">
                <h4 style = "color: yellow">You <?php
                    if ($loyalty['pointsBalance'] >= 10) {
                        if ($hasRedeemedPoint == true) echo "have already redeemed";
                        else echo "are able to redeem";
                    } elseif ($hasRedeemedPoint == false) {
                        echo "<span style = \"color: red;\">are NOT able to redeem</span>";
                    } else
                    {
                        echo "have already redeemed";
                    }
                    ?> a free ticket</h4>


                <?php
                if ($loyalty['pointsBalance'] >= 10 && $hasRedeemedPoint == false) {
                    echo "<input type=\"submit\" name=\"action\" class=\"btn btn-primary\" value=\"Redeem\" class=\"button\">";
                }
                ?>

            </div>

            <div class = "mt-3" style="text-align: center;">
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