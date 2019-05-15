#lang R5RS

;This function reads in data from a text file.
;Read data
(define reader
  (let ((n (open-input-file "range.txt")))
  (let z ((x (read n)))
    (if (eof-object? x)
        (begin
          (close-input-port n))
        (cons x (z (read n)))))))

; Simple function that just returns 4x+1
; Stage 1 
(define alg
  (lambda (x)
    (+ (* 4 x) 1)))

; This function takes in 3 parameters, the lower bound, the upper bound, and the upper bound once again. The last upper bound is acting as the counter variable.
; It pumps the value of n into 4n+1. If the result of 4n+1 is greater than the upper bound, it recursively loops and decrements the value of n by 1.
; With complete disclosure, this program probably runs a little longer than it needs to in this case. When this program was written such that it iterated up instead of down,
; the output onto the text file was printed in reverse. Getting up to that point was a pain, so instead of figuring out how to iterate up and have output that matches
; that of the prompt, I decided to iterate down which resolved my issue but at the expense of runtime. 
; If the value is within the range, then it gets appended to the text file.
; If the value is outside of the bottom end of the range or the value is 1 to account for when the bottom of the range is 0, it closes the file port and displays
; "Completed".
; Stage 2
(define (caller x y n)
  (define my-file (open-output-file #:mode 'text #:exists 'append "primes4np1.txt"))
     (define prime (alg n))
     (cond
       ((> prime y) (caller x y (- n 1)))
       ((or (< prime x) (eqv? prime 1)) (close-output-port my-file) (display "Completed"))
       ((and (<= prime y) (>= prime x))  ; I made this greater than or equal to the value of prime because there's a possiblity that the range could include a prime number. 
           (cond
             ((send-help prime 2) (caller x y (- n 1)))
             (else
              (let* ((roots (root prime prime)) (low (car roots)) (high (car (cdr roots))) (result (list prime low high)))
                (display (string-append (number->string prime) " ") my-file)
                (display (string-append (number->string low) " ") my-file)
                (display (string-append (number->string high) "\r\n") my-file)
                (newline my-file)
                (caller x y (- n 1)))))))
  (close-output-port my-file))
           
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
; is a perfect square. Once it finds two numbers that, when squared, sum up to the target it returns a list containing the square roots of both numbers. 
; Stage 3
(define (root x y)
    (let ((diff (- x y)))
     (cond
      ((and (integer? (sqrt y)) (integer? (sqrt diff))) (list (sqrt diff) (sqrt y)))
      (else (root x (- y 1))))))


;Main function call
(caller (car reader) (car (cdr reader)) (car (cdr reader)))





