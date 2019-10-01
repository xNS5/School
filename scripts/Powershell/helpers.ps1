# Author: Michael Kennedy
# Date Created: 8/16/19
# Taken and modified from: https://stackoverflow.com/questions/15845508/get-idle-time-of-machine
#=========================
# Date Modified + Changes: 
#=========================
# The function idle was taken and modified from: https://stackoverflow.com/questions/15845508/get-idle-time-of-machine 
# The idle function detects whether someone is interacting with the computer. If they don't interact with the computer for 
# 1 hour, then it calls sleep_func.
# The function sleep_func is similar to idle, except that it doesn't keep track of time. It wakes the computer, kills IE, then starts
# the screensaver again. If it detects that something moved on the computer, like a tap on the screen for example, it breaks and returns
# back to kiosk

Add-Type @'
using System;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace PInvoke.Win32 {

    public static class UserInput {

        [DllImport("user32.dll", SetLastError=false)]
        private static extern bool GetLastInputInfo(ref LASTINPUTINFO plii);

        [StructLayout(LayoutKind.Sequential)]
        private struct LASTINPUTINFO {
            public uint cbSize;
            public int dwTime;
        }

        public static DateTime LastInput {
            get {
                DateTime bootTime = DateTime.UtcNow.AddMilliseconds(-Environment.TickCount);
                DateTime lastInput = bootTime.AddMilliseconds(LastInputTicks);
                return lastInput;
            }
        }

        public static TimeSpan IdleTime {
            get {
                return DateTime.UtcNow.Subtract(LastInput);
            }
        }

        public static int LastInputTicks {
            get {
                LASTINPUTINFO lii = new LASTINPUTINFO();
                lii.cbSize = (uint)Marshal.SizeOf(typeof(LASTINPUTINFO));
                GetLastInputInfo(ref lii);
                return lii.dwTime;
            }
        }
    }
}
'@


Function wake_func{
    $wshell = New-Object -ComObject wscript.shell;
    Sleep -Seconds 1
    $wshell.SendKeys('{ENTER}')
}

Function sleep_func{
    param($screensaver)
    $old = [PInvoke.Win32.UserInput]::lastInputTicks
    wake_func
    Stop-Process -Name "iexplore" -Force -ErrorAction SilentlyContinue
    Start-Process $screensaver

    while($true){
        $ticks = [PInvoke.Win32.UserInput]::lastInputTicks
    
        Sleep -Milliseconds 200
    
        if($old -ne $ticks){
            break
        }
    }
}

Function idle{
    param($screensaver)
    $old = [PInvoke.Win32.UserInput]::lastInputTicks

    while ($true) {
        $Idle = [PInvoke.Win32.UserInput]::IdleTime
        $ticks = [PInvoke.Win32.UserInput]::lastInputTicks
        Sleep -Seconds 1

        If(($Idle.Seconds -eq 10)){
            sleep_func -screensaver $screensaver
            break
        }
        elseif($old -ne $ticks){
            break
        }
    }
}

Function tabs{
    $Links = Get-Content (Join-Path -Path $PSScriptRoot -ChildPath "\links.txt")
    Sleep -Seconds 0.75
    foreach ($url in $Links){
        Start-Process $url
        Sleep -Milliseconds 25
    }
 }


