format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        ; Strings for console
        amount_of_elements db 'The number of elements of the Padovan sequence: %d', 10, 0
        max_element        db 'The max element is: %d', 0

        counter            dd 2

        number1            dd 1
        number2            dd 1
        number3            dd 1

        NULL = 0


; ----------------------------------------------------------------


section '.code' code readable executable
        start:             call    process

        process:           inc     [counter]

                           xor     ecx, ecx
                           add     ecx, [number1]
                           add     ecx, [number2]

                           mov     ebx, [number2]
                           mov     [number1], ebx

                           mov     ebx, [number3]
                           mov     [number2], ebx

                           mov     [number3], ecx

                          ;push    [number3]
                          ;push    str_elem
                          ;call    [printf]

                           mov     ebx, [number2]
                           mov     ecx, [number3]
                           cmp     ecx, ebx
                           jge     process

                           push    [counter]
                           push    amount_of_elements
                           call    [printf]

                           push    [number2]
                           push    max_element
                           call    [printf]

                           call    [getch]

                           push    NULL
                           call    [ExitProcess]


; ----------------------------------------------------------------


section '.idata' import data readable
        library            kernel, 'kernel32.dll',\
                           msvcrt, 'msvcrt.dll'

        import             kernel,\
                           ExitProcess, 'ExitProcess'

        import             msvcrt,\
                           printf, 'printf',\
                           getch, '_getch',\
                           scanf, 'scanf'