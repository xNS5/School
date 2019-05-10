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
        ((< prime x) (iter x (+ n 1)))
        ((integer? (sqrt prime)) (iter x (+ n 1)))
        ((< prime y)
           (cond
             ((send-help prime) (iter x (+ n 1)))
             (else
                  (display prime)
                  (let ((roots (root prime)))
                    (let* ((low (car roots)) (high (car (cdr roots))) (lowsq (* low low)) (highsq (* high high)))
                      (display " ")
                      (display low)
                      (display " ")
                      (display high)
                      (newline)
                      (iter x (+ n 1)))))))))
        (iter x 0))

; This function just recursively loops to see if there exists a number that
; when x (or the prime number) modulo another number that itsn't x equals zero.
; I'm starting at 2 because 
; Stage 2 helper
(define (send-help x)
  (define (iter y)
    (cond
      ((eqv? x y) #f)
      ((eqv? 0 (modulo x y)) #t)
      (else (iter (+ y 1)))))
  (iter 2))

; This function finds two numbers that, when suqared,
; sum up to the target number. It finds the difference between it
; and another number, and determines if the difference is a perfect square.
; If it isn't a perfect square, it continues to the next number.
; If it is a perfect square, it returns a list of the two numbers.
; Stage 3
(define (root x)
  (define (iter y)
    (let ((diff (- x y)))
     (cond
      ((and (integer? (sqrt y)) (integer? (sqrt diff))) (list (sqrt diff) (sqrt y)))
      (else (iter (- y 1))))))
  (iter x))

;This function reads in data from a text file.
;Read data
(define loader
  (let ((p (open-input-file "test.txt")))
  (let f ((x (read p)))
    (if (eof-object? x)
        (begin
          (close-input-port p)
          '())
        (cons x (f (read p)))))))

;Main function
(caller (car loader) (car (cdr loader)))


