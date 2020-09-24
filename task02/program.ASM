format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        ; Strings for console
        strSize           db 'Enter size of array: ', 0
        strEl             db 'Enter %d element of array: ', 0
        strInputDone      db 'Array input done.', 10, 0

        fNumber db '%d', 0

        size    rd 1
        element Rw 1
        temp    Rw 1
        counter rd 1
        r_count rd 1
        index   rd 1

        ; Array elements
        str0    db 'Array0', 0
        str1    db 'Array1', 0

        ; Array
        array   rd 200

        NULL = 0


section '.code' code readable executable
        input:
                push strSize
                call [printf]

                push size
                push fNumber
                call [scanf]

                mov ecx, [size]
                mov [r_count], ecx
                mov eax, 0
                add_element:
                        mov eax, [counter]
                        inc eax
                        mov [counter], eax

                        push eax
                        push strEl
                        call [printf]

                        push element
                        push fNumber
                        call [scanf]

                        mov eax, [index]
                        mov [array + eax], element

                        inc eax
                        inc eax
                        inc eax
                        inc eax
                        mov [index], eax

                        mov ecx, [r_count]
                        dec ecx
                        mov [r_count], ecx
                        jecxz input_end

                        jmp add_element

        input_end:
                push strInputDone
                call [printf]

                call [getch]

                push NULL
                call [ExitProcess]

        output:

        process:

        start:
                jmp input

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