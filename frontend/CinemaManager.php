<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');

/// Sean's session variables
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
//?>

<?php
//$ch = curl_init();
//curl_setopt($ch, CURLOPT_URL, "http://localhost:8005/api/api/movie/read/allMovieTitles");
//curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
//$titles = curl_exec($ch);
//$titles = explode(",", substr($titles, 1, -1));
//echo $titles;
//?>

<!--<select name="title" id="title">-->
<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/api/user-account/createUserAccount");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$titles = curl_exec($ch); // e.g. "[junior admin,senior admin,junior manager,senior manager]"
$titles = explode(",", substr($titles, 1, -1));
curl_close($ch);
//for ($i = 0; $i < count($titles); $i++) {
//$titleCapitalized = trim(ucwords($titles[$i]));
//$title = trim($titles[$i]);
//echo <<<EOT
//<option value="$title">$titleCapitalized</option>\n
// EOT;
// }

?>

<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 style="width:25px; margin-bottom: 5px"> HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="CinemaManager.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="movies.php">Movies</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="movie_screening.php">Screening</a>
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
    <h1>Welcome Manager!</h1>
    <p>Username: <?php echo $username; ?></p>
</div>

<div class="container mt-6 row g-3 mx-auto" style="width: 20%">
    <h1 class="text-center text-white">Cinema Manager Information</h1>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="name" value = "<?php echo $user['firstName']; ?>" readonly>
            <label for="name">First Name: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="surname" value = "<?php echo $user['lastName']; ?>" readonly>
            <label for="surname">Last name: </label>
        </div>
    </div>
    <div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" value = "<?php echo $user['email']; ?>" readonly>
            <label for="email">E-Mail: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="title" value = "<?php echo $user['title']; ?>" readonly>
            <label for="phone">Title: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="privilege" value = "<?php echo $privilege ?>" readonly>
            <label for="phone">Privilege: </label>
        </div>
    </div>
</div>

<div class="text-center mt-3">
    <a class="btn btn-danger" href="movies.php" role="button" style="margin-right: 2.7%">View Movies</a>
    <a class="btn btn-danger" href="movie_screening.php" role="button">View Cinema Screenings</a>
    <a class="btn btn-danger" href="ticket_types.php" role="button">View Ticket Types</a>
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