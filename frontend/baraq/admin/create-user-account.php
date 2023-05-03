<?php session_start(); ?>
<!DOCTYPE html>
<html lang="en">
<head>
<?php
include '../html-components/head.php';
echo head("HotDogBun Cinema", "Login and registration page of HotDogBun Cinema") . "\n";
?>
<link rel="stylesheet" href="/css/index.css">
<style>
    section {
        background: #fffd;
        padding: 2rem;
        border-radius: 0.5rem;
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
    }

    section > * {
        max-width: 97.5%;
        margin: auto;
    }

    section > p {
        margin-top: 1rem;
    }

    section > form {
        display: flex;
        flex-direction: column;
    }

    section form label {
        margin-top: 1rem;
    }

    section form button {
        margin-top: 2rem;
        cursor: pointer;
        text-align: left;
        /* Make it slightly bigger: */
        padding: 0.5rem 1rem;
        font-size: 1.1rem;
    }

    section form label + input {
        margin-top: 0.5rem;
    }

    /* Any section which is not the first */
    section + section {
        margin-top: 3rem;
    }

    /* Uses image carousel. */
    #now-showing > div {
        margin-top: 1rem;
        display: grid;
        grid-template-columns: 1fr 1fr 1fr 1fr;
        grid-gap: 1rem;
    }

    /* Select the last section element */
    section:last-child {
        margin-bottom: 1rem;
    }
</style>
</head>
<body>
<header>
<h1>HotDogBun Cinema</h1>
<?php include '../html-components/navigation-admin.php';
// the variable is the name of this current url, use self method.
echo makeNavigation($_SERVER['PHP_SELF'])
?>
</header>
<main>

<div style="background: white; color:red;">
<?php

echo $_SERVER['PHP_SELF'];
// The form below will perform a post request on this page,
// we output the result here.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {

// Debugging:
 echo "<pre>"; print_r($_POST); echo "</pre>";

// Convert to a JSON that is readable by Spring's ObjectMapper.
$json = json_encode($_POST);
echo $json;
/*
e.g.
{
  "username":"a",
  "email":"a@a.com",
  "password":"a",
  "firstName":"fn1","lastName":"ln1","dateOfBirth":"2023-05-03","address":"address1","title":"customer","check":"register"}
*/
echo "<br />";
echo "<br />";
echo "<br />";

// Step 4: Send the JSON to the backend.
// Send to http://localhost:8080/api/user-profile/create
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/api/user-account/create");
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
</div>

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
//    "title":"junior admin"
//  }
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
curl_setopt($ch, CURLOPT_URL, "http://localhost:8080/api/user-profile/read/titles");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
$titles = curl_exec($ch); // e.g. "[junior admin,senior admin,junior manager,senior manager]"
$titles = explode(",", substr($titles, 1, -1));
curl_close($ch);


for ($i = 0; $i < count($titles); $i++) {
$titleCapitalized = trim(ucwords($titles[$i]));
$title = trim($titles[$i]);
echo <<<EOT
<option value="$title">$titleCapitalized</option>\n
EOT;

}


?>
</select>
<button type="submit" name="check" value="register">Submit</button>
</form>
</section>
</main>
</body>
</html>

