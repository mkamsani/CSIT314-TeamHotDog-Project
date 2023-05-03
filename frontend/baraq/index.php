<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en">
<head>
<?php
include 'html-components/head.php';
echo head("HotDogBun Cinema", "Login and registration page of HotDogBun Cinema") . "\n";
?>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
<h1>HotDogBun Cinema</h1>
<?php include 'html-components/navigation-customer.php'; ?>
</header>
<main>

<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
$json = json_encode($_POST);
// echo "<section>\n<h3>DEBUGGING STATEMENTS</h3>\n";
// echo $_SERVER['PHP_SELF'] . "<br />\n" . "<pre>";
// echo "<pre>" . print_r($_POST, true) . "</pre>";
// echo "</pre>" . "<br />" . $json . "</section>";
$ch = curl_init("http://localhost:8000/api/user-account/create-customer");
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
$result = curl_exec($ch);
curl_close($ch);
echo <<<HTML
<section>
<h2>Result</h2>
<p>$result</p>
</section>
HTML;
}
?>

<!-- GET request to display all movies currently showing. -->
<section id="now-showing">
<h2>Now Showing</h2>
<div style="display: grid; grid-template-columns: repeat(4, 1fr); grid-gap: 1rem;">
<?php
$ch = curl_init("http://localhost:8000/api/movie/read/allActiveMoviesDetails");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$result = curl_exec($ch);
curl_close($ch);
$result = json_decode($result, true);
foreach ($result as $movie) {
echo <<<HTML
<img src="{$movie['imageUrl']}" alt="{$movie['title']}">
HTML;
}
?>
</div>
</section>

<?php
// POST request.
// Expected JSON:
// {
//    "username":"username",
//    "password":"password",
//    "privilege":"privilege"
// }
?>
<section id="login">
<h2>Login</h2>
<form action="index.php" method="POST" class="form-login">
<label for="username">Username:</label>
<input type="text" name="username" id="username" required>
<label for="password">Password:</label>
<input type="password" name="password" id="password" required>
<label for="privilege">Select User:</label>
<select name="privilege" id="privilege">
<option value="admin">User Admin</option>
<option value="owner">Cinema Owner</option>
<option value="manager">Cinema Manager</option>
<option value="customer">Customer</option>
</select>
<button type="submit" name="check" value="login">Continue</button>
</form>
</section>


<?php
// POST request.
// Expected JSON:
// {
//    "username":"marcus",
//    "email":"marcus@adm.hotdogbuns.com",
//    "password":"password",
//    "firstName":"Marcus",
//    "lastName":"Hutchins",
//    "address":"099 Study Town, East Brooklyn, NY 29125"
//    "dateOfBirth":"1994,1,1",                            // or, "1994-01-01"
//  }
?>
<section id="registration">
<h2>Registration</h2>
<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="form-registration">
<label for="username">Username:</label>
<input type="text" name="username" id="username" required>
<label for="email">Email:</label>
<input type="email" name="email" id="email" required>
<label for="password">Password:</label>
<input type="password" name="password" id="password" required>
<label for="password-confirm">Confirm Password:</label>
<input type="password" name="password" id="password-confirm" required>

<label for="firstName">First Name:</label>
<input type="text" name="firstName" id="firstName" required>
<label for="lastName">Last Name:</label>
<input type="text" name="lastName" id="lastName" required>
<label for="dateOfBirth">Date of Birth:</label>
<input type="date" name="dateOfBirth" id="dateOfBirth" required>
<label for="address">Address:</label>
<textarea name="address" id="address" cols="30" rows="10" required></textarea>
<button type="submit" name="check" value="register">Submit</button>
</form>
</section>
</main>
</body>
</html>

