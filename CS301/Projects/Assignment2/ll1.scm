;Michael Kennedy
;CS301
;Assignment 2
;This function parses an inputted text file and determines whether the input
;matches the LL1 grammar.

;================================================================================
;Lookup tables
;These functions take in an integer and return a string associated with that integer.
;================================================================================

;Non-term-lookup
;Syntax: int
;This function is only called when the program is printing to the parse stack text file.
(define (non-term-lookup term)
  (cond
    ((= term 1) "program")
    ((<= 2 term 3) "stmt_list")
    ((<= 4 term 6) "stmt")
    ((= term 7) "expr")
    ((<= 8 term 9) "term_tail")
    ((= term 10) "term")
    ((<= 11 term 12) "factor_tail")
    ((<= 13 term 15) "factor")
    ((<= 16 term 17) "add_op")
    ((<= 18 term 19) "mult_op")))

;Token-lookup
;Syntax: int
;This function is only called when the program is printing to the comment text file. 
(define (token-lookup token)
  (cond
    ((= token 20) "id")
    ((= token 21) "number")
    ((= token 22) "read")
    ((= token 23) "write")
    ((= token 24) ":=")
    ((= token 25) "(")
    ((= token 26) ")")
    ((= token 27) "+")
    ((= token 28) "-")
    ((= token 29) "*")
    ((= token 30) "/")
    ((= token 31) "$$")))

;================================================================================
;File operations
;================================================================================

