<!DOCTYPE html>
<html lang="en">


<?php
session_start();
include('header.php');
?>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

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
                <a class="nav-link" href="UserAccounts.php">User Accounts</a>
            </li>
            <li class="nav-item">
                <a class="nav-link active bg-danger" href="UserProfiles.php">User Profiles</a>
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
    <h1>Update a User Profile</h1>
</div>

<?php
$profiletitle = $_GET['title'];
$profiletitle = str_replace(' ', '%20', $profiletitle);

// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-profile/read/' . $profiletitle);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
$profile = $data[0];
curl_close($ch);
?>

<?php
// Code for update
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $targetTitle = str_replace(' ', '%20', $_POST['targetTitle']);
    if ($_POST['action'] == 'Update')
    {
        $updatedProfile = array(
            'privilege' => $_POST['privilege'],
            'title' => $_POST['title']
        );

        $jsonData = json_encode($updatedProfile);
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-profile/update/' . $targetTitle);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
        curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonData);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        $result = curl_exec($ch);
        curl_close($ch);

        if ($result == "Success")
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> User Profile has been updated. Head over to the <a href="UserProfiles.php" class="alert-link">User Profiles</a>
                        to view profile, or go <a href="UserAdmin.php" class="alert-link">main page</a>.
                        </div>
                    </div>';
        }

        else {
            // Error message
            echo '
            <div class="container mt-3">
                <div class="alert alert-danger" style="width: 75%;">
                    <strong>Error:</strong> ' . $result . '
                </div>
            </div>';
        }
    }

    // Suspend user profile when the form is submitted
    if ($_POST['action'] == 'Suspend')
    {
        // Create a JSON payload with the target username
        $payload = json_encode(['title' => $targetTitle]);
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-profile/suspend/' . $targetTitle);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
        curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
        $result = curl_exec($ch);

        if ($result == "Success")
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> Profile has been suspended. Head over to the <a href="UserProfiles.php" class="alert-link">User Profiles</a>
                        to view profiles, or go <a href="UserAdmin.php" class="alert-link">main page</a>.
                        </div>
                    </div>';
        }

        else {
            // Error message
//            echo '
//            <div class="container mt-3">
//                <div class="alert alert-danger" style="width: 75%;">
//                    <strong>Error:</strong> ' . $result . '
//                </div>
//            </div>';
            var_dump($result);
        }

    }
}
?>

<div class="container mt-4" style=" width: 30%">
        <div class="col-4 mx-auto">
            <input class="btn btn-danger" onclick="location.href='UserProfiles.php'" value = "Go back"></input>
        </div>
    <form action="<?php echo $_SERVER['PHP_SELF'] . '?' . 'title=' . $profile['title']; ?>" method="POST" class ="form-registration">
        <input type="hidden" name="targetTitle" value="<?php echo $profile['title']; ?>">
        <div class="col-auto">
            <input class="btn btn-outline-danger text-white" value="Suspend" type="submit" name ="action">
        </div>

        <div class="form-floating col-md mt-4">
            <input type="text" class="form-control" required placeholder="Original Value" id="target-title" name="target-title" value="<?php echo $profile["title"] ?>"  readonly>
            <label for="target-title">Title: </label>
        </div>
        <div class="form-floating col-md mt-2">
            <input type="text" class="form-control" required placeholder="Updated Value" name="title" id="title" value="<?php echo $profile["title"] ?>">
            <label for="title">New Title: </label>
        </div>

        <div class="form-floating col-md mt-4">
            <input type="text" class="form-control" required placeholder="Original Value" name="target-privilege"
                   id="target-privilege" value="<?php echo $profile["privilege"] ?>"  readonly>
            <label for="target-privilege">Privilege: </label>
        </div>
        <div class="form-floating col-md mt-4">
            <select class = "form-select" name="privilege" id="privilege">
                <option value="customer">Customer</option>
                <option value="manager">Manager</option>
                <option value="owner">Owner</option>
                <option value="admin">Admin</option>
            </select>
            <label for="privilege">New Privilege:</label>
        </div>

        <div class="mt-2 row g-2 col-4 mx-auto">
            <input class="btn btn-outline-primary" type="submit" name="action" value="Update">
        </div>

    </form>
</div>
<script>
    // const titleSelector = document.getElementById('title-selector');
    // let title = document.getElementById('title');
    // // We add an event selector so that titleSelector changes, we perform a GET request to the API to get the title and then we set the value of the title input to the title we got from the API.
    // titleSelector.addEventListener('change', () => {
    //     // find from the using query select an option with the value of titleSelector.value
    //     // set the value of title to the value of the option
    //     title.value = titleSelector.value;
    //     // do a for loop across all <option tags>
    //     for (let i = 0; i < titleSelector.options.length; i++) {
    //         if (titleSelector.options[i].value === titleSelector.value) {
    //             // if the value of the option is equal to titleSelector.value
    //             // log its label also
    //             let privilege = titleSelector.options[i].label;
    //             privilege = privilege.substring(0, privilege.indexOf(':')).toLowerCase();
    //             console.log(titleSelector.options[i].value);
    //             console.log(privilege);
    //             document.getElementById('privilege').placeholder = privilege;
    //         }
    //     }
    // });
    // Get the target privilege value
    var targetPrivilege = document.getElementById('target-privilege').value;

    // Set the default value of new-privilege to match target-privilege
    var newPrivilegeSelect = document.getElementById('privilege');
    for (var i = 0; i < newPrivilegeSelect.options.length; i++) {
        if (newPrivilegeSelect.options[i].value === targetPrivilege)
        {
            newPrivilegeSelect.options[i].selected = true;
            break;
        }
    }
</script>
    <?php include('footer.php') ?>
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

</html>