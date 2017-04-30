#@IgnoreInspection BashAddShebang
javac *.java

rm -f results.csv

echo "algo,page size,prepaging" >> results.csv

STR = java Driver programlist programtrace 1 fifo d
echo $STR
$STR >> results.csv
STR = java Driver programlist programtrace 2 fifo d
echo $STR
$STR >> results.csv
STR = java Driver programlist programtrace 4 fifo d
echo STR
STR  >> results.csv
STR = java Driver programlist programtrace 8 fifo d
echo STR
STR  >> results.csv
STR = java Driver programlist programtrace 16 fifo d
echo STR
STR >> results.csv

echo STR = java Driver programlist programtrace 1 lru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 2 lru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 4 lru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 8 lru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 16 lru d
STR  >> results.csv
echo STR =
echo STR = java Driver programlist programtrace 1 sclru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 2 sclru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 4 sclru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 8 sclru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 16 sclru d
STR  >> results.csv
echo STR = java Driver programlist programtrace 1 fifo p
STR  >> results.csv
echo STR = java Driver programlist programtrace 2 fifo p
STR  >> results.csv
echo STR = java Driver programlist programtrace 4 fifo p
STR  >> results.csv
echo STR = java Driver programlist programtrace 8 fifo p
STR  >> results.csv
echo STR = java Driver programlist programtrace 16 fifo p
STR  >> results.csv
echo STR =
echo STR = java Driver programlist programtrace 1 lru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 2 lru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 4 lru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 8 lru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 16 lru p
STR  >> results.csv
echo STR =
echo STR = java Driver programlist programtrace 1 sclru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 2 sclru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 4 sclru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 8 sclru p
STR  >> results.csv
echo STR = java Driver programlist programtrace 16 sclru p
STR  >> results.csv
