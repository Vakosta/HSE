format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        ; Strings for console
        strSize db 'Enter size of array: ', 0
        strEl   db 'Enter %d element of array: ', 10, 0

        fSize   db '%d', 0

        size    Rw 1
        temp    Rw 1

        ; Array elements
        str0    db 'Array0', 0
        str1    db 'Array1', 0

        ; Array
        array   rd 4

        NULL = 0


section '.code' code readable executable
        input:
                push strSize
                call [printf]

                push size
                push fSize
                call [scanf]

                mov cx, [size]
                mov eax, 0
                add_element:

                        push eax
                        push strEl
                        call [printf]
                        pop eax
                        pop eax

                        call [getch]

                        loop add_element

                call [getch]

                push NULL
                call [ExitProcess]

        output:

        process:

        start:
                jmp input

                ; Array initialization
                mov     [array], str0
                mov     [array + 4], str1

                ; Output second element
                push    [array + 4]
                call    [printf]

                call    [getch]

                push    NULL
                call    [ExitProcess]


section '.idata' import data readable
        library kernel, 'kernel32.dll',\
                msvcrt, 'msvcrt.dll'

        import kernel,\
               ExitProcess, 'ExitProcess'

        import msvcrt,\
               printf, 'printf',\
               getch, '_getch',\
               scanf, 'scanf'