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

. (Join-Path $PSScriptRoot -ChildPath helpers.ps1)
tabs

# This while loop checks to see if iexplore is still active. If it is, it does nothing. If it isn't, it opens up again to the WWU map   
 While ($true){
     If (-not ((Get-Process iexplore -ErrorAction SilentlyContinue) -eq $null)){
         Sleep -Milliseconds 100
         Invoke-Command -ScriptBlock { Start-Process "iexplore.exe" -ArgumentList '-private http://www.wwu.edu/map' }
         tabs
     }
     if(-not((Get-Process 'ssText3d.scr' -ErrorAction SilentlyContinue) -eq $null)){
        idle
     }
 }