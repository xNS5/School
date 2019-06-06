;Write program that just writes p_stack to file
;Notes: Check out Splitter and make sure everything makes sense.

;================================================================================
;Opening 3 file ports
(define parse-file (open-output-file #:mode 'text #:exists 'replace "parsestack"))
(define inputstream-file (open-output-file #:mode 'text #:exists 'replace "inputstream"))
(define comment-file (open-output-file #:mode 'text #:exists 'replace  "comment"))

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
;If str is a pair, then it grabs the car of list and assigns it the variable head.
;If the character 'head' is a #\space or a #\newline character, it recursively calls 'loop' with the cdr of str
;along with an empty symbol for 't'.
;Initially I checked to make sure that no matter where a ( or ) character was it would be able to be popped from the stack
;without any trailing characters, so I'm not checking to make sure that there isn't a space in between a open or closing paren
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
;This function pushes a value to the top of the parse stack. 
(define (push val stack)
    (if (or (null? stack) (null? val))
        (cond
          ((null? stack) "Error:  Null Stack")
          ((null? val) "Error: Null Value"))
        (append (list val) stack)))

;id?
;Syntax: input
;This function checks to see if a letter is a valid Id, which is a capital letter. If it's a capital upper case letter
;'val' is a valid id. Otherwise it returns false. 
(define (id? val)
  (if (= 1 (string-length val))
      (begin
        (let ((char_val (car (string->list val))))
              (if (and (char? char_val) (char-upper-case? char_val) (char-alphabetic? char_val))
                  #t
                  #f)))
      #f))

;Prints the parse stack to the text file.
;Syntax: stack
(define print-stack
  (lambda (x)
    (let ((st_head (car x)))
      (if (not (string=? st_head "$$"))
          (begin
            (display (string-append st_head " ") parse-file)
            (print-stack (cdr x)))
          (display (string-append st_head "\r\n") parse-file)))))

;Prints the prediction for each production to the comment text file.
;Syntax: string number
(define (print-predict inp)
    (display (string-append (string-append "predict " inp) "\r\n") comment-file))

;Prints the current top of the input stack to the inputstream text file
;Syntax: inp (top of the input file)
(define (print-input inp)
  (display (string-append inp "\r\n") inputstream-file))

;Passes the stack, input token, and production number to their respective functions. 
(define print-all
  (lambda (stk inpt val)
  (print-stack stk)
  (print-input inpt)
  (print-predict val)))

;Prints out the "swap _" string to text file
;Syntax: string
(define swap
  (lambda (x)
    (cond
      ((id? x) "id")
      ((integer? (string->number x)) "number")
      (else x))))

;Closes the output ports
;Syntax: none
(define (close-ports)
  (close-output-port parse-file)
  (close-output-port inputstream-file)
  (close-output-port comment-file))

;Error handler
;Syntax: parse stack and the current input token.
;Once it prints out the stack and the token, it calls the close-ports function which closes all of the open file ports.
(define (error-handler p_stack token)
  (print-stack p_stack)
  (print-input token)
  (display (string-append "syntax error\r\n") comment-file)
  (close-ports))

;================================================================================
;Parse Table Functions
;The table function looks up the current non-terminal and moves to it's corresponding function.
;If the current token is a valid production, it returns nonterminals pushed to the stack.
;If it isn't a valid production, it returns "error" pushed to the stack and the program quits. 
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
        ((string=? head "mult_op") (mult_op p_stack token))
        (else (error-handler p_stack token))))))

;Productions

;1
(define program
  (lambda (stk token) 
    (cond
    ((or (id? token) (string=? token "read") (string=? token "write") (string=? token "$$"))
     (begin
       (print-all stk token "1") ;write production 1
       (push "stmt_list" (cdr stk))))
     (else (push "error" stk)))))
    
;2
(define stmt_list
  (lambda (stk token)
     (cond
       ((or (id? token) (string=? token "read") (string=? token "write"))
        (begin
        (print-all stk token "2") ;production 2
        (push "stmt" stk)))
       ((string=? token "$$")
        (begin
        (print-all stk token "3") ;production 3
        (cdr stk)))
       (else (push "error" stk)))))
  
;4-6
(define stmt
  (lambda (stk token)
    (cond
      ((id? token)
       (begin
         (print-all stk token "4") ;production 4
         (push "id" (push ":=" (push "expr" (cdr stk)))))) 
      ((string=? token "read")
       (begin
         (print-all stk token "5") ;production 5 
         (push "read" (push "id" (cdr stk)))))
      ((string=? token "write")
       (begin
         (print-all stk token "6") ;production 6
         (push "write" (push "expr" (cdr stk)))))
      (else (push "error" stk)))))
       
;7
(define expr
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token)))
       (begin
         (print-all stk token "7") ;production 7
         (push "term" (push "term_tail" (cdr stk)))))
      (else (push "error" stk)))))

;8 and 9
(define term_tail
  (lambda (stk token)
    (cond
      ((or (string=? token "+") (string=? token "-"))
       (begin
         (print-all stk token "8") ;production 8 
         (push "add_op" (push "term" (push "term_tail" (cdr stk))))))
      ((or (string=? token "$$") (id? token) (string=? token "read") (string=? token "write") (string=? token ")"))
       (begin
         (print-all stk token "9") ;production 9
         (cdr stk)))
      (else (push "error" stk)))))

;10
(define term
  (lambda (stk token)
    (cond
      ((or (id? token) (string=? token "(") (integer? (string->number token)))
       (begin
         (print-all stk token "10") ;production 10
         (push "factor" (push "factor_tail" (cdr stk)))))
      (else (push "error" stk)))))

;11 and 12
(define factor_tail
  (lambda (stk token)
    (cond
    ((or (string=? token "*") (string=? token "/"))
     (begin
       (print-all stk token "11") ;production 11
       (push "mult_op" (push "factor" (push "factor_tail" (cdr stk))))))
    ((or (string=? token "$$")(id? token) (string=? token "read") (string=? token "write") (string=? token ")") (string=? token "+") (string=? token "-"))
      (begin
       (print-all stk token "12") ;production 12
       (cdr stk)))
    (else (push "error" stk)))))

;13 14 and 15
(define factor
  (lambda (stk token)
    (cond
      ((string=? token "(")
       (begin
       (print-all stk token "13") ;production 13
       (push "(" (push "expr" (push ")" (cdr stk))))))
      ((id? token)
       (begin
         (print-all stk token "14") ;production 14
         (push "id" (cdr stk))))
      ((integer? (string->number token))
       (begin
         (print-all stk token "15") ;production 15
         (push "number" (cdr stk))))
      (else (push "error" stk)))))

;16 and 17
(define add_op
  (lambda (stk token)
    (cond
      ((string=? token "+")
       (begin
         (print-all stk token "16") ;production 16
         (push "+" (cdr stk)))) 
      ((string=? token "-")
       (begin
         (print-all stk token "17") ;production 17
         (push "-" (cdr stk)))) 
      (else (push "error" stk)))))

;18 and 19
(define mult_op
  (lambda (stk token)
    (cond
      ((string=? token "*")
       (begin
         (print-all stk token "18") ;production 18
         (push "*" (cdr stk)))) 
      ((string=? token "/")
       (begin
         (print-all stk token "19") ;production 19
         (push "/" (cdr stk))))
      (else (push "error" stk)))))

;================================================================================
;Parse
;Main Function
;This function takes in a list containing "program" "$$" and another list that is the text input.
;It first grabs the heads of the two lists, and makes sure that they aren't any values that indicate that
;there was an error along the way. If the program encounters some kind of error in one of the productions, "error" gets pushed to the parse stack.
;If the stack head value is "error" it pushes the cdr of the stack to the error handler, which pushes all of the values to their respective text files
;and closes the file ports.
;If both stack heads equal "$$", which means both stacks are empty, it pushes "$$" to the text files and closes the output port.
;If the stack head is "id" and the input head is a capital letter, OR if the stack head is "number" and the input head is a number, or if the two heads equal each other,
;it prints out the stack to the parsestack file, the input head to the inputstream file, and pushes "match _" to the comment file. Swap is a function that checks
;for whether the input is a "number" or an "id" in which case it will output "match id" or "match number" to the comment file. After that's done, it calls parse with
;the cdr of both stacks -- meaning that there was a match and the program should move onto the next input.
;If the top of the parse stack is a non-terminal, it looks up the corresponding production to the current parse stack head and the input token head and passes it to
;the main function again along with the input list. 
(define parse
  (lambda (p_stack input)
    (let ((stack_head (car p_stack)) (input_head (car input))) ;Defining the tops of the two stacks. 
      (cond
        ((string=? stack_head "error") (error-handler (cdr p_stack) input_head)) ;If there is an error of some sort, "error" gets pushed to the parse stack
        ((and (string=? stack_head "$$") (string=? input_head "$$")) ;Checks to see if both stacks are empty. 
         (begin
           (print-stack p_stack)
           (print-input input_head)
           (close-ports))) ;close-ports is a function that closes all of the open file ports.
        ((or (and (string=? stack_head "id") (id? input_head)) (and (string=? stack_head "number") (integer? (string->number input_head))) (string=? stack_head input_head))
          (begin
            (print-stack p_stack)
            (print-input input_head)
            (display (string-append (string-append "match " (swap input_head)) "\r\n") comment-file)
            (parse (cdr p_stack) (cdr input))))
        (else (parse (table p_stack input_head) input))))))

(parse (list "program" "$$") input)