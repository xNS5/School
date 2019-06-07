;Michael Kennedy
;CS301
;Assignment 2
;This function parses an inputted text file and determines whether the input
;matches the LL1 grammar.
;Notes: numbers instead of non-terminals

;================================================================================
;Defining non-terminals
;================================================================================
(define (prod-lookup num)
  (cond
    ((= num 1) "program")
    ((or (= num 2) (= num 3)) "stmt_list")
    ((<= 4 num 6) "stmt")
    ((= num 7) "expr")
    ((or (= num 8) (= num 9)) "term_tail")
    ((= num 10) "term")
    ((or (= num 11) (= num 12)) "factor_tail")
    ((<= 13 num 15) "factor")
    ((or (= num 16) (= num 17)) "add_op")
    ((or (= num 18) (= num 19)) "mult_op")

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
(define input (map list->string (stitcher reader)))

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
;Checks to see if a letter is a valid Id. Otherwise returns false.
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
    (let ((st_head (car stk)))
      (if (not (string=? st_head "$$"))
          (begin
            (display (string-append st_head " ") parse-file)
            (print-stack (cdr stk)))
          (display (string-append st_head "\r\n") parse-file)))))

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
(define (print-all stk inpt val)
  (print-stack stk)
  (print-input inpt)
  (print-predict val))

;Swap
;Syntax: string
;If the value of x is an id or an integer, this function returns "id" or "number" which gets pused to the parse stack.
(define swap
  (lambda (token)
    (cond
      ((id? token) "id")
      ((integer? (string->number token)) "number")
      (else token))))

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
    (let ((stack_head (car p_stack)) (input_head (car input))) ;Defining the tops of the two stacks. 
      (cond
        ((string=? stack_head "error") (error-handler (cdr p_stack) input_head)) ;Checks to see if the top of the parse stack is "error" meaning there was an error somewhere. 
        ((or (and (string=? stack_head "id") (id? input_head)) (and (string=? stack_head "number") (integer? (string->number input_head))) (string=? stack_head input_head))
         (print-stack p_stack)
         (print-input input_head)
         (if (and (string=? stack_head "$$") (string=? input_head "$$")) ;Checks to see if both stacks are empty.
             (close-ports)
             (begin
               (display (string-append (string-append "match " (swap input_head)) "\r\n") comment-file) ;calls 'swap', returns 'id' or 'number' if passed an id or a number, otherwise returns input_head
               (parse (cdr p_stack) (cdr input)))))
        (else (parse (table p_stack input_head) input))))))

(parse (list (program) ($$)) input)