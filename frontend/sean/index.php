<!DOCTYPE html>
<html lang="en">


<?php
// Initialize the session
session_start();

include('header.php');
include('idx_nav.php');
?>


<body>

<?php

if(isset($_POST['submit']) && !empty($_POST['userId']) && !empty($_POST['password']))
{
    $userId = $_POST['userId'];
    $password = $_POST['password'];
    $ch = curl_init();
    $loginarr = array('username' => $userId, 'password' => $password);

    $json_data = json_encode($loginarr);

    curl_setopt($ch, CURLOPT_URL, "http://localhost:8000/api/user-account/login");
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json','Content-Length: ' . strlen($json_data)]);
    curl_setopt($ch,CURLOPT_POSTFIELDS, $json_data);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    $http_status_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    // Check the user privilege and redirect
    if ($http_status_code == 200 && $result == 'Success')
    {

    }

    else
    {
        echo $result;
    }
}

?>

<input type="text" name = "result" value = $result hidden>

<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel" style="margin-left: 20%; width: 60%">
    <div class="carousel-indicators">
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active"
                aria-current="true" aria-label="Slide 1"></button>
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"
                aria-label="Slide 2"></button>
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"
                aria-label="Slide 3"></button>
    </div>
    <div class="carousel-inner">
        <div class="carousel-item active" data-bs-interval="5000">
            <img src="Pics\carousel.png" class="d-block w-100" alt="...">
            <div class="carousel-caption d-none d-md-block">
                <h1>HotDog Cinemas</h1>
            </div>
        </div>
        <div class="carousel-item" data-bs-interval="5000">
            <img src="Pics\carousel-2.jpg" class="d-block w-100" alt="...">
        </div>
        <div class="carousel-item" data-bs-interval="5000">
            <img src="Pics\carousel-3.jpg" class="d-block w-100" alt="...">
        </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators"
            data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators"
            data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>

<div class="container mt-4">
    <form class='form-wrapper mx-auto' action="<?php echo $_SERVER["PHP_SELF"]; ?>"
          method='POST' name ="form1" style="width: 30%">
        <div class="input-group mt-3">
            <span class='input-group-text'>User ID : </span>
            <input class='form-control' type='text' name='userId' required>
        </div>
        <div class="input-group mt-3">
            <span class='input-group-text'>Password : </span>
            <input class='form-control' type='text' name='password' required>
        </div>
        <div class="mt-3 d-grid col-6 gap-2 mx-auto">
            <input class="btn btn-danger" type="submit" name="submit" value="Log In">
            <label>Don't have an account?</label>
            <a class="btn btn-danger" href="register.php">Register</a>
        </div>
    </form>
</div>
</body>