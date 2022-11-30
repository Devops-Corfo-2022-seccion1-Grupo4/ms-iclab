#!/bin/bash

if [[ $# -eq 3 ]]; then
        actual="$1";
        nuevo="$2";
        path="$3";
sed -i "12,14 s/$actual/$nuevo/g" $path/pom.xml
else 
<<<<<<< HEAD
	echo "error";
=======
        echo "error";
>>>>>>> a21525d447ceef000d3b50c11d5ee7870ea4b6e3
fi
exit 0
