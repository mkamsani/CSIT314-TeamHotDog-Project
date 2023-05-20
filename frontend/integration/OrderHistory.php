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
                <a class="nav-link" href="Customer.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="CustomerMovies.php">Movies</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="Booking.php">Book a Movie</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="OrderHistory.php">My Bookings</a>
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
    <p>Username: <?php echo $username; ?></p>
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
    curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/customer/ticket/read/' . $username);
    curl_setopt($ch, CURLOPT_HTTPGET, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    $data = json_decode($result, true);

    $tableHtml = '<table id="ordersTable" class="table table-hover-dark table-sm table-responsive text-white">';
    $tableHtml .= '<thead><tr><th>Movie</th><th>Show Date</th><th>Show Time</th><th>Seat Row/Col<th>Cinema Room</th><th>Price</th><th>Ticket Type</th><th>Purchase Date</th></tr></thead>';
    foreach ($data as $row) {
        $strpuchasedate = strtotime($row['purchaseDate']);
        $strpuchasedate = date("Y-m-d h:i:s A", $strpuchasedate);
        $tableHtml .= '<tr>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['movie'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['showDate'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['showTime'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['row'] . $row['column'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['cinemaRoom'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['price'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $row['type'] . '</td>';
        $tableHtml .= '<td style="padding-bottom: 5%;">' . $strpuchasedate . '</td>';
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
<script>
    function tableSearch()
    {
        // Declare variables
        var input, filter, table, tr, td, i;
        input = document.getElementById("searchBox");
        filter = input.value.toUpperCase();
        table = document.getElementById("ordersTable");
        tr = table.getElementsByTagName("tr"),
            th = table.getElementsByTagName("th");

        // Loop through all table rows, and hide those who don't match the search query
        for (i = 1; i < tr.length; i++)
        {
            tr[i].style.display = "none";
            for (var j = 0; j < th.length; j++)
            {
                td = tr[i].getElementsByTagName("td")[j];
                if (td)
                {
                    if (td.innerHTML.toUpperCase().indexOf(filter.toUpperCase()) > -1)
                    {
                        tr[i].style.display = "";
                        break;
                    }
                }
            }
        }
    }
</script>
<?php include('footer.php') ?>

</html>