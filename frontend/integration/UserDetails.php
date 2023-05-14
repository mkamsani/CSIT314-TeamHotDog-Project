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
                <a class="nav-link" href="UserAdmin.php">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CreateUser.php">Create User Account</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="UserAccounts.php">User Accounts</a>
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
    <h1>User Account Details</h1>
</div>
<?php
// Retrieve the username from the URL
$username = $_GET['username'];

// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/' . $username);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
$user = $data[0];
curl_close($ch);

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $_SESSION['username'] = $_POST['username'];

    if ($_POST['action'] == 'Update')
    {
        $updatedUser = array(
            'username' => $_POST['username'],
            'firstName' => $_POST['firstName'],
            'lastName' => $_POST['lastName'],
            'email' => $_POST['email'],
            'address' => $_POST['address'],
            'title' => $_POST['title'],
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/user-account/update/' . $_POST['targetUsername']);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($updatedUser));
        $result = curl_exec($ch);
        var_dump($result);
        curl_close($ch);
    }

    // Suspend user account when the form is submitted
    if ($_POST['action'] == 'Suspend')
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/user-account/suspend/' . $_POST['targetUsername']);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POST, true);
        $result = curl_exec($ch);
        echo $result;
    }

}

?>


<form action="<?php echo $_SERVER['PHP_SELF'] . '?' . 'username=' . $_SESSION["username"]; ?>" method="POST">
<div class="container mt-4">
    <input type="hidden" name="targetUsername" value="<?php echo $user['username']; ?>">
    <div class="mt-4 mx-auto" style="width: 40%;">
        <div class="row">
            <div class="col-4 mx-auto">
                <input class="btn btn-danger" onclick="location.href='UserAccounts.php'" value = "Go back"></input>
            </div>
            <div class="col-auto">
                <input class="btn btn-outline-danger" value="Suspend Account" type="submit" name ="action">
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
<!--            <div class="mt-2 row g-1 mx-auto">-->
<!--                <div class="col-md">-->
<!--                    <div class="form-floating">-->
<!--                        <input type="text" class="form-control" id="dateOfBirth" name="dateOfBirth"-->
<!--                            value="--><?php //echo $user['dateOfBirth']; ?><!--">-->
<!--                        <label for="dateOfBirth">DOB: </label>-->
<!--                    </div>-->
<!--                </div>-->
<!--            </div>-->
            <div class="mt-2 row g-1 mx-auto">
                <div class="form-floating">
                    <input type="text" class="form-control" id="title" name="title" value="<?php echo $user['title']; ?>">
                    <label for="title">Title: </label>
                </div>
            </div>
            <div class="mt-2 row g-1 mx-auto">
                <div class="form-floating">
                    <input type="text" class="form-control" id="address" name="address" value="<?php echo $user['address']; ?>">
                    <label for="address">Address: </label>
                </div>
            </div>

            <div class="row g-2 col-4 mx-auto">
                    <input class="btn btn-primary" type="submit" name="action" value="Update">
            </div>
        </div>
</div>
</form>
</body>
<?php include('footer.php') ?>
</html>