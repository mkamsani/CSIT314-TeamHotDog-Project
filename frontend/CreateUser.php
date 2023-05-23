<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand">
            <h1 style="width:25px; margin-bottom: 5px"> HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="UserAdmin.php">Home</a>
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
    <h1>Create a User Account</h1>
</div>

<?php
// Step 2: The form below performs a post request on this page, we output the result here.
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{

// Step 3: Convert to a JSON that is readable by Java's ObjectMapper.
    $json = json_encode($_POST);

// echo "<section>\n<h3>DEBUGGING STATEMENTS</h3>\n";
// echo $_SERVER['PHP_SELF'] . "<br />\n" . "<pre>";
// echo "<pre>" . print_r($_POST, true) . "</pre>";
// echo "</pre>" . "<br />" . $json . "</section>";

// Step 4: Send the JSON to the backend.
    $ch = curl_init("http://localhost:8000/api/admin/user-account/create");
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
    $result = curl_exec($ch);
    curl_close($ch);

// Step 5: Output the result, surrounded by a <section> tag. Using HereDoc syntax.
    if ($result == "Success")
    {
        echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Account has been created. Head over to the <a href="UserAccounts.php" class="alert-link">User Accounts</a>
                        to view accounts, or go <a href="UserAdmin.php" class="alert-link">main page</a>.
                        </div>
                    </div>';
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


$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "http://localhost:8000/api/admin/user-profile/read/active");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$userprofile = curl_exec($ch); // [{"privilege":"customer","title":"customer"},...,{"privilege":"admin","title":"chief information officer"}]
curl_close($ch);
$user_profiles = json_decode($userprofile, true);

?>

<div class="container mt-4 text-white" style="margin-left: 20%; width: 20%">
    <section id="registration">
        <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="form-registration">
            <div class="mt-3">
                <label for="username" class="form-label">Username:</label>
                <input type="text" class="form-control" name="username" id="username" placeholder="Enter your username for login" required>
            </div>

            <div class="mt-3">
                <label for="email" class="form-label">Email:</label>
                <input type="email" class="form-control" name="email" id="email" placeholder="Enter your email eg. seanissofunn@gmail.com" required>
            </div>

            <div class="mt-3">
                <label for="password" class="form-label">Password:</label>
                <input type="password" class="form-control" name="password" id="password" placeholder="Enter your password minimum 6 characters" required>
            </div>

            <div class="mt-3">
                <label for="password-confirm" class="form-label">Confirm Password:</label>
                <input type="password" class="form-control" name="password" id="password-confirm" placeholder="Confirm password" required>
            </div>

            <div class="mt-3">
                <label for="firstName" class="form-label">First Name:</label>
                <input type="text" class="form-control" name="firstName" id="firstName" placeholder="Enter your first name" required>
            </div>

            <div class="mt-3">
                <label for="lastName" class="form-label">Last Name:</label>
                <input type="text" class="form-control" name="lastName" id="lastName" placeholder="Enter your last name" required>
            </div>

            <div class="mt-3">
                <label for="dateOfBirth" class="form-label">Date of Birth:</label>
                <input type="date" class="form-control" name="dateOfBirth" id="dateOfBirth" placeholder="Select date of birth" required>
            </div>

            <div class="mt-3">
                <label for="address" class="form-label">Address:</label>
                <textarea name="address" class="form-control" id="address" placeholder="Enter your address" cols="30" rows="10" required></textarea>
            </div>

            <label for="title">Title:</label>
            <select class = "form-select" name="title" id="title">
                <?php
                foreach ($user_profiles as $profile) {
                    $titleCapitalized = ucwords($profile['title']);
                    $privilegeCapitalized = ucwords($profile['privilege']);
                    echo '<option value="' . $profile['title'] . '">' . $privilegeCapitalized . ': ' . $titleCapitalized . '</option>';
                }
                ?>
            </select>

            <div class="mt-3">
                <input class="btn btn-danger" type="submit" name="submit" value="Create">
            </div>
        </form>
    </section>
</div>

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

</body>
<?php include('footer.php') ?>

</html>