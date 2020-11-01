format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        string_extra_output       db "Want to have extra output?", 10, 0
        string_yes_no             db "1 -- yes, else -- no: ", 0
        string_element            db "%d'th element is %d.", 10, 0
        string_amount_of_elements db 10, "The number of elements of the Padovan sequence: %d.", 10, 0
        string_max_element        db "The max element is: %d.", 0

        format_number             db "%d", 0

        is_yes                    dd ?
        counter                   dd 3

        number1                   dd 1
        number2                   dd 1
        number3                   dd 1

        NULL = 0



; ----------------------------------------------------------------



section '.code' code readable executable
        start:                    call  input
                                  call  process

                                  push  NULL
                                  call  [ExitProcess]

; ----------------------------------------------------------------

        input:                    push  string_extra_output
                                  call  [printf]
                                  push  string_yes_no
                                  call  [printf]
                                  add   esp, 8

                                  push  is_yes
                                  push  format_number
                                  call  [scanf]
                                  add   esp, 8

                                  ret

; ----------------------------------------------------------------

        process:                  inc   [counter]

                                  xor   ecx, ecx
                                  add   ecx, [number1]
                                  add   ecx, [number2]

                                  mov   ebx, [number2]
                                  mov   [number1], ebx

                                  mov   ebx, [number3]
                                  mov   [number2], ebx

                                  mov   [number3], ecx

                                  mov   ecx, 1
                                  cmp   [is_yes], ecx
                                  jne   comparison

                                  push  [number3]
                                  push  [counter]
                                  push  string_element
                                  call  [printf]
                                  add   esp, 12

        comparison:               mov   ebx, [number2]
                                  mov   ecx, [number3]
                                  cmp   ecx, ebx
                                  jge   process        ; Jump if 1'th more or equal than 2'th argument.
                                                       ; So if 1'th less then overflow has been occured.

                                  dec   [counter]
                                  push  [counter]
                                  push  string_amount_of_elements
                                  call  [printf]
                                  add   esp, 8

                                  push  [number2]
                                  push  string_max_element
                                  call  [printf]
                                  add   esp, 8

                                  call  [getch]

                                  ret



; ----------------------------------------------------------------



section '.idata' import data readable
        library                   kernel,      "kernel32.dll",\
                                  msvcrt,      "msvcrt.dll"

        import                    kernel,\
                                  ExitProcess, "ExitProcess"

        import                    msvcrt,\
                                  printf,      "printf",\
                                  getch,       "_getch",\
                                  scanf,       "scanf"