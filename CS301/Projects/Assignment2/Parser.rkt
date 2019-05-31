;note to self: Include $$ in test files
;Use Define instead of a list
;Upper case letters are the ids


;Opening 3 file ports
;(define parse-file (open-output-file #:mode 'text #:exists 'append "parsestack"))
;(define inputstream-file (open-output-file #:mode 'text #:exists 'append "inputstream"))
;(define comment-file (open-output-file #:mode 'text #:exists 'append "comment"))

;Read data
;This function reads in data from a text file. It reads in the data and constructs a list of characters.
(define reader
  (let ((n (open-input-file #:mode 'text "input")))
  (let z ((x (read-char n)))
      (cond
        ((eof-object? x) (close-input-port n) '())
        (else (cons x (z (read-char n))))))))

;Splitter
;This function splits up a list of characters into a list of individual words.
;It takes in a list of strings, and assigns them to the 'loop' inside of the splitter function.
;t in this case is an accumulator variable.
;If str is a pair, then it grabs the car of the str list and assigns it the variable head.
;If the character 'head' is a #\space or a #\newline character, it recursively calls 'loop' with the cdr of str
;along with an empty symbol for 't'.
(define splitter
  (lambda (str)
  (let iter ((t '()) (lst str))
    (cond
    ((pair? lst)
        (let ((head (car lst)))
          (cond
          ((or (char=? head #\space) (char=? head #\newline)) (cons (reverse t) (iter '() (cdr lst))))
          (else (iter (cons (car lst) t) (cdr lst))))))
     (else (list (reverse t)))))))

;string-split
;Driver function for splitter.
(define (string-split str)
  (map list->string (splitter (string->list str))))

;Assigning the output of string-split to a variable.
(define input (string-split (list->string reader)))

;Push and Pop
;Push adds the value to the top of the list, 
;Syntax: src, dest
(define (pop stack)
    (if (null? stack)
        "Error: Null Stack"
        (car stack)))

;Syntax: Stack, val
;Stack is a list, val is an int
(define (push val stack)
    (if (or (null? stack) (null? val))
        (cond
          ((null? stack) "Error:  Null Stack")
          ((null? val) "Error: Null Value"))
        (append (list val) stack)))

(define program
  (lambda (nonterm lst)))

(define 

;Parse
;Main Function
(define parse
  (lambda (input)
    (
