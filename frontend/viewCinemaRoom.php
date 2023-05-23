<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');


$viewCinemaRoomsCh = curl_init();
curl_setopt($viewCinemaRoomsCh, CURLOPT_URL, "http://localhost:8000/api/manager/cinemaRoom/read/all");
curl_setopt($viewCinemaRoomsCh, CURLOPT_RETURNTRANSFER, 1);
$cinemaRoomDetails = curl_exec($viewCinemaRoomsCh);
curl_close($viewCinemaRoomsCh);
$cinemaRoomDetails = json_decode($cinemaRoomDetails, true);



?>
<head>
    <style>
        .movie-title {
            font-size: 20px;
            font-weight: bold;
        }

        .movie-description {
            font-size: 18px;
            font-family: "Arial", sans-serif;
            color: #555555;
        }

        .movie-genre {
            font-size: 16px;
            font-family: "Arial", sans-serif;
            color: #888888;
            text-transform: uppercase;
        }

        .movie-poster {
            width: 200px;
            height: 300px;
        }

        .movie-poster-title {
            font-weight: bold;
        }

        .fixed-forms-container {
            position: absolute;
            left: 2%; /* Remove the 'right' property */
            top: unset; /* Remove the 'top' property */
            right: unset; /* Update the 'left' property to 'right' */
            width: unset; /* Remove the 'width' property */
            height: unset;
            display: flex;
            flex-direction: row;
            justify-content: flex-start; /* Align forms to the top */
            padding: 20px;
            background-color: rgba(255, 255, 255, 0);
            z-index: 999;
            flex-direction: column;
            justify-content: flex-start;
            align-items: flex-start;
        }

        .movies-container
        {
            margin-left: 25%;
            width: 75%;
        }

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

        .d-flex{
            margin-left: 10px;

        }


    </style>
</head>


<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <h1 class="text-center">HOTDOG CINEMAS</h1>
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="CinemaManager.php">Home</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="movies.php">Movies</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class=" nav-link" href="movie_screening.php">Screening</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="ticket_types.php">Ticket Types</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link bg-danger active" href="viewCinemaRoom.php">Cinema Rooms</a>
            </li>

            <form class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>
<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Cinema Room</h1>
    <!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->

</div>

<div class="container mt-4" style="margin-left: 25%; width: 40%">
    <div class="input-group mb-3" style="width: 40%; margin: auto;">
        <span class="input-group-text" id="searchLbl">Search:</span>
        <input type="text" class="form-control" id="searchBox" onkeyup="tableSearch()">
    </div>

    </div>

    <table id="cinemaRoomTable" border="1" class="table text-white" style="width: 75%; table-layout: fixed; margin: auto auto auto 200px;">
        <thead>
        <tr>
            <th>Cinema Room No</th>
            <th>Status</th>
            <th>Capacity</th>
        </tr>
        </thead>
        <tbody>
        <?php
        foreach ($cinemaRoomDetails as $cinemaRoom) {
            $cinemaRoomID = $cinemaRoom['id'];
            $status = $cinemaRoom['isActive'];
            $capacity = $cinemaRoom['capacity'];
            ?>
            <tr>
                <a>
                    <td class="cinemaRoomID text-white"><?php echo $cinemaRoomID  ?></td>
                    <td class="status text-white"><?php echo $status ?></td>
                    <td class="capacity text-white"><?php echo $capacity ?></td>
                </a>
            </tr>
        <?php } ?>



        </tbody>
    </table>

    <script>
        function tableSearch()
        {
            // Declare variables
            var input, filter, table, tr, td, i;
            input = document.getElementById("searchBox");
            filter = input.value.toUpperCase();
            table = document.getElementById("moviesTable");
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

</div>
</body>


