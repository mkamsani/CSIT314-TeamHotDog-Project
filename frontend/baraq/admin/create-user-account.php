<?php /* Ctrl+F and search for "Step 1:", ..., etc. to see the steps. */ ?>
<!DOCTYPE html>
<html lang="en">
<head>
<?php
include '../html-components/head.php';
echo head("HotDogBun Cinema", "Login and registration page of HotDogBun Cinema") . "\n";
?>
<link rel="stylesheet" href="../css/style.css">
</head>
<body>
<header>
<h1>HotDogBun Cinema</h1>
<section>There is supposed to be a &lt;nav&gt; here, but it's in progress.</section>
</header>
<main>

<?php
// Step 2: The form below performs a post request on this page, we output the result here.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

// Step 3: Convert to a JSON that is readable by Java's ObjectMapper.
$json = json_encode($_POST);

// echo "<section>\n<h3>DEBUGGING STATEMENTS</h3>\n";
// echo $_SERVER['PHP_SELF'] . "<br />\n" . "<pre>";
// echo "<pre>" . print_r($_POST, true) . "</pre>";
// echo "</pre>" . "<br />" . $json . "</section>";

// Step 4: Send the JSON to the backend.
$ch = curl_init("http://localhost:8000/api/user-account/create");
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
$result = curl_exec($ch);
curl_close($ch);

// Step 5: Output the result, surrounded by a <section> tag. Using HereDoc syntax.
echo <<<HTML
<section>
<h2>Result</h2>
<p>$result</p>
</section>
HTML;
}
?>

<?php
// Step 1: POST request.
// Expected JSON:
// {"username":"marcus","email":"marcus@adm.hotdogbuns.com","password":"password","firstName":"Marcus","lastName":"Hutchins","address":"099 Study Town, East Brooklyn, NY 29125","title":"junioradmin","dateOfBirth":"1994,1,1"}
// https://jsonprettier.com/ -- Paste the JSON above to see the prettified version.
// (dateOfBirth can also be "1994-01-01")
?>
<section id="registration">
<h2>Create User Account</h2>
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

<label for="title">Title:</label>
<select name="title" id="title">
<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "http://localhost:8000/api/user-profile/read/active-user-profiles");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$user_profiles = curl_exec($ch); // [{"privilege":"customer","title":"customer"},...,{"privilege":"admin","title":"chief information officer"}]
curl_close($ch);
$user_profiles = json_decode($user_profiles, true);
foreach ($user_profiles as $user_profile) {
$titleCapitalized = ucwords($user_profile['title']);
$privilegeCapitalized = ucwords($user_profile['privilege']);
echo <<<EOF
<option value="{$user_profile['title']}">$privilegeCapitalized: $titleCapitalized</option>\n
EOF;
}
?>
</select>

<button type="submit" name="check" value="register">Submit</button>
</form>
</section>
</main>
</body>
</html>