<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');



    $ticketCh = curl_init();
    curl_setopt($ticketCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/read/allTicketTypes");
    curl_setopt($ticketCh, CURLOPT_RETURNTRANSFER, 1);
    $ticket = curl_exec($ticketCh);
    curl_close($ticketCh);
    $ticket = trim($ticket, '[');
    $ticket = trim($ticket, ']');
    $ticket = explode(", ",$ticket);
    //print_r($ticket);



    $ticketDetailCh = curl_init();
    curl_setopt($ticketDetailCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/read/allTicketTypesDetails");
    curl_setopt($ticketDetailCh, CURLOPT_RETURNTRANSFER, 1);
    $ticketDetails = curl_exec($ticketDetailCh);
    curl_close($ticketDetailCh);
    $ticketDetails = json_decode($ticketDetails, true);
    //print_r($ticketDetails);


if (isset($_POST['updateTicket'])) {

    $ticketTypeName = $_POST['typeName'];
    $newTicketTypeName = $_POST['newTypeName'];
    $newPrice = $_POST['newPrice'];
    $ticketActivity = $_POST['updateIsActive'];
    if(  $ticketActivity == "TRUE"){
        $ticketActivity = true;
    }
    else if($ticketActivity == "FALSE"){
        $ticketActivity = false; //php returns nothing if its false
    }

    $updateTicketCh = curl_init();
    $data = array('targettypename' => $ticketTypeName, 'newtypename'=> $newTicketTypeName, 'typeprice' => $newPrice, 'isactive' => $ticketActivity);
    $data_json = json_encode($data);
    //print_r($data_json);
    curl_setopt($updateTicketCh, CURLOPT_URL, "http://localhost:8000/api/ticketType/update/ticketType");
    curl_setopt($updateTicketCh, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($updateTicketCh, CURLOPT_POSTFIELDS, $data_json);
    curl_setopt($updateTicketCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($updateTicketCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
    $updateTicket = curl_exec($updateTicketCh);
    curl_close($updateTicketCh);
    $updateTicket = json_decode($updateTicket, true);
   // print_r($updateTicket);
}


if (isset($_POST['createTicket'])) {

    $ticketTypeName = $_POST['ticketType'];
    $ticketTypePrice = $_POST['typePrice'];
    $isActive = $_POST['createIsActive'];
    if(  $isActive == "TRUE"){
        $isActive = true;
    }
    else if($isActive == "FALSE"){
        $isActive = false;
    }
    setlocale(LC_MONETARY, "zh_SG");
    $data = array('typename' => $ticketTypeName, 'typeprice' => $ticketTypePrice, 'isactive' => $isActive);
    $data_json = json_encode($data);
    print_r($data_json);
    $ticketTypeCh = curl_init("http://localhost:8000/api/ticketType/create/ticketType");
    curl_setopt($ticketTypeCh, CURLOPT_POST, "1");
    curl_setopt($ticketTypeCh, CURLOPT_POSTFIELDS, $data_json);
    curl_setopt($ticketTypeCh, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ticketTypeCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

    $response = curl_exec($ticketTypeCh);
    curl_close($ticketTypeCh);
   // print_r($response);

}


?>




<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/hotdog_cinemas.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Hotdog Cinemas
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
                <a class="nav-link" href="cinema_screenings.php">Cinemas</a>
            </li>
            <li class="nav-item">
                <a class="nav-link bg-danger active" href="ticket_types.php">Ticket Types</a>
            </li>
            &emsp;
            <form class="d-flex">
                <a class="btn btn-outline-danger" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Ticket Types</h1>
<!--    <p>Admin ID: --><?php //echo $_SESSION["userId"] ?><!--</p>-->
</div>

<div class="container mt-4" style="margin-left: 20%; width: 40%">
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="showTicket">
        <h1>Create Ticket Form</h1>
        <div class="mt-3">
            <input type="text" class="form-control" name="ticketType" id="ticketType" placeholder="Enter ticket type">
            <input type="number" class="form-control" step="0.01" name="typePrice" id="typePrice" placeholder="Enter ticket price">
        </div>

        <div class="mt-3">
            <select class = "form-select" name="createIsActive" id="createIsActive">
                <option>Select ticket type Activity</option>
                <option value="TRUE">Active</option>
                <option value="FALSE">Not Active</option>
            </select>
        </div>

        <div class="mt-3">
            <input type="submit" name="createTicket" class = "btn btn-primary" value="Create Ticket">
        </div>
    </form>

    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="showTicket">
        <br>
        <br>
        <br>
        <br>
        <h1>Update Ticket Form</h1>
        <div class="mt-3">
            <table class="table table-hover" style="margin: auto; width: 100%; table-layout: fixed">
                <thead>
                <tr>
                    <th>Type Name</th>
                    <th>Type Price</th>
                    <th>Active</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <?php
                        $ticketTypeName = array_column($ticketDetails, 'typeName');
                        foreach ($ticketTypeName as $ticketTypeKey) {
                            echo '' . $ticketTypeKey . '<br/>';
                        }
                        ?>
                    </td>
                    <td>
                        <?php
                        $ticketPrice = array_column($ticketDetails, 'typePrice');
                        foreach ($ticketPrice as $ticketPriceKey) {
                            echo '' . $ticketPriceKey . '<br/>';
                        }
                        ?>
                    </td>
                    <td>
                        <?php
                        $isActive = array_column($ticketDetails, 'isActive');
                        foreach ($isActive as $isActiveKey) {
                            echo '' . $isActiveKey . '<br/>';
                        }
                        ?>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="mt-3">
            <select class = "form-select" name="typeName" id="typeName">
                <option>Select Ticket Type</option>
                <?php
                $typeName = array_column($ticketDetails, 'typeName');
                foreach ($typeName as $typeNameKey) {
                    ?>
                    <option><?php echo '' . $typeNameKey . '<br/>'; ?></option>
                    <?php
                }
                ?>
            </select>
        </div>

        <div class="mt-3">
            <input type="text" class = "form-control" name="newTypeName" id="newTypeName" placeholder="Enter new type name">
        </div>

        <div class="mt-3">
            <input type="number" class = "form-control" name="newPrice" step = "0.01" id="newPrice" placeholder="Enter new price">
        </div>

        <div class="mt-3">
            <select name="updateIsActive" class = "form-select" id="updateIsActive">
                <option> Select ticket Activity </option>
                <option value = "TRUE"> Active </option>
                <option value = "FALSE"> Not Active </option>
            </select>
        </div>

        <div class="mt-3">
            <input type="submit" class = "btn btn-primary" name="updateTicket" value="Update Ticket">
        </div>
    </form>
</div>

<?php include('footer.php') ?>

</html>

</table>