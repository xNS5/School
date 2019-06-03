;Write program that just writes p_stack to file

;================================================================================
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

;================================================================================
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
             ((and (not (eq? '() t)) (char=? head #\))) (cons t (iter (list #\)) (cdr lst))))
             ((char=? head #\() (cons (list head) (iter '() (cdr lst))))
             ((char=? head #\)) (cons (list head) (iter '() (cdr lst))))
             (else (iter (cons (car lst) t) (cdr lst))))))
        (else (list (reverse t)))))))

;string-split
;Driver function for splitter.
(define (string-split str)
  (map list->string (splitter (string->list str))))

;Assigning the output of string-split to a variable.
(define input (string-split (list->string reader)))

;================================================================================
;Push, Pop, and Empty? functions
;Push
;Syntax: val, stack
;Stack is a list, val is an int
(define (push val stack)
    (if (or (null? stack) (null? val))
        (cond
          ((null? stack) "Error:  Null Stack")
          ((null? val) "Error: Null Value"))
        (append (list val) stack)))

;Syntax: Stack
(define (empty? stack)
  (or (null? stack) (eqv? (car stack) "$$") (eqv? (length stack) 0)))

;Syntax: input
(define (letter? val)
  (if (= 1 (string-length val))
      (begin
        (let ((char_val (car (string->list val))))
              (if (and (char? char_val) (char-alphabetic? char_val))
                  #t
                  #f)))
      #f))


;================================================================================
;Parse Table Functions
;Make a first and follow function
(define table
  (lambda (p_stack infile)
    (let ((head (car p_stack)))
      (cond
        ((string=? head "program") (program p_stack))
        ((string=? head "stmt_list") (stmt_list p_stack))
        ((string=? head "stmt") "stmt")
        ((string=? head "expr") "expr")
        ((string=? head "term_tail") "term_tail")
        ((string=? head "term") "term")
        ((string=? head "factor_tail") "factor_tail")
        ((string=? head "factor") "factor")
        ((string=? head "add_op") "add_op")
        ((string=? head "mult_op") "mult_op")))))

;1
(define program
  (lambda (stk) ;write production 1
    (push "stmt_list" (cdr stk))))
    
;2
(define stmt_list
  (lambda (stk head) ;write production 2
    (if (string=? head "$$")
        "Predict 3"
        (begin
          (cond
            ((letter? head) (push "id" (push ":=" stk))) ;Producion 4
            ((string=? head "read") (push "read" (push "id" stk))) ;Production 5
            ((string=? head "write") (push "write"(push "expr" stk)))))))) ;Production 6
;7
(define expr
  (lambda (stk)
    (display stk)))

;8 and 9
(define term_tail
  (lambda (stk)
    (display stk)))

;10
(define term
  (lambda (stk)
    (display stk)))

;11 and 12
(define factor_tail
  (lambda (stk)
    (display stk)))

;13 14 and 15
(define factor
  (lambda (stk)
    (display stk)))

;16 and 17
(define add_op
  (lambda (stk)
    (display stk)))

;18 and 19
(define mult_op
  (lambda (stk)
    (display stk)))

;================================================================================
;Parse
;Main Function
(define parse
  (lambda (lst)
    (let ((p_stack (list "program" "$$")) (infile lst))
      (table (table p_stack infile) infile))))

(parse input)