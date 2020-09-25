format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        ; Strings for console
        strSize            db 'Enter size of array: ', 0
        strEl              db 'Enter %d element of array: ', 0
        strInputDone       db 'Array input done.', 10, 10, 0
        strCurEl           db 'The %d element is: %d', 10, 0
        strAnyKey          db 'Press any button.', 0


        fNumber            db '%d', 0

        size               rd 1
        element            rd 1
        temp               Rw 1
        counter            rd 1
        r_count            rd 1
        index              rd 1

        ; Array
        array              rd 200

        NULL = 0




section '.code' code readable executable
        input:             push    strSize
                           call    [printf]

                           push    size
                           push    fNumber
                           call    [scanf]

                           mov     ecx, [size]
                           mov     [r_count], ecx
                           mov     eax, 0

        add_element:       mov     eax, [counter]
                           inc     eax
                           mov     [counter], eax

                           push    eax
                           push    strEl
                           call    [printf]

                           push    element
                           push    fNumber
                           call    [scanf]

                           mov     ebx, [element]
                           mov     eax, [index]
                           mov     [array + eax*4], ebx

                           inc     eax
                           mov     [index], eax

                           mov     ecx, [r_count]
                           dec     ecx
                           mov     [r_count], ecx
                           jecxz   input_end

                           jmp     add_element

        input_end:         push    strInputDone
                           call    [printf]

                           call    [getch]

                           jmp     process

                           push    NULL
                           call    [ExitProcess]



        process:           mov     ecx, [size]
                           mov     [r_count], ecx

                           mov     [counter], 0
                           mov     [index], 0

        process_iteration: mov     eax, [index]
                           cmp     [array + eax*4], 0
                           jge     more_or_equal

                           mov     [array + eax*4], -1
                           jmp     end_process

        more_or_equal:     mov     ecx, 0
                           cmp     ecx, [array + eax*4]
                           jge     equal

                           mov     [array + eax*4], 1
                           jmp     end_process

        equal:             mov     [array + eax*4], 0
                           jmp     end_process

        end_process:       inc     eax
                           mov     [index], eax

                           mov     ecx, [r_count]
                           dec     ecx
                           mov     [r_count], ecx
                           jecxz   output
                           jmp     process_iteration



        output:            mov     ecx, [size]
                           mov     [r_count], ecx

                           mov     [counter], 0
                           mov     [index], 0

        output_element:    mov     eax, [counter]
                           inc     eax
                           mov     [counter], eax

                           mov     eax, [index]

                           push    [array + eax*4]
                           push    [counter]
                           push    strCurEl
                           call    [printf]

                           mov     eax, [index]
                           inc     eax
                           mov     [index], eax

                           mov     ecx, [r_count]
                           dec     ecx
                           mov     [r_count], ecx
                           jecxz   output_end
                           jmp     output_element

        output_end:        push    strAnyKey
                           call    [printf]

                           call    [getch]

                           push    NULL
                           call    [ExitProcess]

        start:             jmp     input




section '.idata' import data readable
        library            kernel, 'kernel32.dll',\
                           msvcrt, 'msvcrt.dll'

        import             kernel,\
                           ExitProcess, 'ExitProcess'

        import             msvcrt,\
                           printf, 'printf',\
                           getch, '_getch',\
                           scanf, 'scanf'