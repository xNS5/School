///////////////////////////////////////////////////////////////////////////////////
// Author: Michael Kennedy                                                       //
// Class: CSCI140                                                                //
// Assignment: Week 3, Program 2                                                 //
// Input: Reads name, hourly wage, and hours worked from user                    // 
// Processing: Calculates weekly salary based on hourly wage and hours worked    // 
// Output: Weekly salary of user                                                 //
///////////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <string> // I'm going to be asking the user for their first and last name, so this is going to be a string. edit: apparently I was making it more complicated that I needed to make it. Oops.
using namespace std;

string first;
string last;
float hourlyWage = 12;
float hoursWorked;
float overtimePay;
float weeklySalary;


int main()
{
//  cout << "Enter Employee Name (Last, First): "<< endl; //Asking employee to enter their name
//  cin >> last >> first;
//  cout << "Input Hourly Wage: " << endl;
//  cin >> hourlyWage;                                               // I was having a little too much fun
    cout << "Input hours worked" << endl;
    cin >> hoursWorked;    
  
    weeklySalary = (hoursWorked * hourlyWage);
    
//  cout << "~~~~~~~~~~~~~~~~~~~~~~~~~~~~EMPLOYEE PAY STUB~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
//  cout << "Employer:   Aperature Laboratories                                                                                                           //   << endl;
//  cout << "Employee Name: " << last << " " << first << endl; <- I was having a little too much fun here. Sorry!
    cout << "Hourly Wage: " << hourlyWage << endl;
    cout << "Hours Worked: " << hoursWorked << endl;
    cout << "Weekly Salary: " << "$" << weeklySalary << endl;


if (hoursWorked > 40) 
    {
    overtimePay = (hoursWorked-40)*18;
    weeklySalary = overtimePay + (hourlyWage * 40);
    cout << "Pay including overtime: " << "$" << weeklySalary << endl;
    }

else if (hoursWorked < 40)
    {
    return 0;
    
    }
}
