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
                <a class="nav-link" href="educator_Profile.php">My Profile</a>
            </li>
            &emsp;
            &emsp;
            <li class="nav-item">
                <a class="nav-link active" href="educator_subjectlist.php">View Subjects</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="educator_teachingsubjects.php">Teaching Subjects</a>
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
    <h1>Subject Details</h1>
    <p>Educator ID: <?php echo $_SESSION["userId"] ?></p>
</div>

<?php
// DB Config
$DB_Username = 'root';
$DB_Password = '';
$DB = 'isit307a2';

$link = new mysqli('localhost', $DB_Username, $DB_Password, $DB) or die("Unable to connect to server.");

$userId = $_SESSION["userId"];
$name = $_SESSION["name"];
$surname = $_SESSION["surname"];
$subCode = $subName = $lecturer = $venue = $status = "";

// Display Subject
if (isset($_GET["subCode"]))
{
    $subCode = $_GET["subCode"];
    $sql = "SELECT * FROM subject WHERE SubjectCode = '$subCode'";
    $result = $link->query($sql);

    if ($result->num_rows > 0)
    {
        while ($row = $result->fetch_assoc())
        {
            $subCode = $row["SubjectCode"];
            $subName = $row["SubjectName"];
            $lecturer = $row["Lecturer"];
            $educatorId = $row["EducatorId"];
            $venue = $row["Venue"];
            $status = $row["Status"];
        }
    }
}

if ($_SERVER["REQUEST_METHOD"] == "POST")
{
    $subCode = mysqli_real_escape_string($link, $subCode);
    $subName = mysqli_real_escape_string($link, $subName);
    $venue = mysqli_real_escape_string($link, $venue);
    $status = mysqli_real_escape_string($link, $status);
 
    // Update Database
    $subCode = $_POST['subCode'];
    $subName = $_POST['subName'];
    $lecturer = $_POST['lecturer'];
    $venue = $_POST['venue'];
    $educatorId = $_POST["educatorId"];

    $sql_if_teaching_exists = "SELECT EducatorId FROM subject WHERE SubjectCode = '$subCode' AND EducatorId = '$userId' AND Status = 'Active'";
    $result = mysqli_query($link, $sql_if_teaching_exists);
    $erow = mysqli_fetch_array($result, MYSQLI_ASSOC);

    if (mysqli_num_rows($result) == 1)
        {
            echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>You are already teaching this subject!</strong> Please select another subject at <a href="educator_subjectlist.php" class="alert-link">Subject List</a>.
                        </div>
                    </div>';
        }

    else
    {
        $sql = "UPDATE subject SET Lecturer = CONCAT('$name', ' ', '$surname'), EducatorId = '$userId', Venue = '$venue' WHERE SubjectCode = '$subCode'";

        if ($link->query($sql) === TRUE)
            {
                echo '
                    <div class="container mt-5">
                        <div class="alert alert-success mb-3 mt-3" id="successMsg" style="width: 75%;">
                        <strong>Successful!</strong> The form has been submitted. Head over to the <a href="educator_teachingsubjects.php" class="alert-link">Teaching Subjects</a>
                        to view your teaching subjects, or go <a href="educator_Profile.php" class="alert-link">back</a>.
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

function test_input($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    return htmlspecialchars($data);
}

?>

<div class="container mt-4">
    <form class="mt-4 mx-auto" novalidate action="<?php echo $_SERVER["PHP_SELF"]; ?>"
          method='POST' style="width: 40%;">
        <div class="row g-2 col-4 mx-auto">
            <a href="educator_subjectlist.php" class="btn btn-primary">Go Back</a>
        </div>
        <div class="mt-2 row g-1 mx-auto">
            <div class="col-md">
                <div class="form-floating">
                    <input type="text" class="form-control" id="subCode" name="subCode"
                           value="<?php echo $subCode ?>" readonly>
                    <label for="subCode">Subject Code: </label>
                </div>
            </div>
            <div class="col-md">
                <div class="form-floating">
                    <input type="text" class="form-control" id="subName" name="subName"
                           value="<?php echo $subName ?>" readonly>
                    <label for="subName">Subject Name: </label>
                </div>
            </div>
        </div>
        <div class="mt-1 row g-1 mx-auto">
            <div class="col-md">
                <div class="form-floating">
                    <input type="text" class="form-control" id="lecturer" name="lecturer"
                           value="<?php echo $lecturer ?>"
                           readonly>
                    <label for="lecturer">Lecturer: </label>
                </div>
            </div>
            <div class="col-md">
                <div class="form-floating">
                    <input type="text" class="form-control" id="educatorId" name="educatorId"
                           value="<?php echo $educatorId ?>"
                           readonly>
                    <label for="lecturer">Educator Id: </label>
                </div>
            </div>
            <div class="col-md">
                <div class="form-floating">
                    <input type="text" class="form-control" id="venue" name="venue" value="<?php echo $venue ?>">
                    <label for="venue">Venue: </label>
                </div>
            </div>
        </div>
        <div class="mt-2 row g-1 mx-auto">
        <input class="btn btn-primary" type="submit" name="submit" value="Teach">
        </div>

        </div>
    </form>
</div>
</body>
<?php include('footer.php') ?>
</html>