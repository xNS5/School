#lang racket

; Simple function that just returns 4x+1
; Stage 1 
(define alg
  (lambda (x)
    (+ (* 4 x) 1)))
          

; Stage 2
(define (caller x y)
   (define (iter x n)
     (define prime (alg n))
       (if (send-help prime)
          (iter x (+ n 1))
          (cond
            [(< prime x) (iter x (+ n 1))]
            [(< prime y)
             (printf "~a" prime)
             (let ([x (root prime)])
               (displayln (format " ~a ~a" (car(cdr x)) (car x))))
             (iter x (+ n 1))])))
             
  (iter x 0))

; Stage 2 helper
(define (send-help x)
  (when (integer? (sqrt x))
    true)
     (for ([t (in-list '(2 3 5 7))])
       (cond
         [(eqv? 0 (modulo x t)) true]
         [(eqv? x t) (continue)]))
  false)


; Stage 3
(define root
  (lambda (x)
    (let* ([y (exact-floor (sqrt x))]
           [z (exact-floor (sqrt (- x (* y y))))])
     (list y z))))

(display (send-help 16))