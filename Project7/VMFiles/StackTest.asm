// push constant 17 
@17
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 17 
@17
D=A
@SP
M=M+1
A=M-1
M=D
// eq 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT0
D;JEQ
@SP
A=M-1
M=0
(CONT0)
// push constant 17 
@17
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 16 
@16
D=A
@SP
M=M+1
A=M-1
M=D
// eq 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT1
D;JEQ
@SP
A=M-1
M=0
(CONT1)
// push constant 16 
@16
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 17 
@17
D=A
@SP
M=M+1
A=M-1
M=D
// eq 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT2
D;JEQ
@SP
A=M-1
M=0
(CONT2)
// push constant 892 
@892
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 891 
@891
D=A
@SP
M=M+1
A=M-1
M=D
// lt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT3
D;JLT
@SP
A=M-1
M=0
(CONT3)
// push constant 891 
@891
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 892 
@892
D=A
@SP
M=M+1
A=M-1
M=D
// lt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT4
D;JLT
@SP
A=M-1
M=0
(CONT4)
// push constant 891 
@891
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 891 
@891
D=A
@SP
M=M+1
A=M-1
M=D
// lt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT5
D;JLT
@SP
A=M-1
M=0
(CONT5)
// push constant 32767 
@32767
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 32766 
@32766
D=A
@SP
M=M+1
A=M-1
M=D
// gt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT6
D;JGT
@SP
A=M-1
M=0
(CONT6)
// push constant 32766 
@32766
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 32767 
@32767
D=A
@SP
M=M+1
A=M-1
M=D
// gt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT7
D;JGT
@SP
A=M-1
M=0
(CONT7)
// push constant 32766 
@32766
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 32766 
@32766
D=A
@SP
M=M+1
A=M-1
M=D
// gt 
@SP
M=M-1
A=M
D=M
A=A-1
D=M-D
@SP
A=M-1
M=-1
@CONT8
D;JGT
@SP
A=M-1
M=0
(CONT8)
// push constant 57 
@57
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 31 
@31
D=A
@SP
M=M+1
A=M-1
M=D
// push constant 53 
@53
D=A
@SP
M=M+1
A=M-1
M=D
// add 
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
// push constant 112 
@112
D=A
@SP
M=M+1
A=M-1
M=D
// sub 
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
// neg 
@SP
A=M-1
M=-M
// and 
@SP
M=M-1
A=M
D=M
A=A-1
M=D&M
// push constant 82 
@82
D=A
@SP
M=M+1
A=M-1
M=D
// or 
@SP
M=M-1
A=M
D=M
A=A-1
M=D|M
// not 
@SP
A=M-1
M=!M
