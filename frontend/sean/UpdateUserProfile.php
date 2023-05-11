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
// Code for update
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $json = json_encode($_POST);
    $ch = curl_init("http://localhost:8000/api/admin/user-profile/update/" . $_POST['title-selector']);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
    $result = curl_exec($ch);
    curl_close($ch);
    echo $result;
}
?>
<div class="container mt-4" style="margin-left: 20%; width: 20%">
    <label class = "form-label" for="title-selector">Select the user profile to update:</label>
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class ="form-registration">
    <select class = "form-select" name="title-selector" id="title-selector">
    <?php
    $ch = curl_init("http://localhost:8000/api/admin/user-profile/read/active");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $user_profiles = curl_exec($ch); // [{"privilege":"customer","title":"customer"},...,{"privilege":"admin","title":"chief information officer"}]
    curl_close($ch);
    $user_profiles = json_decode($user_profiles, true);
    foreach ($user_profiles as $user_profile) {
        if ($user_profile['title'] === 'customer') continue;
        $titleCapitalized = ucwords($user_profile['title']);
        $privilegeCapitalized = ucwords($user_profile['privilege']);
    // if {$user_profile['title']}, move to the next iteration
        echo <<<EOF
    <option value="{$user_profile['title']}">$privilegeCapitalized: $titleCapitalized</option>\n
EOF;
    }
    ?>
    </select>
        <div class="mt-3">
            <label class = "form-label">Title:</label>
            <input type="text" class="form-control" required placeholder="Original Value" id="title" name="target-title"  disabled>
            <input type="text" class="form-control" required placeholder="Updated Value"             name="title">
        </div>

        <div class="mt-3">
            <label class = "form-label">Privilege:</label>
            <input  type="text" class="form-control" required placeholder="Original Value" id="privilege"  disabled>
            <input  type="text" class="form-control" required placeholder="Updated Value"  name="privilege">
        </div>

        <div class="mt-3">
            <input class="btn btn-danger" type="submit" name="submit" value="Update">
        </div>

    </form>
</div>
<script>
    const titleSelector = document.getElementById('title-selector');
    let title = document.getElementById('title');
    // We add an event selector so that titleSelector changes, we perform a GET request to the API to get the title and then we set the value of the title input to the title we got from the API.
    titleSelector.addEventListener('change', () => {
        // find from the using query select an option with the value of titleSelector.value
        // set the value of title to the value of the option
        title.value = titleSelector.value;
        // do a for loop across all <option tags>
        for (let i = 0; i < titleSelector.options.length; i++) {
            if (titleSelector.options[i].value === titleSelector.value) {
                // if the value of the option is equal to titleSelector.value
                // log its label also
                let privilege = titleSelector.options[i].label;
                privilege = privilege.substring(0, privilege.indexOf(':')).toLowerCase();
                console.log(titleSelector.options[i].value);
                console.log(privilege);
                document.getElementById('privilege').placeholder = privilege;
            }
        }
    });
</script>
    <?php include('footer.php') ?>
</body>


</html>