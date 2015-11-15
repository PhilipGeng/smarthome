<?
	$fd = fopen("status.txt","r");
	echo file_get_contents("status.txt").";".filemtime("status.txt");
	fclose($fd);
	
?>