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
<?php include '../html-components/navigation-admin.php'; ?>
</header>
<main>

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
<form action="create-user-account.php" method="POST" class="form-registration">
<label for="username">Username:</label>
<input type="text" name="username" id="username" required>
<label for="email">Email:</label>
<input type="email" name="email" id="email" required>
<label for="password">Password:</label>
<input type="password" name="password" id="password" required>
<label for="password-confirm">Confirm Password:</label>
<input type="password" name="password" id="password-confirm" required>

<label for="firstname">First Name:</label>
<input type="text" name="firstname" id="firstname" required>
<label for="lastname">Last Name:</label>
<input type="text" name="lastname" id="lastname" required>
<label for="dob">Date of Birth:</label>
<input type="date" name="dob" id="dob" required>
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
$titleCapitalized = ucwords($titles[$i]);
echo "<option value=\"$titles[$i]\">$titleCapitalized</option>\n";
}
?>
</select>
<button type="submit" name="check" value="register">Submit</button>
</form>
</section>
</main>
</body>
</html>

