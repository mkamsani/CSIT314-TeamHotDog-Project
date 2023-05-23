<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');

    $ticketDetailCh = curl_init();
    curl_setopt($ticketDetailCh, CURLOPT_URL, 'http://localhost:8000/api/manager/ticketType/read/all');
    curl_setopt($ticketDetailCh, CURLOPT_RETURNTRANSFER, 1);
    $ticketDetails = curl_exec($ticketDetailCh);
    curl_close($ticketDetailCh);
    $ticketDetails = json_decode($ticketDetails, true);
    //print_r($ticketDetails);



?>



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
                <a class="nav-link bg-danger active" href="ticket_types.php">Ticket Types</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="viewCinemaRoom.php">Cinema Rooms</a>
            </li>&emsp;

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
<?php
if (isset($_POST['updateTicket'])) {

$ticketTypeName =  str_replace(' ', '%20', $_POST['updateTypeName']);
$newTicketTypeName = $_POST['updateNewTypeName'];
$newPrice = $_POST['newPrice'];


$updateTicketCh = curl_init();
$data = array('tickettypename'=> $newTicketTypeName, 'tickettypeprice' => $newPrice);
$data_json = json_encode($data);
curl_setopt($updateTicketCh, CURLOPT_URL, 'http://localhost:8000/api/manager/ticketType/update/'.$ticketTypeName);
curl_setopt($updateTicketCh, CURLOPT_CUSTOMREQUEST, "PUT");
curl_setopt($updateTicketCh, CURLOPT_POSTFIELDS, $data_json);
curl_setopt($updateTicketCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($updateTicketCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
$updateResponse = curl_exec($updateTicketCh);
curl_close($updateTicketCh);
//    $updateTicket = json_decode($updateResponse, true);
if (curl_getinfo($updateTicketCh, CURLINFO_HTTP_CODE) === 200)

//Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
        <strong>Success!</strong> Ticket has been updated</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%;">
        <strong>Error:</strong> ' . $updateResponse . '
    </div>
</div>';
}


}


if (isset($_POST['createTicket'])) {

$ticketTypeName = $_POST['createTicketType'];
$ticketTypePrice = $_POST['createTypePrice'];
setlocale(LC_MONETARY, "zh_SG");
$data = array('typename' => $ticketTypeName, 'typeprice' => $ticketTypePrice);
$data_json = json_encode($data);
$ticketTypeCh = curl_init("http://localhost:8000/api/manager/ticketType/create/ticketType");
curl_setopt($ticketTypeCh, CURLOPT_POST, "1");
curl_setopt($ticketTypeCh, CURLOPT_POSTFIELDS, $data_json);
curl_setopt($ticketTypeCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ticketTypeCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));

$createResponse = curl_exec($ticketTypeCh);
if (curl_getinfo($ticketTypeCh, CURLINFO_HTTP_CODE) === 200) //Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
        <strong>Success!</strong> Ticket has been created</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%;">
        <strong>Error:</strong> ' . $createResponse . '
    </div>
</div>';
}

    curl_close($ticketTypeCh);
}


if (isset($_POST['suspendTicket'])) {

$suspendTicketTypeName =  str_replace(' ', '%20',$_POST['updateTypeName']);
$suspendTicketTypeCh = curl_init('http://localhost:8000/api/manager/ticketType/suspend/'.$suspendTicketTypeName);
curl_setopt($suspendTicketTypeCh, CURLOPT_CUSTOMREQUEST, "DELETE");
curl_setopt($suspendTicketTypeCh, CURLOPT_POSTFIELDS, $suspendTicketTypeName);
curl_setopt($suspendTicketTypeCh, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($suspendTicketTypeCh, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
$suspenseResponse = curl_exec($suspendTicketTypeCh);
curl_close($suspendTicketTypeCh);
if (curl_getinfo($suspendTicketTypeCh, CURLINFO_HTTP_CODE) === 200) //Show success message
{
echo '
<div class="container mt-5">
    <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
        <strong>Success!</strong> Ticket has been suspended</a>.
    </div>
</div>';
}

else
{
// Error message
echo '
<div class="container mt-3">
    <div class="alert alert-danger" style="width: 75%;">
        <strong>Error:</strong> ' . $suspenseResponse . '
    </div>
</div>';
}


}
?>

<div class="container mt-4" style="margin-left: 20%; width: 40%">
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="showTicket text-white"">

        <div class="mt-3 ">
            <table class="table text-white" style="margin: auto; width: 100%; table-layout: fixed">
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
                        $ticketTypeName = array_column($ticketDetails, 'typename');
                        foreach ($ticketTypeName as $ticketTypeKey) {
                            echo '' . $ticketTypeKey . '<br/>';
                        }
                        ?>
                    </td>
                    <td>
                        <?php
                        $ticketPrice = array_column($ticketDetails, 'typeprice');
                        foreach ($ticketPrice as $ticketPriceKey) {
                            echo '' . $ticketPriceKey . '<br/>';
                        }
                        ?>
                    </td>
                    <td>
                        <?php
                        $isActive = array_column($ticketDetails, 'isactive');
                        foreach ($isActive as $isActiveKey) {
                            echo '' . $isActiveKey . '<br/>';
                        }
                        ?>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>
        <br>
        <h1 >Create Ticket Form</h1>
        <div class="mt-3">
            <input type="text" class="form-control" name="createTicketType" id="createTicketType" placeholder="Enter ticket type" required>
        </div>

        <div class="mt-3">
            <input type="number" class="form-control" step="0.01" name="createTypePrice" id="createTypePrice" placeholder="Enter ticket price" required>
        </div>

        <div class="mt-3">
            <input type="submit" name="createTicket" class = "btn btn-primary" value="Create Ticket" >
        </div>
    </form>

    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="showTicket text-white"">
        </br>
        </br>
        </br>
        </br>
        <h1>Update Ticket Form</h1>


        <div class="mt-3">
            <select class = "form-select" name="updateTypeName" id="updateTypeName">
                <option>Select Ticket Type</option>
                <?php
                $typeName = array_column($ticketDetails, 'typename');
                foreach ($typeName as $typeNameKey) {
                    ?>
                    <option><?php echo '' . $typeNameKey . '<br/>'; ?></option>
                    <?php
                }
                ?>
            </select>
        </div>

        <div class="mt-3">
            <input type="text" class = "form-control" name="updateNewTypeName" id="updateNewTypeName" placeholder="Enter new type name">
        </div>

        <div class="mt-3">
            <input type="number" class = "form-control" name="newPrice" step = "0.01" id="newPrice" placeholder="Enter new price">
        </div>

        <div class="mt-3">
            <input type="submit" class = "btn btn-primary" name="updateTicket" value="Update Ticket">
            <input type="submit" class = "btn btn-primary" name="suspendTicket" value="Suspend Ticket">
        </div>
    </form>
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

    .d-flex{
        margin-left: 10px;
    }
</style>

<?php include('footer.php') ?>

</html>

</table>