<!DOCTYPE html>
<html lang="en">
<head>
<?php
include 'html-components/head.php';
echo head("HotDogBun Cinema", "Login and registration page of HotDogBun Cinema") . "\n";
?>
<link rel="stylesheet" href="/css/index.css">
<style>
    @import "css/index.css";

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
    #now-playing > div {
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
<?php include 'html-components/navigation-customer.php'; ?>
</header>
<main>

<section id="now-playing">
<h2>Now Playing</h2>
<div>
<?php
$images = array(
"{\"title\":\"Forrest Gump\", \"image\":\"https://image.tmdb.org/t/p/original/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg\"}",
"{\"title\":\"Batman\", \"image\":\"https://image.tmdb.org/t/p/original/cij4dd21v2Rk2YtUQbV5kW69WB2.jpg\"}",
"{\"title\":\"Spider-Man: No Way Home\", \"image\":\"https://image.tmdb.org/t/p/original/uJYYizSuA9Y3DCs0qS4qWvHfZg4.jpg\"}",
"{\"title\":\"Bee Movie\", \"image\":\"https://image.tmdb.org/t/p/original/grUgUWkTCO2UbyKZdswPnR4sAhk.jpg\"}"
);
for ($i = 0; $i < count($images); $i++) {
$object = json_decode($images[$i]);
echo "<img src='$object->image' alt='$object->title' width='100%'>\n";
}
?>
</div>
</section>

<section id="login">
<h2>Login</h2>
<p>For existing users, login to your account with your username and password.</p>
<form action="redirectPage.php" method="POST" class="form-login">
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

<section id="registration"> <!-- Create customer account. -->
<h2>Registration</h2>
<form action="redirectPage.php" method="POST" class="form-registration">
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
<button type="submit" name="check" value="register">Submit</button>
</form>
</section>
</main>
</body>
<!-- When creating other user accounts, e.g. admin, owner, manager, add the following to the form:
<label for="title">Title:</label>
<select name="title" id="title">
< ?php
// Replace this with a GET request to the API.
$titles = array("Mr", "Mrs", "Ms", "Dr", "Prof");
foreach ($titles as $title) {
echo "<option value='$title'>$title</option>";
}
?>
</select>
-->

</html>

