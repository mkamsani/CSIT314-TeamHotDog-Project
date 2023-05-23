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
    <h1>Create a User Profile</h1>
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
    $ch = curl_init("http://localhost:8000/api/admin/user-profile/create");
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
                        <strong>Success!</strong> User Profile has been created. Head over to the <a href="UserProfiles.php" class="alert-link">User Profiles</a>
                        to view profiles, or go <a href="UserAdmin.php" class="alert-link">main page</a>.
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
                <label for="title" class="form-label">Title:</label>
                <input type="text" class="form-control" name="title" id="title" placeholder="Enter user profile title" required>
            </div>
            <div class="mt-3">
                <label for="title">Privilege:</label>
                <div class="form-floating">
                    <select class = "form-select" name="privilege" id="privilege">
                        <option value="customer">Customer</option>
                        <option value="manager">Manager</option>
                        <option value="owner">Owner</option>
                        <option value="admin">Admin</option>
                    </select>
                    <label for="privilege">Privilege:</label>
                </div>
            </div>


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