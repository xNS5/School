;Write program that just writes p_stack to file
;Notes: Check out Splitter and make sure everything makes sense.

;================================================================================
;Opening 3 file ports
(define parse-file (open-output-file  #:exists 'append "parsestack"))
(define inputstream-file (open-output-file #:exists 'append "inputstream"))
(define comment-file (open-output-file #:exists 'append "comment"))

(display "intial stack contents\r\n" comment-file)

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
;It treats the #\( character similar to a #\space or #\newline character. If it detects either '(' or ')' then
;it loops again and creates a separate entry in a list. 
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

;================================================================================
;Helper functions

;Push
;Syntax: val, stack
;Stack is a list, val is not a list
(define (push val stack)
    (if (or (null? stack) (null? val))
        (cond
          ((null? stack) "Error:  Null Stack")
          ((null? val) "Error: Null Value"))
        (append (list val) stack)))

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

;Prints the stack to the text file
;Syntax: stack
(define print-stack
  (lambda (x)
    (let ((st_head (car x)))
      (if (not (string=? st_head "$$"))
          (begin
            (display (string-append st_head " ") parse-file)
            (print-stack (cdr x)))
          (display (string-append st_head "\r\n") parse-file)))))

;Prints the predictions for each production
;Syntax: string number
(define (print-predict inp)
    (display (string-append (string-append "predict " inp) "\r\n") comment-file))

;Prints the current top of the input stack
;Syntax: inp (top of the input file)
(define (print-input inp)
  (display (string-append inp "\r\n") inputstream-file))

;Error handler
;Syntax: parse stack and the current head token
(define (error-handler p_stack token)
  (print-stack p_stack)
  (display token inputstream-file)
  (display (string-append "syntax error: " (car p_stack)) parse-file))
  

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
  (lambda (stk token) 
    (cond
    ((or (id? token) (string=? token "read") (string=? token "write") (string=? token "$$"))
     (begin
       (print-stack stk)
       (print-predict "1") ;write production 1
     (push "stmt_list" (cdr stk))))
     (else (push "error" stk)))))
    
;2
(define stmt_list
  (lambda (stk token)
     (cond
       ((or (id? token) (string=? token "read") (string=? token "write"))
        (begin
        (print-stack stk)
        (print-predict "2") ;production 2
        (push "stmt" stk)))
       ((string=? token "$$")
        (begin
        (print-stack stk)
        (print-predict "3") ;production 3
        (cdr stk)))
       (else (push "error" stk)))))


;4-6
(define stmt
  (lambda (stk token)
    (cond
      ((id? token)
       (begin
         (print-stack stk)
         (print-predict "4") ;production 4
         (push "id" (push ":=" (push "expr" (cdr stk)))))) 
      ((string=? token "read")
       (begin
         (print-stack stk)
         (print-predict "5") ;production 5 
         (push "read" (push "id" (cdr stk)))))
      ((string=? token "write")
       (begin
         (print-stack stk)
         (print-predict "6") ;production 6
         (push "write" (push "expr" (cdr stk)))))
      (else (push "error" stk)))))
       
;7
(define expr
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token)))
       (begin
         (print-stack stk)
         (print-predict "7")
         (push "term" (push "term_tail" (cdr stk)))))
      (else (push "error" stk)))))

;Issue in 8
;8 and 9
(define term_tail
  (lambda (stk token)
    (cond
      ((or (string=? token "+") (string=? token "-"))
       (begin
         (print-stack stk)
         (print-predict "8") ;production 8
         (push "add_op" (push "term" stk))))
      ((or (string=? token "$$") (id? token) (string=? token "read") (string=? token "write") (string=? token ")"))
       (begin
         (print-stack stk)
         (print-predict "9")
         (cdr stk))); production 9
      (else (push "error" stk)))))

;10
(define term
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token)))
       (begin
         (print-stack stk)
         (print-predict "10") ;production 10
         (push "factor" (push "factor_tail" (cdr stk)))))
      (else (push "error" stk)))))

;11 and 12
(define factor_tail
  (lambda (stk token)
    (cond
    ((or (string=? token "*") (string=? token "/"))
     (begin
       (print-stack stk)
       (print-predict "11") ;production 11
       (push "mult_op" (push "factor" (push "factor_tail" (cdr stk))))))
    ((or (string=? token "$$")(id? token) (string=? token "read") (string=? token "write") (string=? token ")") (string=? token "+") (string=? token "-"))
      (begin
       (print-stack stk)
       (print-predict "12")
       (cdr stk)));production 12
    (else (push "error" stk)))))

;13 14 and 15
(define factor
  (lambda (stk token)
    (cond
      ((string=? token "(")
       (begin
       (print-stack stk)
       (print-predict "13") ;production 13
       (push "(" (push "expr" (push ")" (cdr stk))))))
      ((id? token)
       (begin
         (print-stack stk)
         (print-predict "14") ;production 14
         (push "id" (cdr stk))))
      ((integer? (string->number token))
       (begin
         (print-stack stk)
         (print-predict "15")  ;production 15
         (push token (cdr stk))))
      (else (push "error" stk)))))

;16 and 17
(define add_op
  (lambda (stk token)
    (cond
      ((string=? token "+")
       (begin
         (print-stack stk)
         (print-predict "16") ;production 16
         (push "+" (cdr stk)))) 
      ((string=? token "-")
       (begin
         (print-stack stk)
         (print-predict "17") ;production 17
         (push "-" (cdr stk)))) 
      (else (push "error" stk)))))

;18 and 19
(define mult_op
  (lambda (stk token)
    (cond
      ((string=? token "*")
       (begin
         (print-stack stk)
         (print-predict "18") ;production 18
         (push "*" (cdr stk)))) 
      ((string=? token "/")
       (begin
         (print-stack stk)
         (print-predict "19") ;production 19
         (push "/" (cdr stk))))
      (else (push "error" stk)))))

;================================================================================
;Parse
;Main Function
(define parse
  (lambda (p_stack input)
    (let ((stack_head (car p_stack)) (input_head (car input)))
      (cond
        ((string=? stack_head "error") (error-handler p_stack input_head))
        ((or (and (string=? stack_head "id") (id? input_head)) (string=? stack_head input_head))
              (if (and (string=? stack_head "$$") (string=? input_head "$$"))
                  (begin
                    (print-stack p_stack)
                    (display " ")
                    (display input_head)) ;Close input ports here. 
                   (begin
                     (print-stack p_stack)
                     (display input_head)
                     (display " ")
                     (display (string-append (string-append "match " input_head) "\r\n") comment-file)
                     (parse (cdr p_stack) (cdr input)))))
        (else(parse (table p_stack input_head) input))))))
        
(parse (list "program" "$$") input)