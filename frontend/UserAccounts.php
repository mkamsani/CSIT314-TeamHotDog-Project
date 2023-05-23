<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 class="text-center">HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="UserAdmin.php">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="UserAccounts.php">User Accounts</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="UserProfiles.php">User Profiles</a>
            </li>
            &emsp;
            <form method="get" class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>View User Accounts</h1>
</div>

<div class="container mt-3 text-white">
    <div class="input-group mb-3" style="margin: auto; width: 50%">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
        &nbsp;
        &nbsp;
        &nbsp;
        &nbsp;
        <div>
            <a class="btn btn-danger" href="CreateUser.php" role="button">Create User Account</a>
        </div>
    </div>

    <?php
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/active');
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

</div>
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
        table = document.getElementById("accountsTable");
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