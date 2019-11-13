;This function reads in data from a text file.
;Read data
(define reader
  (let ((n (open-input-file "range")))
  (let z ((x (read n)))
    (if (eof-object? x)
        (close-input-port n)
        (cons x (z (read n)))))))

; Writes data to file
(define my-file (open-output-file #:mode 'text #:exists 'append "primes4np1"))

; Simple function that just returns 4x+1
; Stage 1 
(define alg
  (lambda (x)
    (+ (* 4 x) 1)))

; This function takes in 3 parameters, the lower bound, the upper bound, and the counter 0.
; It pumps the value of n into 4n+1. If the result of 4n+1 is lower than the lower bound, it recursively loops and increments the value of n by 1.
; Once it is within the range, it pipes the value to the send-help function to determine if the number is prime. If it is, it returns #f so it skips the
; recursive call to caller. Once it is known that the number is prime, it determines the "roots" of the function by piping the value of the 'prime' number to the
; root function. 'root' returns a list, the two numbers that sum up to the prime number when squared. It then gets written to the text file 'primes4np1' and moves onto
; the next number in the range. 
; Stage 2
(define (caller x y n)
     (define prime (alg n))
     (cond
       ((< prime x) (caller x y (+ n 1)))
       ((> prime y) (close-output-port my-file))
       ((and (<= prime y) (>= prime x))  ; I made this greater than or equal to the value of prime because there's a possiblity that the range could include a prime number
                                         ; as the lower or upper bound.
           (cond
             ((send-help prime 2) (caller x y (+ n 1)))
             (else
              (let* ((roots (root prime prime)) (low (car roots)) (high (car (cdr roots))))
                (display (string-append (number->string prime) " ") my-file)
                (display (string-append (number->string low) " ") my-file)
                (display (string-append (number->string high) "\r\n") my-file)
                (caller x y (+ n 1)))))))
  )
           
; This function recursively loops to see if there exists a number that when a modulus operation
; is applied to the prime number and some integer results in 0. If the number mod another number
; is 0, it returns #t which triggers the recursive call in caller to the top of the function.
; If the counter variable matches the prime number, then it's fair to assume that the number is a prime and returns #f which skips the recursive call.
; Otherwise, it recursively loops until one of the two conditions are met. 
; I'm starting at 2 because any number modulus 1 or 0 will result in 0. 
; Stage 2 helper
(define (send-help x n)
    (cond
      ((eqv? x n) #f)
      ((eqv? 0 (modulo x n)) #t)
      (else (send-help x (+ n 1)))))

; This function finds two numbers that, when suqared, sums up to the target number. It determines if a number less than the target is a perfect square, then
; determines if the difference is a perfect square. If the difference isn't a perfect square, then it continues until it finds a perfect square whose difference
; is a perfect square. Once it finds two numbers that are perfect squares that add up to the target x, it retuns a list of the square roots of those two numbers. 
; Stage 3
(define (root x y)
    (let ((diff (- x y)))
     (cond
      ((and (integer? (sqrt y)) (integer? (sqrt diff))) (list (sqrt diff) (sqrt y)))
      (else (root x (- y 1))))))

; Main function call
(caller (car reader) (car (cdr reader)) 0)





