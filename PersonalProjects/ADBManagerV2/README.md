# ADB Manager

All of the programs for pushing/pulling files to an android device from 
Mac either suck or cost money. Screw that. 

This program utilized ADB to push/pull files from an android file system to macOS. 

1. Figure out a language to use
1.1 Current idea is to use Java or Python. I'm leaning towards Python because I haven't worked with it before

2. Implement program
2.1 Startup with listed devices. Have a refresh button that sends "adb devices" command instead of having a while loop.

2.2 When device is plugged in, have a button that selects that specific device (supports multiple devices)
2.2.1 Maybe add a feature that allows push to both devcices. Maybe spin up threads to complete both tasks simultaneously

2.3 Have split pane file manager for both computer and device. Add feature to move panes back and forth (l -> r, or r -> l)
2.4 Either select file(s) then add button that pushes/pulls file/directory from device/computer. (> and < for push/pull)
2.4.1 Why not both lmao. It's really just file manipulation with a GUI

Note to self: 

`adb shell settings get global device_name` To get device name
Add -s flag then the phone's ID to do things to it

Initial design was to use bash, but maybe java can be better. Or python depending on 
how easy it is to design a UI of some sort
Use `adb shell ls -r /[directory]` to list files in the directory
