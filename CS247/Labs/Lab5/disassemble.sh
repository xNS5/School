gcc Lab5.3.c -o Exercise3 -fstack-protector-all
objdump -s -d Exercise3 | awk '/<main>/,/^\s*$/'
