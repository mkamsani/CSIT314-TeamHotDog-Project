<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');
include('idx_nav.php');
?>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Registration</h1>
</div>

<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST')
    {
        $json = json_encode($_POST);
        // echo "<section>\n<h3>DEBUGGING STATEMENTS</h3>\n";
        // echo $_SERVER['PHP_SELF'] . "<br />\n" . "<pre>";
        // echo "<pre>" . print_r($_POST, true) . "</pre>";
        // echo "</pre>" . "<br />" . $json . "</section>";
        $ch = curl_init("http://localhost:8000/api/customer/user-account/create");
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        $result = curl_exec($ch);
        curl_close($ch);

        if (stripos($result, "successful") == true)
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong> ' . $result . '</strong>. Head over to the <a href="index.php" class="alert-link">Homepage</a>
                        to Log in.
                        </div>
                    </div>';
        }

        else
        {
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
<div class="container mt-4" style="margin-left: 20%; width: 20%">
    <div class="mt-3">
        <input class="btn btn-danger" onclick = "location.href='index.php'" name="return" value="Go back">
    </div>
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

            <div class="mt-3">
                <input class="btn btn-danger" type="submit" name="submit" value="Register">
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
</style>

</body>
<?php include('footer.php') ?>
</html>