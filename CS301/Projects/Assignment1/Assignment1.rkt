#lang racket

; Simple function that just returns 4x+1
; Stage 1 
(define alg
  (lambda (x)
    (+ (* 4 x) 1)))


; This function recursively loops to determine if a number in the range of x and y is a prime.
; First it checks to see that the value of 4x+1 for some x can find an integer square root.
; If the number returned is an integer, then it is not a prime number and the function increments the counter
; If the result of the number isn't an integer, it continues and pipes the number to send-help, a helper function
; that determines if the number is prime. Once that is completed, it either loops and increments the counter or
; continues down to a conditional statement block. If the number is less than the lower bound, then it increments the counter.
; if it isn't less than x and the value of 'prime' is less than the upper bound, it prints out the value of 'prime', then moves to the 'root'
; function to determine which two numbers when squared equal the 'prime' number. 
; Stage 2
(define (caller x y)
   (define (iter x n)
     (define prime (alg n))
     (cond
        [(< prime x) (iter x (+ n 1))]
        [(integer? (sqrt prime)) (iter x (+ n 1))]
        [(< prime y)
           (cond
             [(send-help prime) (iter x (+ n 1))]
             [else
                  (printf "~a" prime)
                  (let ([roots (root prime)])
                  (let* ([low (car roots)] [high (car (cdr roots))] [lowsq (* low low)] [highsq (* high high)])
                  (displayln (format " Sum: ~a  Squares: ~a ~a" (+ lowsq highsq) low high))
                  (iter x (+ n 1))))])]))
  (iter x 0))

; This function just recursively loops to see if there exists a number that
; when x (or the prime number) results in zero with a modulo function. 
; Stage 2 helper
(define (send-help x)
  (define (iter y)
    (cond
      [(eqv? x y) false]
      [(eqv? 0 (modulo x y)) true]
      [else (iter (+ y 1))]))
  (iter 2))
    
; Stage 3
(define (root x)
  (define (iter y)
    (let [(diff (- x y))]
     (cond
      [(and (integer? (sqrt y)) (integer? (sqrt diff))) (list (sqrt diff) (sqrt y))]
        [else (iter (- y 1))])))
  (iter x))
      

;Read data
(define in (open-input-file "test.txt"))
(define read
   (list(read-line in)))

(caller (car read)
