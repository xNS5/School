(define stack '())

(define push
  (lambda (x)
    (cons x stack)))


(define main
  (lambda (x)
    (let ((temp1 (push x)))
      (let ((temp2 (push x)))
        (display temp1)
        (display temp2)))))