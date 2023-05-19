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

// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/' . $username);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
$user = $data[0];
curl_close($ch);

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
                <a class="nav-link" href="">Movies</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="OrderHistory.php">My Bookings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="CustAccountDetails.php">My Account</a>
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
    <h1>My Account</h1>
    <p>Username: <?php echo $username; ?></p>
</div>
<?php
// Retrieve the username from the URL
$username = $_SESSION["username"];

// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/' . $username);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
$user = $data[0];

$year = $user['dateOfBirth'][0];
$month = sprintf('%02d', $user['dateOfBirth'][1]);
$day = sprintf('%02d', $user['dateOfBirth'][2]);
$dateOfBirth = $year . '-' . $month . '-' . $day;

curl_close($ch);

//Read Loyalty Points
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/customer/loyalty-point/read/' . $username);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$loyaltyaccount = json_decode($result, true);
curl_close($ch);

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
        $updatedUser = array(
            'username' => $_POST['username'],
            'firstName' => $_POST['firstName'],
            'lastName' => $_POST['lastName'],
            'email' => $_POST['email'],
            'address' => $_POST['address'],
            'dateOfBirth' => $_POST['dateOfBirth']
        );

        $jsonData = json_encode($updatedUser);
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/customer/user-account/update/' . $_SESSION["username"]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonData);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        $result = curl_exec($ch);
        curl_close($ch);

        if ($result == "Success")
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Account has been updated. Head over to the <a href="" class="alert-link">Book a Movie</a>
                        to book a movie, or go back to <a href="Customer.php" class="alert-link">main page</a>.
                        </div>
                    </div>';
            $_SESSION["username"] = $_POST["username"];
        }

        else {
            // Error message
            echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $result . '
                </div>
            </div>';
        }

}

?>

<div class="container mt-6 row g-3 mx-auto">
    <h1 class="text-center text-white">My Information</h1>
        <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
            <div class="container mt-4">
                <input type="hidden" name="targetUsername" value="<?php echo $user['username']; ?>">
                <div class="mt-4 mx-auto" style="width: 40%;">
                    <div class="col-4 mx-auto">
                        <input class="btn btn-danger" onclick="location.href='Customer.php'" value = "Go back"></input>
                    </div>

                    <div class="mt-2 row g-1 mx-auto">
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="text" class="form-control text-danger" id="loyaltypoints" name="loyaltypoints"
                                       value="<?php echo $loyaltyaccount['pointsBalance']; ?>" readonly style="font-size: 1.5rem;">
                                <label for="loyaltypoints">Loyalty Points:</label>
                            </div>
                        </div>
                    </div>

                    <div class="mt-3 row g-1 mx-auto">
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="username" name="username"
                                       value="<?php echo $user['username']; ?>">
                                <label for="username">Username: </label>
                            </div>
                        </div>
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="firstName" name="firstName"
                                       value="<?php echo $user['firstName']; ?>">
                                <label for="subName">First Name: </label>
                            </div>
                        </div>
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="lastName" name="lastName"
                                       value="<?php echo $user['lastName']; ?>">
                                <label for="lastName">Last Name: </label>
                            </div>
                        </div>
                    </div>
                    <div class="mt-2 row g-1 mx-auto">
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="email" name="email"
                                       value="<?php echo $user['email']; ?>">
                                <label for="email">Email: </label>
                            </div>
                        </div>
                    </div>
                    <div class="mt-2 row g-1 mx-auto">
                        <div class="col-md">
                            <div class="form-floating">
                                <input type="date" class="form-control" name="dateOfBirth" id="dateOfBirth" value="<?php echo date('Y-m-d', strtotime($user['dateOfBirth'])); ?>">
                                <label for="dateOfBirth">Date of Birth:</label>
                            </div>
                        </div>
                    </div>

                    <div class="mt-2 row g-1 mx-auto">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="address" name="address" value="<?php echo $user['address']; ?>">
                            <label for="address">Address: </label>
                        </div>
                    </div>

                    <div class="mt-2 row g-2 col-4 mx-auto">
                        <input class="btn btn-outline-primary" type="submit" name="action" value="Update">
                    </div>
                </div>
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