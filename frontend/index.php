<!DOCTYPE html>
<html lang="en">


<?php
// Initialize the session
session_start();

include('header.php');
include('idx_nav.php');
?>

<head>
<body>
<?php
$result = '';
if(isset($_POST['submit']) && !empty($_POST['username']) && !empty($_POST['password']))
{
    $userId = $_POST['username'];
    $password = $_POST['password'];
    $ch = curl_init();
    $loginarr = array('username' => $userId, 'password' => $password);

    $json_data = json_encode($loginarr);

    curl_setopt($ch, CURLOPT_URL, "http://localhost:8000/api/login");
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json','Content-Length: ' . strlen($json_data)]);
    curl_setopt($ch,CURLOPT_POSTFIELDS, $json_data);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    $http_status_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    // Check the user privilege and redirect
    if ($http_status_code == 200)
    {
        // Set session variable.
        $_SESSION["privilege"] = $result;
        $_SESSION["username"] = $_POST['username']; // Store the username in the session
    }
}

if(isset($_SESSION['privilege']))
{
    switch ($_SESSION["privilege"] )
    {
        // Redirect to the corresponding page based on the user privilege.
        case 'admin':    header("location: UserAdmin.php");     break;
        case 'owner':    header("location: CinemaOwner.php");   break;
        case 'manager':  header("location: CinemaManager.php"); break;
        case 'customer': header("location: Customer.php");      break;
        default:
            echo "<script>document.getElementById('result').innerHTML = '" . $_SESSION["privilege"] . "';</script>";
            break;
    }
}
?>

<input type="text" name = "result" value = $result hidden>

<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel" style="margin-left: 20%; width: 60%">
    <?php
    $ch = curl_init("http://localhost:8000/api/customer/movie/read/active");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $carousel = curl_exec($ch);
    $http_status_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    $carousel = json_decode($carousel, true); // {"title":"Spider-Man","genre":"action","description":"Spider-Man follows the story of Peter Parker (Tobey Maguire), a high school student who gains spider-like abilities and transforms into the superhero Spider-Man. He battles the Green Goblin (Willem Dafoe) to save New York City.","releaseDate":"2002-05-03","imageUrl":"https://www.themoviedb.org/t/p/original/gh4cZbhZxyTbgxQPxD0dOudNPTn.jpg","landscapeImageUrl":"https://www.themoviedb.org/t/p/original/gkINAPOuwUFo2Qphs3OUUbjUKUZ.jpg","isActive":"true","contentRating":"pg13"}
    ?>
    <div class="carousel-indicators">
        <?php
        for ($i = 0; $i < count($carousel); $i++) {
            if ($i == 0) {
                // Append 'class="active" aria-current="true"' to the first carousel indicator
                echo '<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>';
            } else {
                echo '<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="' . $i . "\" aria-label=\"Slide $i\"></button>";
            }
        }
        ?>
    </div>
    <div class="carousel-inner">
        <?php
        for ($i = 0; $i < count($carousel); $i++) {
            $title = $carousel[$i]['title'];
            $landscapeImageUrl = $carousel[$i]['landscapeImageUrl'];
            if ($i == 0)
                echo "<div class='carousel-item active' data-bs-interval='3000'>";
            else
                echo "<div class='carousel-item' data-bs-interval='3000'>";
            echo "<img src='$landscapeImageUrl' class='d-block w-100' alt='$title'>";
            echo "<div class='carousel-caption d-none d-md-block'>" . $title . "</div>" . "</div>";
        }
        ?>
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
            <input class='form-control' type='text' name='username' required>
        </div>
        <div class="input-group mt-3">
            <span class='input-group-text'>Password : </span>
            <input class='form-control' type='password' name='password' required>
        </div>
        <span class="error" style="color:red" id = "result"><?php echo $result; ?></span>
        <div class="mt-3 d-grid col-6 gap-2 mx-auto">
            <input class="btn btn-danger" type="submit" name="submit" value="Log In">
            <label class = "text-white">Don't have an account?</label>
            <a class="btn btn-danger" href="register.php">Register</a>
        </div>
    </form>
</div>
</body>

</head>
</html>