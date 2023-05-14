<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/hotdog_cinemas.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Hotdog Cinemas
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="UserAdmin.php">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="CreateUser.php">Create User Account</a>
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

<div class="container">
    <div class="input-group mb-3" style="margin: auto; width: 40%">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
    </div>
    <?php

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-account/read/active');
    curl_setopt($ch, CURLOPT_HTTPGET, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $result = curl_exec($ch);
    $data = json_decode($result, true);
    $tableHtml = '<table id="accountsTable" class="table table-hover">';
    $tableHtml.= '<thead><tr><th>Username</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Address</th><th>Title</th></tr></thead>';
    foreach ($data as $row)
    {
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