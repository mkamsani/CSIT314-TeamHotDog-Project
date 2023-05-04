<?php
session_start();

if ($_SESSION["userId"] == "CM")
{
    header("location: CinemaManager.php");
}

else
{
    $userIdErr = "Invalid User ID.";
}

