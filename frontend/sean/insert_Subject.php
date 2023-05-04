<!DOCTYPE html>
<html lang="en">

<?php
session_start();
include('header.php');
?>

<nav class="navbar navbar-expand-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="Pics/my_enrolment.png" alt="Avatar Logo" style="width:25px; margin-bottom: 5px"> Online Enrolment System
        </a>
        <ul class="nav nav-pills">
            <li class="nav-item">
                <a class="nav-link" href="admin_Profile.php">My Profile</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link active" href="insert_Subject.php">Insert Subjects</a>
            </li>
            &emsp;
            <li class="nav-item">
                <a class="nav-link" href="subject_List.php">View Subjects</a>
            </li>
            &emsp;
            <form class="d-flex">
                <a class="btn btn-outline-primary" href="logout.php">Log Out</a>
            </form>
        </ul>
    </div>
</nav>

<body>
<div class="container-fluid p-5 bg-primary text-white text-center">
    <h1>Insert Subject</h1>
    <p>Admin ID: <?php echo $_SESSION["userId"] ?></p>
</div>

<<?php
// DB Config
// DB Config
$DB_Username = 'root';
$DB_Password = '';
$DB = 'isit307a2';

$link = new mysqli('localhost', $DB_Username, $DB_Password, $DB) or die("Unable to connect to server.");

// Variable, Error and Validation Defaults
$subCode = $subName = $lecturer = $venue = "";
$status = "Active";
$subCodeErr = $subNameErr = $lecturerErr = $venueErr = "";
$subCodeValidated = $subNameValidated = $lecturerValidated = $venueValidated  = false;
$pattern = "/^[A-Za-z]{4}\d{3}$/";
$ed_id_pattern = '/^ED\d{3}$/';



if ($_SERVER["REQUEST_METHOD"] == "POST")
{
    //Subject Code Validation
    if (empty(strtoupper($_POST["subCode"])))
    {
        $subCodeErr = "Subject Code is required!";
    }
    elseif (!preg_match($pattern, strtoupper($_POST["subCode"])))
    {
        $subCodeErr = "Incorrect Subject Code Format!";
    }
    else
    {
        $subCode = test_input(strtoupper($_POST["subCode"]));
        $subCodeValidated = true;
    }

    //Subject Name Validation
    if (empty($_POST["subName"]))
    {
        $subNameErr = "Subject Name is required!";
    }
    else
    {
        $subName = test_input(strtoupper($_POST["subName"]));
        $subNameValidated = true;
    }

    // Lecturer Validation
    if (empty($_POST["lecturer"]))
    {
        $lecturerErr = "Lecturer name is required!";
    }

    else
    {
        $lecturer = test_input(strtoupper($_POST["lecturer"]));
        $lecturerValidated = true;
    }
    


    // Venue Validation
    if (empty($_POST["venue"]))
    {
        $venueErr = "Venue field is required!";
    }
    else
    {
        $venue = test_input(strtoupper($_POST["venue"]));
        $venueValidated = true;
    }



    // Connect to Database
    if ($subCodeValidated && $subNameValidated && $lecturerValidated && $venueValidated == true)
    {

        $subCode = mysqli_real_escape_string($link, $subCode);
        $subName = mysqli_real_escape_string($link, $subName);
        $lecturer = mysqli_real_escape_string($link, $lecturer);
        $venue = mysqli_real_escape_string($link, $venue);
    
        $sql_if_subCode_exists = "SELECT * FROM subject WHERE SubjectCode = '$subCode'";
        $result = mysqli_query($link, $sql_if_subCode_exists);
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);

        if (mysqli_num_rows($result) == 1)
        {
            $prodIdErr = "Subject already exists. Please try insert another.";
        }
        
        else
        {
            $sql = "INSERT INTO subject (SubjectCode, SubjectName, Lecturer, Venue, Status)
                        VALUES ('$subCode', '$subName', '$lecturer', '$venue', '$status')";

            if ($link->query($sql) === TRUE)
            {
                echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Success!</strong> The form has been submitted. Head over to the <a href="subject_List.php" class="alert-link">Subject List</a>
                        to view your subject, or go <a href="admin_Profile.php" class="alert-link">back</a>.
                        </div>
                    </div>';
             }
            else
            {
                echo "Error: " . $sql . "<br>" . $link->error;
            }
        }
    }
    $link->close();
}

function test_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    return htmlspecialchars($data);
}

?>

<div class="container mt-4" style="margin-left: 20%">
    <form class='form-wrapper mx-5' novalidate action="<?php echo $_SERVER["PHP_SELF"]; ?>"
          method='POST' style="width: 40%">
        <h1>Subject Details</h1>
        <div class="mt-3">
            <label for="subCode" class="form-label">Subject Code: </label>
            <input type="text" class="form-control" id="subCode" name="subCode" maxlength="7" placeholder="Format: CSIT123"
                   style="text-transform: uppercase" required>
        </div>
        <span class="error" style="color:red"><?php echo $subCodeErr ?></span>
        <div class="mt-3">
            <label for="subCode" class="form-label">Subject Name: </label>
            <input type="text" class="form-control" id="subName" name="subName" placeholder="Insert subject name"
                   style="text-transform: uppercase" required>
        </div>
        <span class="error" style="color:red"><?php echo $subNameErr ?></span>
        <div class="mt-3">
            <label for="lecturer" class="form-label">Lecturer: </label>
            <input type="text" class="form-control" id="lecturer" name="lecturer"  style="text-transform: uppercase" placeholder="Example: Sionggo Japit" required>
        </div>
        <span class="error" style="color:red"><?php echo $lecturerErr ?></span>
        <div class="mt-3">
            <label for="venue" class="form-label">Venue: </label>
            <textarea type="text" rows="5" class="form-control" id="venue" name="venue"
            style="text-transform: uppercase"  placeholder="Example: BLK A | LT A.4.08." required></textarea>
        </div>
        <span class="error" style="color:red"><?php echo $venueErr ?></span>
        <div class="mt-3">
            <input class="btn btn-primary" type="submit" name="submit" value="Submit">
        </div>
    </form>
</div>

</body>
<?php include('footer.php') ?>
</html>