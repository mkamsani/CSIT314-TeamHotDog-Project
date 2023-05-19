<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand">
            <h1 style="width:25px; margin-bottom: 5px"> HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="CinemaOwner.php">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="Reports.php">View Reports</a>
            </li>
            <form method="get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>View Reports</h1>
</div>

<form action="<?php echo $_SERVER['PHP_SELF']?>" method="POST">
    <div class="container mt-3 text-white">
        <div class="input-group mb-3" style="margin: auto; width: 50%">
            <span class="input-group-text" id="searchLbl">Search:</span>
            <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
        </div>
    </div>

    <div class="mt-3 container" style="width: 20%">
        <label for="type" class ="text-white">Select Report Type:</label>
        <select class = "form-select" name="type" id="type">
            <option value = "revenue">Revenue</option>
            <option value = "ratings">Ratings/Reviews</option>
        </select>
    </div>

    <div class="mt-3 container" style="width: 20%">
        <label for="type" class ="text-white">Select Time Period:</label>
        <select class = "form-select" name="period" id="period">
            <option value = "daily">Daily</option>
            <option value = "weekly">Weekly</option>
            <option value = "monthly">Monthly</option>
        </select>
    </div>

    <div class="mt-3 container" style="width: 60%; text-align: center">
        <input class="btn btn-primary" type="submit" name="submit" value="Retrieve">
    </div>
</form>
<div class="mt-3 container">
</div>
<?php
// Function to retrieve the report data
function retrieveReportData($type, $period)
{
/*
    http://localhost:8000/api/owner/revenue/daily/read
    http://localhost:8000/api/owner/revenue/weekly/read
    http://localhost:8000/api/owner/revenue/monthly/read
    Example of data returned:
    {"purchaseDate":"2023-04-01","typeName":"child","typePrice":"5.50","typeSumRevenue":"5.50","totalTickets":"1"}

    http://localhost:8000/api/owner/ratings/daily/read
    http://localhost:8000/api/owner/ratings/weekly/read
    http://localhost:8000/api/owner/ratings/monthly/read
    Example of data returned:
    {"username":"user_09","rating":4,"review":"I had a great time at the cinema. The movie was fantastic, and the theater provided a top-notch audiovisual experience.","dateCreated":"2023-04-03"}

    Relevant backend files for revenue:
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRevenueDailyReadController.java
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRevenueWeeklyReadController.java
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRevenueMonthlyReadController.java
    backend/src/main/java/com/hotdog/ctbs/entity/RevenueReport.java
    backend/src/main/java/com/hotdog/ctbs/entity/RevenueReportId.java
    backend/src/main/java/com/hotdog/ctbs/repository/RevenueReportRepository.java

    Relevant backend files for ratings/reviews:
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRatingsDailyReadController.java
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRatingsWeeklyReadController.java
    backend/src/main/java/com/hotdog/ctbs/controller/owner/OwnerRatingsMonthlyReadController.java
    backend/src/main/java/com/hotdog/ctbs/entity/RatingReview.java
    backend/src/main/java/com/hotdog/ctbs/repository/RatingReviewRepository.java
*/

    $ch = curl_init("http://localhost:8000/api/owner/" . $type . "/" . $period . "/read");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    curl_close($ch);
    return json_decode($result, true);
}

// Handle form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST')
{
    $type = $_POST['type'];
    $period = $_POST['period'];

    // Retrieve the report data based on the selected time period
    $reportData = retrieveReportData($type, $period);

    if ($type == "revenue") {
        $th1 = "<th>Ticket Purchase Date</th>";
        $th2 = "<th>Ticket Type</th>";
        $th3 = "<th>Ticket Type Price</th>";
        $th4 = "<th>Total Revenue</th>";
        $th5 = "<th>Total Tickets</th>";
    } else {
        $th1 = "<th>Date</th>";
        $th2 = "<th>Rating</th>";
        $th3 = "<th>Username</th>";
        $th4 = "<th>Review</th>";
        $th5 = "";
    }

    // Display the report table
    echo '<table id="reportsTable" class="table table-hover-dark table-sm table-responsive text-white" style="width: 70%; margin: auto">';
    echo "<thead><tr>{$th1}{$th2}{$th3}{$th4}{$th5}</tr></thead>";
    echo '<tbody>';
    foreach ($reportData as $row)
    {
        // Declare an array called $td of size 5, an each element contains '<td>'
        $td = array_fill(0, 5, "<td style=\"padding-bottom: 1%;\">");
        if ($type == "revenue")
        {
            // Display date as "Sunday, 2 April".
            $purchaseDate = $row['purchaseDate'];
            if ($purchaseDate != "Total") {
                $purchaseDate = date("l, j F", strtotime($purchaseDate));
            }

            $td[0] .= $purchaseDate . '</td>';
            $td[1] .= $row['typeName'] . '</td>';
            $td[2] .= $row['typePrice'] . '</td>';
            $td[3] .= $row['typeSumRevenue'] . '</td>';
            $td[4] .= $row['totalTickets'] . '</td>';
        }
        else
        {
            // Display date as "dd/mm".
            $row['dateCreated'] = substr($row['dateCreated'], 8, 2) . "/" . substr($row['dateCreated'], 5, 2);
            // Replace rating of 1 to 5 with the ⭐ symbol.
            $rating = str_repeat("⭐", $row['rating']);
            // Replace ". " in a review with ".<br />" to display each sentence in a new line.
            $row['review'] = str_replace(". ", ".<br />", $row['review']);

            $td[0] .= $row['dateCreated'] . '</td>';
            $td[1] .= $rating . '</td>';
            $td[2] .= $row['username'] . '</td>';
            $td[3] .= $row['review'] . '</td>';
            $td[4] = "";
        }
        echo "<tr>" . implode($td) . "</tr>";
    }
    echo '</tbody>';
    echo '</table>';
}
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

    .table-hover-dark tbody tr:hover
    {
        background-color: #333; /* Replace with your desired hover color */
    }
</style>
<?php include('footer.php') ?>
<script>
    function tableSearch()
    {
        // Declare variables
        var input, filter, table, tr, td, i;
        input = document.getElementById("searchBox");
        filter = input.value.toUpperCase();
        table = document.getElementById("reportsTable");
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
</html>