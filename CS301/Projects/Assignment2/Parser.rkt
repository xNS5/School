


;Opening 3 file ports
(define my-file (open-output-file #:mode 'text #:exists 'append "parsestack"))
(define my-file (open-output-file #:mode 'text #:exists 'append "inputstream"))
(define my-file (open-output-file #:mode 'text #:exists 'append "comment"))

(define push
  (lambda (x)
    (cons x stack)))


(define main
  (lambda (x)
    
    (let ((temp1 (push x)))
      (let ((temp2 (push x)))
        (display temp1)
        (display temp2)))))