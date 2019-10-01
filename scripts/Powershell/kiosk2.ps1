# Author: Michael Kennedy
# Taken and modified from https://www.reddit.com/r/PowerShell/comments/336b5v/restart_internet_explorer_11_when_closed/
# Last modified: 8/16/19
# Comments:
#          This script is written to re-open internet explorer for the kiosk account in the event it gets closed. Another possible route is to
#          disable the user's ability to actually close the internet explorer window, however I felt that it would add an extra layer of security
#          to have it open in private mode and have the ability to restart a new session every time.
#          In order to modify the tabs that open, all you have to do is edit the "links" text file in the \Scripts directory. Otherwise, 
#          You would need to jump back and forth between the Vumaint account and the Kiosk account to change the tab settings.

# If you run into an issue when running this script with an error that said something about Windows Powershell updating the execution policy, 
# go to this link: https://social.technet.microsoft.com/Forums/ie/en-US/23fdac67-be07-4c4b-9a25-7b947b4cdd33/execution-policy?forum=winserverpowershell

Start-Job -Name restart -ScriptBlock{

    #Declaring function names
    param($ScriptRoot, $screensaver_name)

    #Running the helpers file and calling the tabs function
    . (Join-Path -Path $ScriptRoot -ChildPath "\helpers.ps1")
    tabs

    While ($true){

        #Two booleans to determine whether IE/the screensaver is running
        $ie_bool = -not ((Get-Process iexplore -ErrorAction SilentlyContinue) -eq $null)
        $screensaver =-not ((Get-Process -Name ssText3d.scr -ErrorAction SilentlyContinue) -eq $null)

        #If internet explorer isn't running, it reopens IE and calls the tabs function
        if($ie_bool -eq $false){
            Invoke-Command -ScriptBlock { Start-Process "iexplore.exe" -WindowStyle Maximized -ArgumentList '-private http://www.wwu.edu/map' }
            tabs
        }
        #if the screen saver is running
        if($screensaver -eq $true){
            idle -screensaver $screensaver_name
        }        
    }
} -ArgumentList @($PSScriptRoot, "ssText3d.scr") | Wait-Job

# $PSscriptRoot is where this file is currently located, ssText3d.scr is the name of the screensaver. The | Wait-Job tells the program to wait until the job is finished. 




