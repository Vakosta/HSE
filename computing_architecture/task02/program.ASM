format PE console
entry start
include 'win32a.inc'

section '.data' data readable writable
        ; Strings for console
        str_el             db 'Enter %d element of array: ', 0
        str_size           db 'Enter size of array: ', 0
        str_cur_el         db 'The %d element is: %d', 10, 0
        str_any_key        db 'Press any button.', 0
        str_input_done     db 'Array input done.', 10, 10, 0

        f_number           db '%d', 0

        temp               Rw 1
        size               rd 1
        index              rd 1
        element            rd 1
        r_count            rd 1

        ; Array
        array              rd 200

        NULL = 0


;----------------------------------------------------------------


section '.code' code readable executable
        start:             call    input
                           call    process
                           call    output

;----------------------------------------------------------------

        process:           mov     ecx, [size]
                           mov     [r_count], ecx

                           mov     [index], 0

        process_iteration: mov     eax, [index]
                           cmp     [array + eax*4], 0
                           jge     more_or_equal

                           mov     [array + eax*4], -1
                           jmp     end_process

        more_or_equal:     xor     ecx, ecx
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

;----------------------------------------------------------------

        output:            mov     ecx, [size]
                           mov     [r_count], ecx

                           mov     [index], 0

        output_element:    mov     eax, [index]

                           push    [array + eax*4]
                           push    eax
                           push    str_cur_el
                           call    [printf]
                           add esp, 8

                           mov     eax, [index]
                           inc     eax
                           mov     [index], eax

                           mov     ecx, [r_count]
                           dec     ecx
                           mov     [r_count], ecx
                           jecxz   output_end
                           jmp     output_element

        output_end:        push    str_any_key
                           call    [printf]
                           add esp, 4

                           call    [getch]

                           ret

;----------------------------------------------------------------

        input:             push    str_size
                           call    [printf]
                           add esp, 4

                           push    size
                           push    f_number
                           call    [scanf]
                           add esp, 8

                           mov     ecx, [size]
                           mov     [r_count], ecx
                           xor     eax, eax

        add_element:       push    eax
                           push    str_el
                           call    [printf]
                           add esp, 8

                           push    element
                           push    f_number
                           call    [scanf]
                           add esp, 8

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

        input_end:         push    str_input_done
                           call    [printf]
                           add esp, 4

                           call    [getch]
                           ret


;----------------------------------------------------------------


section '.idata' import data readable
        library            kernel, 'kernel32.dll',\
                           msvcrt, 'msvcrt.dll'

        import             kernel,\
                           ExitProcess, 'ExitProcess'

        import             msvcrt,\
                           printf, 'printf',\
                           getch, '_getch',\
                           scanf, 'scanf'