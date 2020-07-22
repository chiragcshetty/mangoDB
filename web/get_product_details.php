<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$product = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$con = new DB_CONNECT();
$db = $con->conn();
 
// check for GET data
if (isset($_GET["prid"]))
    {
        $prid = $_GET['prid'];
        
        // get products from table
        $query = "SELECT * FROM products  WHERE prid=$prid";
        $result = mysqli_query($db, $query);

        if(($result->num_rows)>0)
        {
            while ($row = mysqli_fetch_array($result)) 
                {
                    //echo $row['prid'] . ' ' . $row['name'] .': '. $row['aisle'] . ' ' .'<br />';
                    
                    
                    //$product["prid"] = $row["prid"];
                    //$product["name"] = $row["name"];
                    //$product["price"] = $row["price"];
                    //$product["aisle"] = $row["aisle"];
                    
                    $product["prid"] = $row["prID"];
                    $product["name"] = $row["prName"];
                    $product["price"] = $row["prPrice"];
                    $product["desc"] = $row["prDesc"];
                    $product["image"] = $row["prImage"];

                    $product["success"] = 1;
                    $product["product"] = array();
 
                    //array_push($response["product"], $product);
                    echo json_encode($product);
                    
                
                }
        }
        else
        {
            $product["success"] = 0;
            $product["message"] = "No product found";
            echo json_encode($product);
        }
    }
else
    {
        // required field is missing
        $product["success"] = 0;
        $product["message"] = "Required field(s) is missing";
        echo json_encode($product);
    }

?>
