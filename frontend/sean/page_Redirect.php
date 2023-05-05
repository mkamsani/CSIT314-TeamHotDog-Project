<?php
session_start();

if ($_SESSION["result"] == "Success")
{
    header("location: CinemaManager.php");
}

else
{
    $userIdErr = "Invalid User ID.";
}