;Opening 3 file ports
(define parse-file (open-output-file #:mode 'text #:exists 'replace "parsestack"))
(define inputstream-file (open-output-file #:mode 'text #:exists 'replace "inputstream"))
(define comment-file (open-output-file #:mode 'text #:exists 'replace  "comment"))
(display "intial stack contents\r\n" comment-file)

;Read data
;Reads in data from a text file and constructs a list of chars.
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
;If the character 'head' is a #\space or a #\newline character, it cons the reverse of concat with a recursive call to iter with the cdr of the input and an
;empty concat list. If the character 'head' is a #\space or a #\newline character AND concat is null, which means that there is more than 1 #\space or #\newline
;character, it loops until it finds something to add to 'concat'.
(define stitcher
  (lambda (str)
    (let iter ((lst str) (concat '()))
      (cond
        ((not (pair? lst))
         (list (reverse concat)))
        (else
         (let ((head (car lst)))
           (cond
             ((and (or (char=? head #\space) (char=? head #\newline)) (null? concat)) (iter (cdr lst) '()))
             ((or (char=? head #\space) (char=? head #\newline)) (cons (reverse concat) (iter (cdr lst) '())))
             (else (iter (cdr lst) (cons (car lst) concat))))))))))

;Driver function for stitcher
;Sytax: input from 'reader' function
;Takes the output from 'reader' which spits out a char list of characters from the input text file and converts the resulting list to a list of symbols.
;My reasoning for this is because instead of evaluating strings, all I would have to do is determine if they point to the same place in memory.
(define input (map string->symbol (map list->string (stitcher reader))))

;================================================================================
;Helper functions
;================================================================================

;Get-term -- driver for lookup functions
;Syntax: int
;This function determines if a number corresponds to an input token or a non-terminal.
;Tokens are values between 20 and 31, non-terminals are values 1 to 19. 
(define (get-term x)
  (cond
    ((<= 1 x 19) (non-term-lookup x))
    ((<= 20 x 31) (token-lookup x))
    (else 99)))

;Push
;Syntax: int or list
;If val or stack is null, goes to the error handler.
;If val is a pair, appends val to stack. Otherwise it puts val in a list and appends to stack. 
(define (push val stack)
  (cond
    ((or (null? stack) (null? val)) (error-handler stack val))
    ((pair? val) (append val stack))
    (else (append (list val) stack))))

;id?
;Syntax: symbol
;Checks to see if a letter is a valid id.
(define (id? val)
  (let ((val (symbol->string val)))
        (if (= 1 (string-length val))
            (let ((char_val (car (string->list val))))
              (if (and (char? char_val) (char-upper-case? char_val) (char-alphabetic? char_val)) #t #f))
            #f)))

;Print-stack
;Syntax: list
;Prints the current parse stack to the parse file. 
(define print-stack
  (lambda (stk)
    (let* ((head (car stk)) (stk_head (get-term head)))
      (if (not (= head 31))
          (begin
            (display (string-append stk_head " ") parse-file)
            (print-stack (cdr stk)))
          (display (string-append stk_head "\r\n") parse-file)))))

;Print-predict
;Syntax: number
;Prints the production number to the 'comment' file.
(define (print-predict inp)
  (display (string-append (string-append "predict " (number->string inp)) "\r\n") comment-file))

;Print-input
;Syntax: string
;Prints the current top of the input stack to the inputstream text file
(define (print-input inp)
  (display (string-append (symbol->string inp) "\r\n") inputstream-file))

;Print-all
;Syntax: list, symbol, int
;Driver function for print functions. Passes the stack, input token, and production number to their respective functions.
(define (print-all stk inpt val)
  (print-stack stk)
  (print-input inpt)
  (print-predict val))

;Swap
;Syntax: string
;If the value of x is an id or an integer, this function returns "id" or "number" if token is an id or a number, otherwise it converts the symbol to a string.
(define swap
  (lambda (token)
    (cond
      ((id? token) "id")
      ((num? token) "number")
      (else (symbol->string token)))))

;num?
;Syntax: string
;Checks to see if a string can be converted to an integer
(define (num? int)
  (integer? (string->number (symbol->string int))))

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
;Terminals
;This function returns a number associated with a given input token. If 'token' isn't a member of the
;symbols accepted by this function it returns the symbol 'error. 
;================================================================================

;Token-num
;Syntax: symbol
(define (token-num token)
  (cond
    ((id? token) 20)
    ((num? token) 21)
    ((eq? token 'read) 22)
    ((eq? token 'write) 23)
    ((eq? token ':=) 24)
    ((eq? token '|(|) 25)
    ((eq? token '|)|) 26)
    ((eq? token '+) 27)
    ((eq? token '-) 28)
    ((eq? token '*) 29)
    ((eq? token '/) 30)
    ((eq? token '$$) 31)
    (else 'error)))

;================================================================================
;Parse Table Functions
;The table function looks up the current non-terminal and moves to its corresponding function.
;p_stack is the parse stack, input_head is the top of the input list, token is the numeric value of the input head.
;If at any point the table is given an input that is an invalid token it returns 99, which is the error flag.
;================================================================================

;table
;Syntax: list, string, int
(define table
  (lambda (p_stack input_head token)
    (let ((head (car p_stack)))
      (cond
        ((= head 1) (program p_stack input_head token))
        ((= head 2) (stmt_list p_stack input_head token))
        ((= head 4) (stmt p_stack input_head token))
        ((= head 7) (expr p_stack input_head token))
        ((= head 8) (term_tail p_stack input_head token))
        ((= head 10) (term p_stack input_head token))
        ((= head 11) (factor_tail p_stack input_head token))
        ((= head 13) (factor p_stack input_head token))
        ((= head 16) (add_op p_stack input_head token))
        ((= head 18) (mult_op p_stack input_head token))
        (else (push 99 p_stack))))))


;=====================================
;Productions
;=====================================

;1
;If the program sees an id (20), read (22), write (23), or $$ (31) it prints out the stack, input head, and the production number.
;Then it pushes stmt_list (2) to the stack and returns it. 
(define program
  (lambda (stk head token) 
    (cond
      ((or (= token 20) (<= 22 token 23) (= token 31))
       (print-all stk head 1) ;write production 1
       (push 2 (cdr stk)))
      (else (push 99 stk)))))
    
;2-3
;If the program sees an id, read, or write it prints out the stack, input head, and production number and pushes
;stmt (4) and stmt_list (2) to the stack and returns it.
;If the program sees $$ (31) it returns the cdr of the stack. 
(define stmt_list
  (lambda (stk head token)
    (cond
      ((or (= token 20) (<= 22 token 23))
       (print-all stk head 2) ;production 2
       (push (list 4 2) (cdr stk)))
      ((= token 31)
       (print-all stk head 3) ;production 3
       (cdr stk))
      (else (push 99 stk)))))
  
;4-6
;If the program sees id (20), it prints out the stack, input head, and production number then pushes id (20), := (24), and expr (7) to the stack.
;The same thing happens with production 5 and 6, except read (22), id (20) and write (23), expr (7) get pushed to the stack respectively.
(define stmt
  (lambda (stk head token)
    (cond
      ((= token 20)
       (print-all stk head 4) ;production 4
       (push (list 20 24 7) (cdr stk)))
      ((= token 22)
       (print-all stk head 5) ;production 5 
       (push (list 22 20) (cdr stk)))
      ((= token 23)
       (print-all stk head 6) ;production 6
       (push (list 23 7) (cdr stk)))
      (else (push 99 stk)))))
       
;7
;If the program sees id (20), number (21), ( (25), it prints out the stack, input head, and production number.
;It then pushes term (10), and term_tail (8) to the stack. 
(define expr
  (lambda (stk head token)
    (cond
      ((or (<= 20 token 21) (= token 25))
         (print-all stk head 7) ;production 7
         (push (list 10 8) (cdr stk)))
      (else (push 99 stk)))))

;8 and 9
;If this program sees + (27) or - (28), it prints out the stack, input head, and production number.
;It then pushes add_op (16), term (10), and term_tail (8) to the stack or the cdr of the stack if it sees
;$$ (31), number (21), read (22), write (23), or ) (26)
(define term_tail
  (lambda (stk head token)
    (cond
      ((<= 27 token 28)
       (print-all stk head 8) ;production 8 
       (push (list 16 10 8) (cdr stk)))
      ((or (= token 20) (<= 22 token 23) (= token 26) (= token 31))
       (print-all stk head 9) ;production 9
       (cdr stk))
      (else (push 99 stk)))))

;10
;If this program sees id (20), number (21), or ( (25),  it prints out the stack, current input head, and production number.
;Then, factor (13) and factor_tail (11) get pushed to the stack. 
(define term
  (lambda (stk head token)
    (cond
      ((or (<= 20 token 21) (= token 25))
       (print-all stk head 10) ;production 10
       (push (list 13 11) (cdr stk)))
      (else (push 99 stk)))))

;11 and 12
;If this program sees * (29) or / (30), it prints out the stack, input head, and production number then pushes mult_op (18), factor (13), and factor_tail (11) to the stack.
;If this program sees id (20), read (22), write (23), ) (26), + (27), or - (28) it returns the cdr of the stack. 
(define factor_tail
  (lambda (stk head token)
    (cond
    ((<= 29 token 30)
     (print-all stk head 11) ;production 11
     (push (list 18 13 11) (cdr stk)))
    ((or (= token 20) (<= 22 token 23) (<= 26 token 28) (= token 31))
     (print-all stk head 12) ;production 12
     (cdr stk))
    (else (push 99 stk)))))

;13 14 and 15
;If this program sees ( (25) it prints out the stack, input head, and production number and pushes ( (25), expr (7), and ) 26 to the stack.
;If this sees id (20) or number (21), it returns id (20) and number (21) respectively.
(define factor
  (lambda (stk head token)
    (cond
      ((= token 25)
       (print-all stk head 13) ;production 13
       (push (list 25 7 26) (cdr stk)))
      ((= token 20)
       (print-all stk head 14) ;production 14
       (push 20 (cdr stk)))
      ((= token 21)
       (print-all stk head 15) ;production 15
       (push 21 (cdr stk)))
      (else (push 99 stk)))))

;16 and 17
;If the program sees an addition or subtraction operation, it returns + (27) or - (28) respectively.
(define add_op
  (lambda (stk head token)
    (cond
      ((= token 27)
       (print-all stk head 16) ;production 16
       (push 27 (cdr stk))) 
      ((= token 28)
       (print-all stk head 17) ;production 17
       (push 28 (cdr stk)))
      (else (push 99 stk)))))

;18 and 19
;If this program sees a multiplication or division operation, it returns * (29) or / (30) respectively.
(define mult_op
  (lambda (stk head token)
    (cond
      ((= token 29)
       (print-all stk head 18) ;production 18
       (push 29 (cdr stk)))
      ((= token 30)
       (print-all stk head 19) ;production 19
       (push 30 (cdr stk)))
      (else (push 99 stk)))))

;================================================================================
;Main Function
;This function takes in a list containing 1 31 (which are the numbers associated with "program" and "$$") and another list that is the text input.
;First, the parse stack and the text file input gets passed to 'parse'. If the input isn't valid, a symbol 'error is returned, otherwise a number is returned.
;Then it attempts to match the top of the parse stack and the input list. If it doesn't match, the stack, the top of the input list, and the number associated with that
;input token is passed to the table function. That function returns a parse stack and that gets passed to the iter loop.
;If it is a match, the cdr of both input files is passed to parse. 
;================================================================================

;Parse
;Syntax: list, list
(define parse
  (lambda (p_stack input)
    (let* ((input_head (car input)) (num_head (token-num input_head)))
      (if (not (integer? num_head))
          (error-handler p_stack input_head)
          (begin
            (let iter ((p_stack p_stack))
              (let ((stack_head (car p_stack)))
              (cond
                ((= stack_head 99) (error-handler (cdr p_stack) input_head)) ; 99 indicates an error
                ((= stack_head num_head)
                 (print-stack p_stack)
                 (print-input input_head)
                 (if (and (= stack_head 31) (= num_head 31)) ; 31 is $$
                     (close-ports)
                     (begin ;swap determines if the input is an id or a number, otherwise it just returns the input_head.
                       (display (string-append (string-append "match " (swap input_head)) "\r\n") comment-file)
                       (parse (cdr p_stack) (cdr input)))))
                (else (iter (table p_stack input_head num_head)))))))))))

(parse (list 1 31) input)