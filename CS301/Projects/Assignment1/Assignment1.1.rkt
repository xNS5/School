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
(define (caller x y n)
     (define prime (alg n))
     (cond  
        ((or(< prime x) (integer? (sqrt prime))) (caller x y (+ n 1)))
        ((<= prime y)
           (cond
             ((send-help prime 2) (caller x y (+ n 1)))
             (else
              (let* ((roots (root prime prime)) (low (car roots)) (high (car (cdr roots))) (result (list prime low high)))
                (let ((result (append (cons (caller x y (+ n 1)) '()) (cons prime (cons low (cons high '()))))))
                  (display result))))))))
           
                

; This function just recursively loops to see if there exists a number that
; when x (or the prime number) modulo another number that itsn't x equals zero.
; I'm starting at 2 because 
; Stage 2 helper
(define (send-help x n)
    (cond
      ((eqv? x n) #f)
      ((eqv? 0 (modulo x n)) #t)
      (else (send-help x (+ n 1)))))

; This function finds two numbers that, when suqared,
; sum up to the target number. It finds the difference between it
; and another number, and determines if the difference is a perfect square.
; If it isn't a perfect square, it continues to the next number.
; If it is a perfect square, it returns a list of the two numbers.
; Stage 3
(define (root x y)
    (let ((diff (- x y)))
     (cond
      ((and (integer? (sqrt y)) (integer? (sqrt diff))) (list (sqrt diff) (sqrt y)))
      (else (root x (- y 1))))))


;This function reads in data from a text file.
;Read data
(define reader
  (let ((n (open-input-file "test.txt")))
  (let z ((x (read n)))
    (if (eof-object? x)
        (begin
          (close-input-port n))
        (cons x (z (read n)))))))

;Main function call
(caller (car reader) (car (cdr reader)) 0)






