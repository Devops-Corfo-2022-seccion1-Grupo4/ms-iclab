#!/bin/bash

if [[ $# -eq 3 ]]; then
        actual="$1";
        nuevo="$2";
        path="$3";
sed -i "12,14 s/$actual/$nuevo/g" $path/pom.xml
else 
        echo "error";
fi
exit 0
