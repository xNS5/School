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
             ((or (char=? head #\() (char=? head #\))) (cons (list head) (iter '() (cdr lst))))
             (else (iter (cons (car lst) t) (cdr lst))))))
        (else (list (reverse t)))))))

;string-split
;Driver function for splitter.
(define (string-split str)
  (map list->string (splitter (string->list str))))

;Assigning the output of string-split to a variable.
(define input (string-split (list->string reader)))

;================================================================================
;Helper functions

;Push
;Syntax: val, stack
;Stack is a list, val is an int
(define (push val stack)
    (if (or (null? stack) (null? val))
        (cond
          ((null? stack) "Error:  Null Stack")
          ((null? val) "Error: Null Value"))
        (append (list val) stack)))

;empty?
;Syntax: Stack
(define (empty? stack)
  (or (null? stack) (eqv? (car stack) "$$") (eqv? (length stack) 0)))

;id?
;Syntax: input
(define (id? val)
  (if (= 1 (string-length val))
      (begin
        (let ((char_val (car (string->list val))))
              (if (and (char? char_val) (char-alphabetic? char_val))
                  #t
                  #f)))
      #f))
;valid?
;Head of the parse stack
(define valid?
  (lambda (x)
    (cond
      ((string=? x "id") #t)
      ((string=? x "read") #t)
      ((string=? x "write") #t)
      ((string=? x "(") #t)
      ((string=? x ")") #t)
      ((string=? x "*") #t)
      ((string=? x "+") #t)
      ((string=? x "/") #t)
      ((string=? x ":=") #t)
      (else #f))))

;================================================================================
;Parse Table Functions
;Make a first and follow function
(define table
  (lambda (p_stack token)
    (let ((head (car p_stack)))
      (cond
        ((string=? head "program") (program p_stack token))
        ((string=? head "stmt_list") (stmt_list p_stack token))
        ((string=? head "stmt") (stmt p_stack token))
        ((string=? head "expr") (expr p_stack token))
        ((string=? head "term_tail") (term_tail p_stack token))
        ((string=? head "term") (term p_stack token))
        ((string=? head "factor_tail") (factor_tail p_stack token))
        ((string=? head "factor") (factor p_stack token))
        ((string=? head "add_op") (add_op p_stack token))
        ((string=? head "mult_op") (mult_op p_stack token))))))

;1
(define program
  (lambda (stk token) ;write production 1
    (cond
    ((or (id? token) (string=? token "read") (string=? token "write") (string=? token "$$")) (push "stmt_list" (cdr stk)))
     (else "Syntax Error: program"))))
    
;2
(define stmt_list
  (lambda (stk token) ;write production 2
     (cond
       ((or (id? token) (string=? token "read") (string=? token "write")) (push "stmt" stk))
       ((string=? token "$$") (cdr stk)) ;production 3
       (else "Syntax error: stmt_list"))))


;4-6
(define stmt
  (lambda (stk token)
    (cond
      ((id? token) (push "id" (push ":=" (push "expr" (cdr stk))))) ;production 4
      ((string=? token "read") (push "read" (push "id" (cdr stk)))) ;production 5 
      ((string=? token "write") (push "write" (push "expr" (cdr stk)))) ;production 6
      (else "Syntax Error: stmt"))))

;7
(define expr
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token))) (push "term" (push "term_tail" (cdr stk))))
      (else "Syntax error: expr"))))

;8 and 9
(define term_tail
  (lambda (stk token)
    (cond
      ((or (string=? token "+") (string=? token "-")) (push "add_op" (push "term" (cdr stk)))) ;production 8
      ((or (string=? token "$$") (id? token) (string=? token "read") (string=? token "write") (string=? token ")")) (cdr stk)); production 9
      (else "Syntax Error: term_tail"))))

;10
(define term
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token))) (push "factor" (push "factor_tail" (cdr stk))))
      (else "Syntax error: term"))))

;11 and 12
(define factor_tail
  (lambda (stk token)
    (cond
    ((or (id? token) (string=? token "read") (string=? token "write") (string=? token ")") (string=? token "+") (string=? token "-") (string=? token "$$")) (cdr stk));production 11
    ((or (string=? token "*") (string=? token "/")) (push "mult_op" (push "factor" (push "factor_tail" (cdr stk)))));production 12
    (else "Syntax Error: factor_tail"))))

;13 14 and 15
(define factor
  (lambda (stk token)
    (cond
      ((string=? token "(") (push "(" (push "expr" (push ")" (cdr stk))))) ;production 13
      ((id? token) (push "id" (cdr stk))) ;production 14
      ((integer? (string->number token)) (push token (cdr stk)))))) ;production 15

;16 and 17
(define add_op
  (lambda (stk token)
    (cond
      ((string=? token "+") (push "+" (cdr stk))) ;production 16
      ((string=? token "-") (push "-" (cdr stk))) ;production 17
      (else "Syntax error: add_op"))))

;18 and 19
(define mult_op
  (lambda (stk token)
    (cond
      ((string=? token "*") (push "*" (cdr stk))) ;production 18
      ((string=? token "/") (push "/" (cdr stk))) ;production 19
      (else "Syntax error: mult_op"))))

;================================================================================
;Parse
;Main Function
(define parse
  (lambda (p_stack input)
    (let ((stack_head (car p_stack)) (input_head (car input)))
      (cond
        ((string=? input_head "") (parse p_stack (cdr input)))
        ((or (and (string=? stack_head "id") (id? input_head)) (string=? stack_head input_head))
         (if (and (string=? stack_head "$$") (string=? input_head "$$"))
             (begin
               (display " ")
               (display input_head))
             (begin
               (display " ")
               (display input_head)
               (parse (cdr p_stack) (cdr input)))))
        (else(parse (table p_stack input_head) input))))))
        
(parse (list "program" "$$") input)