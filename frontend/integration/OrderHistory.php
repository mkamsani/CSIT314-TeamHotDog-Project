<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');

$username = "";
$privilege = "";

if (isset($_SESSION["username"]))
{
    $username = $_SESSION["username"];
    // Use the $username variable as needed
}

if (isset($_SESSION["privilege"]))
{
    $privilege = $_SESSION["privilege"];
    // Use the $username variable as needed
}

// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/' . $username);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
$user = $data[0];
curl_close($ch);

?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 style="width:25px; margin-bottom: 5px"> HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="Customer.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="">Movies</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="OrderHistory.php">My Orders</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CustAccountDetails.php">My Account</a>
            </li>
            &emsp;<li class="nav-item">
                <a class="nav-link" href="CustomerReview.php">Customer Review</a>
            </li>
            <form method = "get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Booking History</h1>
</div>
<div class="container mt-3 text-white">
    <div class="input-group mb-3" style="margin: auto; width: 50%">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
        &nbsp;
        &nbsp;
        &nbsp;
        &nbsp;
    </div>

    <?php
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/customer/ticket/read');
    curl_setopt($ch, CURLOPT_HTTPGET, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    $data = json_decode($result, true);
    $tableHtml = '<table id="accountsTable" class="table table-hover-dark table-sm table-responsive text-white">';
    $tableHtml .= '<thead><tr><th>Username</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Address</th><th>Title</th></tr></thead>';
    foreach ($data as $row) {
        $tableHtml .= '<tr>';
        $tableHtml .= '<td><a href="UserDetails.php?username=' . $row['username'] . '">' . $row['username'] . '</a></td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['firstName'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['lastName'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['email'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['address'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['title'] . '</td>';
        $tableHtml .= '</tr>';

    }
    $tableHtml .= '</table>';
    echo $tableHtml;
    ?>
</body>

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
<?php include('footer.php') ?>

</html>