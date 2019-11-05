#!/bin/bash

# adb shell settings get global device_name 

TITLE="Android Debug Bridge File Transfer Tool"

function getLines {
   return `echo `adb devices | wc -l``
}

function main {
   whiptail --title "$TITLE" --msgbox "Welcome to the ADB File Transfer tool. Please ensure that USB Debugging is enabled" 10 80

}

function chooseDevice {

   DEVICES=()

   for var in $(adb devices | head -2 | tail -1 | sed -e 's/device//g' )
      do
         DEVICES+=$var
      done

   #for device in $DEVICES
   #do
   #   echo `adb -s $device shell settings get global device_name`
   #   echo "Device ID: $device"
   # done

   while [ 1 ]
do
CHOICE=$(
whiptail --title "Operative Systems" --menu "Make your choice" 16 100 9 \
	"1)" "The name of this script."   \
	"2)" "Time since last boot."  \
	"3)" "Number of processes and threads." \
	"4)" "Number of context switches in the last secound." \
	"5)" "How much time used in kernel mode and in user mode in the last secound." \
	"6)" "Number of interupts in the last secound." \
	"9)" "End script"  3>&2 2>&1 1>&3	
)


result=$(whoami)
case $CHOICE in
	"1)")   
		result="I am $result, the name of the script is start"
	;;
	"2)")   
	        OP=$(uptime | awk '{print $3;}')
		result="This system has been up $OP minutes"
	;;

	"3)")   
	        p=$(ps ax | wc -l)
                t=$(ps amx | wc -l)
		result="Number of processes $p\nNumber os threads $t"
        ;;

	"4)")   
	        contextSwitch
		read -r result < result
        ;;

	"5)")   
                userKernelMode
		read -r result < result
        ;;

	"6)")   
		interupts
		read -r result < result
        ;;

	"9)") exit
        ;;
esac
whiptail --msgbox "$result" 20 78
done   #whiptail --title "$TITLE" --msgbox "Select your device: " --menu

}



main
chooseDevice

