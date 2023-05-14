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
<?php
// Code for update
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $json = json_encode($_POST);
    $ch = curl_init("http://localhost:8000/api/user-profile/update");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
    $result = curl_exec($ch);
    curl_close($ch);
    echo $result;
}
?>

<?php
$profiletitle = $_GET['title'];
// Retrieve the user data from the API
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, 'http://localhost:8000/api/admin/user-profile/read/' . $profiletitle);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$result = curl_exec($ch);
$data = json_decode($result, true);
echo $data;

?>
<div class="container-fluid p-5 bg-danger text-white text-center">
    <h1>Update a User Profile</h1>
</div>
<div class="container mt-4" style=" width: 30%">
    <div class="row">
        <div class="col-4 mx-auto">
            <input class="btn btn-danger" onclick="location.href='UserProfiles.php'" value = "Go back"></input>
        </div>
    <form action="<?php echo $_SERVER['PHP_SELF'] . '?' . 'title=' . $profile['title']; ?>" method="POST" class ="form-registration">
        <input type="hidden" name="targetTitle" value="<?php echo $profile['title']; ?>">
        <div class="col-auto">
            <input class="btn btn-outline-danger text-white" value="Suspend" type="submit" name ="action">
        </div>

        <div class="mt-3">
            <label class = "form-control">Title:</label>
            <input type="text" class="form-control" required placeholder="Original Value" id="title" name="target-title" value="<?php echo $profile["title"] ?>"  disabled>
            <input type="text" class="form-control" required placeholder="Updated Value"name="new-title">
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
        font-family: 'Cinzel', Arial, sans-serif;
        font-size: 36px;
        color: #e50914;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    }
</style>

</html>