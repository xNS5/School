# Batch Renumber
I wrote this program initially to just renumber in batch photos I scanned i
from 35mm negatives.
I later implemented this program with a GUI using Java Swing. So far it's 
pretty basic, but it has all the functionalities I need. 

As it stands, it's a little messy but I'll get around to cleaning it up 
eventually.

# How to Use
If the program doesn't find a br_config folder in your root directory, it
will create one. In this config folder, there are 2 files: filter and init.
Init contains the default directory that Renumber navigates to. If init 
is empty, it uses the root directory as the default. Filter contains all of 
the file extensions that you might want to use. Both of these are editable, and
I've implemented functions to edit/change both.

First the user selects the directory to convert files in. Then, they pick
out a naming convention. Lastly, they decide what number they want to iterate
up from. Once all of those fields are filled, a new dialogue will pop up 
showing the progress of renaming the files. By default, the _ character is the
default delimiter between the new name and the number. If all of the boxes are 
unticked in the Delimiter sub-menu, there will be no delimiter.

Once the files are converted, the user has the option to return to the main dialogue
or open the folder that got converted.

I experienced some weird issues with Java's Arrays.sort() method, so I wrote
(ish) a merge sort algorithm that sorts the files according to the number
in their names. I'm fairly certain Arrays.sort() overwrote files which gave the
appearance that it deleted files. Whatever the case, the new sorting algorithm
fixed the issue. 
