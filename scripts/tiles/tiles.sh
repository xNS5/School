#!/bin/bash
echo "Beginning QuickTile Initialization"
echo realpath

path="$(dirname "$(which "$0")")"
tiles=`cat $path/tiles.txt`
line_number=`adb devices | wc -l` 

if [ "$1" == "-h" ]; then
 echo "Usage: ./tiles [-h help]"
 echo "Simply run the shell program and it will handle the rest"
fi


if [ ${#tiles} -eq 0 ]; then
   echo "The Tiles file does not seem to exist."
   read -p "Do you want to create the file? (Y || N) " ans
   shopt -s nocasematch
   case "$ans" in 
      "Y" )
	if [ $line_number -eq 3 ]; then
         adb shell "settings get secure sysui_qs_tiles" > $path/tiles.txt
	else
	echo "Device is not connected"
      *) exit 0;;
   esac
fi


while :
   do
   if [ $line_number -eq 3 ]; then
      echo "Device Found"
      echo "Device ID: "`adb devices | grep -w "device" | awk '{print $1;}'`
      break
   elif [ $line_number -gt 3 ]; then
      echo "Multiple Devices Detected. Please disconnect one"
      echo $line_number
   else
      echo "No Devices Found. Please ensure that your device is connected"

   fi
   sleep 10s
done

adb shell "settings put secure sysui_qs_tiles \"$tiles\""

echo "Completed. Exiting..."
exit 0
