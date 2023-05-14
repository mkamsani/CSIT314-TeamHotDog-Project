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
            <img src="Pics/hotdog_cinemas.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Hotdog Cinemas
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="UserAdmin.php">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CreateUser.php">Create User Account</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="UserAccounts.php">User Accounts</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="UserProfiles.php">User Profiles</a>
            </li>
            &emsp;
            <form method="get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Welcome User Administrator!</h1>
    <p>Username: <?php echo $username; ?></p>
</div>

<div class="container mt-6 row g-3 mx-auto" style="width: 20%">
    <h1 class="text-center">User Admin Information</h1>
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
    <a class="btn btn-danger" href="UserAccounts.php" role="button" style="margin-right: 2.7%">View User Accounts</a>
    <a class="btn btn-danger" href="UserProfiles.php" role="button">View User Profiles</a>
</div>

</body>
<?php include('footer.php') ?>

</html>