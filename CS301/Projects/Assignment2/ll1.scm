;Michael Kennedy
;CS301
;Assignment 2
;This function parses an inputted text file and determines whether the input
;matches the LL1 grammar.
;Notes: numbers instead of non-terminals
;================================================================================
;Defining terminals and non-terminals
;================================================================================
(define (token-lookup x)
  (cond
    ((eq? x 'id) 20)
    ((integer? (string->number (symbol->string x))) 21) ;Essentially 'number
    ((eq? x 'read) 22)
    ((eq? x 'write) 23)
    ((eq? x ':=) 24)
    ((eq? x '|(|) 25)
    ((eq? x '|)|) 26)
    ((eq? x '+) 27)
    ((eq? x '-) 28)
    ((eq? x '*) 29)
    ((eq? x '/) 30)
    ((eq? x '$$) 31)))


(define (non-term-lookup x)
  (cond
    ((= x 1) 'program)
    ((<= 2 x 3) 'stmt_list)
    ((<= 4 x 6) 'stmt)
    ((= x 7) 'expr)
    ((<= 8 x 9) 'term_tail)
    ((= x 10) 'term)
    ((<= 11 x 12) 'factor_tail)
    ((<= 13 x 15) 'factor)
    ((<= 16 x 17) 'add_op)
    ((<= 18 x 19) 'mult_op)))

;================================================================================
;File operations
;================================================================================
;Opening 3 file ports
(define parse-file (open-output-file #:mode 'text #:exists 'replace "parsestack"))
(define inputstream-file (open-output-file #:mode 'text #:exists 'replace "inputstream"))
(define comment-file (open-output-file #:mode 'text #:exists 'replace  "comment"))

;Writing the "Initial stack contents" to the comment file.
(display "intial stack contents\r\n" comment-file)

;Read data
;Reads in data from a text file and constructs a list of chars.
;Using "read" didn't preserve the case of the letters, but making it into a char list did. 
(define reader
  (let ((port (open-input-file #:mode 'text "input")))
    (let iter ((val (read-char port)))
      (cond
        ((eof-object? val) (close-input-port port) '())
        (else (cons val (iter (read-char port))))))))

;Stitcher
;Syntax: list
;Creates a list of strings out of a list of characters. 
;If str isn't a pair, meaning it reached the end of the available input, it returns whatever is stored in concat.
;If the character 'head' is a #\space or a #\newline character, it cons the reverse of concat with
;a recursive call to iter with the input list and an empty concat list.
(define stitcher
  (lambda (str)
    (let iter ((lst str) (concat '()))
      (cond
        ((not (pair? lst))
         (list (reverse concat)))
        (else
         (let ((head (car lst)))
           (cond
             ((or (char=? head #\space) (char=? head #\newline)) (cons (reverse concat) (iter (cdr lst) '())))
             (else (iter (cdr lst) (cons (car lst) concat))))))))))

;Driver function for stitcher
;Sytax: input from 'reader' function
;Takes the output from 'reader' which spits out a char list of characters from the input text file
(define input (map string->symbol (map list->string (stitcher reader))))

;================================================================================
;Helper functions
;================================================================================

;Push
;Syntax: string, list
;If val or stack is null, goes to the error handler.
;If val is a pair, appends val to stack. Otherwise it puts val in a list and appends to stack. 
(define (push val stack)
  (cond
    ((or (null? stack) (null? val)) (error-handler stack val))
    ((pair? val) (append val stack))
    (else (append (list val) stack))))

;id?
;Syntax: string
;Checks to see if a letter is a valid id.
(define (id? val)
  (if (= 1 (string-length val))
      (let ((char_val (car (string->list val))))
        (if (and (char? char_val) (char-upper-case? char_val) (char-alphabetic? char_val)) #t #f))
      #f))

;Print-stack
;Syntax: list
;Prints the current parse stack to the parse file. 
(define print-stack
  (lambda (stk)
      (if (not (= st_head "$$"))
          (begin
            (display (string-append st_head " ") parse-file)
            (print-stack (cdr stk)))
          (display (string-append st_head "\r\n") parse-file))))

;Print-predict
;Syntax: number
;Prints the production number to the 'comment' file.
(define (print-predict inp)
  (display (string-append (string-append "predict " inp) "\r\n") comment-file))

;Print-input
;Syntax: string (top of the input file)
;Prints the current top of the input stack to the inputstream text file
(define (print-input inp)
  (display (string-append inp "\r\n") inputstream-file))

;Print-all
;Syntax: list, list, int
;Driver function for print functions. Passes the stack, input token, and production number to their respective functions.
(define (print-all stk inpt  val)
  (print-stack stk)
  (print-input inpt)
  (print-predict val))

;Get-Term driver
;Syntax: symbol
;This function determines if a number corresponds to an input token or a non-termina
(define (get-term x)
  (cond
    ((<= 1 x 19) (non-term-lookup x))
    ((<= 20 x 31) (token-lookup x))
    (else 99)))

;Swap
;Syntax: string
;If the value of x is an id or an integer, this function returns "id" or "number" which gets pused to the parse stack.
(define swap
  (lambda (token)
    (cond
      ((id? token) "id")
      ((integer? (string->number token)) "number")
      (else token))))

;num?
;Syntax: string
;Checks to see if a string can be converted to an integer
(define (num? int)
  (integer? (string->number int)))

;Close-ports
;Syntax: none
;Closes the output ports
(define (close-ports)
  (close-output-port parse-file)
  (close-output-port inputstream-file)
  (close-output-port comment-file))

;Error-handler
;Syntax: parse stack, input token, and error val
;Once it prints out the stack and the token, it calls the close-ports function which closes all of the open file ports.
(define (error-handler p_stack token)
   (print-stack p_stack)
   (print-input token)
   (display "syntax error\r\n" comment-file)
   (close-ports))
      
;================================================================================
;Parse Table Functions
;================================================================================
; 1 Program
; 2, 3 stmt_list
; 4, 5, 6 stmt
; 7 expr
; 8, 9 term_tail
; 10 term
; 11, 12  factor_tail
; 13, 14, 15 factor
; 16, 17 add_op
; 18, 19 mult_op

;Syntax: list, string
;The table function looks up the current non-terminal and moves to its corresponding function.
;If the current token is a valid production, it returns nonterminals pushed to the stack.
;If it isn't a valid production, "error" is pushed to the stack and the program goes to error-handler and quits. 
(define table
  (lambda (p_stack token)
    (let ((head (car p_stack)))
      (cond
        ((= head 1) (program p_stack token))
        ((<= 2 head 3) (stmt_list p_stack token))
        ((<= 4 head 6) (stmt p_stack token))
        ((= head 7) (expr p_stack token))
        ((<= 8 head 9) (term_tail p_stack token))
        ((= head 10) (term p_stack token))
        ((<= 11 head 12) (factor_tail p_stack token))
        ((<= 13 head 15) (factor p_stack token))
        ((<= 16 head 17) (add_op p_stack token))
        ((<= 18 head 19) (mult_op p_stack token))
        (else (push 99 p_stack))))))

;Productions
;1
(define program
  (lambda (stk token) 
    (cond
      ((or (= token 21) (= token 23) (= token 24) (= token 31))
       (print-all stk token (prod-lookup 1)) ;write production 1
       (push 2 (cdr stk)))
      (else (push 99 stk)))))
    
;2-3
(define stmt_list
  (lambda (stk token)
    (cond
      ((or (= token 20) (= token 22) (= token 23))
       (print-all stk token 2) ;production 2
       (push 2 (cdr stk)))
      ((= token 31)
       (print-all stk token 3) ;production 3
       (cdr stk))
      (else (push 99 stk)))))
  
;4-6
(define stmt
  (lambda (stk token)
    (cond
      ((= token 20)
       (print-all stk token 4) ;production 4
       (push (list 20 24 7) (cdr stk)))
      ((= token 22)
       (print-all stk token 5) ;production 5 
       (push (list 22 20) (cdr stk)))
      ((= token 23)
       (print-all stk token 6) ;production 6
       (push (list 23 7) (cdr stk)))
      (else (push 99 stk)))))
       
;7
(define expr
  (lambda (stk token)
    (cond
      ((or (<= 20 token 21) (= token 25))
       (begin
         (print-all stk token 7) ;production 7
         (push (list 10 8) (cdr stk))))
      (else (push 99 stk)))))

;8 and 9
(define term_tail
  (lambda (stk token)
    (cond
      ((or (= token 27) (= token 28))
       (print-all stk token 8) ;production 8 
       (push (list 16 10 8) (cdr stk)))
      ((or (= token 31) (= token 21) (<= 22 token 23) (= token 26))
       (print-all stk token 9) ;production 9
       (cdr stk))
      (else (push 99 stk)))))

;10
(define term
  (lambda (stk token)
    (cond
      ((or (= token 20) (= token 21) (= token 25))
       (print-all stk token 10) ;production 10
       (push (list 13 11) (cdr stk)))
      (else (push 99 stk)))))

;11 and 12
(define factor_tail
  (lambda (stk token)
    (cond
    ((<= 29 token 30)
     (print-all stk token 11) ;production 11
     (push (list 18 13 11) (cdr stk)))
    ((or (= token 26) (= token 20) (= token 21) (= token 23) (= token 31))
     (print-all stk token 12) ;production 12
     (cdr stk))
    (else (push 99 stk)))))

;13 14 and 15
(define factor
  (lambda (stk token)
    (cond
      ((= token 25)
       (print-all stk token 13) ;production 13
       (push (list 25 7 26) (cdr stk)))
      ((= token 20)
       (print-all stk token 14) ;production 14
       (push 20 (cdr stk)))
      ((= token 21)
       (print-all stk token 15) ;production 15
       (push 21 (cdr stk)))
      (else (push 99 stk)))))

;16 and 17
(define add_op
  (lambda (stk token)
    (cond
      ((= token 27)
       (print-all stk token 16) ;production 16
       (push 27 (cdr stk))) 
      ((= token 28)
       (print-all stk token 17) ;production 17
       (push 28 (cdr stk)))
      (else (push 99 stk)))))

;18 and 19
(define mult_op
  (lambda (stk token)
    (cond
      ((string=? token 29)
       (print-all stk token 18) ;production 18
       (push 29 (cdr stk)))
      ((string=? token 30)
       (print-all stk token 19) ;production 19
       (push 30 (cdr stk)))
      (else (push 99 stk)))))

;================================================================================
;Main Function
;================================================================================
;Parse
;Syntax: list, list
;This function takes in a list containing "program" "$$" and another list that is the text input.
;It first grabs the heads of the two lists, and makes sure that they aren't any values that indicate that there was an error somewhere in the parsing of the file.
;If the program encounters some kind of error in one of the productions, "error" gets pushed to the parse stack and the error-handler gets called and the program quits.
;If there is no indication of an error, the program evaluates the input
(define parse
  (lambda (p_stack input)
    (let ((stack_head (car p_stack)) (input_head (car input)) (num_head (token-lookup input_head))) ;Defining the tops of the two stacks. 
      (cond
        ((= stack_head 99) (error-handler (cdr p_stack) input_head)) ;Checks to see if the top of the parse stack is "error" meaning there was an error somewhere. 
        ((= stack_head num_head)
         (print-stack p_stack)
         (print-input input_head)
         (if (and (= stack_head 31) (= input_head 31)) ;Checks to see if both stacks are empty.
             (close-ports)
             (begin
               (display (string-append (string-append "match " (swap input_head)) "\r\n") comment-file) ;calls 'swap', returns 'id' or 'number' if passed an id or a number, otherwise returns input_head
               (parse (cdr p_stack) (cdr input)))))
        (else (parse (table p_stack input_head) input))))))