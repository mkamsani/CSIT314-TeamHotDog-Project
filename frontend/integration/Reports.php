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
            <option value = "weekly">Monthly</option>
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
function retrieveReportData($period)
{
    // Replace this with your code to fetch report data from the database/API based on the selected time period
    // You may use the $period variable to query the data accordingly
    $reportData = array(
        array("2023-05-01", "Adult", 10.00),
        array("2023-05-01", "Child", 5.00),
        array("2023-05-02", "Adult", 10.00),
        array("2023-05-02", "Child", 5.00),
        array("2023-05-03", "Adult", 10.00),
        array("2023-05-03", "Child", 5.00),
    );

    return $reportData;
}

// Handle form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST')
{
    $period = $_POST['period'];

    // Retrieve the report data based on the selected time period
    $reportData = retrieveReportData($period);

    $totalRevenue = 330;
    $totalTickets = count($reportData);

    // Display the report table
    echo '<table id="reportsTable" class="table table-hover-dark table-sm table-responsive text-white" style="width: 70%; margin: auto">';
    echo '<thead><tr><th>Ticket Purchase Date</th><th>Ticket Type</th><th>Ticket Type Price</th><th>Total Revenue</th><th>Total Tickets</th></tr></thead>';
    echo '<tbody>';
    foreach ($reportData as $row)
    {
        echo '<tr>';
        echo '<td style="padding-bottom: 1%;">' . $row[0] . '</td>';
        echo '<td style="padding-bottom: 1%;">' . $row[1] . '</td>';
        echo '<td style="padding-bottom: 1%;">' . $row[2] . '</td>';
        echo "<td>" . $totalRevenue . "</td>";
        echo "<td>" . $totalTickets . "</td>";
        echo '</tr>';
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