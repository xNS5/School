if (hoursWorked > 40) 
    {
    overtimePay == (hoursWorked-40)*18;
    weeklySalary == (hoursWorked * hourlyWage) + overtimePay;
    cout << "Pay including overtime: " << "$" << weeklySalary << endl;
    }

else if (hoursWorked < 40)
    {
    return 0;
    
    }
}