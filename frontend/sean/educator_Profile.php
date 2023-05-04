<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');
?>

<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/my_enrolment.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Online Enrolment System
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link active" href="educator_Profile.php">My Profile</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="educator_subjectlist.php">Teach a Subject</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="educator_teachingsubjects.php">View Teaching Subjects</a>
            </li>
            &emsp;
            <form class="d-flex">
                <a class="btn btn-outline-primary" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-primary text-white text-center">
    <h1>Welcome <?php echo $_SESSION["name"] ?>!</h1>
    <p>Educator ID: <?php echo $_SESSION["userId"] ?></p>
</div>

<div class="container mt-6 row g-3 mx-auto" style="width: 20%">
    <h1 class="text-center">Educator Information</h1>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="name" value="<?php echo $_SESSION["name"] ?>" readonly>
            <label for="name">Name: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="surname" value="<?php echo $_SESSION["surname"] ?>" readonly>
            <label for="surname">Last name: </label>
        </div>
    </div>
    <div>
        <div class="form-floating">
            <input type="email" class="form-control" id="email" value="<?php echo $_SESSION["email"] ?>" readonly>
            <label for="email">E-Mail: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="phone" value="<?php echo $_SESSION["phone"] ?>" readonly>
            <label for="phone">Phone No: </label>
        </div>
    </div>
    <div class="col-md">
        <div class="form-floating">
            <input type="text" class="form-control" id="type" value="<?php echo $_SESSION["type"] ?>" readonly>
            <label for="type">Type: </label>
        </div>
    </div>
    
</div>
<div class="text-center mt-3">
    <a class="btn btn-primary" href="educator_subjectlist.php" role="button" style="margin-right: 2.7%">Teach a Subject</a>
    <a class="btn btn-primary" href="educator_teachingsubjects.php" role="button">View Teaching Subjects</a>
</div>

</body>
<?php include('footer.php') ?>

</html>