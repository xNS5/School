#!/bin/bash
echo "Beginning Backup"

function getLines(){
   return `adb devices | wc -l`;
}


if ["$1" == "-h"]; then
   echo "Usage: ./backup [-h help]"
   echo "Simply run this shell program and it will handle the rest"
fi

while :
   do
   getLines
   if [ $? -eq 3] then
      echo "Device Found"
      echo "Device ID: "`adb devices | grep -w "device" | awk '{print $1;}'`
      echo "Device Model: "`adb shell getprop | grep "ro.product.model" | sed 's/^.*: //'`
      echo "Device Carrier: "`adb shell getprop | grep "nfc.fw.dfl_areacode" | sed 's/^.*: //'`
      break

   elif [ $? -gt 3]; then
      echo "Multiple Devices Detected. Please disconnect one"
   else
      echo "No devices found. Please ensure that your device is connected."
   fi
   sleep 5s
done

adb pull /system/sdcard/ ~/Downloads

echo "Completed. Exiting..."
exit 0
