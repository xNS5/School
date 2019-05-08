#lang racket

; Stage 1 
(define alg
  (lambda (x)
    (+ (* 4 x) 1)))
          

; Stage 2
(define (caller x y)
    (define (iter x n)
      (define prime (alg n))
        (cond
            [(< prime x) (iter x (+ n 1))]
            [(< prime y)
             (printf "~o\n" prime)
             (iter x (+ n 1))]))
             
  (iter x 0))

; Stage 3
(define (root y)
  (define (iter z)
    (cond
      [(integer? (sqrt z))
       (sqrt (- y z))
       (sqrt z)]
      [(not(integer? (sqrt z))) (iter ( - z 1))]))
  (iter y))

(caller 2 20)
       
    