<?php

class DB_CONNECT
{

	//constructor
	function conn()
	{
		require_once __DIR__ . '/db_config.php';

        $db = mysqli_connect(DB_SERVER , DB_USER , DB_PASSWORD , DB_DATABASE) or die('Error connecting to MySQL server!');
        //echo "Done";
		return $db;
		
	}
	
		function __destruct()
	{
		$this->close(); //Close the connection
	}


	function close()
	{
		mysql_close();
	}

}


?>