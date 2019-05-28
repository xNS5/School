;Opening 3 file ports
(define my-file (open-output-file #:mode 'text #:exists 'append "parsestack"))
(define my-file (open-output-file #:mode 'text #:exists 'append "inputstream"))
(define my-file (open-output-file #:mode 'text #:exists 'append "comment"))

;This function reads in data from a text file.
;Read data
(define reader
  (let ((n (open-input-file "input")))
  (let z ((x (read n)))
    (if (eof-object? x)
        (close-input-port n)
        (cons x (z (read n)))))))


