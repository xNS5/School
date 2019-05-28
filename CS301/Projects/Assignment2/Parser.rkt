
;Opening 3 file ports
(define my-file (open-output-file #:mode 'text #:exists 'append "parsestack"))
;(define my-file (open-output-file #:mode 'text #:exists 'append "inputstream"))
;(define my-file (open-output-file #:mode 'text #:exists 'append "comment"))

;This function reads in data from a text file.
;Read data


(call-with-input-file "a.txt"
  (lambda (input-port)
    (let loop ((x (read-char input-port)))
      (if (not (eof-object? x))
          (begin
            (display x)
            (loop (read-char input-port)))))))


(define reader
  (let ((n (open-input-file #:mode 'text #:for-module? #t "input")))
  (let z ((x (read n)))
      (cond
        ((eof-object? x) (close-input-port n) '())
        (else (cons x (z (read n))))))))

;Push and Pop
;All I actually do for Pop is the car of a list, so why do I need a function that does that? We'll see. 
;Syntax: src, dest
(define pop
  (lambda (stack)
    (if (null? stack)
        '()
        (car stack))))

(define push
  (lambda (stack val)
    (if (or (null? stack) (null? val))
        '()
        (append stack val))))
  
;Main Function
(define parse
  (let ((input reader))
    (display input)))

